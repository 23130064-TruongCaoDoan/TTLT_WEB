const overlay = document.getElementById("overlay");
const popupEdit = document.getElementById("orderForm");
const popupListSP = document.getElementById("listSP");


document.querySelectorAll(".sua").forEach(btn => {
    btn.addEventListener("click", () => {
        overlay.style.display = "block";
        popupEdit.style.display = "block";

        document.getElementById("orderCode").value = btn.dataset.id;
        document.getElementById("orderTitle").value = btn.dataset.name;
        document.getElementById("orderValue").value = btn.dataset.total;
        document.getElementById("typeBook").value = btn.dataset.address;
        document.getElementById("ageBook").value = btn.dataset.phone;
        document.querySelector("textarea").value = btn.dataset.note || "";
    });
});

document.getElementById("btnSaveOrder").addEventListener("click", () => {
    const id = document.getElementById("orderCode").value;
    const total = document.getElementById("orderValue").value;
    const status = document.getElementById("orderStatus").value;

    fetch(contextPath+"/UpdateOrder", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
        },
        body: "id=" + id +
            "&total=" + total +
            "&status=" + encodeURIComponent(status)
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                show("Cập nhật thành công");
                setTimeout(() => {
                    location.reload();
                }, 2000);
                location.reload();
            } else {
                show("Cập nhật không thành công", false);
            }
        });

});


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
    }, 2000);
}


document.querySelectorAll(".cacsp").forEach(btn => {
    btn.addEventListener("click", () => {
        overlay.style.display = "block";
        popupListSP.style.display = "block";

        const orderId = btn.dataset.id;

        fetch(contextPath + "/GetOrderItemsServlet?orderId=" + orderId)
            .then(res => res.json())
            .then(items => {
                const tbody = document.getElementById("productTableBody");
                tbody.innerHTML = "";

                items.forEach(item => {
                    const tr = document.createElement("tr");
                    tr.innerHTML = `
                        <td>${item.bookId}</td>
                        <td>${item.bookName}</td>
                        <td>${item.author}</td>
                        <td>${item.price} ₫</td>
                        <td>${item.quantity}</td>
                        <td>${item.category}</td>
                        <td>${item.age}</td>
                        <td>
                            <img src="${item.image}" width="60">
                        </td>
                    `;
                    tbody.appendChild(tr);
                });
            });
    });
});


overlay.addEventListener("click", () => {
    overlay.style.display = "none";
    popupEdit.style.display = "none";
    popupListSP.style.display = "none";
});


let deleteOrderId = null;

function deleteOrder(id) {
    deleteOrderId = id;
    overlay.style.display = "block";
    document.getElementById("deleteOrderPopup").style.display = "block";
}

function closeDeleteOrderPopup() {
    deleteOrderId = null;
    overlay.style.display = "none";
    document.getElementById("deleteOrderPopup").style.display = "none";
}

function confirmDeleteOrder() {
    if (!deleteOrderId) return;

    fetch("DeleteOrderServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "id=" + encodeURIComponent(deleteOrderId)
    })
        .then(res => res.json())
        .then(data => {
            closeDeleteOrderPopup();
            if (data.success) {
                show(data.message);
                setTimeout(() => location.reload(), 1200);
            } else {
                show(data.message, false);
            }
        });
}


overlay.addEventListener("click", () => {
    overlay.style.display = "none";
    popupEdit.style.display = "none";
    popupListSP.style.display = "none";
    closeDeleteOrderPopup();
});

