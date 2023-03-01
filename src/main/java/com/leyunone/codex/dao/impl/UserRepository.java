package com.leyunone.codex.dao.impl;

import com.leyunone.codex.dao.UserDao;
import com.leyunone.codex.dao.entry.User;
import com.leyunone.codex.dao.mapper.UserMapper;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.vo.UserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends BaseRepository<UserMapper, User> implements UserDao {
}
