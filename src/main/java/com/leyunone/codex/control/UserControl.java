package com.leyunone.codex.control;

import com.leyunone.codex.model.DataResponse;
import com.leyunone.codex.model.bo.UserBO;
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

    @RequestMapping("/login")
    public DataResponse login(String account, String password) {
        //TODO 登录调试
//        if(account.equals("leyunone") && password.equals("admin")){
            return DataResponse.buildSuccess();
//        }else{
//            return DataResponse.buildFailure("账号或密码错误");
//        }
    }

    @RequestMapping("/save")
    public DataResponse save(UserBO userBO){
        actionService.saveUser(userBO);
        return DataResponse.buildSuccess();
    }

}
