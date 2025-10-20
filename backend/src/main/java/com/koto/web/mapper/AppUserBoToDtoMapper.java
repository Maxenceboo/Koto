package com.koto.web.mapper;

import com.koto.service.bo.appuser.AppUserBo;
import com.koto.web.dto.appuser.AppUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserBoToDtoMapper {
    AppUserDto toDto (final AppUserBo bo);
}
