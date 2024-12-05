package ute.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ute.dto.request.*;
import ute.dto.response.*;
import ute.entity.Account;
import ute.entity.Cart;
import ute.enums.Roles;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.mapper.AccountMapper;
import ute.repository.AccountRepository;
import ute.util.EmailUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    final AccountRepository accountRepository;
    final EmailUtil emailUtil;
    AccountMapper accountMapper;
    @NonFinal
    @Value("${signer.key}")
    String SIGNER_KEY;
    @NonFinal
    @Value("${auth.valid.duration}")
    long VALID_DURATION;
    @NonFinal
    @Value("${refreshable.duration}")
    long REFRESHABLE_DURATION;
    @NonFinal
    @Value("${frontend.url}")
    String frontendUrl;
    @NonFinal
    @Value("${app.url}")
    String appUrl;
    @NonFinal
    @Value("${bcrypt.salt.round}")
    int bcryptSaltRound;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public ApiResponse createAccount(UserCreationRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String username = request.getUsername();
        String name = username;
        Date birthday = request.getBirthday();
        String sex = request.getSex();

        if (email != null && password != null && name != null && username != null && birthday != null && sex != null) {
            try {
                Account existingUser = accountRepository.findByEmail(email);
                if (existingUser != null && existingUser.getIs_active()) {
                    throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
                }
                if (existingUser != null && !(existingUser.getIs_active())) {
                    accountRepository.delete(existingUser);
                }
                Optional<Account> existingUsername = accountRepository.findByUsername(username);
                log.info("Existing username: " + existingUsername);
                if (!existingUsername.isEmpty()) {
                    throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
                }
                if (password.length() < 8) {
                    throw new AppException(ErrorCode.PASSWORD_INVALID);
                }

                Date today = new Date();
                if (birthday.after(today)) {
                    throw new AppException(ErrorCode.INVALID_BIRTHDAY);
                }

                String hashedPassword = passwordEncoder.encode(password);
                Account account = new Account();
                account.setEmail(email);
                account.setUsername(username);
                account.setName(name);
                account.setBirthday(birthday);
                account.setSex(sex);
                account.setPassword(hashedPassword);
                HashSet<String> role = new HashSet<>();
                role.add(Roles.USER.name());
                account.setRoles(role);
                account.setPhone(request.getPhone());
                account.setIs_active(false);
                account.setIs_admin(false);
                account.setIs_deleted(false);
                account.setBonusPoint(0);
                Cart cart = new Cart();
                cart.setAccount(account); // Liên kết với tài khoản
                account.setCarts(cart);
                account.setAvatar("https://1.bp.blogspot.com/-CV8fOXMMw60/YZ-UJ4X9sAI/AAAAAAAACMc/2Svet97exjgNdJ9CeTKUU3OuA-mnCQEzwCLcBGAsYHQ/s595/3a.jpg");

                Account savedUser =  accountRepository.save(account);

                String hashedEmail = passwordEncoder.encode(savedUser.getEmail());
                String verifyUrl = appUrl + "/auth/verify?email=" + savedUser.getEmail() + "&token=" + hashedEmail;
                String emailTemplatePath = "src/main/resources/templates/email.html";
                String emailTemplate = new String(Files.readAllBytes(Paths.get(emailTemplatePath)));

                String emailContent = emailTemplate.replace("{{verifyUrl}}", verifyUrl);
                emailUtil.sendMail(savedUser.getEmail(), "Chỉ còn một bước nữa để hoàn tất đăng ký của bạn!", emailContent);

                return ApiResponse.builder()
                        .message("Tài khoản đăng ký thành công. Vui lòng xác nhận qua email.")
                        .code(201)
                        .build();
            } catch (IOException e) {
                log.error("Error reading email template", e);
                throw new AppException(ErrorCode.UNCAUGHT_EXCEPTION);
            }
        } else {
            throw new AppException(ErrorCode.MISSING_REQUIRED_FIELDS);
        }
    }
    public String forgotPassword(ForgotRequest request) {
        try {
            String email = request.getEmail();
            Account user = accountRepository.findByEmail(email);
            if (user == null) {
                throw new AppException(ErrorCode.EMAIL_NO_EXISTS);
            }

            String hashedEmail = passwordEncoder.encode(email);
            String resetUrl = frontendUrl + "/reset-password?email=" + email + "&token=" + hashedEmail;
            String emailTemplatePath = "src/main/resources/templates/forgotPassword.html";
            String emailTemplate = new String(Files.readAllBytes(Paths.get(emailTemplatePath)));
            String emailContent = emailTemplate.replace("{{resetUrl}}", resetUrl);

            emailUtil.sendMail(email, "Khôi phục mật khẩu", emailContent);
            return  "Đã gửi yêu cầu khôi phục mật khẩu.";
        } catch (IOException  e) {
            return "Internal Server Error";
        }
    }
    public String resetPassword(ResetPasswordRequest request) {
        try {
            String email = request.getEmail();
            String token = request.getToken();
            String newPassword = request.getNewPassword();
            if (email == null || token == null) {
                throw new AppException(ErrorCode.RESET_PASSWORD_FAILED);
            }

            boolean isMatch = passwordEncoder.matches(email, token);
            if (!isMatch) {
                throw new AppException(ErrorCode.INVALID_VERIFICATION_TOKEN);
            }

            Account user = accountRepository.findByEmail(email);
            if (user == null) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }

            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            accountRepository.save(user);

            return "Tài khoản đã được khôi phục.";
        } catch (Exception e) {
            log.error("Error resetting password", e);
            return "Internal Server Error";
        }
    }
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expirationTime.after(new Date()))
                .build();
    }

    public LoginResponse authenticate(LoginRequest request, HttpServletResponse response) {
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());
        if (!account.getIs_active()){
            throw new AppException(ErrorCode.USER_NOT_ACTIVATED);
        }

        if (!authenticated) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

        if (account.getIs_deleted()) {
            throw new AppException(ErrorCode.USER_DELETED);
        }
        var token = generateToken(account);
        var refreshToken = generateRefreshToken(account);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)          // Cookie chỉ truy cập từ phía server
                .secure(false)           // Dùng HTTP
                .path("/")              // Áp dụng cho toàn bộ domain
                .sameSite("Lax")        // SameSite bảo vệ chống CSRF
                .maxAge((int) REFRESHABLE_DURATION) // Thời gian sống tính bằng giây
                .build();

        // Thêm cookie vào header phản hồi
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return LoginResponse.builder()
                .account(account)
                .accessToken(token)
                .build();
    }
    public void verify(String email, String token, HttpServletResponse response) {
        try {
            boolean isMatch = passwordEncoder.matches(email, token);
            if (isMatch) {
                Account user = accountRepository.findByEmail(email);
                if (user != null) {
                    user.setIs_active(true);
                    accountRepository.save(user);
                    response.sendRedirect(frontendUrl + "/verification-success");
                } else {
                    throw new AppException(ErrorCode.USER_NOT_FOUND);
                }
            } else {
                throw new AppException(ErrorCode.INVALID_VERIFICATION_TOKEN);
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCAUGHT_EXCEPTION);
        }
    }

    public RefreshResponse refreshToken(String refreshToken, HttpServletResponse response) throws ParseException, JOSEException {
        // Xác thực Refresh Token
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(refreshToken);

        // Lấy thông tin trong token
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        Date expirationTime = claims.getExpirationTime();
        String username = claims.getSubject();

        // Kiểm tra tính hợp lệ và thời gian hết hạn
        if (!signedJWT.verify(verifier) || expirationTime.before(new Date())) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Lấy thông tin tài khoản
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Tạo Access Token mới
        String newAccessToken = generateToken(account);

        // Tạo Refresh Token mới
        String newRefreshToken = generateRefreshToken(account);

        // Cập nhật Refresh Token trong cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)          // Cookie chỉ truy cập từ phía server
                .secure(false)           // Dùng HTTP
                .path("/")              // Áp dụng cho toàn bộ domain
                .sameSite("Lax")        // SameSite bảo vệ chống CSRF
                .maxAge((int) REFRESHABLE_DURATION) // Thời gian sống tính bằng giây
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Trả về phản hồi
        return RefreshResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Chỉ dùng trong HTTPS
        cookie.setPath("/"); // Áp dụng cho toàn bộ domain
        cookie.setMaxAge(0); // Thời gian hết hạn
        response.addCookie(cookie);
    }

    private String generateToken(Account account) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("bookstore")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("scope", buildScope(account))
                .build();
        Payload payload = new Payload(claims.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error signing token", e);
            throw new RuntimeException(e);
        }
    }

    private String generateRefreshToken(Account user) {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("bookstore")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS512), payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error when signing refresh token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(Account account) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(account.getRoles())) {
            account.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
}