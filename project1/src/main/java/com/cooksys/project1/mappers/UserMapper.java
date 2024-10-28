package com.cooksys.project1.mappers;

import ch.qos.logback.core.model.ComponentModel;
import com.cooksys.project1.dtos.UserRequestDto;
import com.cooksys.project1.dtos.UserResponseDto;
import com.cooksys.project1.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel ="spring", uses = {CredentialsMapper.class, ProfileMapper.class})
public interface UserMapper{

  @Mapping(target = "username", source = "credentials.username")
  UserResponseDto entityToDto(User user);

  List<UserResponseDto> entitiesToDtos(List<User> entities);

  User dtoToEntity(UserRequestDto userRequestDto);

  User dtoToEntity(UserResponseDto userDto);
}
