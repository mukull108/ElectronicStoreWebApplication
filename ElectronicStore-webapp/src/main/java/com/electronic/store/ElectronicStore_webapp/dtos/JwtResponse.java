package com.electronic.store.ElectronicStore_webapp.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private UserDto userDto;
    private RefreshTokenDto refreshToken;
}
