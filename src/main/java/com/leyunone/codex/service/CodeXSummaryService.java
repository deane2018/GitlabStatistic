package com.leyunone.codex.service;

import cn.hutool.core.collection.CollectionUtil;
import com.leyunone.codex.dao.*;
import com.leyunone.codex.dao.*;
import com.leyunone.codex.dao.entry.Branches;
import com.leyunone.codex.dao.entry.Commit;
import com.leyunone.codex.dao.entry.ProjectUser;
import com.leyunone.codex.dao.mapper.CommitMapper;
import com.leyunone.codex.model.*;
import com.leyunone.codex.model.bo.BranchesBO;
import com.leyunone.codex.model.bo.CommitBO;
import com.leyunone.codex.model.bo.ProjectBO;
import com.leyunone.codex.model.bo.UserBO;
import com.leyunone.codex.model.ResponseCell;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.gitlab4j.api.models.Project;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 代码统计总结服务
 */
@Service
public class CodeXSummaryService {

    @Autowired
    private GitLabAPIService gitLabAPIService;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private BranchesDao branchesDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommitDao commitDao;
    @Autowired
    private ProjectUserDao projectUserDao;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 前置处理
     */
    public void preResole(){
        List<Commit> commits = commitDao.selectByCon(null);

    }

    /**
     * 全统计 项目 分支 每次提交
     */
    public void summaryCodeX() {
        //当前可见所有项目
        ResponseCell<List<ProjectBO>, List<Project>> responseCell = gitLabAPIService.resoleProjects();
        this.projectNew(responseCell.getDataBO());

        Map<Integer, List<BranchesBO>> projectTobranches = new HashMap<>();
        List<Branches> branches = new ArrayList<>();
        for (Project project : responseCell.getDataApi()) {
            List<BranchesBO> branchesBOS = gitLabAPIService.resoleBranches(project);
            branchesBOS.forEach((t) -> {
                branches.add(Branches.builder().id(project.getId() + "#" + t.getBranchName()).projectId(project.getId()).branchesName(t.getBranchName()).build());
            });
            projectTobranches.put(project.getId(), branchesBOS);
        }
        //TODO 分支
        this.branchesNew(branches);

        Map<String, UserBO> userMap = new HashMap<>();
        Map<String, ProjectUser> projectUserMap = new HashMap<>();
        List<CommitBO> commits = new ArrayList<>();
        for (Integer projectId : projectTobranches.keySet()) {
            List<CommitBO> commitBOS = gitLabAPIService.resoleCommits(projectId, null, null);
            commitBOS.forEach((t)->t.setProjectId(projectId));
            for (CommitBO commitBO : commitBOS) {
                //遍历提交记录时，绑定项目和成员的关系
                if(!projectUserMap.containsKey(projectId+"#"+commitBO.getCommitterName())){
                    ProjectUser projectUser = new ProjectUser();
                    projectUser.setId(commitBO.getCommitterName()+"#"+projectId);
                    projectUser.setProjectId(projectId);
                    projectUser.setUserName(commitBO.getCommitterName());
                    projectUserMap.put(projectId+"#"+commitBO.getCommitterName(),projectUser);
                }


                //累加用户
                if (userMap.containsKey(commitBO.getCommitterName())) {
                    UserBO userBO = userMap.get(commitBO.getCommitterName());
                    userBO.setCodeAdditions(userBO.getCodeAdditions()+commitBO.getAdditions());
                    userBO.setCodeDeletions(userBO.getCodeDeletions()+commitBO.getDeletions());
                    userBO.setCodeTotal(userBO.getCodeTotal()+commitBO.getTotal());
                }else{
                    UserBO userBO = new UserBO();
                    userBO.setCodeAdditions(commitBO.getAdditions());
                    userBO.setCodeDeletions(commitBO.getDeletions());
                    userBO.setCodeTotal(commitBO.getTotal());
                    userBO.setUserEmail(commitBO.getCommitterEmail());
                    userBO.setUserName(commitBO.getCommitterName());
                    userMap.put(userBO.getUserName(),userBO);
                }
            }
            commits.addAll(commitBOS);
        }
        this.userNew(CollectionUtil.newArrayList(userMap.values()));
        //全量同步提交记录
        this.commitNew(commits);
        this.projectUserNew(CollectionUtil.newArrayList(projectUserMap.values()));
    }

    /**
     * 全量更新项目 非事务
     *
     * @param projects
     */
    private void projectNew(List<ProjectBO> projects) {
        projectDao.insertOrUpdateBatch(projects);
    }

    /**
     * 全量更新项目下分支 非事务
     *
     * @param branches
     */
    private void branchesNew(List<Branches> branches) {
        branchesDao.insertOrUpdateBatch(branches);
    }

    /**
     * 全量更新所有人的提交状态 非事务
     *
     * @param users
     */
    private void userNew(List<UserBO> users){
        userDao.insertOrUpdateBatch(users);
    }

    /**
     * 全量更新所有提交 非事务
     * @param commitBOS
     */
    private void commitNew(List<CommitBO> commitBOS){
        SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
        //可以执行批量操作的sqlSession
        SqlSession openSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            CommitMapper mapper = openSession.getMapper(CommitMapper.class);
            List<CommitBO> list = new ArrayList<>();
            for (CommitBO commitBO : commitBOS) {
                list.add(commitBO);
                if (list.size() == 1000) {
                    mapper.batchInsert(list);
                    openSession.commit();
                    list.clear();
                }
            }
            if (list.size() > 0) {
                mapper.batchInsert(list);
            }
            openSession.commit();
        } finally {
            openSession.clearCache();
            openSession.close();
        }
    }

    private void projectUserNew(List<ProjectUser> projectUsers){
        projectUserDao.insertOrUpdateBatch(projectUsers);
    }
}
