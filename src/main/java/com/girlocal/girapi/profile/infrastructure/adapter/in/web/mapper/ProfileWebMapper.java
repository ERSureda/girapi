package com.girlocal.girapi.profile.infrastructure.adapter.in.web.mapper;

import com.girlocal.girapi.profile.application.command.CreateAddressCommand;
import com.girlocal.girapi.profile.application.command.DeleteAddressCommand;
import com.girlocal.girapi.profile.application.command.UpdateAddressCommand;
import com.girlocal.girapi.profile.application.command.UpdateProfileCommand;
import com.girlocal.girapi.profile.infrastructure.adapter.in.web.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ProfileWebMapper {

    UpdateProfileCommand toUpdateProfileCommand(UpdateProfileHttpRequest request);
    CreateAddressCommand toCreateAddressCommand(CreateAddressHttpRequest request);

    @Mapping(target = "addressId", source = "addressId")
    UpdateAddressCommand toUpdateAddressCommand(UUID addressId, UpdateAddressHttpRequest request);

    DeleteAddressCommand toDeleteAddressCommand(UUID addressId);
}
