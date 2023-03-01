package com.leyunone.codex.dao.impl;

import com.leyunone.codex.dao.GroupUserDao;
import com.leyunone.codex.dao.entry.GroupUser;
import com.leyunone.codex.dao.mapper.GroupUserMapper;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.vo.GroupUserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupUserRepository extends BaseRepository<GroupUserMapper, GroupUser> implements GroupUserDao {

    @Override
    public List<GroupUserVO> selectCodeByGroup() {
        return this.baseMapper.selectCodeByGroup();
    }

    @Override
    public List<GroupUserVO> groupTimeCode(CodeTimeQuery query) {
        return this.baseMapper.groupTimeCode(query);
    }
}
