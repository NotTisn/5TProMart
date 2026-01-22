# 📊 IS_ACTIVE FILTER STATUS - TẤT CẢ ENTITIES

**Ngày:** 22 Tháng 1, 2026  
**Mục đích:** Lọc và hiển thị các bản ghi chưa bị xóa mềm

---

## ✅ ENTITIES ĐÃ CÓ IS_ACTIVE FILTER (9/9 Master Data)

### 1. ✅ Product (Sản phẩm)
**Database:** ✅ `products.is_active`  
**Controller:** ✅ `GET /api/v1/products?includeDeleted=true`  
**Mặc định:** Chỉ lấy `is_active = true`  
**Use Case:** ProductUseCase - Filter theo isActive

---

### 2. ✅ Category (Danh mục sản phẩm)
**Database:** ✅ `product_categories.is_active`  
**Controller:** ✅ `GET /api/v1/product-categories?includeDeleted=true`  
**Mặc định:** Chỉ lấy `is_active = true`  
**Use Case:** CategoryUseCase - Filter theo isActive

---

### 3. ✅ Supplier (Nhà cung cấp)
**Database:** ✅ `suppliers.is_active`  
**Controller:** ✅ `GET /api/v1/suppliers?includeDeleted=true`  
**Mặc định:** Chỉ lấy `is_active = true`  
**Use Case:** SupplierUseCase - Filter theo isActive

---

### 4. ✅ Customer (Khách hàng)
**Database:** ✅ `customers.is_active`  
**Controller:** ✅ `GET /api/v1/customers?includeDeleted=true`  
**Mặc định:** Chỉ lấy `is_active = true`  
**Use Case:** CustomerUseCase - Filter theo isActive

---

### 5. ✅ Promotion (Khuyến mãi)
**Database:** ✅ `promotions.is_active`  
**Controller:** ✅ `GET /api/v1/promotions?includeDeleted=true`  
**Mặc định:** Chỉ lấy `is_active = true`  
**Use Case:** PromotionUseCase - Filter theo isActive

---

### 6. ✅ Staff (Nhân viên)
**Database:** ✅ `staff_profiles.is_active`  
**Controller:** ✅ `GET /api/v1/staff?includeDeleted=true`  
**Mặc định:** Chỉ lấy `is_active = true`  
**Use Case:** StaffUseCase - Filter theo isActive

---

### 7. ✅ WorkShift (Ca làm việc)
**Database:** ✅ `work_shifts.is_active`  
**Controller:** ✅ `GET /api/v1/work-shifts?includeDeleted=true`  
**Mặc định:** Chỉ lấy `is_active = true`  
**Use Case:** WorkShiftUseCase - Filter theo isActive + includeDeleted

---

### 8. ✅ Expense (Chi phí)
**Database:** ✅ `expenses.is_active`  
**Controller:** ✅ `GET /api/v1/expenses?includeDeleted=true`  
**Mặc định:** Chỉ lấy `is_active = true`  
**Use Case:** ExpenseUseCaseImpl - Filter theo isActive

---

### 9. ✅ ShiftRoleConfig (Cấu hình ca làm việc)
**Database:** ✅ `shift_role_configs.is_active`  
**Controller:** ✅ `GET /api/v1/shift-role-configs?isActive=true`  
**Mặc định:** Lấy tất cả (có thể filter với `?isActive=true/false`)  
**Use Case:** ShiftRoleConfigUseCase - Filter theo isActive

---

## ⚠️ ENTITIES KHÔNG CẦN IS_ACTIVE (Transactional Data)

### 1. ⚠️ Order (Đơn hàng)
**Database:** ❌ Không có `is_active`  
**Lý do:** Đơn hàng sử dụng **STATUS** thay vì soft delete
- `PAID` - Đã thanh toán
- `UNPAID` - Chưa thanh toán  
- `CANCELLED` - Đã hủy

**Controller:** `GET /api/v1/orders?status=PAID`  
**Gợi ý:** Không cần thêm isActive, dùng status để filter

---

### 2. ⚠️ PurchaseOrder (Đơn đặt hàng)
**Database:** ❌ Không có `is_active`  
**Lý do:** Đơn đặt hàng sử dụng **STATUS** để quản lý lifecycle
- `PENDING` - Chờ xử lý
- `APPROVED` - Đã duyệt
- `COMPLETED` - Hoàn thành
- `CANCELLED` - Đã hủy

**Controller:** `GET /api/v1/purchase-orders?status=PENDING`  
**Gợi ý:** Không cần thêm isActive, dùng status để filter

---

### 3. ⚠️ StockInventory (Tồn kho)
**Database:** ❌ Không có `is_active`  
**Lý do:** Inventory tracking theo lô (lot-based)
- Số lượng tồn kho được cập nhật realtime
- Không nên xóa mềm vì ảnh hưởng đến báo cáo tồn kho
- Khi hết hàng: `stock_quantity = 0`

**Controller:** `GET /api/v1/stock-inventories?productId=xxx`  
**Gợi ý:** Không cần thêm isActive, filter theo số lượng tồn kho

---

## 📋 SUMMARY

### ✅ Master Data Entities (9 entities) - CÓ IS_ACTIVE
Các entity này là **dữ liệu chủ** (master data), cần soft delete để:
- Giữ lại lịch sử dữ liệu
- Có thể khôi phục khi cần
- Không ảnh hưởng đến transaction data
- Audit trail đầy đủ

**Tất cả 9 entities này đã có đầy đủ:**
1. Database column: `is_active BOOLEAN DEFAULT TRUE`
2. Controller parameter: `?includeDeleted=true` hoặc `?isActive=true`
3. Repository queries: Filter `is_active = true` by default
4. Restore endpoint: `POST /{id}/restore`

### ⚠️ Transactional Entities (3 entities) - KHÔNG CẦN IS_ACTIVE
Các entity này là **dữ liệu giao dịch** (transactional data):
- Order - Quản lý bằng STATUS
- PurchaseOrder - Quản lý bằng STATUS  
- StockInventory - Tracking realtime, không xóa

**Không nên thêm isActive cho các entity này vì:**
- Có hệ thống status riêng hoàn chỉnh
- Xóa mềm làm phức tạp logic tính toán
- Ảnh hưởng đến báo cáo tài chính
- Vi phạm quy tắc kế toán (không xóa transaction)

---

## 🎯 KẾT LUẬN

### ✅ HOÀN THÀNH 100%
**Tất cả 9 Master Data entities đã có đầy đủ isActive filter:**

```
✅ Product          - Filter by isActive ✓
✅ Category         - Filter by isActive ✓
✅ Supplier         - Filter by isActive ✓
✅ Customer         - Filter by isActive ✓
✅ Promotion        - Filter by isActive ✓
✅ Staff            - Filter by isActive ✓
✅ WorkShift        - Filter by isActive ✓
✅ Expense          - Filter by isActive ✓
✅ ShiftRoleConfig  - Filter by isActive ✓
```

### 📊 Transactional Entities
```
⚠️ Order            - Dùng STATUS thay vì isActive
⚠️ PurchaseOrder    - Dùng STATUS thay vì isActive
⚠️ StockInventory   - Tracking realtime, không xóa
```

---

## 💡 CÁCH SỬ DỤNG

### 1. Lấy dữ liệu active (mặc định)
```http
GET /api/v1/products
GET /api/v1/customers
GET /api/v1/suppliers
```
**Kết quả:** Chỉ trả về records có `is_active = true`

### 2. Lấy tất cả bao gồm đã xóa mềm
```http
GET /api/v1/products?includeDeleted=true
GET /api/v1/customers?includeDeleted=true
GET /api/v1/suppliers?includeDeleted=true
```
**Kết quả:** Trả về tất cả records (active + deleted)

### 3. Lọc theo trạng thái cụ thể (ShiftRoleConfig)
```http
GET /api/v1/shift-role-configs?isActive=true   # Chỉ active
GET /api/v1/shift-role-configs?isActive=false  # Chỉ inactive
GET /api/v1/shift-role-configs                  # Tất cả
```

### 4. Lấy đơn hàng theo status (không cần isActive)
```http
GET /api/v1/orders?status=PAID        # Đã thanh toán
GET /api/v1/orders?status=CANCELLED   # Đã hủy
GET /api/v1/orders                     # Tất cả
```

---

## 🚀 FRONTEND INTEGRATION

### React Component Example
```typescript
// Component với toggle để show/hide deleted items
function ProductList() {
  const [showDeleted, setShowDeleted] = useState(false);
  const [products, setProducts] = useState([]);

  useEffect(() => {
    const url = showDeleted 
      ? '/api/v1/products?includeDeleted=true'
      : '/api/v1/products';
    
    fetch(url)
      .then(res => res.json())
      .then(data => setProducts(data.data));
  }, [showDeleted]);

  return (
    <div>
      <label>
        <input 
          type="checkbox" 
          checked={showDeleted}
          onChange={(e) => setShowDeleted(e.target.checked)}
        />
        Hiển thị các mục đã xóa
      </label>

      <table>
        {products.map(product => (
          <tr 
            key={product.productId}
            className={!product.isActive ? 'text-gray-400 line-through' : ''}
          >
            <td>{product.productName}</td>
            <td>{product.price}</td>
            <td>
              {product.isActive ? (
                <span className="badge badge-success">Đang hoạt động</span>
              ) : (
                <span className="badge badge-secondary">Đã xóa</span>
              )}
            </td>
          </tr>
        ))}
      </table>
    </div>
  );
}
```

---

## 📝 NOTES

1. **Tất cả Master Data entities** đã có filter `isActive` hoàn chỉnh
2. **Transactional entities** không cần isActive vì có status riêng
3. **Frontend** có thể dùng parameter `?includeDeleted=true` để show tất cả
4. **Mặc định** API chỉ trả về active records (best practice)
5. **Admin** role required để xem deleted records và restore

---

**Status:** ✅ **100% COMPLETE**  
**Date:** January 22, 2026  
**Conclusion:** Tất cả các endpoint cần thiết đã có filter isActive. Không cần thêm cho các transactional entities.
