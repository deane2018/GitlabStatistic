package com.leyunone.codex.task;

import com.leyunone.codex.service.CodeXSummaryService;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GitLab 代码全量总结 任务
 */
@Component
public class GitLabSummaryHandler extends IJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(GitLabSummaryHandler.class);

    @Autowired
    private CodeXSummaryService codexSummaryService;

    @XxlJob(value = "git_summary")
    @Override
    public void execute() {
        codexSummaryService.summaryCodeX();
    }
}
