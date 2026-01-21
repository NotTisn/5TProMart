## Salary API

# 1. Get current salary configs

**Endpoint:** `GET /api/v1/salary-configs`

**Query Parameters**

```json
{
  "success": true,
  "message": "Get salary config successfully.",
  "data": [
    {
      "id": "String",
      "role": "SalesStaff",
      "hourlySalary": 25000,
      "updatedAt": "2025-10-20"
    },
    {
      "id": "String",
      "role": "WarehouseStaff",
      "hourlySalary": 50000,
      "updatedAt": "2025-10-20"
    }
  ]
}
```

## 2. Update salary configs

**Endpoint:** `PUT /api/v1/salary-configs`

**Request body**

```json
{
  "configs": [
    {
      "role": "SalesStaff",
      "hourlyRate": 28000
    },
    {
      "role": "WarehouseStaff",
      "hourlyRate": 30000
    }
  ]
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Salary configs updated successfully.",
  "data": [
    {
      "role": "SalesStaff",
      "hourlyRate": 28000,
      "updatedAt": "2025-10-21"
    },
    {
      "role": "WarehouseStaff",
      "hourlyRate": 30000,
      "updatedAt": "2025-10-21"
    }
  ]
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Hourly rate cannot be negative.",
  "data": null
}
```

---

## 3. Calculate Daily Salary

**Endpoint:** `POST /api/v1/salary/daily-salary`

**Request body**

```json
{
  "date": "22-01-2026"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Daily salary calculation completed.",
  "data": {
    "processedDate": "15-11-2025",
    "status": "SUCCESS"
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Date must be before today."
}
```

---

## 4. Get salaries query

**Endpoint:** `GET /api/v1/salary/salary-reports`

**Query Parameters**

```json
{
  "startDate": "Date", // Required
  "endDate": "Date" // Required
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get salary report succesfully.",
  "data": {
    "range": {
      "startDate": "01-02-2026",
      "endDate": "31-02-2026"
    },
    "summary": {
      "totalSalaryCost": 7500000, // Tổng lương phải trả
      "totalWorkHours": 220.0, // Tổng số giờ công
      "totalStaffs": 3 // Tổng số nhân viên
    },
    "staffSalaryDetails": [
      {
        "userId": "String",
        "fullName": "String",
        "role": "SalesStaff",
        "totalWorkHours": 104.0,
        "totalSalary": 2912000
      },
      {
        "userId": "String",
        "fullName": "StringB",
        "role": "WarehouseStaff",
        "totalWorkHours": 96.0, // Tổng số giờ làm việc
        "totalSalary": 4800000 // Tổng lương
      }
    ]
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Start date must before end date"
}
```

---

## 5. Get salary by staff

**Endpoint:** `GET /api/v1/salary/salary-reports/{id}`

**Query Parameters**

```json
{
  "startDate": "Date", // Required
  "endDate": "Date" // Required
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get staff salary sucessfully",
  "data": {
    "userId": "String",
    "fullName": "String",
    "role": "SalesStaff",
    "range": {
      "fromDate": "01-02-2026",
      "toDate": "31-02-2026"
    },
    "summary": {
      "totalSalary": 3000000,
      "totalWorkHours": 110.0
    },
    "dailyDetails": [
      {
        "date": "01-11-2025",
        "workHours": 8.0,
        "hourlyRate": 28000,
        "dailyAmount": 224000
      },
      {
        "date": "02-11-2025",
        "workHours": 8.0,
        "appliedRate": 28000,
        "dailyAmount": 224000
      }
    ]
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Start date must before end date"
}
```

**Note xử lý BE**

> 1.  Khi chạy **## 2. Update salary configs**, tìm xem role có config chưa, có thì update, chưa thì tạo mới. Kiểm tra hourlyRate phải >= 0 để trả lỗi

**Luồng xử lý**

**## 3. Calculate Daily Salary**

> 1. Query collection WorkSchedule theo date trong request
> 2. Query SalaryRoleConfig để lấy toàn bộ lương theo role hiện tại
> 3. Duyệt từng phần tử WorkSchedule, tính số giờ làm của mỗi ca làm việc, duyệt mảng assignments lấy role của từng nhân viên
> 4. Với mỗi nhân viên, lấy role, tra vào bảng lương theo role để lấy hourlyRate của ngày hôm đó. Lưu tất cả thông tin vào **Daily Salary**, tính dailySalary bằng hourlyRate nhân workHours
> 5. Ở **API 4.** sử dụng bảng dailySalary dể tính tổng lương cho nhân viên, ở **API 5.** sử dụng bảng dailySalary để trả dữ liệu về FE

> **New collection**

**SalaryRoleConfig**

```json
{
  "_id": "String",
  "role": "SalesStaff",
  "hourlyRate": 25000, // Mức lương hiện tại
  "updatedAt": "22-01-2026"
}
```

**Daily Salary**

```json
{
  "_id": "ObjectId",
  "userId": "String", // id nhân viên
  "date": "Date", // Ngày tính lương

  "role": "SalesStaff", // Role tại thời điểm tính
  "hourlyRate": 25000, // Giá lương tại thời điểm tính

  "workHours": 8.0,
  "dailySalary": 200000,

  "createdAt": "Date"
}
```
