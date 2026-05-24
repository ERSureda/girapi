package com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.adapter;

import com.girlocal.girapi.identity.application.port.out.UserPort;
import com.girlocal.girapi.identity.domain.model.User;
import com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.mapper.UserMapper;
import com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.repository.UserRepository;
import com.girlocal.girapi.shared.application.port.out.OutboxPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final OutboxPublisherPort outboxPublisherPort;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(userMapper.toEntity(user));
        if (user.hasDomainEvents()) {
            outboxPublisherPort.publishAll(user.pullDomainEvents());
        }
    }
}
