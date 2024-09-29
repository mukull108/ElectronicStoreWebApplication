package com.electronic.store.ElectronicStore_webapp.repositories;

import com.electronic.store.ElectronicStore_webapp.entities.RefreshToken;
import com.electronic.store.ElectronicStore_webapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
