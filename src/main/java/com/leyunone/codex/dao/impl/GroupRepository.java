package com.leyunone.codex.dao.impl;

import com.leyunone.codex.dao.GroupDao;
import com.leyunone.codex.dao.entry.Group;
import com.leyunone.codex.dao.mapper.GroupMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GroupRepository extends BaseRepository<GroupMapper, Group> implements GroupDao {

}
