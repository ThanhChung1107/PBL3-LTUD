# 🛒 MegaMart - E-commerce Platform

## 1️⃣ Overall System Architecture  
The system consists of **three main layers**:

- 🎨 **Frontend**: HTML, CSS, JavaScript  
- ⚙️ **Backend**: Spring Boot (Java)  
- 🗄️ **Database**: MySQL  
- 🔄 **Realtime Communication**: WebSocket / STOMP (for group chat)  
- 🔐 **Authentication & Authorization**: Spring Security + JWT  

---

## 2️⃣ User Roles & Permissions  

### 👑 **Admin**
- 👥 Manage users: view list, lock accounts, assign roles  
- 🏪 Manage shops: approve or delete shops  
- 📦 Manage products: approve new products, remove or edit violating products  
- 📑 Manage orders: monitor and intervene in transaction processes when issues arise  
- 💬 Manage chat groups: monitor discussions and handle reports  

---

### 🛍 **Seller**
- ➕ Post, edit, hide, or delete their own products  
- 📦 Manage orders of their shop  
- 📊 View revenue statistics and product reviews  
- 💬 Join or create chat groups by topics *(e.g., “Korean Cosmetics,” “Affordable Electronics”)*  

---

### 👤 **Buyer (Customer)**
- 🔍 Browse and search products, add to cart, place orders  
- ⭐ Review products after purchase  
- 💬 Participate in chat groups, ask questions, share comments, invite friends to shop together  

---

## 3️⃣ Main Features  

### 🛒 Ordering  
- 🛍 **Shopping cart**: add, edit, remove products  
- 💳 Place orders and make payments *(simulate VNPay)*  

---

## 📸 Demo Screenshots  

### 🏠 Homepage  
![Homepage](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/homepage.png)  

### 📄 Product Detail  
![Product Detail](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/productdetail.png)  

### 📦 Order Page  
![Order Page](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/order.png)  
