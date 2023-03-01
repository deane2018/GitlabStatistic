package com.leyunone.codex.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.codex.dao.entry.Commit;
import com.leyunone.codex.model.bo.CommitBO;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.query.CommitQuery;
import com.leyunone.codex.model.vo.CommitVO;

import java.util.Date;
import java.util.List;

public interface CommitDao extends BaseDao<Commit> {

    Page<Commit> selectByPage(CommitQuery commitQuery);

    void saveBatch(List<CommitBO> commits);

    Date selectLastDate();

    List<CommitVO> selectProjectCodeGroupUser(CodeTimeQuery query);

    List<String> preDate(String date);
}
