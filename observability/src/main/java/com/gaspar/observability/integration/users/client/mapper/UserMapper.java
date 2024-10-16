package com.gaspar.observability.integration.users.client.mapper;

import com.gaspar.observability.controller.domain.UserResponse;
import com.gaspar.observability.integration.users.client.domain.UserClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse map(UserClientResponse value);
    List<UserResponse> userListToUserListResponse(List<UserClientResponse> userList);
}
