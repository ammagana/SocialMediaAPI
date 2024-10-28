package com.cooksys.project1.mappers;

import com.cooksys.project1.dtos.HashtagDto;
import com.cooksys.project1.entities.HashTag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HashtagMapper {
    HashtagDto entityToDto(HashTag entity);
    List<HashtagDto> entitiesToDtos(List<HashTag> entities);
}
