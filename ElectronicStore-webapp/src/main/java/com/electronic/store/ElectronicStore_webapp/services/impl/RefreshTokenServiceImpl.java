package com.electronic.store.ElectronicStore_webapp.services.impl;

import com.electronic.store.ElectronicStore_webapp.dtos.RefreshTokenDto;
import com.electronic.store.ElectronicStore_webapp.dtos.UserDto;
import com.electronic.store.ElectronicStore_webapp.entities.RefreshToken;
import com.electronic.store.ElectronicStore_webapp.entities.User;
import com.electronic.store.ElectronicStore_webapp.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore_webapp.repositories.RefreshTokenRepository;
import com.electronic.store.ElectronicStore_webapp.repositories.UserRepository;
import com.electronic.store.ElectronicStore_webapp.services.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository tokenRepository;

    @Autowired
    private ModelMapper mapper;
    @Override
    public RefreshTokenDto createRefreshToken(String username) {

        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not " +
                "found!!"));
        RefreshToken refreshToken = tokenRepository.findByUser(user).orElse(null);
        if(refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusSeconds(5 * 24 * 60 * 60))
                    .build();
        }else{
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusSeconds(5 * 24 * 60 * 60));
        }
        RefreshToken savedRefreshToken = tokenRepository.save(refreshToken);
        return mapper.map(savedRefreshToken, RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto findByToken(String token) {
        RefreshToken refreshToken = tokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Token Not found!!"));

        return mapper.map(refreshToken,RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto) {
        var token = mapper.map(refreshTokenDto, RefreshToken.class);
        if(refreshTokenDto.getExpiryDate().compareTo(Instant.now()) < 0){
            tokenRepository.delete(token);
            throw new RuntimeException("Refresh Token Expired!!");
        }
        return refreshTokenDto;
    }

    @Override
    public UserDto getUser(RefreshTokenDto dto) {
        RefreshToken refreshToken = tokenRepository.findByToken(dto.getToken()).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
        User user = refreshToken.getUser();
        return mapper.map(user,UserDto.class);
    }
}
