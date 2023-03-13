package com.leyunone.codex.dao.impl;

import com.leyunone.codex.dao.ProjectDao;
import com.leyunone.codex.dao.entry.Project;
import com.leyunone.codex.dao.mapper.ProjectMapper;
import com.leyunone.codex.model.vo.ProjectUserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository extends BaseRepository<ProjectMapper, Project> implements ProjectDao {

    @Override
    public List<ProjectUserVO> selectByUserName(String userName) {
        return this.baseMapper.selectByUserName(userName);
    }
}
