package com.leyunone.codex.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.codex.dao.*;
import com.leyunone.codex.dao.entry.*;
import com.leyunone.codex.model.bo.GroupBO;
import com.leyunone.codex.model.bo.UserBO;
import com.leyunone.codex.model.query.CommitQuery;
import com.leyunone.codex.model.vo.*;
import com.leyunone.codex.util.UserNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 操作行为服务
 */
@Service
public class ActionService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ProjectUserDao projectUserDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private CommitDao commitDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private GroupUserDao groupUserDao;

    public List<UserVO> queryUserCodex(Integer index, Integer size) {
        List<User> users = userDao.selectByCon(null);

        List<ProjectUser> projectUsers = projectUserDao.selectByCon(null);
        Map<String, List<ProjectUser>> projectMap = projectUsers.stream().collect(Collectors.groupingBy(ProjectUser::getUserName));
        List<Project> projects = projectDao.selectByCon(null);
        Map<String, Project> projectName = projects.stream().collect(Collectors.toMap(Project::getProjectId, Function.identity()));

        List<UserVO> us = new ArrayList<>();
        for (User user : users) {
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user, userVO);

            List<ProjectUser> pu = projectMap.get(user.getUserName());
            if (CollectionUtil.isNotEmpty(pu)) {
                pu.forEach((t) -> t.setProjectName(projectName.containsKey(t.getProjectId()) ? projectName.get(t.getProjectId()).getProjectName() : "项目不存在"));
            }
            userVO.setProjectList(pu);
            us.add(userVO);
        }
        return us;
    }

    public List<ProjectVO> getProjects() {
        List<ProjectVO> projectVOS = projectDao.selectByCon(null, ProjectVO.class);
        return projectVOS;
    }

    public Page<Commit> queryCommitCodeX(CommitQuery query) {
        Page<Commit> commitPage = commitDao.selectByPage(query);
        Map<String, String> userRealNames = UserNameUtils.getUserRealNames();
        List<Commit> records = commitPage.getRecords();
        records.forEach((t) -> {
            if (userRealNames.containsKey(t.getCommitterName())) {
                t.setCommitterName(userRealNames.get(t.getCommitterName()));
            }
        });
        return commitPage;
    }

    public void saveUser(UserBO userBO) {
        userDao.insertOrUpdate(userBO);
    }

    public List<GroupVO> getGroups() {
        List<GroupVO> groups = groupDao.selectByCon(null, GroupVO.class);
        List<GroupUserVO> groupUsers = groupUserDao.selectByCon(null, GroupUserVO.class);
        Map<Integer, List<GroupUserVO>> groupUserMap = groupUsers.stream().collect(Collectors.groupingBy(GroupUserVO::getGroupId));
        for (GroupVO group : groups) {
            if (groupUserMap.containsKey(group.getGroupId())) {
                group.setGroupUsers(groupUserMap.get(group.getGroupId()));
            }
        }
        return groups;
    }

    public List<String> noGroupUsers(Integer groupId) {
        //查询groupId的人员 和没有分组的人员
        List<GroupUserVO> groupUserVOS = groupUserDao.selectGroupUser();

        groupUserVOS.removeIf(next -> ObjectUtil.isNotNull(next.getGroupId()) && !next.getGroupId().equals(groupId));
        List<String> result = null;
        if (CollectionUtil.isNotEmpty(groupUserVOS)) {
            result = groupUserVOS.stream().map(GroupUserVO::getUserName).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 不使用事务
     *
     * @param groupBO
     */
    public void saveGroupUser(GroupBO groupBO) {
        //将关系全删除
        int i = groupUserDao.deleteByGroupId(groupBO.getGroupId());

        //重新添加关系
        List<GroupUser> gu = new ArrayList<>();
        for (String userName : groupBO.getUserNames()) {
            gu.add(GroupUser.builder().userName(userName).groupId(groupBO.getGroupId()).build());
        }
        boolean b = groupUserDao.insertOrUpdateBatch(gu);
        if (!b) throw new RuntimeException();
    }


    /**
     * 选择项目，得到项目下人员；
     * 选择人员，得到人员有的项目；
     *
     * @param projectId
     * @param userName
     */
    public List<ProjectUserVO> selectProjectOrUser(Integer projectId, String userName, Integer type) {
        List<ProjectUserVO> projectUserVOS = new ArrayList<>();
        if (type == 0) {
            //人员搜索
            if(ObjectUtil.isNull(projectId)){
                projectUserVOS = userDao.selectByCon(null, ProjectUserVO.class);
            }else{
                projectUserVOS = userDao.selectByProjectId(projectId);
            }
        }
        if (type == 1) {
            //项目搜索
            if(StringUtils.isBlank(userName)){
                projectUserVOS = projectDao.selectByCon(null,ProjectUserVO.class);
            }else{
                projectUserVOS = projectDao.selectByUserName(userName);
            }
        }
        return projectUserVOS;
    }
}

