package ute.controllers;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.*;
import ute.dto.response.*;
import ute.services.AuthService;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    @PostMapping("/login")
    public ApiResponse<LoginResponse> authenticate(@RequestBody LoginRequest request,  HttpServletResponse response) {
        var result = authService.authenticate(request, response);
        return ApiResponse.<LoginResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("/register")
    public ApiResponse createAccount(@RequestBody UserCreationRequest request, HttpServletResponse response) {
        return authService.createAccount(request);
    }
    @GetMapping("/verify")
    public void verify(@RequestParam String email, @RequestParam String token, HttpServletResponse response) {
        authService.verify(email, token, response);
    }
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletResponse response) {
        authService.logout(response);
        return ApiResponse.<String>builder()
                .data("Logout successfully")
                .build();
    }
    @PostMapping("/refresh")
    public ApiResponse<RefreshResponse> refresh(HttpServletRequest request, HttpServletResponse response) throws ParseException, JOSEException {
        // Lấy cookie từ request
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) { // Tên cookie lưu trữ refresh token
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token not found in cookies");
        }

        // Gọi service để làm mới token
        var result = authService.refreshToken(refreshToken, response);

        // Trả về response thành công
        return ApiResponse.<RefreshResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(@RequestBody ForgotRequest request) {
        String response = authService.forgotPassword(request);
        return ApiResponse.builder()
                .message(response)
                .build();
    }
    @PostMapping("/reset-password")
    public ApiResponse resetPassword(@RequestBody ResetPasswordRequest request) {
        String response = authService.resetPassword(request);
        return ApiResponse.builder()
                .message(response)
                .build();
    }


}
