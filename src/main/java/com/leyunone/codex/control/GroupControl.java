package com.leyunone.codex.control;

import com.leyunone.codex.model.DataResponse;
import com.leyunone.codex.model.bo.GroupBO;
import com.leyunone.codex.model.vo.GroupVO;
import com.leyunone.codex.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupControl {

    @Autowired
    private ActionService actionService;

    @RequestMapping("/groups")
    public DataResponse groups(){
        List<GroupVO> groups = actionService.getGroups();
        return DataResponse.of(groups);
    }

    @RequestMapping("/noGroupUsers")
    public DataResponse noGroupUsers(Integer groupId){
        List<String> userVOS = actionService.noGroupUsers(groupId);
        return DataResponse.of(userVOS);
    }

    @PostMapping("/saveGroupUser")
    public DataResponse saveGroupUser(@RequestBody GroupBO groupBO){
        actionService.saveGroupUser(groupBO);
        return DataResponse.buildSuccess();
    }
}
