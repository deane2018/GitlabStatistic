package com.leyunone.codex.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leyunone.codex.dao.GroupDao;
import com.leyunone.codex.dao.GroupUserDao;
import com.leyunone.codex.dao.entry.Group;
import com.leyunone.codex.dao.entry.GroupUser;
import com.leyunone.codex.dao.mapper.GroupMapper;
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

    @Override
    public List<GroupUserVO> selectCodeSumByGroup(String startDate, String endDate) {
        return this.baseMapper.selectCodeSumByGroup(startDate, endDate);
    }

    @Override
    public int deleteByGroupId(Integer id) {
        LambdaQueryWrapper<GroupUser> lambda = new QueryWrapper<GroupUser>().lambda();
        lambda.eq(GroupUser::getGroupId,id);
        return this.baseMapper.delete(lambda);
    }
}
