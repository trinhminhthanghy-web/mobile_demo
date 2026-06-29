# 📱 MobileShop - Ứng dụng Mobile Bán Thực Phẩm

## Giới thiệu đề tài

### Bài toán

Trong bối cảnh mua sắm trực tuyến ngày càng phát triển, nhu cầu đặt mua thực phẩm trên thiết bị di động ngày càng tăng. Tuy nhiên nhiều ứng dụng hiện nay còn thiếu tính trực quan, quản lý dữ liệu chưa hiệu quả và trải nghiệm người dùng chưa tối ưu.

Đề tài **MobileShop** được xây dựng nhằm phát triển một ứng dụng Android hỗ trợ người dùng tìm kiếm, mua thực phẩm trực tuyến đồng thời giúp quản trị viên dễ dàng quản lý sản phẩm, danh mục và đơn hàng.

---

### Mục tiêu

- Xây dựng ứng dụng Android bán thực phẩm.
- Hỗ trợ đăng ký, đăng nhập tài khoản.
- Quản lý sản phẩm và danh mục.
- Quản lý giỏ hàng.
- Thanh toán đơn hàng.
- Quản lý yêu thích.
- Theo dõi lịch sử mua hàng.
- Thống kê doanh thu cho quản trị viên.

---

# Dataset

> **Lưu ý:** Đây là dự án phát triển ứng dụng Mobile nên **không sử dụng Dataset Machine Learning**.

Nguồn dữ liệu:

- Firebase Authentication
- Firebase Realtime Database / Firestore
- Firebase Storage

Các bảng dữ liệu chính:

| Bảng | Mô tả |
|------|------|
| users | Thông tin người dùng |
| products | Danh sách thực phẩm |
| categories | Danh mục |
| favorites | Sản phẩm yêu thích |
| orders | Đơn hàng |
| detailOrders | Chi tiết đơn hàng |

---

# Pipeline

Do đây không phải bài toán Machine Learning nên Pipeline được thay thế bằng quy trình xử lý của ứng dụng.

```
Đăng nhập
        ↓
Lấy dữ liệu Firebase
        ↓
Hiển thị sản phẩm
        ↓
Tìm kiếm sản phẩm
        ↓
Thêm vào giỏ hàng
        ↓
Thanh toán
        ↓
Lưu đơn hàng
        ↓
Thống kê
```

---

# Mô hình sử dụng

Không áp dụng các mô hình Machine Learning như:

- Logistic Regression
- Decision Tree
- Random Forest
- SVM

Ứng dụng được phát triển theo mô hình:

- Android Native
- Java
- Firebase Authentication
- Firebase Firestore / Realtime Database
- Firebase Storage

Lý do lựa chọn:

- Đồng bộ dữ liệu thời gian thực.
- Dễ triển khai.
- Không cần xây dựng Backend riêng.
- Tích hợp xác thực người dùng nhanh chóng.

---

# Kết quả

Đây là dự án phát triển ứng dụng nên không đánh giá bằng:

- MAE
- RMSE
- R²
- AUC
- Confusion Matrix

Các kết quả đạt được:

✅ Đăng ký tài khoản

✅ Đăng nhập

✅ Quản lý thực phẩm

✅ Quản lý danh mục

✅ Giỏ hàng

✅ Thanh toán

✅ Lịch sử mua hàng

✅ Danh sách yêu thích

✅ Quản lý đơn hàng

✅ Thống kê doanh thu

---

# Công nghệ sử dụng

- Java
- Android Studio
- Firebase Authentication
- Firebase Firestore
- Firebase Storage
- XML
- RecyclerView
- Material Design

---

# Hướng dẫn chạy

## Cài môi trường

Yêu cầu:

- Android Studio Hedgehog trở lên
- JDK 17
- Android SDK 35
- Firebase Project

Clone project

```bash
git clone https://github.com/yourusername/MobileShop.git
```

Mở bằng Android Studio

Sync Gradle

Kết nối Firebase

Chạy ứng dụng.

---

## Chạy train

Không áp dụng vì đây không phải dự án Machine Learning.

---

## Chạy Demo / Inference

1. Kết nối thiết bị Android hoặc Emulator.

2. Nhấn

```
Run ▶
```

3. Đăng ký tài khoản.

4. Đăng nhập.

5. Thêm sản phẩm.

6. Thêm vào giỏ hàng.

7. Thanh toán.

---

# Cấu trúc thư mục

```
MobileShop/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   ├── res/
│   │   │   ├── AndroidManifest.xml
│   │
│   ├── build.gradle.kts
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── README.md
```

---

# Chức năng chính

### Người dùng

- Đăng ký
- Đăng nhập
- Tìm kiếm thực phẩm
- Xem danh mục
- Thêm yêu thích
- Thêm vào giỏ hàng
- Thanh toán
- Lịch sử mua hàng
- Cập nhật thông tin cá nhân

### Quản trị viên

- Thêm sản phẩm
- Sửa sản phẩm
- Xóa sản phẩm
- Quản lý danh mục
- Quản lý đơn hàng
- Thống kê doanh thu

---

# Cơ sở dữ liệu

Ứng dụng sử dụng Firebase với các collection:

- users
- products
- categories
- favorites
- orders
- detailOrders

---

# Kết quả đạt được

- Hoàn thành ứng dụng bán thực phẩm trên Android.
- Giao diện thân thiện.
- Quản lý dữ liệu bằng Firebase.
- Đồng bộ dữ liệu thời gian thực.
- Hỗ trợ cả người dùng và quản trị viên.

---

# Hướng phát triển

- Thanh toán VNPay/Momo.
- Thông báo đẩy Firebase Cloud Messaging.
- Chat giữa khách hàng và cửa hàng.
- Đánh giá sản phẩm.
- Theo dõi vận chuyển.
- Tích hợp AI gợi ý sản phẩm.

---

# Tác giả

**Họ và tên:** Trịnh Minh Thắng

**Mã sinh viên:** 10123306

**Lớp:** 12523T.1

**Môn học:** Lập trình Mobile cơ bản

**Trường:** Đại học Sư phạm Kỹ thuật Hưng Yên
