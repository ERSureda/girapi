package com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.repository;

import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
    List<AddressEntity> findByUserId(UUID userId);
}
