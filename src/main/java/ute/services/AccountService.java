package ute.services;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ute.dto.request.AccountUpdateRequest;
import ute.dto.request.UserCreationRequest;
import ute.entity.Account;
import ute.enums.Roles;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.mapper.AccountMapper;
import ute.repository.AccountRepository;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    AccountRepository accountRepository;
    AccountMapper accountMapper;
    PasswordEncoder passwordEncoder;

    public Account createRequest(UserCreationRequest request) {
        // In giá trị của request


        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> role = new HashSet<>();
        role.add(Roles.USER.name());
        account.setRoles(role);
        account.setName(request.getUsername());
        return accountRepository.save(account);
    }

    @PreAuthorize("hasRole('ADMIN')") // set quyền cho chuc nang getAllAccounts, xet quyen truoc khi thuc hien chuc nang
    public List<Account> getAllAccounts() {
        log.info("Get all accounts");
        return accountRepository.findAll();
    }
    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')") // set quyền cho chuc nang getAccountById, set quyen sau khi thuc hien chuc nang
    public Account getAccountById(Integer id) {
        log.info("Get account by id");
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account không tìm thấy."));
    }

    public Account getMyInfo(){
        var context =   SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account account = accountRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return account;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Account updateAccount(Integer id, AccountUpdateRequest request) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account không tìm thấy."));
        accountMapper.updateAccount(account, request);
        account.setPhone(request.getPhone());
        if (request.getIs_admin()) {
            account.setRoles(new HashSet<>(List.of(Roles.ADMIN.name())));
        } else {
            account.setRoles(new HashSet<>(List.of(Roles.USER.name())));
        }
        return accountRepository.save(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAccount(Integer id) {
        accountRepository.deleteById(id);
    }
}