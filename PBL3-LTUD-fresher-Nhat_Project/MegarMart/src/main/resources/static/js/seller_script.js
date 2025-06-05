document.addEventListener("DOMContentLoaded", function () {
  const headers = document.querySelectorAll(".sidebar-item-header");

  headers.forEach(header => {
    header.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation(); // Ngăn sự kiện lan truyền (nếu có)

      const currentDropdown = this.nextElementSibling;
      const icon = this.querySelector(".item-icon");

      // Đóng tất cả dropdown khác (trừ dropdown hiện tại)
      document.querySelectorAll(".dropdown-menu").forEach(menu => {
        if (menu !== currentDropdown) {
          menu.classList.remove("active");
          // Reset icon của các dropdown khác
          const otherIcon = menu.previousElementSibling.querySelector(".item-icon");
          otherIcon.classList.replace("bx-chevron-down", "bx-chevron-up");
        }
      });

      // Toggle dropdown hiện tại
      currentDropdown.classList.toggle("active");

      // Toggle icon (đổi giữa up/down)
      if (currentDropdown.classList.contains("active")) {
        icon.classList.replace("bx-chevron-up", "bx-chevron-down");
      } else {
        icon.classList.replace("bx-chevron-down", "bx-chevron-up");
      }
    });
  });

  // Đóng dropdown khi click ra ngoài
  document.addEventListener("click", function (e) {
    if (!e.target.closest(".sidebar-item-header")) {
      document.querySelectorAll(".dropdown-menu").forEach(menu => {
        menu.classList.remove("active");
        const icon = menu.previousElementSibling.querySelector(".item-icon");
        icon.classList.replace("bx-chevron-down", "bx-chevron-up");
      });
    }
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

    // Hiển thị section được chọn
    document.getElementById(sectionId).classList.add('active');

    // Cập nhật active tab
    document.querySelectorAll('.all-product-item').forEach(item => {
        item.classList.remove('item-active');
    });
    event.currentTarget.classList.add('item-active');

    // Lấy keyword từ URL hoặc input
    const keyword = document.querySelector('.category-search-input').value;
    const statusMap = {
        'all-product': null,
        'is-active-product': 1,
        'violate-product': 2,
        'pending-product': 0
    };
    const status = statusMap[sectionId];

    // Chuyển hướng với các tham số
    window.location.href = `/seller/product?page=1${keyword ? `&keyword=${encodeURIComponent(keyword)}` : ''}${status !== null ? `&status=${status}` : ''}`;
}

// Khi load trang, chỉ hiển thị "Đơn mua"
document.addEventListener("DOMContentLoaded", function() {
    showSection('order'); // Đặt mặc định là 'order'
});



