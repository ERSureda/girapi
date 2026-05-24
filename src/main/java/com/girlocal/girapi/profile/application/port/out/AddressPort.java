package com.girlocal.girapi.profile.application.port.out;

import com.girlocal.girapi.profile.domain.model.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressPort {
    Optional<Address> findById(UUID id);
    List<Address> findByUserId(UUID profileId);
    void save(Address address);
    void delete(Address address);
}
