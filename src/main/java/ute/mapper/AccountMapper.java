package ute.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ute.dto.request.AccountUpdateRequest;
import ute.dto.request.UserCreationRequest;
import ute.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toAccount(UserCreationRequest request);
    void updateAccount(@MappingTarget Account account, AccountUpdateRequest request);
}