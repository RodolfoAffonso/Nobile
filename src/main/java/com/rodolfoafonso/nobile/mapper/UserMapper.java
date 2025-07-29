package com.rodolfoafonso.nobile.mapper;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.dto.UserDTO;
import org.mapstruct.Mapping;


@org.mapstruct.Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User mapper(UserDTO userDTO) ;

    UserDTO mapper(User user);
}