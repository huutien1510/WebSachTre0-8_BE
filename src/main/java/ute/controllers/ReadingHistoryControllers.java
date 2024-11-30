package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.ApiResponse;
import ute.dto.request.ReadingHistoryRequest;
import ute.dto.response.ReadingHistoryResponse;
import ute.entity.Account;
import ute.services.ReadingHistoryServices;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/readinghistory")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReadingHistoryControllers {
    ReadingHistoryServices readingHistoryServices;
    @GetMapping("account/{accountID}")
    public ApiResponse<List<ReadingHistoryResponse>> getReadingHistoryByAccount(@PathVariable int accountID) {
        ApiResponse<List<ReadingHistoryResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(readingHistoryServices.getReadingHistoryByAccount(accountID));
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<Account> addReadingHistory(@RequestBody ReadingHistoryRequest body) {
        ApiResponse<Account> apiResponse = new ApiResponse<>();
        apiResponse.setData(readingHistoryServices.addReadingHistory(body));
        return apiResponse;
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<String> handleRuntimeException(RuntimeException ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(500);
        apiResponse.setMessage(ex.toString());
        apiResponse.setData("Fault data");
        return apiResponse;
    }
}
