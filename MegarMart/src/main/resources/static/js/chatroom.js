// Hiển thị modal
function showCreateGroupModal() {
    document.getElementById('createGroupModal').style.display = 'flex';
}

function showJoinGroupModal() {
    document.getElementById('joinGroupModal').style.display = 'flex';
}

// Đóng modal
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// Xử lý tạo nhóm
function createGroup() {
    const groupName = document.querySelector('.group-name-input').value;
    if (!groupName) {
        alert('Vui lòng nhập tên nhóm');
        return;
    }
    alert(`Đã tạo nhóm "${groupName}" thành công!`);
    closeModal('createGroupModal');
}

// Xử lý tham gia nhóm
function joinGroup() {
    const groupCode = document.querySelector('.group-code-input').value;
    if (!groupCode) {
        alert('Vui lòng nhập mã nhóm');
        return;
    }
    alert(`Đã tham gia nhóm với mã "${groupCode}"!`);
    closeModal('joinGroupModal');
}

// Xử lý tải ảnh lên
document.getElementById('groupAvatar').addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(event) {
            const avatarPreview = document.querySelector('.avatar-preview');
            avatarPreview.innerHTML = `<img src="${event.target.result}" style="width:100%;height:100%;border-radius:50%;object-fit:cover;">`;
        }
        reader.readAsDataURL(file);
    }
});
        // Chat switching functionality
        document.querySelectorAll('.chat-item').forEach(item => {
            item.addEventListener('click', function() {
                // Remove active class from all items
                document.querySelectorAll('.chat-item').forEach(i => i.classList.remove('active'));

                // Add active class to clicked item
                this.classList.add('active');

                // Không cần update header vì Spring đã xử lý
                // Header sẽ được update khi trang reload qua link
            });
        });

        function openProductModal() {
            document.getElementById('productModal').style.display = 'block';
        }

        function closeProductModal() {
            document.getElementById('productModal').style.display = 'none';
        }

        function shareProduct(name, price, emoji) {
            const messagesContainer = document.getElementById('messages');
            const messageDiv = document.createElement('div');
            messageDiv.className = 'message sent';
            messageDiv.innerHTML = `
                <div class="message-bubble product-message">
                    <div class="product-card">
                        <div class="product-image">${emoji}</div>
                        <div class="product-info">
                            <div class="product-name">${name}</div>
                            <div class="product-price">${price}</div>
                            <div class="product-description">Sản phẩm chất lượng cao, giá cả phải chăng</div>
                            <div class="product-actions">
                                <button class="product-btn view-btn">Xem chi tiết</button>
                                <button class="product-btn buy-btn">Mua ngay</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="message-avatar">B</div>
            `;
            messagesContainer.appendChild(messageDiv);
            messagesContainer.scrollTop = messagesContainer.scrollHeight;

            closeProductModal();
        }

//        function loadChatMessages(chatId) {
//            const messagesContainer = document.getElementById('messages');
//            messagesContainer.innerHTML = '';
//
//            // Sample messages for different chats
//            const chatMessages = {
//                '1': [
//                    {type: 'received', avatar: 'M', message: 'Chào mọi người! Có ai biết shop nào bán iPhone 15 Pro uy tín không?'},
//                    {type: 'sent', avatar: 'B', message: 'Mình biết một shop rất tốt, để mình share sản phẩm nhé!'},
//                    {type: 'product', avatar: 'M', product: {name: 'iPhone 15 Pro Max 256GB', price: '29.990.000₫', emoji: '📱'}},
//                    {type: 'received', avatar: 'L', message: 'Wow giá tốt đấy! Shop này ở đâu vậy?'},
//                    {type: 'sent', avatar: 'B', message: 'Shop ở Hà Nội, ship toàn quốc. Bảo hành 12 tháng chính hãng'}
//                ],
//                '2': [
//                    {type: 'received', avatar: 'T', message: 'Ai có laptop gaming tầm 30 triệu không?'},
//                    {type: 'received', avatar: 'N', message: 'Mình có ASUS ROG mới, còn bảo hành'},
//                    {type: 'product', avatar: 'N', product: {name: 'Gaming Laptop ASUS', price: '35.990.000₫', emoji: '🎮'}}
//                ],
//                '3': [
//                    {type: 'received', avatar: 'A', message: 'Bạn có máy ảnh Canon không?'},
//                    {type: 'sent', avatar: 'B', message: 'Có nhiều loại, bạn cần tầm giá nào?'},
//                    {type: 'received', avatar: 'A', message: 'Khoảng 50-80 triệu'},
//                    {type: 'product', avatar: 'B', product: {name: 'Canon EOS R5', price: '89.990.000₫', emoji: '📷'}}
//                ],
//                '4': [
//                    {type: 'received', avatar: 'H', message: 'Sale 50% toàn bộ áo khoác!'},
//                    {type: 'received', avatar: 'K', message: 'Có size L không shop?'},
//                    {type: 'sent', avatar: 'B', message: 'Còn nhiều size, inbox shop nhé!'}
//                ]
//            };
//
//            const messages = chatMessages[chatId] || [];
//            messages.forEach(msg => {
//                const messageDiv = document.createElement('div');
//                messageDiv.className = `message ${msg.type === 'sent' ? 'sent' : 'received'}`;
//
//                if (msg.type === 'product') {
//                    messageDiv.innerHTML = `
//                        <div class="message-avatar">${msg.avatar}</div>
//                        <div class="message-bubble product-message">
//                            <div class="product-card">
//                                <div class="product-image">${msg.product.emoji}</div>
//                                <div class="product-info">
//                                    <div class="product-name">${msg.product.name}</div>
//                                    <div class="product-price">${msg.product.price}</div>
//                                    <div class="product-description">Sản phẩm chất lượng cao, giá cả phải chăng</div>
//                                    <div class="product-actions">
//                                        <button class="product-btn view-btn">Xem chi tiết</button>
//                                        <button class="product-btn buy-btn">Mua ngay</button>
//                                    </div>
//                                </div>
//                            </div>
//                        </div>
//                    `;
//                } else {
//                    messageDiv.innerHTML = `
//                        ${msg.type === 'sent' ? '' : `<div class="message-avatar">${msg.avatar}</div>`}
//                        <div class="message-bubble">${msg.message}</div>
//                        ${msg.type === 'sent' ? `<div class="message-avatar">${msg.avatar}</div>` : ''}
//                    `;
//                }
//
//                messagesContainer.appendChild(messageDiv);
//            });
//
//            messagesContainer.scrollTop = messagesContainer.scrollHeight;
//        }


//===========================================
   // WebSocket Connection
   //===========================================

   const socket = new SockJS('/chat-websocket');
   const stompClient = Stomp.over(socket);
   let currentGroupId = document.getElementById('currentGroupId')?.value;
   let currentUsername = document.getElementById('username')?.value;

   // Kết nối đến server
   stompClient.connect({}, function(frame) {
       console.log('Connected: ' + frame);

       // Subscribe vào topic nhận tin nhắn cho nhóm hiện tại
       if (currentGroupId) {
           subscribeToGroup(currentGroupId);
           joinChat(currentGroupId);
       }
       console.log('kết nối thành công')
   });

   function subscribeToGroup(groupId) {
       // Subscribe vào topic của nhóm cụ thể
       stompClient.subscribe(`/topic/group/${groupId}`, function(message) {
           showMessage(JSON.parse(message.body));
       });
   }

   function joinChat(groupId) {
       if (!currentUsername) {
           console.error('Username not found');
           return;
       }

       const joinMessage = {
           senderName: currentUsername,
           groupId: groupId,
           joinTime: new Date().toISOString()
       };

       stompClient.send(`/app/chat/${groupId}/join`, {}, JSON.stringify(joinMessage));
   }

   function leaveChat(groupId) {
       if (!currentUsername) return;

       const leaveMessage = {
           senderName: currentUsername,
           groupId: groupId
       };

       stompClient.send(`/app/chat/${groupId}/leave`, {}, JSON.stringify(leaveMessage));
   }

   function sendMessage() {
       const messageInput = document.getElementById('messageInput');
       const content = messageInput.value.trim();
       const groupId = document.getElementById('currentGroupId').value;
       const username = document.getElementById('username').value;
       const useravatar = document.getElementById('useravatar').value;

       if (content && groupId && stompClient && stompClient.connected) {
           const message = {
               content: content,
               senderName: username,
               groupId: groupId,
               senderAvatar: useravatar
           };

           // Gửi qua WebSocket với đúng mapping
           stompClient.send(`/app/chat/${groupId}/sendMessage`, {}, JSON.stringify(message));

           // Xóa nội dung input
           messageInput.value = '';
           messageInput.style.height = 'auto';
       } else {
           console.error('Cannot send message:', {
               content: !!content,
               groupId: !!groupId,
               connected: stompClient?.connected
           });
       }
   }

   function showMessage(message) {
       const messagesContainer = document.getElementById('messages');
       const currentUser = document.getElementById('username').value;

       let messageElement;

       if (message.type === 'JOIN' || message.type === 'LEAVE') {
           // Tin nhắn hệ thống

           messageElement = createSystemMessage(message);
       } else if (message.type === 'PRODUCT_SHARE') {
           // Tin nhắn chia sẻ sản phẩm
           messageElement = createProductMessage(message);
       } else {
           // Tin nhắn văn bản thông thường
           messageElement = createTextMessage(message, currentUser);
       }

       messagesContainer.appendChild(messageElement);
       messagesContainer.scrollTop = messagesContainer.scrollHeight;
   }

   function createTextMessage(message, currentUser) {
       const messageDiv = document.createElement('div');
       const isSentByMe = message.senderName === currentUser;

       messageDiv.className = `message ${isSentByMe ? 'sent' : 'received'}`;

       if (isSentByMe) {
           messageDiv.innerHTML = `
               <div class="message-bubble">${message.content}</div>
               <div class="message-avatar">
                   <img src="${'/img/' + message.senderAvatar || message.senderName.charAt(0)}" alt="Avatar">
               </div>
           `;
       } else {
           messageDiv.innerHTML = `
               <div class="message-avatar">${message.senderAvatar}</div>
               <div class="message-avatar">
                   <img src="${'/img/' + message.senderAvatar || message.senderName.charAt(0)}" alt="Avatar">
              </div>
           `;
       }

       return messageDiv;
   }

   function createSystemMessage(message) {
       const messageDiv = document.createElement('div');
       messageDiv.className = 'message system';
       messageDiv.innerHTML = `
           <div class="system-message">${message.content}</div>
       `;
       return messageDiv;
   }

   function createProductMessage(message) {
       const messageDiv = document.createElement('div');
       const currentUser = document.getElementById('username').value;
       const isSentByMe = message.senderName === currentUser;

       messageDiv.className = `message ${isSentByMe ? 'sent' : 'received'}`;

       const productHtml = `
           <div class="message-bubble product-message">
               <div class="product-card">
                   <div class="product-image">${message.sharedProduct?.image || '📦'}</div>
                   <div class="product-info">
                       <div class="product-name">${message.sharedProduct?.name || 'Sản phẩm'}</div>
                       <div class="product-price">${message.sharedProduct?.price || 'Liên hệ'}</div>
                       <div class="product-description">${message.sharedProduct?.description || 'Mô tả sản phẩm'}</div>
                       <div class="product-actions">
                           <button class="product-btn view-btn">Xem chi tiết</button>
                           <button class="product-btn buy-btn">Mua ngay</button>
                       </div>
                   </div>
               </div>
           </div>
       `;

       if (isSentByMe) {
           messageDiv.innerHTML = `
               ${productHtml}
               <div class="message-avatar">${message.senderAvatar}</div>
           `;
       } else {
           messageDiv.innerHTML = `
               <div class="message-avatar">${message.senderAvatar}</div>
               ${productHtml}
           `;
       }

       return messageDiv;
   }

   // Xử lý sự kiện gửi tin nhắn
   document.getElementById('messageInput').addEventListener('keypress', function(e) {
       if (e.key === 'Enter' && !e.shiftKey) {
           e.preventDefault();
           sendMessage();
       }
   });

   // Xử lý khi chuyển nhóm
   function switchGroup(newGroupId) {
       // Rời nhóm cũ
       if (currentGroupId) {
           leaveChat(currentGroupId);
       }

       // Cập nhật thông tin nhóm mới
       currentGroupId = newGroupId;
       document.getElementById('currentGroupId').value = newGroupId;

       // Subscribe vào nhóm mới
       subscribeToGroup(newGroupId);

       // Tham gia nhóm mới
       joinChat(newGroupId);
   }

   // Error handling
   stompClient.onStompError = function(frame) {
       console.error('STOMP error: ' + frame.body);
   };

   socket.onclose = function() {
       console.log('WebSocket connection closed');
   };