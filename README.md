# Ma_nguon_mo_Backend
Hướng dẫn cài đặt và chạy project
1. Cài đặt môi trường
   Trước khi bắt đầu, cần chuẩn bị các công cụ sau:
   IDE: Cài đặt IntelliJ IDEA Ultimate (hỗ trợ đầy đủ thư viện, Spring Boot, ReactJS,TypeScript).
   Node.js: Tải và cài đặt Node.js, đảm bảo đã cấu hình cơ bản.
   MySQL: Cài đặt MySQL để quản lý cơ sở dữ liệu.
   HeidiSQL (khuyến khích): Công cụ hỗ trợ thao tác với MySQL dễ dàng hơn.
2. Tải project về máy
   Có hai cách để tải source code:
   Cách 1: Sử dụng Git để clone repository:
   git clone <link-github>
   Cách 2: Tải file .zip của project từ GitHub, sau đó giải nén.
3. Cấu hình project
   Mở thư mục project và mở hai cửa sổ để chạy Backend (BE) và Frontend (FE) cùng lúc.
   Cấu hình file application.properties sao cho phù hợp với hệ thống của bạn.
   spring.datasource.url=jdbc:mysql://localhost:3306/web_ban_sach
   spring.datasource.username=spring  --thay thế bằng tên username trong MySQL
   spring.datasource.password=spring  --thay thế bằng mật khẩu của username

.Nhập username và password của MySQL mà bạn tạo
Lưu ý:
Trước khi chạy project, cần tạo cơ sở dữ liệu trong MySQL:
CREATE DATABASE web_ban_hang;
4. Chạy Backend
   Mở BE trong IntelliJ và chạy project.
   Nếu API phản hồi dữ liệu, Backend đã chạy thành công.
5. Cài đặt & chạy Frontend
   Mở Frontend trong IntelliJ, chạy Terminal và cài đặt thư viện:
   npm install
   Chạy ứng dụng React:
   npm start
   Nếu thành công, trình duyệt sẽ tự động mở tại localhost:3000.
6. Thêm dữ liệu vào MySQL
   Hiện tại, ứng dụng chưa có dữ liệu. Bạn cần thêm dữ liệu vào các bảng trong MySQL để hiển thị trên web. Có thể thêm bằng HeidiSQL hoặc bằng lệnh SQL.
