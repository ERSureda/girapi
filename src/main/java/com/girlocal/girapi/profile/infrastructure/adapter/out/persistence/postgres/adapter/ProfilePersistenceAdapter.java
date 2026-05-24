package com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.adapter;

import com.girlocal.girapi.profile.application.port.out.ProfilePort;
import com.girlocal.girapi.profile.domain.model.Profile;
import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.entity.ProfileEntity;
import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.mapper.ProfileMapper;
import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProfilePersistenceAdapter implements ProfilePort {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Profile> findByUserId(UUID userId) {
        return profileRepository.findById(userId)
                .map(profileMapper::toDomain);
    }

    @Override
    @Transactional
    public Profile save(Profile profile) {
        ProfileEntity entity = profileMapper.toEntity(profile);
        ProfileEntity savedEntity = profileRepository.save(entity);
        return profileMapper.toDomain(savedEntity);
    }
}
