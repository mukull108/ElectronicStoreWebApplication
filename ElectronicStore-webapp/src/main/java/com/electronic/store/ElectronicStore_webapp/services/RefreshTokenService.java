package com.electronic.store.ElectronicStore_webapp.services;

import com.electronic.store.ElectronicStore_webapp.dtos.RefreshTokenDto;
import com.electronic.store.ElectronicStore_webapp.dtos.UserDto;
import com.electronic.store.ElectronicStore_webapp.entities.RefreshToken;
import com.electronic.store.ElectronicStore_webapp.entities.User;

public interface RefreshTokenService {
    RefreshTokenDto createRefreshToken(String username);
    RefreshTokenDto findByToken(String token);
    RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto);
    UserDto getUser(RefreshTokenDto dto);
}
