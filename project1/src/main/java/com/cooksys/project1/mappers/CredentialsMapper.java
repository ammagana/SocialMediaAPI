package com.cooksys.project1.mappers;

import com.cooksys.project1.dtos.CredentialsDto;
import com.cooksys.project1.embeddables.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
    //Credentials dtoToEntity(CredentialsDto credentialsDto);

    Credentials dtoToCredentials(CredentialsDto credentialsDto);

}
