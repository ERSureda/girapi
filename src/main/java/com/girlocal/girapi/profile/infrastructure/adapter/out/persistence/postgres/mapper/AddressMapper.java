package com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.mapper;

import com.girlocal.girapi.profile.domain.model.Address;
import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AddressMapper {

    default AddressEntity toEntity(Address domain) {
        if (domain == null) {
            return null;
        }
        AddressEntity entity = new AddressEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setAddressLine1(domain.getAddressLine1());
        entity.setAddressLine2(domain.getAddressLine2());
        entity.setLocality(domain.getLocality());
        entity.setAdministrativeArea(domain.getAdministrativeArea());
        entity.setPostalCode(domain.getPostalCode());
        entity.setCountryCode(domain.getCountryCode());
        return entity;
    }

    default Address toDomain(AddressEntity entity) {
        if (entity == null) {
            return null;
        }
        return Address.reconstruct(
                entity.getId(),
                entity.getUserId(),
                entity.getAddressLine1(),
                entity.getAddressLine2(),
                entity.getLocality(),
                entity.getAdministrativeArea(),
                entity.getPostalCode(),
                entity.getCountryCode()
        );
    }
}
