package com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.mapper;

import com.girlocal.girapi.identity.domain.model.VerificationToken;
import com.girlocal.girapi.identity.domain.model.enums.VerificationTokenType;
import com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.postgres.entity.VerificationTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface VerificationTokenMapper {

    default VerificationTokenEntity toEntity(VerificationToken domain) {
        if (domain == null) {
            return null;
        }
        VerificationTokenEntity entity = new VerificationTokenEntity();
        entity.setId(domain.getToken());
        entity.setUserId(domain.getUserId());
        entity.setType(domain.getType() != null ? domain.getType().name() : null);
        entity.setExpiresAt(domain.getExpiryDate());
        return entity;
    }

    default VerificationToken toDomain(VerificationTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        return VerificationToken.reconstruct(
                entity.getId(),
                entity.getUserId(),
                entity.getType() != null ? VerificationTokenType.valueOf(entity.getType()) : null,
                entity.getExpiresAt()
        );
    }
}
