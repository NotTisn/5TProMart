# 1. Orders API

**_ Information _**

**_ Header And Required _**

```json
"Authorization": "Bearer {token}",
"role": {
    "name": "Admin"
}
```

## **Base Endpoint** `/api/v1/orders`

## 1.1 Get orders query

**Endpoint:** `GET /api/v1/orders`

**Query Parameters**

```json
{
  "search": "string", // orderId, customerName, customerId
  "staffId": "string",
  "startDate": "dd-MM-yyyy",
  "endDate": "dd-MM-yyyy",
  "paymentMethod": "string", // Filter: "Tiền mặt", "Chuyển khoản"
  "status": "string", // Filter: "Đã thanh toán", "Chưa thanh toán", "Đã huỷ"
  "page": 0,
  "size": 10,
  "sort": "orderDate,desc"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get order list successfully.",
  "data": [
    {
      "orderId": "string",
      "orderDate": "dd-MM-yyyy hh-mm-ss",
      "customerName": "string", // if customerId is null => "Khách lẻ"
      "staffName": "string",
      "totalAmount": number,
      "paymentMethod": "string", // "Tiền mặt", "Chuyển khoản"
      "status": "string",   // "Đã thanh toán", "Chưa thanh toán", "Đã huỷ"
      "createdAt": "dd-MM-yyyy hh-mm-ss"
    }
  ],
  "pagination": {
    "totalItems": 100,
    "totalPages": 10,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

---

---

## 1.2 Get order detail

**Endpoint:** `GET /api/v1/orders/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "Get order detail successfully.",
  "data": {
    "orderId": "string",
    "orderDate": "10-01-2026 14:30:00",
    "status": "Đã thanh toán",
    "paymentMethod": "Tiền mặt",
    "customer": {
      "customerId": "string",
      "fullName": "string",
      "phoneNumber": "string"
    },
    "staff": {
      "profileId": "string",
      "fullName": "string"
    },
    "items": [
      {
        "productId": "string",
        "productName": "string",
        "quantity": 2,
        "unitPrice": 5000,
        "subTotal": 10000
      }
    ],
    "subTotal": 10000,
    "discountAmount": 0,
    "totalAmount": 10000,
    "amountGiven": 20000,
    "changeReturned": 10000,
    "pointsEarned": 100
  }
}
```

---

## 1.3 Check product (scan product code)

**Endpoint**: `POST /api/v1/orders/check-product`

**Request Body**

```json
{
  "lotId": "string", // Mã quét được
  "quantity": 1 // Default : 1
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Item check successfully.",
  "data": {
    "lotId": "string",
    "productId": "string",
    "productName": "string", // Product.productName
    "unitOfMeasure": "string", // productName.unitOfMeasure
    "unitPrice": 10000, // Product.sellingPrice
    "quantity": 2,
    "subTotal": 20000, // unitPrice * quantity
    "currentStock": 50, // StockInventory.stockQuantity
    "status": "string", // StockInventory.status
    
    // NEW: Promotion info (auto-applied when available)
    "promotion": {
      "promotionId": "string", // null if no active promotion
      "promotionName": "string",
      "promotionType": "Discount" | "Buy X Get Y",
      "discountPercent": 20, // for Discount type
      "buyQuantity": 2, // for Buy X Get Y
      "getQuantity": 1, // for Buy X Get Y
      "promotionalPrice": 8000, // unitPrice after discount
      "savings": 2000 // unitPrice - promotionalPrice
    } // null if no promotion
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Product validation failed.",
  "errors": {
    "lotId": "Lot not found or Expired.",
    "quantity": "Insufficient stock. Current stock lower than quantity."
  }
}
```

---

## 1.4 Create Order (Checkout)

**Endpoint**: `POST /api/v1/orders`

**Request Body**

```json
{
  "customerId": "string", // Nullable - null = walk-in customer
  "paymentMethod": "CASH", // "CASH" | "BANK_TRANSFER"
  "amountGiven": 360000,
  "items": [
    { "lotId": "lotId_1", "quantity": 3 },
    { "lotId": "lotId_2", "quantity": 6 }
  ],
  
  // NEW: Optional discount (supports loyalty points, percentage, fixed amount)
  "discount": {
    "type": "LOYALTY_POINTS", // "NONE" | "PERCENTAGE" | "FIXED_AMOUNT" | "LOYALTY_POINTS"
    "pointsToUse": 5000, // For LOYALTY_POINTS: 1 point = 1 VND discount
    "percentage": null, // For PERCENTAGE: e.g., 10 = 10% off
    "maxAmount": null, // For PERCENTAGE: cap the discount at this amount
    "amount": null // For FIXED_AMOUNT: exact VND discount
  } // Optional - omit for no discount
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Order created",
  "data": {
    "orderId": "string",
    "orderDate": "dd-MM-yyyy hh-mm-ss", // now
    "subTotal": 360000, // Sum of all items before discount
    "discountAmount": 5000, // Amount deducted (from loyalty or other discount)
    "totalAmount": 355000, // subTotal - discountAmount (rounded for cash)
    "originalAmount": 355000, // Before cash rounding
    "roundingAdjustment": 0, // +/- for cash rounding to 1000đ
    "amountGiven": 360000,
    "changeReturned": 5000, // amountGiven - totalAmount
    "pointsEarned": 3550, // 1% of totalAmount, added to customer.loyaltyPoints
    "pointsRedeemed": 5000, // If LOYALTY_POINTS discount used
    "items": [
      {
        "productId": "productId_01",
        "productName": "product_name_1",
        "lotId": "lot_id_1",
        "quantity": 3,
        "subTotal": 300000
      },
      {
        "productId": "productId_02",
        "productName": "product_name_2",
        "lotId": "lot_id_2",
        "quantity": 6,
        "subTotal": 60000
      }
    ]
  }
}
```

> **NOTE LUỒNG:**
>
> 1. Mỗi lần quét mã vạch sẽ gọi API **1.3 Check Product**
> 2. Sau khi quét hết sản phẩm và nhận tiền gọi API **1.4 Create Order**

> **NOTE FOR FRONTEND:**
>
> 1. Xử lý frontend khi quét:
> 2. **UI hoá đơn:** Nếu quét 2 lô khác nhau của cùng 1 sản phẩm (Cùng `productId` và `unitPrice`), gộp lại thành 1 dòng trên màn hình POS với tổng số lượng, tách riêng các sản phẩm cùng loại sản phẩm nhưng khác giá (khuyến mãi...)
> 3. **Lưu trữ:** Bên dưới hiển thị đó, Frontend phải lưu giữ danh sách các `lotId` thành phần
> 4. **Mục đích:** Để khi gọi API **1.4 Create Order**, Frontend có thể tách ra gửi chính xác từng `lotId` cho Backend xử lý trừ lô
