# 1. Statistics Management

**_ Information _**

**_ Header And Required _**

```json
"Authorization": "Bearer {token}",
"role": {
    "name": "Admin"
}
```

## **_ Endpoint base _** `/api/v1/statistics`

## 1.1 Get Dashboard Summary

**Endpoint:** `GET /api/v1/statistics/summary`

**Query Parameters**

```json
{
  "startDate": "dd-MM-yyyy", // Required
  "endDate": "dd-MM-yyyy" // Required
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get summary successfully.",
  "data": {
    "totalRevenue": 15000000, // Tổng doanh thu
    "netProfit": 3500000, // Lợi nhuận
    "totalOrders": 150, // Tổng số đơn hàng
    "totalProductsSold": 320, // Tổng số lượng sản phẩm bán ra
    "averageOrderValue": 100000, // Giá trị trung bình đơn
    "totalCustomers": 45, // Số khách đã mua hàng trong kỳ
    "newCustomers": 5, // Số khách đăng ký mới trong kỳ
    "incurredStats": 2000000
  }
}
```

---

## 1.2 Get Revenue & Profit Chart

**Endpoint:** `GET /api/v1/statistics/revenue-profit-chart`

**Request Body**

````json
{
  "startDate": "dd-MM-yyyy",
  "endDate": "dd-MM-yyyy"
}
```

**Response 200**

```json
{
  "success": true,
  "data": [
    {
      "date": "20-01-2026",
      "revenue": 500000,
      "expense": 45000,
      "profit": 450000,
    },
    {
      "date": "21-01-2026",
      "revenue": 120000,
      "expense": 20000,
      "profit": 100000
    }
  ]
}
````

---

## 1.3 Get Order By Days Chart

**Endpoint:** `GET /api/v1/statistics/orders-chart`

**Request Body**

````json
{
  "startDate": "dd-MM-yyyy",
  "endDate": "dd-MM-yyyy"
}
```

**Response 200**

```json
{
  "success": true,
  "data": [
    {
      "date": "20-01-2026",
      "completedOrders": 12,
    },
    {
      "date": "21-01-2026",
      "completedOrders": 10,
    }
  ]
}
```
````

## 1.4 Get revenue by category

**Endpoint:** `GET /api/v1/statistics/category-revenue`

**Request Body**

````json
{
  "limit": 5,
  "startDate": "dd-MM-yyyy",
  "endDate": "dd-MM-yyyy"
}
```

**Response 200**

```json
{
  "success": true,
  "data": [
    {
      "categoryId": "id",
      "categoryName": "Đồ uống",
      "totalRevenue": 50000000,
      "totalQuantitySold": 2500,
      "orderCount": 150
    },
    {
      "categoryId": "id",
      "categoryName": "Bánh kẹo",
      "totalRevenue": 30000000,
      "totalQuantitySold": 1200,
      "orderCount": 80
    }
  ]
}
````

## 1.5 Get Top Selling Products

**Endpoint:** `GET /api/v1/statistics/top-products`

**Request Body**

````json
{
  "limit": 10,
  "startDate": "dd-MM-yyyy",
  "endDate": "dd-MM-yyyy"
}
```

**Response 200**

```json
{
  "success": true,
  "data": [
    {
      "productId": "id",
      "productName": "Coca Cola",
      "categoryName": "Nước ngọt",

      "totalRevenue": 15000000,
      "totalQuantitySold": 1500,
      "totalStockQuantity": 50,
    },
    {
      "productId": "id",
      "productName": "Hảo Hảo",
      "categoryName": "Mì tôm",
      "totalRevenue": 4800000,
      "totalQuantitySold": 1200,
      "currentStock": 200,
      "totalStockQuantity": "Gói"
    }
  ]
}
```
````

**NOTE LOGIC BE**

> 1.  Doanh thu (Revenue): Tổng giá trị totalAmount của các đơn hàng có status = "Completed"

Công thức tính Lợi nhuận (Profit):

Giá vốn hàng bán: Lấy từ importPrice của StockInventory đó

Chi phí phát sinh (Incurred Stats): Lấy từ amount của collection Stats có ngày trong phạm vi muốn lấy.

Lợi nhuận = Doanh thu - Giá vốn hàng bán - Chi phí phát sinh.

> 2. Khi xử lý **## 1.4 Get revenue by category**, lấy danh sách sau khi xử lý, sort giảm dần theo doanh thu, tất cả các hàng từ limit+1 trở đi thì gộp lại thành 1 category "Khác" để gửi lên FE

> 3. Khi xử lý **## 1.5 Get Top Selling Products**, sắp xếp giảm dần theo "totalQuantitySold" và chỉ gửi 'limit' sản phẩm đầu tiên
