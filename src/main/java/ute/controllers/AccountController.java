package ute.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.AccountUpdateRequest;
import ute.dto.request.UpdateMyInfoRequest;
import ute.dto.response.ApiResponse;
import ute.entity.Account;
import ute.services.AccountService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    public ApiResponse<List<Account>> getAllAccounts() {
        return ApiResponse.<List<Account>>builder()
                .code(200)
                .message("Get all accounts successfully")
                .data(accountService.getAllAccounts())
                .build();
    }
    @GetMapping("/my-info")
    public Account getMyInfo() {
        return accountService.getMyInfo();
    }

    @GetMapping("/account/{id}")
    public Account getAccountById(@PathVariable Integer id) {
        return accountService.getAccountById(id);
    }

    @PutMapping("/account/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Integer id, @RequestBody AccountUpdateRequest accountUpdateRequest) {
        Account updatedAccount = accountService.updateAccount(id, accountUpdateRequest);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/account/{id}")
    public void deleteAccount(@PathVariable Integer id) {
        accountService.deleteAccount(id);
    }

    @PutMapping("/account/my-info")
    public ApiResponse<Account> updateMyInfo(@Valid @RequestBody UpdateMyInfoRequest accountUpdateRequest) {
        Account result =  accountService.updateMyInfo(accountUpdateRequest);
        return ApiResponse.<Account>builder()
                .code(200)
                .data(result)
                .build();
    }
}