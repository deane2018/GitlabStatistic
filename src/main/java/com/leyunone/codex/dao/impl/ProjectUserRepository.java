package com.leyunone.codex.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leyunone.codex.dao.ProjectUserDao;
import com.leyunone.codex.dao.entry.ProjectUser;
import com.leyunone.codex.dao.mapper.ProjectUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectUserRepository extends BaseRepository<ProjectUserMapper, ProjectUser> implements ProjectUserDao {

    @Override
    public List<ProjectUser> getProjectByUser(List<String> userName) {
        LambdaQueryWrapper<ProjectUser> lambda = new QueryWrapper<ProjectUser>().lambda();
        lambda.in(ProjectUser::getUserName,userName);
        return this.baseMapper.selectList(lambda);
    }
}
