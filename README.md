# Regista

## Tổng Quan

Regista là một ứng dụng web được thiết kế để đơn giản hóa việc quản lý và đăng ký sự kiện. Ứng dụng cung cấp giao diện thân thiện, cho phép người dùng duyệt sự kiện, đăng ký tham gia, và nhận thông báo qua email. Với bảng điều khiển quản trị, người quản lý có thể dễ dàng theo dõi và quản lý sự kiện cũng như người dùng.

## Tính Năng

-   **Đăng nhập**: Hỗ trợ đăng nhập qua email.
-   **Duyệt và đăng ký sự kiện**: Người dùng có thể tìm kiếm, xem chi tiết và đăng ký tham gia sự kiện.
-   **Thông báo qua email**: Gửi thông báo về cập nhật sự kiện, xác nhận đăng ký, hoặc đặt lại mật khẩu.
-   **Quản lý dữ liệu**: Tích hợp Supabase để lưu trữ thông tin ảnh của người dùng và sự kiện.

## Công Nghệ Sử Dụng

### Backend

-   **Java với Spring Boot**: Xây dựng backend mạnh mẽ, dễ mở rộng.
-   **Gradle**: Công cụ tự động hóa xây dựng.
-   **Docker**: Đóng gói ứng dụng trong container để triển khai nhất quán.
-   **PostgreSQL**: Cơ sở dữ liệu chính, sử dụng H2 Database cho môi trường kiểm thử.
-   **Redis**: Bộ nhớ đệm để cải thiện hiệu suất.
-   **Brevo**: Dịch vụ gửi email thông báo.

### Frontend

-   **Next.js**: Framework React để xây dựng giao diện nhanh, tối ưu SEO.
-   **React**: Thư viện JavaScript cho giao diện tương tác.
-   **Tailwind CSS**: Framework CSS để tạo giao diện responsive, hiện đại.
-   **Radix UI**: Các thành phần giao diện không kiểu dáng, dễ tùy chỉnh.
-   **React Hook Form & Zod**: Quản lý và xác thực biểu mẫu hiệu quả.
-   **Supabase**: Xác thực người dùng và lưu trữ dữ liệu sự kiện.
-   **Framer Motion**: Tạo hiệu ứng chuyển động mượt mà.
-   **React Toastify & Sonner**: Hiển thị thông báo người dùng trực quan.
-   **Date-fns & React Datepicker**: Quản lý và chọn ngày giờ.
-   **Axios**: Gửi yêu cầu HTTP tới backend.
-   **Lucide React**: Bộ biểu tượng chất lượng cao.
-   **Next Themes**: Hỗ trợ chế độ sáng/tối.

### Công cụ phát triển

-   **ESLint & Prettier**: Đảm bảo chất lượng mã và định dạng nhất quán.
-   **Husky & Lint-staged**: Tự động kiểm tra và định dạng mã trước khi commit.
-   **Commitlint**: Chuẩn hóa thông điệp commit theo quy ước.
-   **TypeScript**: Tăng cường an toàn kiểu dữ liệu.
-   **PostCSS & Autoprefixer**: Xử lý CSS nâng cao.

## Cài Đặt

### Yêu cầu

-   **Node.js**: >= 18.x
-   **Java**: >= 17
-   **Docker**: Tùy chọn cho container hóa
-   **Redis**: Dịch vụ bộ nhớ đệm
-   **Gradle**: Công cụ xây dựng
-   **Tài khoản dịch vụ**:
    -   Supabase (xác thực và cơ sở dữ liệu)
    -   Brevo (gửi email)

### Hướng dẫn cài đặt

1.  **Sao chép kho mã nguồn**
    
    ```bash
    git clone <repository-url>
    cd event-registration
    ```
    
2.  **Cài đặt frontend**
    
    ```bash
    cd frontend
    npm install
    npm run dev
    ```
    
    Lệnh `npm run dev` sẽ khởi động ứng dụng ở chế độ phát triển tại `http://localhost:3000`.
    
3.  **Cài đặt backend**
    
    ```bash
    cd backend
    ./gradlew build
    ./gradlew bootRun
    ```
    
    Backend sẽ  mặc định chạy tại `http://localhost:8000` .
    
4.  **Cấu hình môi trường**
    
    #### Frontend
    
    Tạo file `.env` trong thư mục `frontend` với các biến sau:
    
    ```
    NEXT_PUBLIC_SUPABASE_URL=<your-supabase-project-url>
    NEXT_PUBLIC_SUPABASE_ANON_KEY=<your-supabase-anon-key>
    NEXT_PUBLIC_API_BASE_URL=<your-backend-api-url>
    NEXT_PUBLIC_DEV_URL=<your-backend-api-url>
    ```
    
    -   **NEXT_PUBLIC_SUPABASE_URL**: URL của dự án Supabase (ví dụ: `https://your-project.supabase.co`).
    -   **NEXT_PUBLIC_SUPABASE_ANON_KEY**: Khóa công khai của Supabase, lấy từ cài đặt dự án.
    -   **NEXT_PUBLIC_API_BASE_URL**: URL của backend API (ví dụ: `https://your-backend.com/api`).
    -   **NEXT_PUBLIC_DEV_URL**: URL dùng cho môi trường phát triển, thường giống `NEXT_PUBLIC_API_BASE_URL`.
    
    #### Backend
    
    Tạo file `.env`  trong thư mục `backend` với các biến sau:
    
    ```properties
    # Database configuration (PostgreSQL)
    spring.datasource.url=<your-postgresql-url>
    spring.datasource.username=<your-db-username>
    spring.datasource.password=<your-db-password>
    
    # JWT Keys
    jwt.access-signer-key=<your-jwt-access-signer-key>
    jwt.refresh-signer-key=<your-jwt-refresh-signer-key>
    jwt.reset-password-signer-key=<your-jwt-reset-password-signer-key>
    jwt.access-token-duration=7200
    jwt.refresh-token-duration=604800
    jwt.reset-password-token-duration=60
    
    # Email Client (Brevo)
    brevo.api-url=https://api.brevo.com
    brevo.api-key=<your-brevo-api-key>
    brevo.sender-email=<your-sender-email>
    brevo.sender-name=Regista
    
    # Client URLs
    client.url=<your-frontend-url>
    
    # Redis configuration
    spring.redis.host=<your-redis-host>
    spring.redis.port=<your-redis-port>
    spring.redis.password=<your-redis-password>
    
    ```
    
    -   **Database**: Cung cấp URL, tên người dùng, và mật khẩu cho PostgreSQL (lấy từ nhà cung cấp như Aiven, AWS, ...).
    -   **JWT**: Tạo các khóa ký JWT.
    -   **Brevo**: Lấy API key từ Brevo và cấu hình email gửi.
    -   **Client URL**: URL của frontend (ví dụ: `http://localhost:3000` hoặc `https://your-domain.com`).
    -   **Redis**: Cung cấp thông tin host, port, và mật khẩu từ dịch vụ Redis (như Redis Cloud).
    

    

## Sử Dụng

-   **Truy cập ứng dụng**: Mở trình duyệt tại `http://localhost:3000` (hoặc URL frontend đã triển khai).
-   **Đăng nhập/đăng ký**: Sử dụng email để đăng nhập.
-   **Duyệt sự kiện**: Tìm kiếm và xem chi tiết sự kiện, đăng ký tham gia.
-   **Thông báo**: Kiểm tra email để nhận xác nhận đăng ký, cập nhật sự kiện.

## Quy ước Commit

| Hotkey    | Mô tả |
|-----------|-------------|
| feat      | Add new feature |
| fix       | Fix a bug |
| docs      | Documentation updates |
| style     | Changes that don't affect code logic (formatting, spacing, etc.) |
| refactor  | Code refactoring without changing functionality |
| perf      | Performance improvements |
| test      | Add or modify test cases |
| build     | Changes affecting build system or dependencies |
| ci        | CI/CD configuration changes |
| chore     | Regular maintenance tasks, not affecting src/test |
| revert    | Revert previous commit |
| merge     | Merge branches |

## Demo
[Video Demo](https://youtu.be/LPepD-YM59Q)
**Mô tả**: Video giới thiệu các chức năng chính của Regista.
