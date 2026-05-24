package com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.dynamodb.mapper;

import com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.dynamodb.entity.SessionEntity;
import com.girlocal.girapi.shared.domain.model.AuthenticatedUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SessionMapper {

    default SessionEntity toEntity(AuthenticatedUser domain) {
        if (domain == null) {
            return null;
        }
        SessionEntity entity = new SessionEntity();
        entity.setUserId(domain.userId());
        entity.setRole(domain.role());
        return entity;
    }

    default AuthenticatedUser toDomain(SessionEntity entity) {
        if (entity == null) {
            return null;
        }
        return new AuthenticatedUser(
                entity.getUserId(),
                entity.getRole()
        );
    }
}
