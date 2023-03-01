package com.leyunone.codex.control;

import com.leyunone.codex.model.DataResponse;
import com.leyunone.codex.model.vo.UserVO;
import com.leyunone.codex.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControl {

    @Autowired
    private ActionService actionService;

    @RequestMapping("/alluser")
    public DataResponse getUser(Integer index, Integer size) {
        List<UserVO> userVOS = actionService.queryUserCodex(index, size);
        return DataResponse.of(userVOS);
    }

}
