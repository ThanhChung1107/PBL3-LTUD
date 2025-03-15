document.querySelectorAll(".sidebar-item").forEach(item => {
    item.addEventListener("click", function () {
        this.classList.toggle("active"); // Toggle class active
    });
});

// Chọn tất cả các mục sidebar
document.addEventListener("DOMContentLoaded", function () {
    let dropdownLinks = document.querySelectorAll(".dropdown-item a");

    dropdownLinks.forEach(link => {
        link.addEventListener("click", function (event) {
            event.preventDefault(); // Ngăn chặn load lại trang khi click

            // Xóa class active khỏi tất cả các link
            dropdownLinks.forEach(item => item.classList.remove("active"));

            // Thêm class active vào link được click
            this.classList.add("active");
        });
    });
});