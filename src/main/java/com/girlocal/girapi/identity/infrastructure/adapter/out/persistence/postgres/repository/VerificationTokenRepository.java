package com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.repository;

import com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, UUID> {
}
