# 1. Work Shift Management API

**_ Information _**

**_ Header And Required _**

```json
"Authorization": "Bearer {token}",
"role": {
    "name": "Admin"
}
```

## **Base Endpoint** `/api/v1`

---

## 2.1. Shift Role Configs

---

## 2.1.1 Get role configs list

**Endpoint:** `GET /api/v1/shift-role-configs`

**Query Parameters**

```json
{
  "isActive": true
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get role configs successfully.",
  "data": [
    {
      "id": "String",
      "configName": "String",
      "description": "String",
      "requirements": [
        {
          "accountType": "SalesStaff",
          "quantity": 2
        },
        {
          "accountType": "WarehouseStaff",
          "quantity": 2
        }
      ]
    }
  ]
}
```

---

## 2.1.2 Create role config

**Endpoint:** `POST /api/v1/shift-role-configs`

**Request Body**

```json
{
  "configName": "String",
  "requirements": [
    {
      "accountType": "SalesStaff",
      "quantity": 3
    },
    {
      "accountType": "WarehouseStaff",
      "quantity": 3
    }
  ]
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Role config created successfully.",
  "data": {
    "id": "String",
    "configName": "String",
    "isActive": true
  }
}
```

---

---

## 2.1.3 Update role config

**Endpoint:** `PUT /api/v1/shift-role-configs/{id}`

**Request Body**

```json
{
  "configName": "String",
  "requirements": [
    {
      "accountType": "SalesStaff",
      "quantity": 3
    },
    {
      "accountType": "WarehouseStaff",
      "quantity": 3
    }
  ],
  "isActive": "true" || "false"
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Role config updated successfully.",
  "data": {
    "id": "String",
    "configName": "Ca sáng",
    "requirements": [
      {
        "accountType": "SalesStaff",
        "quantity": 3
      },
      {
        "accountType": "WarehouseStaff",
        "quantity": 2
      }
    ],
    "isActive": true
  }
}
```

## 2.1.4 Delete role config

**Endpoint:** `DELETE /api/v1/shift-role-configs/{id}`

**Request Body**

```json
{
  "success": true,
  "message": "Role config deleted successfully.", //  Xoá mềm, set isActive = falsé
  "data": null
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Role config updated successfully.",
  "data": {
    "id": "String",
    "configName": "Ca sáng",
    "requirements": [
      {
        "accountType": "SalesStaff",
        "quantity": 3
      },
      {
        "accountType": "WarehouseStaff",
        "quantity": 2
      }
    ],
    "isActive": true
  }
}
```

---

## 2.2. Work Shift

---

## 2.2.1 Get work shifts

**Endpoint:** `GET /api/v1/work-shifts`

```json
{
  "isActive": true
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get word shifts successfully.",
  "data": [
    {
      "id": "String",
      "shiftName": "String",
      "startTime": "HH:mm",
      "endTime": "HH:mm",
      "isActive": true,
      "roleConfig": {
        "id": "String",
        "configName": "String"
      }
    }
  ]
}
```

---

## 2.2.2 Create shift template

**Endpoint:** `POST /api/v1/work-shift-templates`

**Request Body**

```json
{
  "shiftName": "String",
  "startTime": "HH:mm",
  "endTime": "HH:mm",
  "roleConfigId": "ConfigId_01" //ref WorkShift_Config
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Shift template created successfully.",
  "data": {
    "id": "String",
    "shiftName": "Ca Sáng",
    "isActive": true
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "endTime": "End time must be after start time.",
    "roleConfigId": "Role config not found."
  }
}
```

## 2.3 Work Schedules

## 2.3.1 Get schedules

**Endpoint:** `GET /api/v1/work-schedules`

**Request Body**

```json
{
  "startDate": "dd-MM-yyyy", // Required
  "endDate": "dd-MM-yyyy", // Required
  "profileId": "string", // Filter lịch của riêng 1 nhân viên
  "workShiftId": "string" // Filter theo ca
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get schedules successfully.",
  "data": [
    {
      "id": "String",
      "workDate": "Date",
      "workShiftId": "String",
      "shiftName": "String",
      "startTime": "HH:mm",
      "endTime": "HH:mm",
      "isCompliant": false,
      "missingRoles": [{ "accountType": "String", "quantity": 1 }],
      "requirementsRoles": [{ "accountType": "String", "quantity": 1 }],
      "assignments": [
        {
          "profileId": "StaffId",
          "fullName": "String",
          "accountType": "String",
          "status": "Assigned"
        }
      ]
    }
  ]
}
```

## 2.3.2 Assign Staff to Shift (Phân ca)

**Endpoint:** `POST /api/v1/work-schedules`

**Request Body**

```json
{
  "workDates": ["20-01-2026", "21-01-2026", "22-01-2026"], // Mảng các ngày muốn phân ca
  "workShiftId": "String", // Chọn ca làm
  "assignedStaffIds": ["staff_1", "staff_2"] // Chọn danh sách nhân viên
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Staff assigned successfully.",
  "data": {
    "assignedCount": 2, //Số ngày đã đếm được để hiển thị list UI

    "scheduleStatus": [
      {
        "workDate": "20-01-2026",
        "isCompliant": false, //Thiếu người -> FE hiện đỏ/warning...
        "missingRoles": [{ "accountType": "String", "quantity": 1 }] //Hiển thị số còn thiếu để thêm
      },
      {
        "workDate": "21-01-2026",
        "isCompliant": true, //Đủ người -> FE hiện check complete/xanh...
        "missingRoles": []
      }
    ]
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Assignment failed due to conflicts.",
  "errors": {
    "20-01-2026": {
      "staff_1": "Staff 'fullName' is already assigned to this shift."
    },
    "21-01-2026": {
      "staff_2": "Staff 'fullName' exceeds 8 working hours that day."
    }
  }
}
```

---

## 2.3.2 Remove staff

**Endpoint:** `DELETE /api/v1/work-schedules`

**Request Body**

```json
{
  "workDates": ["20-01-2026", "21-01-2026", "22-01-2026"], // Mảng các ngày muốn phân ca
  "workShiftId": ["String"], // Chọn danh sách ca làm
  "assignedStaffIds": ["staff_1", "staff_2"] // Chọn danh sách nhân viên muốn huỷ
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Staff removed successfully.",
  "data": {
    "workDate": "20-01-2026",
    "isCompliant": false, // Xóa xong check thiếu người -> Update lại để hiển thị
    "missingRoles": [{ "accountType": "String", "quantity": 1 }] //Hiển thị số còn thiếu để thêm
  }
}
```

> **Note: Cho phép tạo ca trùng giờ nhau để xử lý trường hợp ca có ít nhân và ca cần nhiều nhân viên hoặc ca part-time thì không phải tạo nhiều ca**

> 1.  Sau khi **API 5.3.2 Phân ca** thực hiện lưu dữ liệu vào db, backend so sánh số lượng nhân viên theo role và quantity của ca đó với requirement và đánh dấu isCompliant theo tình trạng (thiếu = false, đủ = true)

> **Logic xử lý BE phân ca**
>
> 1. **API 5.3.2 Phân ca** kiểm tra trước khi lưu dữ liệu:
> 2. Kiểm tra trong ngày đó nếu nhân viên có ca làm bị trùng giờ thì báo lỗi về FE, không thêm và response 400
> 3. Nếu nhân viên thêm vào không trùng giờ, kiểm tra trong ngày đó nếu chưa tạo ca làm với WorkShiftId đó thì tạo WorkSchedule mới. Thêm nhân viên vào và kiểm tra requirement. Nếu đã có ca làm thì chỉ push thêm nhân viên mới vào assignments (cần check duplicate) và kiểm tra nếu thêm danh sách nhân viên vào thì còn đảm bảo requuirement không

> **Logic lưu**
>
> 1. Khi tạo một WorkSchedule cho ngày cụ thể, Backend copy toàn bộ thông tin config và workShift vào workSchedule
> 2. Khi phân ca hàng loạt bằng dùng mảng workDates và mảng staff, nếu bất kỳ trường hợp nào bị lỗi thì huỷ tất cả và báo lỗi

> **New collection db**

**WorkShift_RoleConfig**

**Lưu config về số nhân viên yêu cầu**

```json
{
  "_id": "String",
  "configName": "String",
  "isActive": "Boolean", // Thêm để xoá mềm
  "requirements": [
    { "accountType": "String", "Quantity": "Number" } //Account type: Role, Quantity: So nhan vien role do can cho ca
  ]
}
```

**WorkShift**

**Lưu các ca làm mẫu**

```json
{
  "id": "String",
  "shiftName": "String",
  "startTime": "String",
  "endTime": "String",
  "isActive": "Boolean", // Thêm để xoá mềm
  "roleConfigId": "ObjectId", // Thêm cái này để config nhân viên trong ca
  "roleConfigName": "String" // Lưu cache hiển thị
}
```

**WorkSchedule**

**Lưu lại lịch sử làm việc để xem lại khi cần**

```json
{
  "id": "String",
  "workDate": "Date",
  "shiftName": "String",
  "startTime": "String",
  "endTime": "String",

  "isCompliant": false, // Trạng thái ca đã đủ nhân viên hay không?
  "requirements": [{ "accountType": "String", "Quantity": "Number" }], // List yêu cầu
  "missingRoles": [{ "accountType": "String", "Quantity": "Number" }], // List còn thiếu

  "assignments": [
    {
      "profileId": "String",
      "fullName": "String",
      "accountType": "String",
      "email": "String",
      "phoneNumber": "String",
      "status": "String"
    }
  ]
}
```
