package com.gaspar.observability.integration.users.client;

import com.gaspar.observability.integration.users.client.domain.UserClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "userClient")
public interface UserClient {

    @GetMapping(value = "/users/{userId}")
    List<UserClientResponse> getUserList(
            @PathVariable(required = false) Optional<String> userId
    );

    @GetMapping(value = "/users/{userId}")
    UserClientResponse getUser(
            @PathVariable(required = false) Optional<String> userId
    );
}
