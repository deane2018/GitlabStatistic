package com.leyunone.codex.dao;

import com.leyunone.codex.dao.entry.User;
import com.leyunone.codex.model.vo.ProjectUserVO;

import java.util.List;


public interface UserDao extends BaseDao<User> {

    List<ProjectUserVO> selectByProjectId(Integer projectId);

    int updateUserCodeTotal0();
}
