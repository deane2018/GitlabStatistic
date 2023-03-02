package com.leyunone.codex.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyunone.codex.dao.entry.Group;
import com.leyunone.codex.dao.entry.GroupUser;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.vo.GroupUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupUserMapper extends BaseMapper<GroupUser> {

    List<GroupUserVO> selectCodeByGroup();

    List<GroupUserVO> groupTimeCode(@Param("con") CodeTimeQuery con);

    List<GroupUserVO> selectCodeSumByGroup(String startDate,String endDate);
}
