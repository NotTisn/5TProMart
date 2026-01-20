package com.fivetpromart.infrastructure.error;

import com.fivetpromart.domain.exception.*;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  private static final String MIN_ATTRIBUTE = "min";

  // ============================================================================
  // DOMAIN EXCEPTION HANDLERS
  // ============================================================================

  @ExceptionHandler(EmptyFieldException.class)
  public ResponseEntity<ApiResponse> handleEmptyField(EmptyFieldException ex) {
    log.warn("Empty field: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("EMPTY_FIELD"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(InvalidCustomerDataException.class)
  public ResponseEntity<ApiResponse> handleInvalidCustomerData(InvalidCustomerDataException ex) {
    log.warn("Invalid customer data: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_CUSTOMER_DATA"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<ApiResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
    log.warn("Customer not found: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("CUSTOMER_NOT_FOUND"))
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(InvalidPhoneNumberException.class)
  public ResponseEntity<ApiResponse> handleInvalidPhoneNumber(InvalidPhoneNumberException ex) {
    log.warn("Invalid phone number: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_PHONE_NUMBER"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(InsufficientLoyaltyPointsException.class)
  public ResponseEntity<ApiResponse> handleInsufficientLoyaltyPoints(InsufficientLoyaltyPointsException ex) {
    log.warn("Insufficient loyalty points: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INSUFFICIENT_LOYALTY_POINTS"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(InvalidProductDataException.class)
  public ResponseEntity<ApiResponse> handleInvalidProductData(InvalidProductDataException ex) {
    log.warn("Invalid product data: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_PRODUCT_DATA"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ApiResponse> handleProductNotFound(ProductNotFoundException ex) {
    log.warn("Product not found: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("PRODUCT_NOT_FOUND"))
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(InvalidPriceException.class)
  public ResponseEntity<ApiResponse> handleInvalidPrice(InvalidPriceException ex) {
    log.warn("Invalid price: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_PRICE"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(InvalidCategoryDataException.class)
  public ResponseEntity<ApiResponse> handleInvalidCategoryData(InvalidCategoryDataException ex) {
    log.warn("Invalid category data: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_CATEGORY_DATA"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ApiResponse> handleCategoryNotFound(CategoryNotFoundException ex) {
    log.warn("Category not found: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("CATEGORY_NOT_FOUND"))
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(InvalidSupplierDataException.class)
  public ResponseEntity<ApiResponse> handleInvalidSupplierData(InvalidSupplierDataException ex) {
    log.warn("Invalid supplier data: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_SUPPLIER_DATA"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(SupplierNotFoundException.class)
  public ResponseEntity<ApiResponse> handleSupplierNotFound(SupplierNotFoundException ex) {
    log.warn("Supplier not found: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("SUPPLIER_NOT_FOUND"))
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(InvalidStockDataException.class)
  public ResponseEntity<ApiResponse> handleInvalidStockData(InvalidStockDataException ex) {
    log.warn("Invalid stock data: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_STOCK_DATA"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(InvalidDateRangeException.class)
  public ResponseEntity<ApiResponse> handleInvalidDateRange(InvalidDateRangeException ex) {
    log.warn("Invalid date range: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_DATE_RANGE"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(NegativeValueException.class)
  public ResponseEntity<ApiResponse> handleNegativeValue(NegativeValueException ex) {
    log.warn("Negative value: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("NEGATIVE_VALUE"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    log.warn("Resource not found: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("RESOURCE_NOT_FOUND"))
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(OtpExpiredException.class)
  public ResponseEntity<ApiResponse> handleOtpExpired(OtpExpiredException ex) {
    log.warn("OTP expired: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("OTP_EXPIRED"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(InvalidOtpException.class)
  public ResponseEntity<ApiResponse> handleInvalidOtp(InvalidOtpException ex) {
    log.warn("Invalid OTP: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_OTP"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(InvalidOperationException.class)
  public ResponseEntity<ApiResponse> handleInvalidOperation(InvalidOperationException ex) {
    log.error("Invalid operation: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_OPERATION"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  // Catch-all for any DomainException
  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ApiResponse> handleDomainException(DomainException ex) {
    log.error("Domain exception: {}", ex.getMessage(), ex);
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("DOMAIN_EXCEPTION"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  // ============================================================================
  // LEGACY HANDLERS (Keep for backward compatibility)
  // ============================================================================

  // ============================================================================
  // LEGACY HANDLERS (Keep for backward compatibility)
  // ============================================================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("ILLEGAL_ARGUMENT"))
                .build();

        return ResponseEntity.badRequest().body(response);
    }

  // ============================================================================
  // STOCK RESERVATION EXCEPTION HANDLERS (NEW)
  // ============================================================================

  @ExceptionHandler(InsufficientStockException.class)
  public ResponseEntity<ApiResponse> handleInsufficientStock(InsufficientStockException ex) {
    log.warn("Insufficient stock: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INSUFFICIENT_STOCK"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(InvalidStockReservationException.class)
  public ResponseEntity<ApiResponse> handleInvalidStockReservation(InvalidStockReservationException ex) {
    log.warn("Invalid stock reservation: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("INVALID_STOCK_RESERVATION"))
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(StockInventoryNotFoundException.class)
  public ResponseEntity<ApiResponse> handleStockInventoryNotFound(StockInventoryNotFoundException ex) {
    log.warn("Stock inventory not found: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .error(com.fivetpromart.presentation.dto.response.ErrorDetail.of("STOCK_INVENTORY_NOT_FOUND"))
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  // ============================================================================
  // GENERIC APP EXCEPTION HANDLERS
  // ============================================================================

  @ExceptionHandler(value = AppException.class)
  ResponseEntity<ApiResponse> handleAppException(AppException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    ApiResponse response = new ApiResponse();

    response.setStatusCode(errorCode.getCode());
    response.setMessage(errorCode.getMessage());
    response.setSuccess(false);
    return ResponseEntity.status(errorCode.getStatusCode()).body(response);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex) {
    String enumKey = ex.getFieldError().getDefaultMessage();
    ErrorCode errorCode = ErrorCode.INVALID_KEY;
    Map<String, Object> attributes = null;
    try {
      errorCode = ErrorCode.valueOf(enumKey);
      var constraintViolation =
          ex.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

      attributes = constraintViolation.getConstraintDescriptor().getAttributes();
      log.info(attributes.toString());
    } catch (IllegalArgumentException e) {

    }
    ApiResponse response = new ApiResponse();
    response.setStatusCode(errorCode.getCode());
    response.setMessage(
        Objects.nonNull(attributes)
            ? mapAttributes(errorCode.getMessage(), attributes)
            : errorCode.getMessage());

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException ex) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
    return ResponseEntity.status(errorCode.getStatusCode())
        .body(
            ApiResponse.builder()
                .statusCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
  }

  private String mapAttributes(String message, Map<String, Object> attributes) {
    String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
    return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
  }
}
