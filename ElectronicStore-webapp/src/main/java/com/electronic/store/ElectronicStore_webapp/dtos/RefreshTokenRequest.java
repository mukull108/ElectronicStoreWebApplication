package com.electronic.store.ElectronicStore_webapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class RefreshTokenRequest {
    private String refreshToken;

}
