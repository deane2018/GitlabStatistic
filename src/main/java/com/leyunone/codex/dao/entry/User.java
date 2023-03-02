package com.leyunone.codex.dao.entry;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("x_user")
public class User {

    @TableId(value = "user_name")
    private String userName;

    private String userEmail;

    private String userRealName;

    private Integer codeAdditions;

    private Integer codeDeletions;

    private Integer codeTotal;
}
