package com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.mapper;

import com.girlocal.girapi.profile.domain.model.Address;
import com.girlocal.girapi.profile.domain.model.Profile;
import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.entity.AddressEntity;
import com.girlocal.girapi.profile.infrastructure.adapter.out.persistence.postgres.entity.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ProfileMapper {

    default ProfileEntity toEntity(Profile domain) {
        if (domain == null) {
            return null;
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setId(domain.getId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setPhone(domain.getPhone());
        entity.setActiveAddressId(domain.getActiveAddressId());
        return entity;
    }

    default Profile toDomain(ProfileEntity entity) {
        if (entity == null) {
            return null;
        }
        return Profile.reconstruct(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getActiveAddressId()
        );
    }
}
