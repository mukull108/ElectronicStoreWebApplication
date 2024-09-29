package com.electronic.store.ElectronicStore_webapp.dtos;

import com.electronic.store.ElectronicStore_webapp.entities.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDto {
    private int id;
    private String token;
    private Instant expiryDate;
}
