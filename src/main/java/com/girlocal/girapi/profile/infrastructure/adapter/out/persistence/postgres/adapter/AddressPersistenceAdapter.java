package com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.adapter;

import com.girlocal.girapi.profile.application.port.out.AddressPort;
import com.girlocal.girapi.profile.domain.model.Address;
import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.entity.AddressEntity;
import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.mapper.AddressMapper;
import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AddressPersistenceAdapter implements AddressPort {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findById(UUID id) {
        return addressRepository.findById(id)
                .map(addressMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findByUserId(UUID userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(addressMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void save(Address address) {
        addressRepository.save(addressMapper.toEntity(address));
    }

    @Override
    @Transactional
    public void delete(Address address) {
        addressRepository.delete(addressMapper.toEntity(address));
    }
}
