package com.csq.fweb.gateway.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role_resources")
public class RoleResource {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String role;
    private String resourceUrl;
}    