package com.leyunone.codex.dao;

import com.leyunone.codex.dao.entry.GroupUser;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.vo.GroupUserVO;

import java.util.List;

public interface GroupUserDao extends BaseDao<GroupUser> {

    List<GroupUserVO> selectCodeByGroup();

    List<GroupUserVO> groupTimeCode(CodeTimeQuery query);

    List<GroupUserVO> selectCodeSumByGroup(String startDate, String endDate);

    int deleteByGroupId(Integer id);
}
