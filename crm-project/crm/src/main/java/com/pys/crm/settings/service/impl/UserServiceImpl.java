package com.pys.crm.settings.service.impl;

import com.pys.crm.settings.domain.User;
import com.pys.crm.settings.mapper.UserMapper;
import com.pys.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        User user = userMapper.selectUserByLoginActAndPwd(map);
        return user;
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }
}
