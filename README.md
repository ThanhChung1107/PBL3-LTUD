# 🛒 MegaMart - E-commerce Platform

## 📖 Project Overview
MegaMart is an **e-commerce web platform** that connects buyers and sellers,  
allowing users to **buy and sell products online, manage carts, place orders,  
make payments, and track orders**.  
The system also provides **chat groups by topic** for users to share product  
information, discuss, and shop together conveniently and safely.  

---

## 1️⃣ Overall System Architecture
The system consists of **three main layers**:

- 🎨 **Frontend**: HTML, CSS, JavaScript  
- ⚙️ **Backend**: Spring Boot (Java)  
- 🗄️ **Database**: MySQL  
- 🔄 **Realtime Communication**: WebSocket / STOMP (for group chat)  
- 🔐 **Authentication & Authorization**: Spring Security + JWT  

---

## 2️⃣ User Roles & Permissions

### 👑 Admin
- 👥 Manage users: view list, lock accounts, assign roles  
- 🏪 Manage shops: approve or delete shops  
- 📦 Manage products: approve new products, remove or edit violating products  
- 📑 Manage orders: monitor and intervene in transaction processes when issues arise  
- 💬 Manage chat groups: monitor discussions and handle reports  

### 🛍 Seller
- ➕ Post, edit, hide, or delete their own products  
- 📦 Manage orders of their shop  
- 📊 View revenue statistics and product reviews  
- 💬 Join or create chat groups by topics *(e.g., “Korean Cosmetics,” “Affordable Electronics”)*  

### 👤 Buyer (Customer)
- 🔍 Browse and search products, add to cart, place orders  
- ⭐ Review products after purchase  
- 💬 Participate in chat groups, ask questions, share comments, invite friends to shop together  

---
## MegarMart Project Directory Structure

```  
src
└── main
├── java/com/ProjectPBL3/MegarMart
│ ├── Controller
│ │ ├── AccountController.java
│ │ ├── AdminController.java
│ │ ├── ChatController.java
│ │ ├── SellerController.java
│ │ └── UserController.java
│ │
│ ├── Entity
│ │ ├── Account.java
│ │ ├── Cart.java
│ │ ├── Category.java
│ │ ├── ChatGroup.java
│ │ ├── Coupon.java
│ │ ├── GroupMember.java
│ │ ├── Message.java
│ │ ├── OrderDetail.java
│ │ ├── Orders.java
│ │ ├── Product.java
│ │ ├── RatingLevel.java
│ │ ├── ReviewProduct.java
│ │ ├── Role.java
│ │ ├── SharedProduct.java
│ │ └── Shop.java
│ │
│ ├── PaymentConfig
│ │ ├── VNPAYConfig.java
│ │ └── VNPAYService.java
│ │
│ ├── Repository
│ │ ├── AccountRepository.java
│ │ ├── CartRepository.java
│ │ ├── CategoryRepository.java
│ │ ├── ChatGroupRepository.java
│ │ ├── CouponRepository.java
│ │ ├── GroupMemberRepository.java
│ │ ├── MessageRepository.java
│ │ ├── OrderDetailRepository.java
│ │ ├── OrdersRepository.java
│ │ ├── ProductRepository.java
│ │ ├── ReviewProductRepository.java
│ │ ├── RoleRepository.java
│ │ └── ShopRepository.java
│ │
│ ├── SecurityConfig
│ │ ├── CustomAccountDetailsService.java
│ │ ├── CustomLoginSuccessHandler.java
│ │ ├── OAuth2LoginSuccessHandler.java
│ │ └── SecurityConfig.java
│ │
│ ├── Service
│ │ ├── AccountService.java
│ │ ├── CartService.java
│ │ ├── CategoryService.java
│ │ ├── ChatGroupService.java
│ │ ├── CouponService.java
│ │ ├── EmailService.java
│ │ ├── FileSystemStorageService.java
│ │ ├── MessageService.java
│ │ ├── OrderDetailService.java
│ │ ├── OrdersService.java
│ │ ├── ProductService.java
│ │ ├── ReviewProductService.java
│ │ └── ShopService.java
│ │
│ ├── config
│ │ └── WebSocketConfig.java
│ │
│ └── MegarMartApplication.java
│
└── resources
├── static
│ ├── css/
│ ├── img/
│ └── js/
│
└── templates
├── Admin
│ ├── Add_Shop.html
│ ├── Home.html
│ ├── OrderHistory.html
│ ├── Shop.html
│ ├── add_category.html
│ ├── add_coupon.html
│ ├── category.html
│ ├── coupon.html
│ ├── edit_category.html
│ ├── edit_coupon.html
│ ├── product.html
│ └── stats_shop.html
│
├── Payment/
├── Seller/
├── SignUp_SignIn/
├── User/
└── fragments/
```  


## 3️⃣ Main Features

### 🛒 Ordering
- 🛍 **Shopping cart**: add, edit, remove products  
- 💳 Place orders and make payments *(simulate VNPay)*  

### 💬 Chatbox
- 📢 **Group chat by topic**: users can join or create chat groups (e.g., Cosmetics, Electronics)  
- 🔄 **Realtime messaging**: powered by WebSocket / STOMP  
- 🛍 **Product sharing in chat**: click product link in chat to open product detail page  
- 👥 **Community shopping**: discuss, recommend products, and invite friends  

---

## 📸 Program Screenshots

### 🏠 Homepage
![Homepage](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/homepage.png)  

### 📄 Product Detail
![Product Detail](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/productdetail.png)  

### 📦 Order Page
![Order Page](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/order.png)  

### 💬 Chatbox
![Chatbox](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/chatbox.png)  
