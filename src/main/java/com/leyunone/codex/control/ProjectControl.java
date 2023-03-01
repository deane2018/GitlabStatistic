package com.leyunone.codex.control;

import com.leyunone.codex.model.DataResponse;
import com.leyunone.codex.model.vo.ProjectVO;
import com.leyunone.codex.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectControl {

    @Autowired
    private ActionService actionService;

    @RequestMapping("/projects")
    public DataResponse projects(){
        List<ProjectVO> projects = actionService.getProjects();
        return DataResponse.of(projects);
    }
}
