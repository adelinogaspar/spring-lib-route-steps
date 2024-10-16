package com.gaspar.observability.service;

import com.gaspar.observability.integration.users.client.UserClient;
import com.gaspar.observability.integration.users.client.domain.UserClientResponse;
import com.gaspar.util.logs.annotation.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessService {

    @Autowired
    UserClient userClient;

    @Loggable
    public List<UserClientResponse> getUserListFromOutside(Optional<String> userId) {
        List<UserClientResponse> userClientResponseList = new ArrayList<>();
        if (userId.isEmpty()) {
            return userClient.getUserList(userId);
        }

        userClientResponseList.add(userClient.getUser(userId));
        return userClientResponseList;
    }
}
