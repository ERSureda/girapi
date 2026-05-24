package com.girlocal.girapi.identity.infrastructure.adapter.in.web.mapper;

import com.girlocal.girapi.identity.application.command.*;
import com.girlocal.girapi.identity.infrastructure.adapter.in.web.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AuthWebMapper {

    LoginUserCommand toLoginUserCommand(LoginUserHttpRequest request);
    RegisterUserCommand toRegisterUserCommand(RegisterUserHttpRequest request);
    VerifyUserCommand toVerifyUserCommand(String token);
    ForgotPasswordUserCommand toForgotPasswordUserCommand(ForgotPasswordUserHttpRequest request);
    ResetPasswordUserCommand toResetPasswordUserCommand(ResetPasswordUserHttpRequest request);
    UpdatePasswordUserCommand toUpdatePasswordUserCommand(UpdatePasswordUserHttpRequest request);
}
