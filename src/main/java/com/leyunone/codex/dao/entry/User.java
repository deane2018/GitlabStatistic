package com.leyunone.codex.dao.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("x_user")
public class User {

    private String userName;

    private String userEmail;

    private Integer codeAdditions;

    private Integer codeDeletions;

    private Integer codeTotal;
}
