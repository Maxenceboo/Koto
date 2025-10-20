package com.koto.service.mapper;

import com.koto.domain.AppUser;
import com.koto.service.bo.appuser.AppUserBo;
import com.koto.web.dto.appuser.AppUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserEntityToBoMapper {
    AppUserBo toBo (final AppUser entity);
	AppUserDto toDto (final AppUserBo bo);
}