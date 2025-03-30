document.addEventListener("DOMContentLoaded", function () {
    let menuItems = document.querySelectorAll(".has-submenu");

    menuItems.forEach(item => {
        item.addEventListener("click", function (event) {
            event.stopPropagation();

            let submenu = item.querySelector(".submenu");
            let arrow = item.querySelector(".arrow");

            let isOpen = item.classList.contains("active");

            // Đóng tất cả submenu khác trước khi mở submenu mới
            document.querySelectorAll(".has-submenu").forEach(i => {
                if (i !== item) {
                    i.classList.remove("active");
                    i.querySelector(".submenu").style.maxHeight = "0px";
                    i.querySelector(".arrow").style.transform = "rotate(0deg)";
                }
            });

            if (!isOpen) {
                item.classList.add("active");
                submenu.style.maxHeight = submenu.scrollHeight + "px"; // Mở submenu theo chiều cao thực tế
                arrow.style.transform = "rotate(90deg)";
            } else {
                item.classList.remove("active");
                submenu.style.maxHeight = "0px"; // Đóng submenu
                arrow.style.transform = "rotate(0deg)";
            }
        });
    });

    // Đóng submenu khi click ra ngoài
    document.addEventListener("click", function () {
        document.querySelectorAll(".has-submenu").forEach(i => {
            i.classList.remove("active");
            i.querySelector(".submenu").style.maxHeight = "0px";
            i.querySelector(".arrow").style.transform = "rotate(0deg)";
        });
    });
});
