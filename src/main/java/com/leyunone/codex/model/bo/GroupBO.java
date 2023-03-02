package com.leyunone.codex.model.bo;

import lombok.Data;

import java.util.List;

@Data
public class GroupBO {

    private Integer groupId;

    private List<String> userNames;
}
