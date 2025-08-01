1. Overall System Architecture
 - The system consists of three main layers:
  Frontend: HTML/CSS, JavaScript
  Backend: Spring Boot (Java)
  Database: MySQL 
  Realtime Communication: WebSocket / STOMP (for group chat)
  Authentication & Authorization: Spring Security + JWT
2. User Roles and Permissions
 a. Admin
  - Manage users: view list, lock accounts, assign roles.
  - Manage shops: approve or delete shops.
  - Manage products: approve new products, remove or edit violating products.
  - Manage orders: monitor and intervene in transaction processes when issues arise.
  - Manage chat groups: monitor discussion groups and handle reports.
 b. Seller
  - Post, edit, hide/delete their own products.
  - Manage orders of their shop.
  - View revenue statistics and product reviews.
  - Join or create chat groups to promote products by topics (e.g., “Korean Cosmetics,” “Affordable Electronics”).
 c. Buyer (User/Customer)
  - Browse and search products, add to cart, place orders.
  - Review products after purchase.
  - Participate in chat groups, ask questions, share comments, invite friends to shop together.

3. Main Features
 a. Ordering
  - Shopping cart: add, edit, remove products.
  - Place orders and make payments (simulate VNPay).
== demo image 
 --Homepage
![Logo GitHub](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/homepage.png)

 -- Product Detail
![Logo GitHub](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/productdetail.png)

 -- Order Page
![Logo GitHub](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/order.png)

 -- Successful order notification via gmail
![Logo GitHub](https://github.com/ThanhChung1107/PBL3-LTUD/blob/MegaMart_scp/tb_dathangthanhcong.png)

 ➕
 ➕ 
 ➕
 ➕





 c. Topic-based Group Chat
  - Real-time operation (using WebSocket).
  - Users can join existing groups or create new groups.
  - Send messages and share products within the chat group.
  - Click on products in the chat to view details or make quick purchases.
  - Chat groups are categorized by topics.



