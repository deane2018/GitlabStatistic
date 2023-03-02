package com.leyunone.codex.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.codex.dao.*;
import com.leyunone.codex.dao.entry.*;
import com.leyunone.codex.model.bo.GroupBO;
import com.leyunone.codex.model.bo.UserBO;
import com.leyunone.codex.model.query.CommitQuery;
import com.leyunone.codex.model.vo.GroupUserVO;
import com.leyunone.codex.model.vo.GroupVO;
import com.leyunone.codex.model.vo.ProjectVO;
import com.leyunone.codex.model.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Map<Integer, Project> projectName = projects.stream().collect(Collectors.toMap(Project::getId, Function.identity()));

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

    public List<UserVO> noGroups(){
        List<GroupUser> groupUsers = groupUserDao.selectByCon(null);
        List<UserVO> users = userDao.selectByCon(null,UserVO.class);

        Set<String> groupUser = groupUsers.stream().map(GroupUser::getUserName).collect(Collectors.toSet());
        Iterator<UserVO> iterator = users.iterator();
        while(iterator.hasNext()){
            if(groupUser.contains(iterator.next())) iterator.remove();
        }
        return users;
    }

    /**
     * 不使用事务
     * @param groupBO
     */
    public void saveGroupUser(GroupBO groupBO){
        //将关系全删除
        int i = groupUserDao.deleteByGroupId(groupBO.getGroupId());

        //重新添加关系
        List<GroupUser> gu = new ArrayList<>();
        for(String userName : groupBO.getUserNames()){
            gu.add(GroupUser.builder().userName(userName).groupId(groupBO.getGroupId()).build());
        }
        boolean b = groupUserDao.insertOrUpdateBatch(gu);
        if(!b) throw new RuntimeException();
    }
}

