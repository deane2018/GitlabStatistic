package com.leyunone.codex.dao.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.leyunone.codex.dao.UserDao;
import com.leyunone.codex.dao.entry.User;
import com.leyunone.codex.dao.mapper.UserMapper;
import com.leyunone.codex.model.vo.ProjectUserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends BaseRepository<UserMapper, User> implements UserDao {

    @Override
    public List<ProjectUserVO> selectByProjectId(Integer projectId) {
        return this.baseMapper.selectByProjectId(projectId);
    }

    @Override
    public int updateUserCodeTotal0() {
        LambdaUpdateWrapper<User> lambda = new UpdateWrapper<User>().lambda();
        lambda.set(User::getCodeTotal,0);
        lambda.set(User::getCodeAdditions,0);
        lambda.set(User::getCodeDeletions,0);
        return this.baseMapper.update(null,lambda);
    }
}
