package com.leyunone.codex.dao.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("x_group_user")
public class GroupUser {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer groupId;

    private String userName;
}
