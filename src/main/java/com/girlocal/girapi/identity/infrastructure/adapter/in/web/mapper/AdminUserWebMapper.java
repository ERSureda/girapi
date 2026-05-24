package com.girlocal.girapi.identity.infrastructure.adapter.in.web.mapper;

import com.girlocal.girapi.identity.application.command.AdminForceResetUserCommand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

// TODO: Validate.
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AdminUserWebMapper {
    AdminForceResetUserCommand toAdminForceResetUserCommand(String userId);
}
