package ute.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.AccountUpdateRequest;
import ute.dto.request.UserCreationRequest;
import ute.dto.response.SuccessResponse;
import ute.entity.Account;
import ute.services.AccountService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    public SuccessResponse<Account> createAccount(@RequestBody @Valid UserCreationRequest account) {
        SuccessResponse<Account> response = new SuccessResponse<>();
        response.setSuccess(true);
        response.setStatus(200);
        response.setMessage("Create account successfully");
        response.setData(accountService.createRequest(account));
        return response;
    }

    @GetMapping
    public SuccessResponse<List<Account>> getAllAccounts() {
        return SuccessResponse.<List<Account>>builder()
                .success(true)
                .status(200)
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
}