let deleteType = null;
let deleteItemId = null;

let editOrderProducts = [];
let createOrderProducts = [];


function showOverlay() {
    document.getElementById("overlay").style.display = "block";
}

function hideOverlay() {
    document.getElementById("overlay").style.display = "none";
}

function openPopup(id) {
    showOverlay();
    document.getElementById(id).style.display = "block";
}

function closeAllPopups() {
    hideOverlay();
    document.querySelectorAll(".popup-box, .delete-popup")
        .forEach(p => p.style.display = "none");
}

document.getElementById("overlay").addEventListener("click", closeAllPopups);


function openNotifPopup() {
    openPopup("notifPopup");
}

const userId = window.userId;

function sendNotification() {
    const ititle = document.getElementById("notifTitle");
    const icontent = document.getElementById("notifContent");
    const title = document.getElementById("notifTitle").value;
    const content = document.getElementById("notifContent").value;


    ititle.classList.remove("input-error");
    icontent.classList.remove("input-error");
    if (!title) {
        ititle.classList.add("input-error");
        ititle.focus();
        return;
    }
    if (!content) {
        icontent.classList.add("input-error");
        icontent.focus();
        return;
    }
    fetch("Notify", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "userIds=" + encodeURIComponent(userId) +
            "&title=" + encodeURIComponent(title) +
            "&content=" + encodeURIComponent(content)
    })
        .then(res => res.json())
        .then(data => {
            closeAllPopups();
            if (data.success) {
                show(data.message);

            } else {
                show(data.message, false);
            }
        })
        .catch(err => console.log(err));


}


function openGiftVoucherPopup() {
    openPopup("giftVoucherPopup");
}

function toggleGiftVoucher(el) {
    el.classList.toggle("selected");
    el.querySelector("input").checked =
        el.classList.contains("selected");
}

function submitGiftVoucher() {
    const selected = document.querySelectorAll(".voucher-gift-item.selected");
    if (selected.length === 0) {
        show("Chưa chọn voucher", false);
        return;
    }

    const voucherIds = Array.from(selected).map(item => item.dataset.code);

    fetch("GiftVoucher", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "userId=" + encodeURIComponent(userId) + "&voucherIds=" + voucherIds.join(",")
    })
        .then(res => res.json())
        .then(data => {
            closeAllPopups();
            if (data.success) {
                show(data.message);
                setTimeout(() => {
                    location.reload();
                }, 1500);
            } else {
                show(data.message, false);
            }
        })
        .catch(err => console.log(err));
}


function confirmDeleteItem(type, id) {
    deleteType = type;
    deleteItemId = id;

    document.getElementById("deletePopupMsg")
        .innerText = "Bạn có chắc muốn xoá không?";

    openPopup("deletePopup");
}

function confirmDelete() {

    if (deleteType === "voucher") {
        fetch("deleteVoucher", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "userId=" + encodeURIComponent(userId) + "&id=" + encodeURIComponent(deleteItemId)
        })
            .then(res => res.json())
            .then(data => {
                closeAllPopups();
                if (data.success) {
                    show("Thực hiện thành công");
                    setTimeout(() => {
                        location.reload();
                    }, 1500);
                } else {

                    show(data.message, false);
                }
            })
            .catch(err => console.log(err));

        closeAllPopups();
    }
    if (deleteType === "order") {
        fetch("DeleteOrderServlet", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "userId=" + encodeURIComponent(userId) + "&id=" + encodeURIComponent(deleteItemId)
        })
            .then(res => res.json())
            .then(data => {
                closeAllPopups();
                if (data.success) {
                    show(data.message);
                    setTimeout(() => {
                        location.reload();
                    }, 1500);
                } else {
                    show(data.message, false);
                }
            })
            .catch(err => console.log(err));

        closeAllPopups();
    }
    if (deleteType === "address") {
        fetch("deleteAddress", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "userId=" + encodeURIComponent(userId) + "&id=" + encodeURIComponent(deleteItemId)
        })
            .then(res => res.json())
            .then(data => {
                closeAllPopups();
                if (data.success) {
                    show("Xóa địa chỉ thành công");
                    setTimeout(() => {
                        location.reload();
                    }, 1500);
                } else {
                    show(data.message, false);
                }
            })
            .catch(err => console.log(err));

        closeAllPopups();
    }
}


function parseDate(str) {
    if (!str) return new Date(0);

    const [time, date] = str.split(" ");
    const [hours, minutes, seconds] = time.split(":");
    const [day, month, year] = date.split("-");

    return new Date(year, month - 1, day, hours, minutes, seconds);
}

function filterOrders() {
    const status = document.getElementById("filterStatus").value;
    const time = document.getElementById("filterTime").value;
    const tbody = document.getElementById("orderTableBody");
    let rows = Array.from(tbody.querySelectorAll("tr"));

    rows.forEach(r => {
        if (!status) {
            r.style.display = "";
        } else {
            r.style.display = r.dataset.status === status ? "" : "none";
        }
    });

    let visibleRows = rows.filter(r => r.style.display !== "none");

    if (time === "newest") {
        visibleRows.sort((a, b) =>
            parseDate(b.dataset.date) - parseDate(a.dataset.date));
    }
    if (time === "oldest") {
        visibleRows.sort((a, b) =>
            parseDate(a.dataset.date) - parseDate(b.dataset.date));
    }

    visibleRows.forEach(r => tbody.appendChild(r));

}


function viewOrderProducts(orderId) {
    fetch("GetOrderItemsServlet?orderId=" + orderId)
        .then(res => res.json())
        .then(data => {

            const tbody = document.getElementById("orderProductsBody");
            tbody.innerHTML = "";

            if (!data || data.length === 0) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="4" style="text-align:center">
                            Không có sản phẩm
                        </td>
                    </tr>
                `;
            } else {

                console.log(data);

                data.forEach(item => {
                    tbody.innerHTML += `
                        <tr>
                            <td>${item.bookName}</td>
                            <td>${item.quantity}</td>
                            <td>${Number(item.price).toLocaleString()} đ</td>
                            <td>${(item.subtotal).toLocaleString()} đ</td>
                        </tr>
                    `;
                });
            }

            openPopup("orderProductsPopup");
        })
        .catch(err => console.log("Lỗi load sản phẩm:", err));
}


function startEdit(field, currentVal, type) {
    const row = document.querySelector("#field-" + field + " .info-value-row");
    row.innerHTML = `
        <input type="${type}" value="${currentVal}" id="edit-${field}">
        <button onclick="saveInline('${field}')">Lưu</button>
        <button onclick="location.reload()">Hủy</button>
    `;
}

function saveInline(field) {
    const input = document.getElementById("edit-" + field);
    if (!input) return;

    let value = input.value.trim();


    if (!value) {
        show("Không được để trống", false);
        input.focus();
        return;
    }
    if (field === "email") {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            show("Email không hợp lệ", false);
            input.focus();
            return;
        }
    }
    if (field === "phone") {
        const phoneRegex = /^(03|05|07|08|09)[0-9]{8}$/;
        if (!phoneRegex.test(value)) {
            show("Số điện thoại không hợp lệ", false);
            input.focus();
            return;
        }
    }

    fetch("UpdataUserServlet", {
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded"},
        body: "userId=" + encodeURIComponent(userId) +
            "&field=" + encodeURIComponent(field) +
            "&value=" + encodeURIComponent(value)
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                show("Cập nhật thành công");
                setTimeout(() => location.reload(), 1500);
            } else {
                show("Thất bại", false);
            }
        })
        .catch(err => {
            console.error(err)
            show("Lỗi", false);
        });

}


function openEditOrderPopup(orderId, status, totalAmount) {

    document.getElementById("editOrderId").value = orderId;
    const statusSelect = document.getElementById("editOrderStatus");
    if (statusSelect) {
        statusSelect.value = status;
    }
    document.getElementById("editOrderTotal").value = totalAmount;

    openPopup("editOrderPopup");
}

function renderEditProducts() {
    const list = document.getElementById("editProductsList");
    if (editOrderProducts.length === 0) {
        list.innerHTML = "<div>Chưa có sản phẩm</div>";
        return;
    }
    list.innerHTML = editOrderProducts.map((p, i) => `
        <div class="edit-product-item">
            ${p.name} x${p.quantity}
            <i class="fa-solid fa-trash" onclick="removeEditProduct(${i})"></i>
        </div>
    `).join("");
}

function removeEditProduct(i) {
    editOrderProducts.splice(i, 1);
    renderEditProducts();
}

function saveEditOrder() {
    const orderId = document.getElementById("editOrderId").value;
    const status = document.getElementById("editOrderStatus").value;
    const total = Number(document.getElementById("editOrderTotal").value);

    if (!total || total <= 0 || !Number.isInteger(total)) {
        show("Tổng tiền không hợp lệ", false);
        return;
    }

    fetch("UpdateOrderServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body:
            "orderId=" + encodeURIComponent(orderId) +
            "&status=" + encodeURIComponent(status) +
            "&totalAmount=" + encodeURIComponent(total)
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                show("Cập nhật đơn hàng thành công");
                closeAllPopups();
                setTimeout(() => location.reload(), 1500);
            } else {
                show(data.message || "Cập nhật thất bại", false);
            }
        })
        .catch(() => show("Lỗi hệ thống", false));
}


function openCreateOrderPopup() {
    createOrderProducts = [];
    renderCreateProducts();
    openPopup("createOrderPopup");
}

function renderCreateProducts() {

    const list = document.getElementById("createProductsList");

    if (createOrderProducts.length === 0) {
        list.innerHTML = "<div>Chưa có sản phẩm</div>";
        return;
    }

    list.innerHTML = createOrderProducts.map((p, i) => `
        <div class="edit-product-item">
            <div class="product-name">${p.name}</div>
        
            <div class="product-qty">
                <input type="number"
                       min="1"
                       max="${p.stock}"
                       value="${p.quantity}"
                       onchange="changeQuantity(${i}, this.value)">
            </div>
        
            <div class="product-price">
                ${(p.price * p.quantity).toLocaleString()} đ
            </div>
        
            <div class="product-action">
                <i class="fa-solid fa-trash"
                   onclick="removeCreateProduct(${i})"></i>
            </div>
        </div>
    `).join("");
}

function removeCreateProduct(i) {
    createOrderProducts.splice(i, 1);
    renderCreateProducts();
    calculateSubTotal();
}

function submitCreateOrder() {
    const receiverName = document.getElementById("receiverName");
    const receiverPhone = document.getElementById("receiverPhone");
    const province = document.getElementById("provinceSelect");
    const district = document.getElementById("districtSelect");
    const ward = document.getElementById("wardSelect");
    const address = document.getElementById("specificAddress");

    [receiverName, receiverPhone, province, district, ward, address]
        .forEach(el => el.classList.remove("input-error"));

    if (!receiverName.value.trim()) {
        receiverName.classList.add("input-error");
        receiverName.focus();
        show("Vui lòng nhập tên người nhận", false);
        return;
    }

    const phoneRegex = /^(03|05|07|08|09)[0-9]{8}$/;
    if (!phoneRegex.test(receiverPhone.value.trim())) {
        receiverPhone.classList.add("input-error");
        receiverPhone.focus();
        show("Số điện thoại không hợp lệ", false);
        return;
    }

    if (!province.value) {
        province.classList.add("input-error");
        show("Vui lòng chọn tỉnh / thành", false);
        return;
    }
    if (!district.value) {
        district.classList.add("input-error");
        show("Vui lòng chọn quận / huyện", false);
        return;
    }
    if (!ward.value) {
        ward.classList.add("input-error");
        show("Vui lòng chọn phường / xã", false);
        return;
    }
    if (!address.value.trim()) {
        address.classList.add("input-error");
        address.focus();
        show("Vui lòng nhập địa chỉ cụ thể", false);
        return;
    }
    if (createOrderProducts.length === 0) {
        show("Vui lòng chọn ít nhất 1 sản phẩm", false);
        return;
    }

    const subTotalText = document.getElementById("subTotal").innerText;
    const subTotal = parseInt(subTotalText.replace(/\D/g, "")) || 0;
    if (subTotal <= 0) {
        show("Tổng tiền không hợp lệ", false);
        return;
    }

    const productString = createOrderProducts.map(p => p.id + ":" + p.quantity).join(",");

    const params = new URLSearchParams();
    params.append("userId", userId);
    params.append("receiverName", receiverName.value.trim());
    params.append("receiverPhone", receiverPhone.value.trim());
    params.append("province", province.selectedOptions[0].text);
    params.append("district", district.selectedOptions[0].text);
    params.append("ward", ward.selectedOptions[0].text);
    params.append("specificAddress", address.value.trim());
    params.append("shippingServiceId", document.getElementById("createOrderShipping").value);
    params.append("products", productString);

    fetch("CreateOrderForCustomerServlet", {
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded"},
        body: params.toString()
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                show(data.message);
                closeAllPopups();
                setTimeout(() => location.reload(), 1500);
            } else {
                show(data.message, false);
            }
        })
        .catch(() => show("Lỗi kết nối", false));
}


function triggerAvatarUpload() {
    document.getElementById("avatarInput").click();
}

function uploadAvatar(input) {
    if (input.files.length === 0) return;

    const file = input.files[0];

    if (!file.type.startsWith("image/")) {
        show("Chỉ được chọn file ảnh", false);
        return;
    }

    const formData = new FormData();
    formData.append("avatar", file);
    formData.append("userId", userId);

    fetch("UpdateAvatarServlet", {
        method: "POST",
        body: formData
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                document.getElementById("avatarImg").src = data.avatarUrl;
                show("Cập nhật avatar thành công");
            } else {
                show(data.message, false);
            }
        })
        .catch(err => {
            console.error(err);
            show("Lỗi upload", false);
        });
}


function show(message, isSuccess = true) {
    const toast = document.getElementById("toast");
    toast.innerText = message;
    toast.classList.remove("success", "error");
    if (isSuccess) {
        toast.classList.add("success");
    } else {
        toast.classList.add("error");
    }
    toast.classList.add("show");
    setTimeout(() => {
        toast.classList.remove("show");
    }, 3000);
}

function searchVouchers(keyword) {
    keyword = keyword.toLowerCase().trim();

    const items = document.querySelectorAll(".voucher-gift-item");
    const list = document.getElementById("voucherGiftList");
    const oldEmpty = document.getElementById("noVoucherFound");

    let hasResult = false;

    if (oldEmpty) oldEmpty.remove();

    if (keyword === "") {
        items.forEach(item => item.style.display = "");
        return;
    }

    items.forEach(item => {
        const code = item.dataset.code?.toLowerCase() || "";
        const type = item.dataset.type?.toLowerCase() || "";
        const desc = item.dataset.desc?.toLowerCase() || "";
        if (code.includes(keyword) || type.includes(keyword) || desc.includes(keyword)) {
            hasResult = true;
            item.style.display = "";
        } else {
            item.style.display = "none";
        }
    });

    if (!hasResult) {
        const div = document.createElement("div");
        div.id = "noVoucherFound";
        div.style.padding = "10px";
        div.style.textAlign = "center";
        div.innerHTML = "<i class='fa-solid fa-circle-exclamation'></i> Không tìm thấy voucher";
        list.appendChild(div);
    }

}

function startEditSelect(field, currentVal, options) {
    const row = document.querySelector("#field-" + field + " .info-value-row");

    let html = `<select id="edit-${field}">`;

    options.forEach(opt => {
        const [value, label] = opt.split(":");
        html += `<option value="${value}" ${value == currentVal ? "selected" : ""}>
                    ${label}
                 </option>`;
    });

    html += `</select>
        <button onclick="saveInline('${field}')">Lưu</button>
        <button onclick="location.reload()">Hủy</button>
    `;

    row.innerHTML = html;
}

let provinces = [];
let districts = [];
let wards = [];

window.onload = function () {
    loadProvinces();
};

function loadProvinces() {
    fetch("https://provinces.open-api.vn/api/p/")
        .then(res => res.json())
        .then(data => {
            provinces = data;

            let html = `<option value="">Chọn tỉnh / thành</option>`;
            data.forEach(p => {
                html += `<option value="${p.code}">${p.name}</option>`;
            });

            document.getElementById("provinceSelect").innerHTML = html;
        });
}

function loadDistricts() {
    const provinceCode = document.getElementById("provinceSelect").value;
    if (!provinceCode) return;

    fetch(`https://provinces.open-api.vn/api/p/${provinceCode}?depth=2`)
        .then(res => res.json())
        .then(data => {
            districts = data.districts;

            let html = `<option value="" selected disabled>Chọn quận / huyện</option>`;
            districts.forEach(d => {
                html += `<option value="${d.code}">${d.name}</option>`;
            });

            const districtSelect = document.getElementById("districtSelect");
            districtSelect.innerHTML = html;
            districtSelect.value = "";

            document.getElementById("wardSelect").innerHTML =
                `<option value="" selected disabled>Chọn phường / xã</option>`;
        });
}

function loadWards() {
    const districtCode = document.getElementById("districtSelect").value;
    if (!districtCode) return;

    fetch(`https://provinces.open-api.vn/api/d/${districtCode}?depth=2`)
        .then(res => res.json())
        .then(data => {
            wards = data.wards;

            let html = `<option value="" selected disabled>Chọn phường / xã</option>`;
            wards.forEach(w => {
                html += `<option value="${w.code}">${w.name}</option>`;
            });

            const wardSelect = document.getElementById("wardSelect");
            wardSelect.innerHTML = html;
            wardSelect.value = "";
        });
}

function loadShippingFromForm() {

    const provinceEl = document.getElementById("provinceSelect");
    const districtEl = document.getElementById("districtSelect");
    const wardEl = document.getElementById("wardSelect");

    const province = provinceEl.selectedOptions[0]?.text;
    const district = districtEl.selectedOptions[0]?.text;
    const ward = wardEl.selectedOptions[0]?.text;


    if (!province || !district || !ward) return;

    fetch("calculateShippingFromForm", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body:
            "province=" + encodeURIComponent(province) +
            "&district=" + encodeURIComponent(district) +
            "&ward=" + encodeURIComponent(ward)
    })
        .then(res => res.json())
        .then(data => {
            if (data.services) {
                renderShippingOptions(data.services);
            }
        })
        .catch(err => console.log(err));
}

function renderShippingOptions(services) {

    const select = document.getElementById("createOrderShipping");

    let html = "";

    services.forEach(s => {
        html += `
            <option value="${s.service_id}" data-fee="${s.fee}">
                ${s.service_name} - ${s.fee.toLocaleString()} đ
            </option>
        `;
    });

    select.innerHTML = html;

    updateTotal();
}

function updateTotal() {

    const select = document.getElementById("createOrderShipping");

    const option = select.selectedOptions[0];
    if (!option) return;

    const fee = parseInt(option.dataset.fee || 0);

    document.getElementById("shippingFee").innerText =
        fee.toLocaleString() + " đ";

    const subTotalText = document.getElementById("subTotal").innerText;
    const subTotal = parseInt(subTotalText.replace(/\D/g, "")) || 0;

    document.getElementById("grandTotal").innerText =
        (subTotal + fee).toLocaleString() + " đ";
}

document.getElementById("provinceSelect").addEventListener("change", () => {
    document.getElementById("createOrderShipping").innerHTML = "";
});


function searchProducts(keyword) {
    keyword = keyword.toLowerCase().trim();


    const l = document.getElementById("createProductResults");

    const items = document.querySelectorAll(".product-search-item");

    let hasResult = false;

    items.forEach(item => {
        const name = item.dataset.name?.toLowerCase() || "";

        if (name.includes(keyword)) {
            item.style.display = "";
            hasResult = true;
        } else {
            item.style.display = "none";
        }
    });

    const oldMsg = document.getElementById("noProductMessage");
    if (oldMsg) oldMsg.remove();

    if (!hasResult) {
        const msg = document.createElement("div");
        msg.id = "noProductMessage";
        msg.textContent = "Chưa có sản phẩm";

        l.appendChild(msg);
    }
}


function addProductToOrder(id, name, price, stock) {
    const existing = createOrderProducts.find(p => p.id === id);

    if (existing) {
        if (existing.quantity < stock) {
            existing.quantity += 1;
        } else {
            show("Vượt quá tồn kho", false);
        }
    } else {
        createOrderProducts.push({
            id,
            name,
            price,
            stock,
            quantity: 1
        });
    }

    renderCreateProducts();
    calculateSubTotal();
}

function changeQuantity(index, value) {

    const p = createOrderProducts[index];

    let qty = parseInt(value);

    if (qty < 1) qty = 1;
    if (qty > p.stock) {
        qty = p.stock;
        show("Vượt quá tồn kho", false);
    }

    p.quantity = qty;

    renderCreateProducts();
    calculateSubTotal();
}

function calculateSubTotal() {

    let total = 0;

    createOrderProducts.forEach(p => {
        total += p.price * p.quantity;
    });

    document.getElementById("subTotal").innerText =
        total.toLocaleString() + " đ";

    updateTotal();
}