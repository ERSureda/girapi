package com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.repository;

import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {
}
