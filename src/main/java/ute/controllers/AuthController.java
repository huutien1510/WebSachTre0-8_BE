package ute.controllers;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.*;
import ute.dto.response.*;
import ute.services.AuthService;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    @PostMapping("/login")
    public SuccessResponse<LoginResponse> authenticate(@RequestBody LoginRequest request,  HttpServletResponse response) {
        var result = authService.authenticate(request, response);
        return SuccessResponse.<LoginResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("/introspect")
    public SuccessResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authService.introspect(request);
        return SuccessResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("/register")
    public ResponseDetail createAccount(@RequestBody UserCreationRequest request, HttpServletResponse response) {
        return authService.createAccount(request);
    }
    @GetMapping("/verify")
    public void verify(@RequestParam String email, @RequestParam String token, HttpServletResponse response) {
        authService.verify(email, token, response);
    }
    @PostMapping("/logout")
    public SuccessResponse<String> logout(HttpServletResponse response) {
        authService.logout(response);
        return SuccessResponse.<String>builder()
                .data("Logout successfully")
                .build();
    }
    @PostMapping("/refresh")
    public SuccessResponse<RefreshResponse> refresh(HttpServletRequest request, HttpServletResponse response) throws ParseException, JOSEException {
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
        return SuccessResponse.<RefreshResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDetail> forgotPassword(@RequestBody ForgotRequest request) {
        ResponseDetail response = authService.forgotPassword(request);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDetail> resetPassword(@RequestBody ResetPasswordRequest request) {
        ResponseDetail response = authService.resetPassword(request);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }


}
