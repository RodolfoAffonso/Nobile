package com.rodolfoafonso.nobile.mapper;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.dto.UserResponseDTO;
import org.mapstruct.Mapping;


@org.mapstruct.Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(target = "id", ignore = true)
    User mapper(UserDTO userDTO) ;

    UserDTO mapper(User user);

    User mapper(UserResponseDTO userResponseDTO);
    UserResponseDTO mapperResponse(User user);
}