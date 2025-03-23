package com.csq.fweb.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csq.fweb.gateway.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}    