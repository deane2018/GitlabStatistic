package com.leyunone.codex.control;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.codex.dao.entry.Commit;
import com.leyunone.codex.model.DataResponse;
import com.leyunone.codex.model.query.CommitQuery;
import com.leyunone.codex.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commit")
public class CommitControl {

    @Autowired
    private ActionService actionService;

    @RequestMapping("/commitBy")
    public DataResponse getCommit(CommitQuery query){
        Page<Commit> commitPage = actionService.queryCommitCodeX(query);
        return DataResponse.of(commitPage.getRecords());
    }
}
