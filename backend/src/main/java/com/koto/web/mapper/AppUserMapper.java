package com.koto.web.mapper;

import com.koto.domain.AppUser;
import com.koto.web.dto.appuser.AppUserDto;

public class AppUserMapper {
    public static AppUserDto toDto(AppUser u) {
        return new AppUserDto(u.getId(), u.getUsernameGlobal(), u.getCreatedAt());
    }
}
