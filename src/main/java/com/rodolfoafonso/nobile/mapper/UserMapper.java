package com.rodolfoafonso.nobile.mapper;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.dto.UserResponseDTO;
import com.rodolfoafonso.nobile.dto.UserUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@org.mapstruct.Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(target = "id", ignore = true)
    User mapper(UserDTO userDTO) ;

    UserDTO mapper(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserUpdateDTO dto, @MappingTarget User entity);


    UserResponseDTO mapperResponse(User user);
}