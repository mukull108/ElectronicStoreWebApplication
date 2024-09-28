package com.electronic.store.ElectronicStore_webapp.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequest {
    private String username;
    private String password;
}
