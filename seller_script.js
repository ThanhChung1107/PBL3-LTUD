document.querySelectorAll(".sidebar-item").forEach(item => {
    item.addEventListener("click", function () {
        this.classList.toggle("active"); // Toggle class active
    });
});

document.querySelectorAll(".all-product-item").forEach(item => {
    item.addEventListener("click", function () {
        // Xóa class "item-active" khỏi tất cả các phần tử
        document.querySelectorAll(".all-product-item").forEach(el => el.classList.remove("item-active"));

        // Thêm class "item-active" vào phần tử được click
        this.classList.add("item-active");
    });
});


// Chọn tất cả các mục sidebar
document.addEventListener("DOMContentLoaded", function () {
    let dropdownLinks = document.querySelectorAll(".dropdown-item a");

    dropdownLinks.forEach(link => {
        link.addEventListener("click", function (event) {
            

            // Xóa class active khỏi tất cả các link
            dropdownLinks.forEach(item => item.classList.remove("active"));

            // Thêm class active vào link được click
            this.classList.add("active");
        });
    });
});
// Chọn tất cả các mục sidebar
document.addEventListener("DOMContentLoaded", function () {
    let dropdownLinks = document.querySelectorAll(".dropdown-item a");

    dropdownLinks.forEach(link => {
        link.addEventListener("click", function (event) {
            let linkHref = this.getAttribute("href");

            // Nếu href rỗng (""), ngăn load lại trang
            if (!linkHref || linkHref === "") {
                event.preventDefault();
            }
            // Xóa class active khỏi tất cả các link
            dropdownLinks.forEach(item => item.classList.remove("active"));

            // Thêm class active vào link được click
            this.classList.add("active");
        });
    });
});

document.getElementById("filter").addEventListener("change", function() {
    let selectedText = this.options[this.selectedIndex].text;
    document.getElementById("SearchInput").placeholder = selectedText;
});

function showSection(sectionId) {
    // Ẩn tất cả các section
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });

    // Hiện section được chọn
    setTimeout(() => {
        document.getElementById(sectionId).classList.add('active');
    }, 50);
}

// Khi load trang, chỉ hiển thị "Đơn mua"
document.addEventListener("DOMContentLoaded", function() {
    showSection('order'); // Đặt mặc định là 'order'
});



