package com.leyunone.codex.dao.impl;

import com.leyunone.codex.dao.ProjectDao;
import com.leyunone.codex.dao.entry.Project;
import com.leyunone.codex.dao.mapper.ProjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository extends BaseRepository<ProjectMapper, Project> implements ProjectDao {

}
