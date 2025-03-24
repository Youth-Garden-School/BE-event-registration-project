package com.eventregistration.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // UNCATEGORIZED ERROR (9000-9099)
    UNCATEGORIZED_EXCEPTION(9000, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    // USER ERROR (1000-1099)
    USER_INVALID_EMAIL(1000, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_EMAIL_REQUIRED(1001, "Email không được để trống", HttpStatus.BAD_REQUEST),
    USER_INVALID_PASSWORD(1002, "Mật khẩu phải có ít nhất {0} ký tự", HttpStatus.BAD_REQUEST),
    USER_WRONG_FORMAT_PASSWORD(
            1003,
            "Mật khẩu phải có tối thiểu 1 chữ số, 1 chữ cái, 1 chữ cái in hoa và 1 ký tự đặc biệt",
            HttpStatus.BAD_REQUEST),
    USER_UNAUTHENTICATED(1004, "Vui lòng đăng nhập để sử dụng tính năng này", HttpStatus.UNAUTHORIZED),
    USER_EMAIL_EXISTED(1005, "Email đã được sử dụng", HttpStatus.BAD_REQUEST),
    USER_UNAUTHORIZED(1006, "Bạn không thể truy cập tính năng này", HttpStatus.FORBIDDEN),
    USER_NOT_EXISTED(1007, "Tài khoản không tồn tại", HttpStatus.BAD_REQUEST),
    USER_WRONG_PASSWORD(1008, "Mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1009, "Tài khoản đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_MISMATCH(1010, "Mật khẩu không khớp với xác nhận mật khẩu", HttpStatus.BAD_REQUEST),
    USER_FILE_UPLOAD_FAIL(1011, "Thay đổi ảnh đại diện thất bại", HttpStatus.BAD_REQUEST),
    USER_NEW_PASSWORD_REQUIRED(1012, "Mật khẩu mới không được để trống", HttpStatus.BAD_REQUEST),
    USER_CONFIRM_PASSWORD_REQUIRED(1013, "Xác nhận mật khẩu mới không được để trống", HttpStatus.BAD_REQUEST),

    // TOKEN ERROR (1100-1199)
    TOKEN_EMPTY(1100, "Token rỗng", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(1101, "Token giải mã không chính xác", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1102, "Token đã hết hạn", HttpStatus.BAD_REQUEST),
    TOKEN_WAS_LOGOUT(1103, "Token đã đăng xuất, vui lòng đăng nhập lại", HttpStatus.BAD_REQUEST),

    // API ERROR (1200-1299)
    API_LACK_OF_PARAMETER(1200, "API còn thiếu tham số", HttpStatus.BAD_REQUEST),

    // ROLE ERROR (1300-1399)
    ROLE_EXISTED(1300, "Vai trò đã tồn tại", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1301, "Vai trò không tồn tại", HttpStatus.BAD_REQUEST),
    ROLE_NAME_REQUIRED(1302, "Tên vai trò không được để trống", HttpStatus.BAD_REQUEST),

    // FILE ERROR (1400-1499)
    FILE_EMPTY(1400, "File rỗng", HttpStatus.BAD_REQUEST),

    // OAUTH2 ERROR (1600-1699)
    OAUTH2_USER_EXISTED_WITH_DIFFERENT_PROVIDER(
            1600, "Tài khoản đã được liên kết với nhà cung cấp OAuth2 khác trước đó", HttpStatus.BAD_REQUEST),
    OAUTH2_USER_EXISTED_WITH_BASIC_ACCOUNT(
            1601, "Tài khoản đã tồn tại trên hệ thống với email/password", HttpStatus.BAD_REQUEST),
    OAUTH2_USER_EXISTED_WITH_OAUTH2(1602, "Tài khoản đã tồn tại với nhà cung cấp OAuth2", HttpStatus.BAD_REQUEST),

    // PERMISSION ERROR (1700-1799)
    PERMISSION_NAME_REQUIRED(1700, "Tên quyền hạn không được để trống", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1701, "Quyền hạn không tồn tại trên hệ thống", HttpStatus.BAD_REQUEST),
    PERMISSION_ALREADY_EXISTED(1702, "Quyền hạn đã tồn tại trong vai trò", HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED(1703, "Quyền hạn đã tồn tại trên hệ thống", HttpStatus.BAD_REQUEST),

    // PAGINATE ERROR (1800-1899)
    PAGINATE_INVALID_PAGE_NUMBER(1800, "Số trang phải lớn hơn hoặc bằng 1", HttpStatus.BAD_REQUEST),
    PAGINATE_INVALID_PAGE_SIZE(1801, "Kích thước trang phải lớn hơn hoặc bằng 1", HttpStatus.BAD_REQUEST),
    PAGINATE_INVALID_SORT_DIRECTION(1802, "Hướng sắp xếp không hợp lệ", HttpStatus.BAD_REQUEST),
    PAGINATE_INVALID_SORT_BY(1803, "Trường sắp xếp không hợp lệ", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}