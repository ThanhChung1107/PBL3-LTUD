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
                document.querySelectorAll('.chat-item').forEach(i => i.classList.remove('active'));
                this.classList.add('active');

                const chatId = this.getAttribute('data-chat');
                const chatName = this.querySelector('.chat-name').textContent;
                const avatar = this.querySelector('.chat-avatar').textContent;

                document.querySelector('.chat-header-name').textContent = chatName;
                document.querySelector('.chat-header-avatar').textContent = avatar;

                // Load different messages based on chat
                loadChatMessages(chatId);
            });
        });

        // Message input functionality
        const messageInput = document.getElementById('messageInput');
        messageInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        });

        messageInput.addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = this.scrollHeight + 'px';
        });

        function sendMessage() {
            const input = document.getElementById('messageInput');
            const message = input.value.trim();

            if (message) {
                const messagesContainer = document.getElementById('messages');
                const messageDiv = document.createElement('div');
                messageDiv.className = 'message sent';
                messageDiv.innerHTML = `
                    <div class="message-bubble">${message}</div>
                    <div class="message-avatar">B</div>
                `;
                messagesContainer.appendChild(messageDiv);
                messagesContainer.scrollTop = messagesContainer.scrollHeight;

                input.value = '';
                input.style.height = 'auto';

                // Simulate response
                setTimeout(() => {
                    const responseDiv = document.createElement('div');
                    responseDiv.className = 'message received';
                    responseDiv.innerHTML = `
                        <div class="message-avatar">M</div>
                        <div class="message-bubble">Cảm ơn bạn đã chia sẻ!</div>
                    `;
                    messagesContainer.appendChild(responseDiv);
                    messagesContainer.scrollTop = messagesContainer.scrollHeight;
                }, 1000);
            }
        }

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

        function loadChatMessages(chatId) {
            const messagesContainer = document.getElementById('messages');
            messagesContainer.innerHTML = '';

            // Sample messages for different chats
            const chatMessages = {
                '1': [
                    {type: 'received', avatar: 'M', message: 'Chào mọi người! Có ai biết shop nào bán iPhone 15 Pro uy tín không?'},
                    {type: 'sent', avatar: 'B', message: 'Mình biết một shop rất tốt, để mình share sản phẩm nhé!'},
                    {type: 'product', avatar: 'M', product: {name: 'iPhone 15 Pro Max 256GB', price: '29.990.000₫', emoji: '📱'}},
                    {type: 'received', avatar: 'L', message: 'Wow giá tốt đấy! Shop này ở đâu vậy?'},
                    {type: 'sent', avatar: 'B', message: 'Shop ở Hà Nội, ship toàn quốc. Bảo hành 12 tháng chính hãng'}
                ],
                '2': [
                    {type: 'received', avatar: 'T', message: 'Ai có laptop gaming tầm 30 triệu không?'},
                    {type: 'received', avatar: 'N', message: 'Mình có ASUS ROG mới, còn bảo hành'},
                    {type: 'product', avatar: 'N', product: {name: 'Gaming Laptop ASUS', price: '35.990.000₫', emoji: '🎮'}}
                ],
                '3': [
                    {type: 'received', avatar: 'A', message: 'Bạn có máy ảnh Canon không?'},
                    {type: 'sent', avatar: 'B', message: 'Có nhiều loại, bạn cần tầm giá nào?'},
                    {type: 'received', avatar: 'A', message: 'Khoảng 50-80 triệu'},
                    {type: 'product', avatar: 'B', product: {name: 'Canon EOS R5', price: '89.990.000₫', emoji: '📷'}}
                ],
                '4': [
                    {type: 'received', avatar: 'H', message: 'Sale 50% toàn bộ áo khoác!'},
                    {type: 'received', avatar: 'K', message: 'Có size L không shop?'},
                    {type: 'sent', avatar: 'B', message: 'Còn nhiều size, inbox shop nhé!'}
                ]
            };

            const messages = chatMessages[chatId] || [];
            messages.forEach(msg => {
                const messageDiv = document.createElement('div');
                messageDiv.className = `message ${msg.type === 'sent' ? 'sent' : 'received'}`;

                if (msg.type === 'product') {
                    messageDiv.innerHTML = `
                        <div class="message-avatar">${msg.avatar}</div>
                        <div class="message-bubble product-message">
                            <div class="product-card">
                                <div class="product-image">${msg.product.emoji}</div>
                                <div class="product-info">
                                    <div class="product-name">${msg.product.name}</div>
                                    <div class="product-price">${msg.product.price}</div>
                                    <div class="product-description">Sản phẩm chất lượng cao, giá cả phải chăng</div>
                                    <div class="product-actions">
                                        <button class="product-btn view-btn">Xem chi tiết</button>
                                        <button class="product-btn buy-btn">Mua ngay</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
                } else {
                    messageDiv.innerHTML = `
                        ${msg.type === 'sent' ? '' : `<div class="message-avatar">${msg.avatar}</div>`}
                        <div class="message-bubble">${msg.message}</div>
                        ${msg.type === 'sent' ? `<div class="message-avatar">${msg.avatar}</div>` : ''}
                    `;
                }

                messagesContainer.appendChild(messageDiv);
            });

            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        }

        // Close modal when clicking outside
        document.getElementById('productModal').addEventListener('click', function(e) {
            if (e.target === this) {
                closeProductModal();
            }
        });


// Hiển thị modal chi tiết nhóm
function showGroupDetail() {
    // Cập nhật dữ liệu nhóm
    // document.getElementById('detailGroupAvatar').src = groupData.avatar || 'https://via.placeholder.com/150';
    // document.getElementById('detailGroupName').textContent = groupData.name;
    // document.getElementById('detailMemberCount').textContent = `${groupData.memberCount} thành viên`;
    // document.getElementById('detailGroupCode').textContent = groupData.groupCode;

    // // Hiển thị modal
    document.getElementById('groupDetailModal').style.display = 'flex';
}

// Các hàm xử lý
function changeGroupAvatar() {
    alert('Chức năng thay đổi ảnh nhóm');
    // Thêm logic upload ảnh
}

function editGroupName() {
    const currentName = document.getElementById('detailGroupName').textContent;
    const newName = prompt("Nhập tên nhóm mới:", currentName);
    if (newName && newName !== currentName) {
        document.getElementById('detailGroupName').textContent = newName;
        alert('Đã cập nhật tên nhóm!');
    }
}

function viewMembers() {
    alert('Chức năng xem danh sách thành viên');
}

function copyGroupCode() {
    const groupCode = document.getElementById('detailGroupCode').textContent;
    navigator.clipboard.writeText(groupCode);
    alert('Đã sao chép mã nhóm: ' + groupCode);
}

function shareGroup() {
    alert('Chức năng chia sẻ nhóm');
}

function leaveGroup() {
    if (confirm('Bạn có chắc chắn muốn rời nhóm này?')) {
        alert('Đã rời nhóm thành công');
        closeModal('groupDetailModal');
    }
}

// Xử lý form submit bằng AJAX
document.getElementById('joinGroupForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const form = e.target;
    const formData = new FormData(form);
    const errorElement = document.getElementById('joinGroupError');
    errorElement.textContent = '';

    // Validate client-side
    const groupCode = formData.get('groupCode');
    if (!/^[A-Za-z0-9]{6}$/.test(groupCode)) {
        document.getElementById('groupCodeError').textContent = 'Mã nhóm phải gồm 6 ký tự chữ hoặc số';
        return;
    }

    // Gửi request bằng AJAX
    fetch(form.action, {
        method: 'POST',
        body: formData,
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw err; });
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            // Thành công - reload trang hoặc cập nhật UI
            window.location.reload();
        } else {
            // Hiển thị lỗi
            errorElement.textContent = data.message;
        }
    })
    .catch(error => {
        errorElement.textContent = error.message || 'Có lỗi xảy ra khi tham gia nhóm';
    });
});