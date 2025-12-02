package com.fivetpromart.infrastructure.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

  // GENERAL
  INTERNAL_SERVER_ERROR(500, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_INPUT(400, "Invalid input data", HttpStatus.BAD_REQUEST),
  UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),
  INVALID_REQUEST(400, "Invalid request", HttpStatus.BAD_REQUEST),
  DUPLICATE_KEY(409, "Duplicate key", HttpStatus.CONFLICT),

  // USER
  USER_EXISTED(409, "User already exists", HttpStatus.CONFLICT),
  USER_NOT_EXISTED(404, "User not exists", HttpStatus.NOT_FOUND),
  USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
  EMAIL_EXISTED(409, "Email existed, please choose another one", HttpStatus.CONFLICT),
  EMAIL_NOT_EXISTED(404, "Email not existed", HttpStatus.NOT_FOUND),
  USERNAME_NOT_EXISTED(404, "Username not existed", HttpStatus.NOT_FOUND),
  USERNAME_IS_MISSING(400, "Please enter username", HttpStatus.BAD_REQUEST),
  INVALID_USERNAME(400, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
  INVALID_PASSWORD(400, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
  TERMS_NOT_ACCEPTED(400, "Terms not accepted", HttpStatus.BAD_REQUEST),
  ALREADY_FRIEND(409, "You are already friends", HttpStatus.CONFLICT),

  // BOOK
  BOOK_EXISTED(409, "Book already exists", HttpStatus.CONFLICT),
  BOOK_NOT_FOUND(404, "Book not found", HttpStatus.NOT_FOUND),
  BOOK_IMAGE_UPLOAD_FAILED(400, "Book cover upload failed", HttpStatus.BAD_REQUEST),
  BOOK_NOT_FOUND_IN_OPEN_LIBRARY(404, "Book not found in open library", HttpStatus.NOT_FOUND),
  BOOK_META_NOT_FOUND(404, "Book meta not found", HttpStatus.NOT_FOUND),

  // CATEGORY
  CATEGORY_NOT_FOUND(404, "Category not found", HttpStatus.NOT_FOUND),
  CATEGORY_EXISTED(409, "Category already exists", HttpStatus.CONFLICT),

  // INVENTORY
  NOT_ENOUGH_LISTING(400, "Not enough listing", HttpStatus.BAD_REQUEST),
  NO_LISTING_FOUND(404, "No listing found", HttpStatus.NOT_FOUND),
  LISTING_NOT_EXISTED(404, "Listing not existed", HttpStatus.NOT_FOUND),

  // REVIEW
  REVIEW_NOT_FOUND(404, "Review not found", HttpStatus.NOT_FOUND),
  POST_NOT_FOUND(404, "Post not found", HttpStatus.NOT_FOUND),
  POST_EDIT_EXPIRED(400, "Post edit expired", HttpStatus.BAD_REQUEST),

  // GENERAL FILE/UPLOAD
  FILE_UPLOAD_FAILED(400, "File upload failed", HttpStatus.BAD_REQUEST),
  FILE_DELETE_FAILED(400, "File delete failed", HttpStatus.BAD_REQUEST),

  // ROLE / AUTHOR / PROFILE
  ROLE_NOT_FOUND(404, "Role not found", HttpStatus.NOT_FOUND),
  AUTHOR_NOT_EXISTED(404, "Author not existed", HttpStatus.NOT_FOUND),
  AUTHOR_EXISTED(409, "Author already exists", HttpStatus.CONFLICT),
  NO_AUTHOR_FOUND(404, "No author found", HttpStatus.NOT_FOUND),
  PROFILE_ALREADY_EXISTS(409, "Profile already exists", HttpStatus.CONFLICT),
  PROFILE_NOT_FOUND(404, "Profile not found", HttpStatus.NOT_FOUND),

  // REQUEST
  REQUEST_NOT_FOUND(404, "Friend request not found", HttpStatus.NOT_FOUND),

  // OTHER
  INVALID_KEY(400, "Invalid key", HttpStatus.BAD_REQUEST),
  DO_NOT_HAVE_PERMISSION(403, "You do not have this permission!", HttpStatus.FORBIDDEN),
  UNCATEGORIZED_EXCEPTION(500, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
  NOT_FRIEND(400, "You are not friends", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(409, "Phone number existed", HttpStatus.CONFLICT),
    PHONE_NOT_EXISTED(404, "Phone number not existed", HttpStatus.NOT_FOUND ),
    INVALID_PHONE(400, "Invalid phone number", HttpStatus.BAD_REQUEST),
    CANNOT_BE_EMPTY(400, "This field cannot be empty", HttpStatus.BAD_REQUEST),
    CUSTOMER_NOT_EXISTED(404, "Customer not existed", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(409, "Product existed", HttpStatus.CONFLICT),
    PRODUCT_NOT_EXISTED(404, "Product not existed", HttpStatus.NOT_FOUND),
    SUPPLIER_NOT_EXISTED(404 , "Supplier not existed", HttpStatus.NOT_FOUND ),
    ;

  private final int code;
  private final String message;
  private final HttpStatusCode statusCode;

  ErrorCode(int code, String message, HttpStatusCode statusCode) {
    this.code = code;
    this.message = message;
    this.statusCode = statusCode;
  }
}
