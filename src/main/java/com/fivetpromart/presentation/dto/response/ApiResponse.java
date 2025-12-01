package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor // Cần cho @Builder
@Builder // Thêm @Builder để tạo đối tượng linh hoạt
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // Vẫn giữ nguyên, rất tốt!
public class ApiResponse<T> {

    @Builder.Default // Đặt giá trị mặc định cho success
    boolean success = true;
    int statusCode;
    String message;

    T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    PaginationMeta pagination;

    // --- CÁC HÀM FACTORY TIỆN LỢI (NÂNG CẤP) ---


    /**
     * Dùng cho các yêu cầu GET, PUT, PATCH thành công
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value()) // 200
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Dùng cho các yêu cầu GET thành công (thông điệp mặc định)
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Request successful"); // Thông điệp rõ ràng hơn
    }

    /**
     * Dùng cho các yêu cầu POST thành công (status 201)
     */
    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value()) // 201
                .message("Resource created successfully")
                .data(data)
                .build();
    }

    /**
     * Dùng cho các yêu cầu DELETE hoặc POST/PUT không cần trả về dữ liệu
     */
    public static <T> ApiResponse<T> successNoContent(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value()) // Hoặc .NO_CONTENT.value() (204)
                .message(message)
                .data(null)
                .build();
    }

    /**
     * Dùng cho các lỗi (4xx, 5xx)
     * NÊN dùng HttpStatus enum để đảm bảo tính nhất quán
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(status.value())
                .message(message)
                .data(null)
                .build();
    }

    /**
     * (Tùy chọn) Giữ lại hàm cũ của bạn nếu muốn,
     * nhưng nên dùng hàm có HttpStatus ở trên
     */
    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(statusCode)
                .message(message)
                .data(null)
                .build();
    }
}