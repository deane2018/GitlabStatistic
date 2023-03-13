package com.leyunone.codex.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyunone.codex.dao.entry.Project;
import com.leyunone.codex.model.vo.ProjectUserVO;

import java.util.List;

public interface ProjectMapper extends BaseMapper<Project> {

    List<ProjectUserVO> selectByUserName(String userName);
}
