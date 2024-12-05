package ute.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EXISTED(409,HttpStatus.BAD_REQUEST, "Tài khoản đã tồn tại"),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND,"Tài khoản không tồn tại"),
    UNCAUGHT_EXCEPTION(500, HttpStatus.INTERNAL_SERVER_ERROR ,"Lỗi không xác định"),
    PASSWORD_INVALID(400,HttpStatus.BAD_REQUEST, "Mật khẩu chứa ít nhất 8 ký tự"),
    INVALID_KEY(400,HttpStatus.BAD_REQUEST, "Lỗi sai ErrorCode"),
    AUTHENTICATION_FAILED(401,HttpStatus.UNAUTHORIZED, "Login Failed hehe"),
    ACCESS_DENIED(403,HttpStatus.FORBIDDEN, "Không có quyền truy cập"),
    INVALID_REFRESH_TOKEN(401,HttpStatus.UNAUTHORIZED, "Refresh Token không hợp lệ"),
    INVALID_VERIFICATION_TOKEN(400,HttpStatus.BAD_REQUEST, "Token xác thực không hợp lệ"),
    EMAIL_ALREADY_EXISTS(400,HttpStatus.BAD_REQUEST, "Email đã tồn tại"),
    USERNAME_ALREADY_EXISTS(400,HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại"),
    INVALID_BIRTHDAY(400,HttpStatus.BAD_REQUEST, "Ngày sinh không hợp lệ"),
    MISSING_REQUIRED_FIELDS(400,HttpStatus.BAD_REQUEST, "Vui lòng điền đầy đủ thông tin"),
    USER_NOT_ACTIVATED(400,HttpStatus.BAD_REQUEST, "Tài khoản chưa được kích hoạt"),
    EMAIL_NO_EXISTS(400,HttpStatus.BAD_REQUEST, "Tài khoản không tồn tại"),
    LOGIN_FAILED(400,HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không đúng!"),
    RESET_PASSWORD_FAILED(400,HttpStatus.BAD_REQUEST, "Reset mật khẩu thất bại"),
    USER_DELETED(400,HttpStatus.BAD_REQUEST, "Tài khoản đã bị khóa"),
    BOOK_NOT_FOUND(404,HttpStatus.NOT_FOUND, "Sách không tồn tại"),
    BOOK_ALREADY_IN_CART(400,HttpStatus.BAD_REQUEST, "Sách đã có trong giỏ hàng"),
    DISCOUNT_NOT_FOUND(404,HttpStatus.NOT_FOUND, "Mã giảm giá không tồn tại"),
    SOFT_BOOK(400,HttpStatus.BAD_REQUEST, "Sách đã được mua"),
    PAYMENT_ERROR(400,HttpStatus.BAD_REQUEST, "Không thể tạo URL thanh toán MoMo."),
    INVALID_REQUEST(400,HttpStatus.BAD_REQUEST, "Vui lòng nhập đầy đủ thông tin"),
    ARTICLE_EXISTED(400,HttpStatus.BAD_REQUEST, "Bài viết đã tồn tại"),
    ;

    private int status;
    private HttpStatusCode statuscode;
    private String message;
}
