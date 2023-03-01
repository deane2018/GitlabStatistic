package com.leyunone.codex.dao;

import com.leyunone.codex.dao.entry.ProjectUser;

import java.util.List;

public interface ProjectUserDao extends BaseDao<ProjectUser> {

    List<ProjectUser> getProjectByUser(List<String> userName);
}
