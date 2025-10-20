package com.koto.web.mapper;

import com.koto.service.bo.appuser.CreateUserBo;
import com.koto.web.dto.appuser.CreateUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateUserBoToDtoMapper {
	CreateUserDto toDto (final CreateUserBo bo);
	CreateUserBo toBo (final CreateUserDto dto);
}
