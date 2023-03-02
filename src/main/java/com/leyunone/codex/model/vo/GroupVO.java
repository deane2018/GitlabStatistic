package com.leyunone.codex.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class GroupVO {

    private Integer groupId;

    private String groupName;

    private List<GroupUserVO> groupUsers;
}
