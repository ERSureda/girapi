package com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.adapter;

import com.girlocal.girapi.identity.application.port.out.VerificationTokenPort;
import com.girlocal.girapi.identity.domain.model.VerificationToken;
import com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.mapper.VerificationTokenMapper;
import com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VerificationTokenPersistanceAdapter implements VerificationTokenPort {

    private final VerificationTokenRepository verificationTokenRepository;
    private final VerificationTokenMapper verificationTokenMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<VerificationToken> findById(UUID id) {
        return verificationTokenRepository.findById(id)
                .map(verificationTokenMapper::toDomain);
    }

    @Override
    @Transactional
    public void save(VerificationToken token) {
        verificationTokenRepository.save(verificationTokenMapper.toEntity(token));
    }

    @Override
    @Transactional
    public void delete(VerificationToken token) {
        verificationTokenRepository.delete(verificationTokenMapper.toEntity(token));
    }
}
