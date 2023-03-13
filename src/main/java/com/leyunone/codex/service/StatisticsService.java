package com.leyunone.codex.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.codex.dao.CommitDao;
import com.leyunone.codex.dao.GroupDao;
import com.leyunone.codex.dao.GroupUserDao;
import com.leyunone.codex.dao.UserDao;
import com.leyunone.codex.dao.entry.Group;
import com.leyunone.codex.dao.entry.User;
import com.leyunone.codex.model.ChartBean;
import com.leyunone.codex.model.enums.StatisticsTypeEnum;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.vo.ChartVO;
import com.leyunone.codex.model.vo.CommitVO;
import com.leyunone.codex.model.vo.GroupUserVO;
import com.leyunone.codex.model.vo.UserVO;
import com.leyunone.codex.util.UserNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupUserDao groupUserDao;
    @Autowired
    private CommitDao commitDao;
    @Autowired
    private GroupDao groupDao;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 统计人员基础代码信息
     */
    public ChartVO userBaseCode(CodeTimeQuery codeTimeQuery) {
        ChartVO chartVO = new ChartVO();

        List<UserVO> users = null;
        if (StringUtils.isBlank(codeTimeQuery.getStartDate())) {
            //未入时间，则截止至今
            users = userDao.selectByConOrder(null, UserVO.class, 1, User::getCodeTotal);
            chartVO.setDate(LocalDateTime.now().format(format));
        } else {
            users = commitDao.selectSumGroupUser(codeTimeQuery.getStartDate(), codeTimeQuery.getEndDate());
            chartVO.setDate(codeTimeQuery.getEndDate());
        }
        Object[][] datas = new Object[users.size()][2];
        for (int i = 0; i < users.size(); i++) {
            UserVO userVO = users.get(i);
            if (StringUtils.isNotBlank(userVO.getUserRealName())) {
                datas[i][0] = userVO.getUserRealName();
            } else {
                datas[i][0] = userVO.getUserName();
            }
            datas[i][1] = userVO.getCodeTotal();
        }
        chartVO.setSeriesData(datas);
        chartVO.setTitle("人员代码量");
        return chartVO;
    }

    /**
     * 统计小组基础代码信息
     */
    public ChartVO groupBaseCode(CodeTimeQuery codeTimeQuery) {
        ChartVO chartVO = new ChartVO();
        List<GroupUserVO> groupUserVOS = null;
        if (StringUtils.isBlank(codeTimeQuery.getStartDate())) {
            //未入时间，则截止至今
            groupUserVOS = groupUserDao.selectCodeByGroup();
            chartVO.setDate(LocalDateTime.now().format(format));
        } else {
            groupUserVOS = groupUserDao.selectCodeSumByGroup(codeTimeQuery.getStartDate(), codeTimeQuery.getEndDate());
            chartVO.setDate(codeTimeQuery.getEndDate());
        }

        groupUserVOS.sort(Comparator.comparing(GroupUserVO::getCodeTotal));
        Object[][] datas = new Object[groupUserVOS.size()][2];
        for (int i = 0; i < groupUserVOS.size(); i++) {
            GroupUserVO groupUserVO = groupUserVOS.get(i);
            datas[i][0] = groupUserVO.getGroupName();
            datas[i][1] = groupUserVO.getCodeTotal();
        }
        chartVO.setSeriesData(datas);
        chartVO.setTitle("小组代码量");
        return chartVO;
    }

    /**
     * 统计 小组随时间
     *
     * @param codeTimeQuery
     * @return
     */
    public ChartVO groupTimeCode(CodeTimeQuery codeTimeQuery) {
        this.reloadTime(codeTimeQuery);
        //前置加载数据库日期
        List<String> strings = commitDao.preDate(codeTimeQuery.getEndDate());

        List<GroupUserVO> groupUserVOS = groupUserDao.groupTimeCode(codeTimeQuery);

        //解析页面可用的chart对象 日期 -》 提交人集合
        Map<String, List<GroupUserVO>> datamap = new HashMap<>();
        Set<String> setX = new LinkedHashSet<>();
        Set<String> names = new LinkedHashSet<>();
        for(GroupUserVO groupUserVO : groupUserVOS){
            String date = groupUserVO.getDate();
            if(datamap.containsKey(date)){
                datamap.get(date).add(groupUserVO);
            }else{
                ArrayList<GroupUserVO> l = CollectionUtil.newArrayList();
                l.add(groupUserVO);
                datamap.put(date,l);
            }
            //X坐标
            setX.add(date);
            if(StringUtils.isNotBlank(groupUserVO.getGroupName())){
                //提交人
                names.add(groupUserVO.getGroupName());
            }
        }

        if (CollectionUtil.isEmpty(names)) {
            names = groupDao.selectByCon(null).stream().map(Group::getGroupName).collect(Collectors.toSet());
        }
        Map<String, List<Integer>> nameDate = new HashMap<>();
        names.forEach((t) -> nameDate.put(t, new ArrayList<>()));

        for (String date : setX) {
            List<GroupUserVO> groupUsers = datamap.get(date);
            if (StringUtils.isBlank(CollectionUtil.getFirst(groupUsers).getGroupName())) {
                //全为空
                Collection<List<Integer>> values = nameDate.values();
                for (List<Integer> data : values) {
                    data.add(0);
                }
            } else {
                //找到每个提交的对应人员
                for (GroupUserVO groupUserVO : groupUsers) {
                    List<Integer> integers = nameDate.get(groupUserVO.getGroupName());
                    if (CollectionUtil.isNotEmpty(integers)) {
                        integers.add(groupUserVO.getTotal());
                    }
                }
            }
        }
        ChartVO chartVO = new ChartVO();
        chartVO.setXChart(CollectionUtil.newArrayList(setX));
        chartVO.setTitle("部门代码提交量");
        List<ChartBean> series = new ArrayList<>();
        for (String name : nameDate.keySet()) {
            ChartBean chartBean = new ChartBean();
            chartBean.setData(nameDate.get(name));
            chartBean.setName(name);
            series.add(chartBean);
        }
        chartVO.setSeries(series);

        return chartVO;
    }

    /**
     * 统计 人员根据时间
     *
     * @param codeTimeQuery
     * @return
     */
    public ChartVO userProjectTimeCode(CodeTimeQuery codeTimeQuery) {
        if (StringUtils.isBlank(codeTimeQuery.getStartDate())) {
            String statisticsDate = StatisticsTypeEnum.getStatisticsType(0).getStatisticsDate();
            codeTimeQuery.setStartDate(statisticsDate);
        }
        if (StringUtils.isBlank(codeTimeQuery.getEndDate())) {
            String endDate = this.format.format(LocalDateTime.now());
            codeTimeQuery.setEndDate(endDate);
        }

        List<CommitVO> commitVOS = commitDao.selectProjectCodeGroupUser(codeTimeQuery);

        //解析页面可用的chart对象 日期 -》 提交人集合
        Map<String, List<CommitVO>> datamap = new HashMap<>();
        Set<String> setX = new LinkedHashSet<>();
        Set<String> names = new LinkedHashSet<>();
        for(CommitVO commitVO : commitVOS){
            String date = commitVO.getDate();
            if(datamap.containsKey(date)){
                datamap.get(date).add(commitVO);
            }else{
                ArrayList<CommitVO> l = CollectionUtil.newArrayList();
                l.add(commitVO);
                datamap.put(date,l);
            }
            //X坐标
            setX.add(date);
            if(StringUtils.isNotBlank(commitVO.getCommitterName())){
                //提交人
                names.add(commitVO.getCommitterName());
            }
        }

        if (CollectionUtil.isEmpty(names)) {
            names = userDao.selectByCon(null).stream().map(User::getUserName).collect(Collectors.toSet());
        }
        Map<String, List<Integer>> nameDate = new HashMap<>();
        names.forEach((t) -> nameDate.put(t, new ArrayList<>()));

        Map<String, String> userNames = UserNameUtils.getUserRealNames();
        for (String date : setX) {
            List<CommitVO> commits = datamap.get(date);
            if (StringUtils.isBlank(CollectionUtil.getFirst(commits).getCommitterName())) {
                //全为空
                Collection<List<Integer>> values = nameDate.values();
                for (List<Integer> data : values) {
                    data.add(0);
                }
            } else {
                //找到每个提交的对应人员
                for (CommitVO commit : commits) {
                    List<Integer> integers = nameDate.get(commit.getCommitterName());
                    integers.add(commit.getTotal());
                }
            }
        }
        ChartVO chartVO = new ChartVO();
        chartVO.setXChart(CollectionUtil.newArrayList(setX));
        chartVO.setTitle("人员时间提交记录量");
        List<ChartBean> series = new ArrayList<>();
        for (String name : nameDate.keySet()) {
            ChartBean chartBean = new ChartBean();
            chartBean.setData(nameDate.get(name));
            if (userNames.containsKey(name)) {
                name = userNames.get(name);
            }
            chartBean.setName(name);

            series.add(chartBean);
        }
        chartVO.setSeries(series);

        return chartVO;
    }

    private void reloadTime(CodeTimeQuery codeTimeQuery) {
        if (StringUtils.isBlank(codeTimeQuery.getStartDate())) {
            String statisticsDate = StatisticsTypeEnum.getStatisticsType(0).getStatisticsDate();
            codeTimeQuery.setStartDate(statisticsDate);
        }
        if (StringUtils.isBlank(codeTimeQuery.getEndDate())) {
            String endDate = this.format.format(LocalDateTime.now());
            codeTimeQuery.setEndDate(endDate);
        }
    }
}
