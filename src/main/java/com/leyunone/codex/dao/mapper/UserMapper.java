package com.leyunone.codex.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyunone.codex.dao.entry.User;
import com.leyunone.codex.model.vo.ProjectUserVO;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<ProjectUserVO> selectByProjectId(Integer projectId);
}
