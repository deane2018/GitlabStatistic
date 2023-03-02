package com.leyunone.codex.dao.impl;

import com.leyunone.codex.dao.UserDao;
import com.leyunone.codex.dao.entry.User;
import com.leyunone.codex.dao.mapper.UserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BaseRepository<UserMapper, User> implements UserDao {

}
