package com.leyunone.codex.dao;

import com.leyunone.codex.dao.entry.Project;
import com.leyunone.codex.model.vo.ProjectUserVO;

import java.util.List;

public interface ProjectDao extends BaseDao<Project> {

    List<ProjectUserVO> selectByUserName(String userName);
}
