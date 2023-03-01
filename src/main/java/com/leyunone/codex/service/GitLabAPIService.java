package com.leyunone.codex.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.leyunone.codex.model.bo.BranchesBO;
import com.leyunone.codex.model.bo.CommitBO;
import com.leyunone.codex.model.bo.ProjectBO;
import com.leyunone.codex.model.ResponseCell;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * GITLab API解析服务
 */
@Service
public class GitLabAPIService {

    private static final Logger logger = LoggerFactory.getLogger(GitLabAPIService.class);

    @Autowired
    private GitLabApi gitLabApi;

    /**
     * 解析用户下的所有项目
     */
    public ResponseCell<List<ProjectBO>, List<Project>> resoleProjects() {
        //所有项目
        List<Project> projects = null;
        try {
            projects = gitLabApi.getProjectApi().getProjects();
        } catch (GitLabApiException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("projects is empty");
        }

        List<ProjectBO> projectBOS = new ArrayList<>();
        for (Project project : projects) {
            ProjectBO projectBO = new ProjectBO();
            projectBO.setId(project.getId());
            projectBO.setProjectName(project.getName());
            projectBO.setPath(project.getPath());
            projectBO.setCreateDate(project.getCreatedAt());
            projectBOS.add(projectBO);
        }
        return ResponseCell.build(projectBOS, projects);
    }

    /**
     * 解析项目下所有分支
     *
     * @param project 项目
     */
    public List<BranchesBO> resoleBranches(Project project) {
        //项目下的所有分支
        List<ProtectedBranch> protectedBranches = null;
        try {
            protectedBranches = gitLabApi.getProtectedBranchesApi().getProtectedBranches(project);
        } catch (GitLabApiException e) {
            logger.error(e.getMessage());
        }
        if (CollectionUtil.isEmpty(protectedBranches)) return CollectionUtil.newArrayList();

        List<BranchesBO> branchess = new ArrayList<>();
        for (ProtectedBranch protectedBranch : protectedBranches) {
            BranchesBO branchesBO = new BranchesBO();
            branchesBO.setBranchName(protectedBranch.getName());
            branchesBO.setProjectId(project.getId());
            branchess.add(branchesBO);
        }
        return branchess;
    }

    /**
     * 解析分支下的所有提交记录
     *
     * @param projectId  项目id
     * @param brunchName 分支名
     * @param lastDate   开始时间
     */
    public List<CommitBO> resoleCommits(Integer projectId, String brunchName, Date lastDate) {
        //项目下的所有提交记录
        List<Commit> allCommits = null;
        try {
            if (ObjectUtil.isNotNull(lastDate)) {
                allCommits = gitLabApi.getCommitsApi().getCommits(projectId, brunchName, lastDate, new Date());
            } else {
                //该项目全提交记录
                allCommits = gitLabApi.getCommitsApi().getCommits(projectId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if (CollectionUtil.isEmpty(allCommits)) return CollectionUtil.newArrayList();

        List<CommitBO> commits = new ArrayList<>();
        for (Commit commit : allCommits) {
            CommitBO commitBO = new CommitBO();
            commitBO.setCommitDate(commit.getCommittedDate());
            commitBO.setCommitterName(commit.getCommitterName());
            commitBO.setCommitterEmail(commit.getCommitterEmail());
            commitBO.setTitle(commit.getTitle());
            commitBO.setId(commit.getId());
            commitBO.setMessage(commit.getMessage());

            //本次提交的修改记录
            CommitStats stats = commit.getStats();
            commitBO.setAdditions(stats.getAdditions());
            commitBO.setDeletions(stats.getDeletions());
            commitBO.setTotal(stats.getTotal());
            commits.add(commitBO);
        }
        return commits;
    }
}
