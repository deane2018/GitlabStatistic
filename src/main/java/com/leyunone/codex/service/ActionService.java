package com.leyunone.codex.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.codex.dao.CommitDao;
import com.leyunone.codex.dao.ProjectDao;
import com.leyunone.codex.dao.ProjectUserDao;
import com.leyunone.codex.dao.UserDao;
import com.leyunone.codex.dao.entry.Commit;
import com.leyunone.codex.dao.entry.Project;
import com.leyunone.codex.dao.entry.ProjectUser;
import com.leyunone.codex.dao.entry.User;
import com.leyunone.codex.model.query.CommitQuery;
import com.leyunone.codex.model.vo.ProjectVO;
import com.leyunone.codex.model.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            if(CollectionUtil.isNotEmpty(pu)){
                pu.forEach((t) -> t.setProjectName(projectName.containsKey(t.getProjectId()) ? projectName.get(t.getProjectId()).getProjectName() : "项目不存在"));
            }
            userVO.setProjectList(pu);
            us.add(userVO);
        }
        return us;
    }

    public List<ProjectVO> getProjects(){
        List<ProjectVO> projectVOS = projectDao.selectByCon(null, ProjectVO.class);
        return projectVOS;
    }

    public Page<Commit> queryCommitCodeX(CommitQuery query){
        Page<Commit> commitPage = commitDao.selectByPage(query);
        return commitPage;
    }
}

