package com.notes.selling.repo;

import java.util.Optional;
import java.util.UUID;

import com.notes.selling.helper.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import com.notes.selling.entity.Users;

public interface AuthRepository extends JpaRepository<Users , UUID>{

    Optional<Users> findByUsername(String username);

    Optional<Users> findByProviderAndProviderId(
            Provider provider,
            String providerId
    );

    Optional<Users> findByEmail(String email);

    Optional<Users> findByProviderId(String subId);
}
