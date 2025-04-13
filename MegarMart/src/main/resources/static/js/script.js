const mainImg = document.querySelector(".main-img");
const thumbnails = document.querySelectorAll(".thumbnail");

// Lặp qua từng ảnh thumbnail
thumbnails.forEach(thumbnail => {
    thumbnail.addEventListener("mouseenter", function() {
        // Đổi ảnh lớn thành ảnh được hover
        mainImg.src = this.src;

        // Xóa class "active" khỏi ảnh cũ
        thumbnails.forEach(img => img.classList.remove("active"));
        
        // Thêm class "active" vào ảnh được hover
        this.classList.add("active");
    });
});
const btnSub = document.querySelector(".product-info-sub");
const btnAdd = document.querySelector(".product-info-add");
const numCount = document.querySelector(".product-info-numcount");

// Xử lý sự kiện khi nhấn "-"
btnSub.addEventListener("click", function () {
    let currentValue = parseInt(numCount.textContent); // Lấy số hiện tại
    if (currentValue > 0) { // Đảm bảo không nhỏ hơn 0
        numCount.textContent = currentValue - 1;
    }
});

// Xử lý sự kiện khi nhấn "+"
btnAdd.addEventListener("click", function () {
    let currentValue = parseInt(numCount.textContent); // Lấy số hiện tại
    numCount.textContent = currentValue + 1;
});

function showSection(sectionId) {
    // Ẩn tất cả các section
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });

    // Hiện section được chọn
    document.getElementById(sectionId).classList.add('active');
}

// Khi load trang, chỉ hiển thị "Đơn mua"
document.addEventListener("DOMContentLoaded", function() {
    showSection('order'); // Đặt mặc định là 'order'
});


function hienPopup() {
    document.getElementById("popup").style.display = "block";
    document.getElementById("overlay").style.display = "block";
}

function anPopup() {
    document.getElementById("popup").style.display = "none";
    document.getElementById("overlay").style.display = "none";
}









