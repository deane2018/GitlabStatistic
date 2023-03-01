package com.leyunone.codex.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyunone.codex.dao.entry.Commit;
import com.leyunone.codex.model.bo.CommitBO;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.vo.CommitVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CommitMapper extends BaseMapper<Commit> {

    void batchInsert(List<CommitBO> commits);

    Date selectLastDate();

    List<CommitVO> selectProjectCodeGroupUser(@Param("con") CodeTimeQuery con);

    List<String> preDate(String date);
}
