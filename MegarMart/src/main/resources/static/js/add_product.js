const selectImage = document.querySelector('.select-image');
const inputFile = document.querySelector('#file');
const imgAreaa = document.querySelector('.img-area');

selectImage.addEventListener('click', function () {
	inputFile.click();
})

inputFile.addEventListener('change', function () {
	const image = this.files[0]
	if(image.size < 2000000) {
		const reader = new FileReader();
		reader.onload = ()=> {
			const allImg = imgAreaa.querySelectorAll('img');
			allImg.forEach(item=> item.remove());
			const imgUrl = reader.result;
			const img = document.createElement('img');
			img.src = imgUrl;
			imgAreaa.appendChild(img);
			imgAreaa.classList.add('active');
			imgAreaa.dataset.img = image.name;
		}
		reader.readAsDataURL(image);
	} else {
		alert("Image size more than 2MB");
	}
})


// chọn ngành hàng trong thêm sản phẩm
const categoryItems = document.querySelectorAll(".category_item");
const categoryInput = document.querySelector(".category_input");
categoryItems.forEach(item => {
    item.addEventListener("click", function () {
        categoryInput.value = this.textContent;
    });
});


document.addEventListener("DOMContentLoaded", function () {
    const basicInfo = document.querySelector(".basic_info_lable");
    const sellInfo = document.querySelector(".sell_info_lable");

    function activateLabel(clickedLabel) {
        // Xóa class "active" khỏi cả hai thẻ
        basicInfo.classList.remove("add_active");
        sellInfo.classList.remove("add_active");

        // Thêm class "add_active" cho thẻ được nhấn
        clickedLabel.classList.add("add_active");
    }

    // Gán sự kiện click cho từng thẻ
    basicInfo.addEventListener("click", function () {
        activateLabel(basicInfo);
    });

    sellInfo.addEventListener("click", function () {
        activateLabel(sellInfo);
    });
});




document.addEventListener("DOMContentLoaded", function () {
    const btnSave = document.querySelector(".btn-save");
    const inputs = document.querySelectorAll(".register-input-type");

    btnSave.addEventListener("click", function (event) {
        let isValid = true;

        inputs.forEach(input => {
            if (input.value.trim() === "") {
                isValid = false;
                input.classList.add("error"); // Thêm class báo lỗi
            } else {
                input.classList.remove("error"); // Xóa class nếu đã nhập
            }
        });

        if (!isValid) {
            event.preventDefault(); // Ngăn chặn chuyển trang
            alert("Vui lòng nhập đầy đủ thông tin!");
        }
    });
});

const profileName = document.querySelector(".seller_profile_text")
const profileNameInput = document.querySelector(".seller_profile_text_edit");
profileNameInput.value = profileName.textContent;

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
    showSection('seller_profile');
});