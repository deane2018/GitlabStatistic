package com.leyunone.codex.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.codex.dao.CommitDao;
import com.leyunone.codex.dao.GroupUserDao;
import com.leyunone.codex.dao.UserDao;
import com.leyunone.codex.dao.entry.User;
import com.leyunone.codex.model.ChartBean;
import com.leyunone.codex.model.enums.StatisticsTypeEnum;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.vo.ChartVO;
import com.leyunone.codex.model.vo.CommitVO;
import com.leyunone.codex.model.vo.GroupUserVO;
import com.leyunone.codex.model.vo.UserVO;
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

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 统计人员基础代码信息
     */
    public ChartVO userBaseCode(CodeTimeQuery codeTimeQuery) {
        List<UserVO> users = userDao.selectByConOrder(null, UserVO.class, 1, User::getCodeTotal);
        ChartVO chartVO = new ChartVO();
        Object[][] datas = new Object[users.size()][2];
        for (int i = 0; i < users.size(); i++) {
            UserVO userVO = users.get(i);
            datas[i][0] = userVO.getUserName();
            datas[i][1] = userVO.getCodeTotal();
        }
        chartVO.setSeriesData(datas);
        chartVO.setTitle("人员代码量");
        return chartVO;
    }

    /**
     * 统计 分组后的代码基本量
     *
     * @return
     */
    public List<GroupUserVO> groupBaseCode() {
        List<GroupUserVO> groupUserVOS = groupUserDao.selectCodeByGroup();
        return groupUserVOS;
    }

    /**
     * 统计 人员根据时间
     *
     * @param codeTimeQuery
     * @return
     */
    public ChartVO userProjectTimeCode(CodeTimeQuery codeTimeQuery) {
        this.reloadTime(codeTimeQuery);
        //前置加载数据库日期
        List<String> strings = commitDao.preDate(codeTimeQuery.getEndDate());

        List<CommitVO> commitVOS = commitDao.selectProjectCodeGroupUser(codeTimeQuery);
        //解析页面可用的chart对象 日期 -》 提交人集合
        Map<String, List<CommitVO>> datamap = commitVOS.stream().collect(Collectors.groupingBy(CommitVO::getDate));

        //X坐标
        Set<String> setX = commitVOS.stream().map(CommitVO::getDate).collect(Collectors.toSet());
        Collections.sort(new ArrayList<>(setX));
        //提交人
        Set<String> names = commitVOS.stream().filter((t) -> StringUtils.isNotBlank(t.getCommitterName())).map(CommitVO::getCommitterName).collect(Collectors.toSet());
        Map<String, List<Integer>> nameDate = new HashMap<>();
        names.forEach((t) -> nameDate.put(t, new ArrayList<>()));

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
        if (ObjectUtil.isNull(codeTimeQuery.getProjectId())) {
            chartVO.setTitle("人员时间提交记录量");
        } else {
            chartVO.setTitle("项目时间提交记录量");
        }
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
     * 统计 小组随时间
     * @param codeTimeQuery
     * @return
     */
    public ChartVO groupTimeCode(CodeTimeQuery codeTimeQuery) {
        this.reloadTime(codeTimeQuery);
        //前置加载数据库日期
        List<String> strings = commitDao.preDate(codeTimeQuery.getEndDate());

        List<GroupUserVO> groupUserVOS = groupUserDao.groupTimeCode(codeTimeQuery);

        //解析页面可用的chart对象 日期 -》 提交人集合
        Map<String, List<GroupUserVO>> datamap = groupUserVOS.stream().collect(Collectors.groupingBy(GroupUserVO::getDate));

        //X坐标
        Set<String> setX = groupUserVOS.stream().map(GroupUserVO::getDate).collect(Collectors.toSet());
        Collections.sort(new ArrayList<>(setX));
        //提交人
        Set<String> names = groupUserVOS.stream().filter((t) -> StringUtils.isNotBlank(t.getGroupName())).map(GroupUserVO::getGroupName).collect(Collectors.toSet());
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
                    integers.add(groupUserVO.getTotal());
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

    private void reloadTime(CodeTimeQuery codeTimeQuery){
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
