package com.eventregistration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

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
    USER_NOT_FOUND(1014, "Không tìm thấy tài khoản", HttpStatus.NOT_FOUND),
    USER_PASSWORD_NOT_SET(1015, "Tài khoản chưa thiết lập mật khẩu", HttpStatus.BAD_REQUEST),
    USER_USERNAME_GENERATION_FAILED(1016, "Không thể tạo tên tài khoản", HttpStatus.INTERNAL_SERVER_ERROR),

    // TOKEN ERROR (1100-1199)
    TOKEN_EMPTY(1100, "Token rỗng", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(1101, "Token giải mã không chính xác", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1102, "Token đã hết hạn", HttpStatus.BAD_REQUEST),
    TOKEN_WAS_LOGOUT(1103, "Token đã đăng xuất, vui lòng đăng nhập lại", HttpStatus.BAD_REQUEST),
    TOKEN_GENERATION_FAILED(1106, "Không thể tạo token", HttpStatus.INTERNAL_SERVER_ERROR),
    OTP_EMPTY(1104, "Mã OTP không được để trống", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(1104, "Mã OTP đã hết hạn", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1105, "Mã OTP không chính xác", HttpStatus.BAD_REQUEST),

    // API ERROR (1200-1299)
    API_LACK_OF_PARAMETER(1200, "API còn thiếu tham số", HttpStatus.BAD_REQUEST),

    // API KEY ERROR (1250-1299)
    API_KEY_BREVO_REQUIRED(1250, "Brevo API Key không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_PEXELS_REQUIRED(1251, "Pexels API Key không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_TRANSLATE_REQUIRED(1252, "Translate API Key không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_GOOGLE_CLIENT_ID_REQUIRED(1253, "Google Client ID không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_GOOGLE_CLIENT_SECRET_REQUIRED(1254, "Google Client Secret không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_FACEBOOK_CLIENT_ID_REQUIRED(1255, "Facebook Client ID không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_FACEBOOK_CLIENT_SECRET_REQUIRED(1256, "Facebook Client Secret không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_JWT_ACCESS_REQUIRED(1257, "JWT Access Signer Key không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_JWT_REFRESH_REQUIRED(1258, "JWT Refresh Signer Key không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_JWT_ACCESS_DURATION_REQUIRED(1259, "JWT Access Token Duration không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_JWT_REFRESH_DURATION_REQUIRED(
            1260, "JWT Refresh Token Duration không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_DB_URL_REQUIRED(1261, "Database URL không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_DB_USERNAME_REQUIRED(1262, "Database Username không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_DB_PASSWORD_REQUIRED(1263, "Database Password không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_REDIS_HOST_REQUIRED(1264, "Redis Host không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_REDIS_PORT_REQUIRED(1265, "Redis Port không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_REDIS_PASSWORD_REQUIRED(1266, "Redis Password không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_SENDER_EMAIL_REQUIRED(1267, "Sender Email không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_SENDER_NAME_REQUIRED(1268, "Sender Name không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_FIREBASE_BUCKET_NAME_REQUIRED(1269, "Firebase Bucket Name không được để trống", HttpStatus.BAD_REQUEST),
    API_KEY_JWT_DURATION_INVALID(1270, "JWT Token Duration phải lớn hơn 0", HttpStatus.BAD_REQUEST),

    // ROLE ERROR (1300-1399)
    ROLE_EXISTED(1300, "Vai trò đã tồn tại", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1301, "Vai trò không tồn tại", HttpStatus.BAD_REQUEST),
    ROLE_NAME_REQUIRED(1302, "Tên vai trò không được để trống", HttpStatus.BAD_REQUEST),

    // FILE ERROR (1400-1499)
    FILE_EMPTY(1400, "File rỗng", HttpStatus.BAD_REQUEST),

    // EMAIL ERROR (1500-1599)
    EMAIL_SENDING_FAILED(1500, "Gửi email thất bại", HttpStatus.INTERNAL_SERVER_ERROR),

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
    PAGINATE_INVALID_SORT_BY(1803, "Trường sắp xếp không hợp lệ", HttpStatus.BAD_REQUEST),

    // CALENDAR ERROR (1900-1999)
    CALENDAR_NOT_FOUND(1900, "Lịch không tồn tại", HttpStatus.NOT_FOUND),
    CALENDAR_NAME_REQUIRED(1901, "Tên lịch không được để trống", HttpStatus.BAD_REQUEST),
    CALENDAR_ALREADY_EXISTS(1902, "Lịch với tên này đã tồn tại", HttpStatus.BAD_REQUEST),
    CALENDAR_UNAUTHORIZED_ACCESS(1903, "Bạn không có quyền truy cập lịch này", HttpStatus.FORBIDDEN),
    CALENDAR_CANNOT_DELETE_LAST(1904, "Không thể xóa lịch này", HttpStatus.BAD_REQUEST),
    CALENDAR_COLOR_REQUIRED(1905, "Màu Lịch không được để trống", HttpStatus.BAD_REQUEST),

    // EVENT ERROR (2000-2099)
    EVENT_NOT_FOUND(2000, "Sự kiện không tồn tại", HttpStatus.NOT_FOUND),
    EVENT_TITLE_REQUIRED(2001, "Tiêu đề sự kiện không được để trống", HttpStatus.BAD_REQUEST),
    EVENT_START_TIME_REQUIRED(2002, "Thời gian bắt đầu không được để trống", HttpStatus.BAD_REQUEST),
    EVENT_END_TIME_REQUIRED(2003, "Thời gian kết thúc không được để trống", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_TIME_RANGE(2004, "Thời gian kết thúc phải sau thời gian bắt đầu", HttpStatus.BAD_REQUEST),
    EVENT_UNAUTHORIZED_ACCESS(2005, "Bạn không có quyền truy cập sự kiện này", HttpStatus.FORBIDDEN),
    EVENT_ALREADY_IN_CALENDAR(2006, "Sự kiện này đã được thêm vào lịch", HttpStatus.BAD_REQUEST),

    // REGISTRATION ERROR (2100-2199)
    USER_ALREADY_REGISTERED(2100, "Người dùng đã đăng ký tham gia sự kiện này", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_REGISTERED(2101, "Email đã được đăng ký tham gia sự kiện này", HttpStatus.BAD_REQUEST),
    GUEST_EMAIL_REQUIRED(2102, "Email khách không được để trống", HttpStatus.BAD_REQUEST),
    USER_EMAIL_NOT_FOUND(2103, "Người dùng không có địa chỉ email chính", HttpStatus.BAD_REQUEST),
    REGISTRATION_NOT_FOUND(2104, "Không tìm thấy đăng ký tham gia", HttpStatus.NOT_FOUND),
    ACCESS_DENIED(2105, "Bạn không có quyền truy cập tài nguyên này", HttpStatus.FORBIDDEN),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
