package com.project.mapper;

import com.project.dto.UserInfoResponseDto;
import com.project.dto.UserSaveRequestDto;
import com.project.dto.UserUpdateRequestDto;
import com.project.dto.UserUpdateResponseDto;
import com.project.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity userSaveRequestDtoToUser(UserSaveRequestDto userSaveRequestDto);

    UserInfoResponseDto userToInfoResponseDto(UserEntity users);

    UserEntity userSaveRequestDtoToUserEntity (UserSaveRequestDto userSaveRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserUpdateRequestDto dto, @MappingTarget UserEntity entity);

    UserUpdateResponseDto userEntityToUserUpdateResponseDto(UserEntity userEntity);


}
