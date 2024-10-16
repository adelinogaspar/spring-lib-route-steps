package com.gaspar.observability.controller;

import com.gaspar.observability.controller.domain.UserResponse;
import com.gaspar.observability.integration.users.client.mapper.UserMapper;
import com.gaspar.observability.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UsersController {

    @Autowired
    ProcessService processService;

    @Autowired
    UserMapper userMapper;

    @GetMapping({"/user/{userId}", "/user"})
    List<UserResponse> getUsers(
            @PathVariable(required = false) Optional<String> userId
            ) {
        return userMapper.userListToUserListResponse(processService.getUserListFromOutside(userId));
    }
}
