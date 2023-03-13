package com.leyunone.codex.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.codex.dao.UserDao;
import com.leyunone.codex.dao.entry.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserNameUtils {

    public static Map<String, String> getUserRealNames() {
        UserDao userDao = ApplicationContextProvider.getBean(UserDao.class);
        List<User> users = userDao.selectByCon(null);
        Map<String, String> userMap = users.stream().filter((t) -> StringUtils.isNotBlank(t.getUserRealName())).
                collect(Collectors.toMap(User::getUserName, User::getUserRealName));
        return userMap;
    }
}
