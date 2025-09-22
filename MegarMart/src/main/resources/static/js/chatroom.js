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

function showGroupDetail() {
    const modal = document.getElementById("groupDetailModal");
    if (modal) {
      modal.style.display = "block";
    }
  }

  // Đóng modal (nếu chưa có)
  function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
      modal.style.display = "none";
    }
  }

  // Đóng modal khi bấm ra ngoài vùng nội dung
  window.onclick = function(event) {
    const modal = document.getElementById("groupDetailModal");
    if (event.target === modal) {
      modal.style.display = "none";
    }
  }

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

       const avatar = message.senderAvatar
           ? '/img/' + message.senderAvatar
           : message.senderName.charAt(0);

       messageDiv.className = `message ${isSentByMe ? 'sent' : 'received'}`;

       if (isSentByMe) {
           messageDiv.innerHTML = `
               <div class="message-bubble">${message.content}</div>
               <div class="message-avatar">
                   <img src="${avatar}" alt="Avatar">
               </div>
           `;
       } else {
           messageDiv.innerHTML = `
               <div class="message-avatar">
                   <img src="${avatar}" alt="Avatar">
               </div>
               <div class="message-bubble">${message.content}</div>
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
    function shareProductToGroup(groupId, product) {
        const senderId = document.getElementById("userId").value;

        stompClient.send(
            `/app/chat/${groupId}/shareProduct`,
            {},
            JSON.stringify({
                senderId: senderId,
                product: product
            })
        );
    }
