package com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.mapper;

import com.girlocal.girapi.identity.domain.model.User;
import com.girlocal.girapi.identity.domain.model.enums.UserRole;
import com.girlocal.girapi.identity.domain.model.enums.UserStatus;
import com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {

    default UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setEmail(domain.getEmail());
        entity.setPasswordHash(domain.getPasswordHash());
        entity.setRole(domain.getRole() != null ? domain.getRole().name() : null);
        entity.setStatus(domain.getStatus() != null ? domain.getStatus().name() : null);
        return entity;
    }

    default User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.reconstruct(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole() != null ? UserRole.valueOf(entity.getRole()) : null,
                entity.getStatus() != null ? UserStatus.valueOf(entity.getStatus()) : null
        );
    }
}
