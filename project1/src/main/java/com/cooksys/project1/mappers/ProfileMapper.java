package com.cooksys.project1.mappers;

import ch.qos.logback.core.model.ComponentModel;
import com.cooksys.project1.dtos.ProfileDto;
import com.cooksys.project1.embeddables.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel ="spring")
public interface ProfileMapper {
    Profile dtoToEntity(ProfileDto profileDto);
}
