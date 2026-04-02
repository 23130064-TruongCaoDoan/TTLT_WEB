<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Kho voucher</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/choices.js/public/assets/styles/choices.min.css" />
    <script src="https://cdn.jsdelivr.net/npm/choices.js/public/assets/scripts/choices.min.js"></script>
    <link rel="stylesheet" href="assets/css_admin/khoVoucher.css">
    <link rel="stylesheet" href="assets/css_admin/admin.css">
    <link rel="stylesheet" href="assets/css_admin/notifySuccess.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
</head>
<body>
<main>
    <c:import url="headerAdmin.jsp"></c:import>
    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp"></c:import>
        <div class="voucher-container">
            <h2>Kho Voucher</h2>
            <div class="function">
                <button id="add">Thêm voucher</button>
                <form action="filterVoucher" method="get" class="timkiem">
                    <input type="text"
                           name="keyword"
                           class="search"
                           placeholder="Tìm kiếm voucher"
                           value="${param.keyword}">

                    <select name="time" class="locVoucher">
                        <option value="">Tất cả</option>
                        <option value="1" ${param.time == '1' ? 'selected' : ''}>Mới nhất</option>
                        <option value="2" ${param.time == '2' ? 'selected' : ''}>Cũ nhất</option>
                    </select>

                    <select name="type" class="locVoucher">
                        <option value="">Tất cả</option>
                        <option value="discount" ${param.type == 'discount' ? 'selected' : ''}>Giảm giá</option>
                        <option value="ship" ${param.type == 'ship' ? 'selected' : ''}>Vận chuyển</option>
                    </select>

                    <button type="submit" class="buttonSearch">Tìm kiếm</button>
                </form>
            </div>
            <div class="voucher-list">
                <div class="title">
                    <h3>Danh sách voucher hiện có</h3>
                </div>
                <div class="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã</th>
                            <th>Mô tả</th>
                            <th>Loại</th>
                            <th>Giá trị</th>
                            <th>Thời gian</th>
                            <th>Giới hạn</th>
                            <th>Hoạt động</th>
                            <th>Chỉnh sửa</th>
                        </tr>
                        </thead>
                        <tbody id="voucherTable">
                        <c:forEach items="${listVoucher}" var="voucher">
                            <tr>
                                <td>${voucher.code}</td>
                                <td>${voucher.description}</td>
                                <td>${voucher.type}</td>
                                <td>${voucher.valuee}</td>
                                <td>${voucher.getStartDateFormatted()} - ${voucher.getEndDateFormatted()}</td>
                                <td>${voucher.usage_limit}</td>
                                <td>
                                    <c:if test="${voucher.isActive()}">
                                        <span class="status active">Còn hạn</span>
                                    </c:if>
                                    <c:if test="${!voucher.isActive()}">
                                        <span class="status inactive">Hết hạn</span>
                                    </c:if>
                                </td>

                                <td>
                                    <i class="fa-solid fa-pen sua"
                                       onclick="editVoucher(
                                           ${voucher.id},
                                               '${voucher.code}',
                                               '${voucher.description}',
                                               '${voucher.type}',
                                           ${voucher.valuee},
                                               '${voucher.start_date}',
                                               '${voucher.end_date}',
                                           ${voucher.usage_limit},
                                           ${voucher.valuee},
                                           ${voucher.conditionPrice},
                                               '${voucher.conditionBook}',
                                               '${voucher.conditionPublisher}'
                                               )"></i>
                                    <i class="fa-solid fa-trash xoa" onclick="deleteVoucher(${voucher.id})"></i>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div id="overlay"></div>
    <div id="deletePopup" class="delete-popup">
        <p>Bạn có chắc chắn muốn xóa voucher này không?</p>
        <div class="delete-actions">
            <button class="btn-delete" onclick="confirmDelete()">Xóa</button>
            <button class="btn-cancel" onclick="closeDeletePopup()">Hủy</button>
        </div>
    </div>

    <form id="voucherForm" method="post">
        <div class="form-group">
            <label>Mã voucher</label>
            <input type="text" name="code" id="code" placeholder="Nhập mã voucher">
            <small class="error"></small>
        </div>

        <div class="form-group">
            <label>Mô tả</label>
            <input type="text" name="description" id="description" placeholder="Giảm 50% cho đơn trên 200K">
        </div>
        <div class="form-group" id="condition-group">
            <label>Điều Kiện</label>
            <div class="condition-group">
                <input type="number" name="gia" id="gia" placeholder="Đơn hàng trên">

                <select name ="loaisach" id="loaisach" multiple>
                    <option value="" selected>Tất cả loại sách</option>
                    <c:forEach items="${listTypes}" var="type">
                        <option value="${type}">${type}</option>
                    </c:forEach>
                </select>

                <select name="nxb" id="nxb" multiple>
                    <option value="" selected>Tất cả Nhà Xuất Bản</option>
                    <c:forEach items="${listPublishers}" var="pub">
                        <option value="${pub}">${pub}</option>
                    </c:forEach>
                </select>
            </div>
            <small class="error"></small>
        </div>

        <div class="form-group">
            <label>Loại</label>
            <select id="type" name="type">
                <option value="">Chọn loại voucher</option>
                <option value="discount">Giảm giá</option>
                <option value="ship">Vận chuyển</option>
            </select>
            <small class="error"></small>
        </div>

        <div class="form-group">
            <label>Giá trị</label>
            <input type="number" name="value" id="value" placeholder="50">
            <small class="error"></small>
        </div>

        <div class="form-group-inline">
            <div>
                <label>Ngày bắt đầu</label>
                <input type="date" name="start_date" id="start_date">
                <small class="error"></small>
            </div>
            <div>
                <label>Ngày kết thúc</label>
                <input type="date" name="end_date" id="end_date">
                <small class="error"></small>
            </div>
        </div>

        <div class="form-group">
            <label>Giới hạn sử dụng</label>
            <input type="number" name="usage_limit" id="usage_limit" placeholder="10">
            <small class="error"></small>
        </div>

        <button type="submit" id="btn-save">Lưu voucher</button>
    </form>
</main>
<script>
    let mode = "add";
    let editId = null;
    const overlay = document.getElementById("overlay");
    const add = document.getElementById("add")
    const sua = document.querySelector(".sua")
    const popup = document.getElementById("voucherForm");


    overlay.addEventListener('click', () => {
        overlay.style.display = "none";
        popup.style.display = "none";
        closeDeletePopup();
    });
    add.addEventListener('click', () => {
        mode = "add";
        editId = null;
        form.reset();

        overlay.style.display = "block";
        popup.style.display = "block";
    })
    const code = document.getElementById("code");
    const description = document.getElementById("description");
    const price = document.getElementById("gia");
    const loaisach = document.getElementById("loaisach");
    const nxb = document.getElementById("nxb");
    const type = document.getElementById("type");
    const value = document.getElementById("value");
    const start_date = document.getElementById("start_date");
    const end_date = document.getElementById("end_date");
    const usage_limit = document.getElementById("usage_limit");
    const form = document.getElementById("voucherForm");

    function setError(inputElement, message) {
        const parent = inputElement.parentElement;
        const error = parent.querySelector('.error');
        if (error) {
            error.textContent = message;
            error.style.color = 'red';
        }
        inputElement.style.border = '1px solid red';
    }

    function clearError(inputElement) {
        const parent = inputElement.parentElement;
        const error = parent.querySelector('.error');
        if (error) {
            error.textContent = '';
        }
        inputElement.style.border = '1px solid #0d3164';
    }


    form.addEventListener('submit', function (e) {
        e.preventDefault();
        let hasError = false;
        if (code.value.trim() === "") {
            setError(code, "Vui lòng nhập mã voucher");
            hasError = true;
        } else clearError(code);
        if (description.value.trim() === "") {
            setError(description, "Vui lòng nhập mô tả");
            hasError = true;
        } else clearError(description);

        const selectedLoaiSach = getMultiSelectValues(loaisach);
        const selectedNXB = getMultiSelectValues(nxb);

        if (
            price.value.trim() === "" &&
            selectedLoaiSach === "" &&
            selectedNXB === ""
        ) {
            setGroupError("condition-group", "Vui lòng nhập ít nhất 1 điều kiện");
            hasError = true;
            hasError = true;
        } else {
            clearGroupError("condition-group");
        }
        if (type.value === "") {
            setError(type, "Vui lòng chọn loại voucher");
            hasError = true;
        } else clearError(type);
        if (value.value.trim() === "") {
            setError(value, "Vui lòng trị giá voucher");
            hasError = true;
        } else clearError(value);
        if (!start_date.value) {
            setError(start_date, "Vui lòng nhập ngày bắt đầu");
            hasError = true;
        } else {
            clearError(start_date);
        }

        if (!end_date.value) {
            setError(end_date, "Vui lòng nhập ngày kết thúc");
            hasError = true;
        } else {
            clearError(end_date);
        }
        if (start_date.value && end_date.value) {
            if (start_date.value > end_date.value) {
                setError(start_date, "Ngày bắt đầu phải nhỏ hơn ngày kết thúc");
                setError(end_date, "Ngày kết thúc phải lớn hơn ngày bắt đầu");
                hasError = true;
            }
        }
        if (usage_limit.value.trim() === "") {
            setError(usage_limit, "Vui lòng nhập giới hạn số lượng sử dụng");
            hasError = true;
        } else clearError(usage_limit);

        if (!hasError) {
            if (mode === "add") {
                addVoucher();
            } else {
                updateVoucher();
            }
        }
    });


    function setGroupError(groupId, message) {
        const group = document.getElementById(groupId);
        if (!group) return;

        const error = group.querySelector('.error');
        if (error) {
            error.textContent = message;
            error.style.color = 'red';
        }
    }

    function clearGroupError(groupId) {
        const group = document.getElementById(groupId);
        if (!group) return;

        const error = group.querySelector('.error');
        if (error) {
            error.textContent = '';
        }
    }

    function editVoucher(id, codeV, desc, typeV, valueV, start, end, limit, valuee, conditionPrice, conditionBook, conditionPulisher) {
        mode = "edit";
        editId = id;

        code.value = codeV;
        description.value = desc;

        type.value = typeV;
        value.value = valueV;
        start_date.value = start;
        end_date.value = end;
        usage_limit.value = limit;
        value.value = valuee;
        price.value = conditionPrice;
        setChoicesValues(loaiSachChoices, conditionBook);
        setChoicesValues(nxbChoices, conditionPulisher);


        overlay.style.display = "block";
        popup.style.display = "block";
    }

    function updateVoucher() {
        fetch("updateVoucher", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body:
                "id=" + editId +
                "&code=" + encodeURIComponent(code.value) +
                "&description=" + encodeURIComponent(description.value) +
                "&gia=" + encodeURIComponent(price.value) +
                "&loaisach=" + encodeURIComponent(getMultiSelectValues(loaisach)) +
                "&nxb=" + encodeURIComponent(getMultiSelectValues(nxb)) +
                "&type=" + encodeURIComponent(type.value) +
                "&start_date=" + encodeURIComponent(start_date.value) +
                "&end_date=" + encodeURIComponent(end_date.value) +
                "&usage_limit=" + encodeURIComponent(usage_limit.value) +
                "&value=" + encodeURIComponent(value.value)
        })
            .then(res => res.json())
            .then(data => {
                overlay.style.display = "none";
                popup.style.display = "none";
                if (data.success) {
                    show(data.message);
                    form.reset();
                    setTimeout(() => {
                        location.reload();
                    }, 1800);
                } else {
                    show(data.message, false);
                }
            });
    }


    function addVoucher() {
        fetch("addVoucher", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body:
                "code=" + encodeURIComponent(code.value) +
                "&description=" + encodeURIComponent(description.value) +
                "&gia=" + encodeURIComponent(price.value) +
                "&loaisach=" + encodeURIComponent(getMultiSelectValues(loaisach)) +
                "&nxb=" + encodeURIComponent(getMultiSelectValues(nxb)) +
                "&type=" + encodeURIComponent(type.value) +
                "&start_date=" + encodeURIComponent(start_date.value) +
                "&end_date=" + encodeURIComponent(end_date.value) +
                "&usage_limit=" + encodeURIComponent(usage_limit.value) +
                "&value=" + encodeURIComponent(value.value)
        })
            .then(res => res.json())
            .then(data => {
                overlay.style.display = "none";
                popup.style.display = "none";
                if (data.success) {
                    show(data.message);
                    form.reset();
                    setTimeout(() => {
                        location.reload();
                    }, 1800);
                } else {
                    show(data.message);
                }
            })
            .catch(err => console.log(err));
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
        }, 2000);
    }


    let deleteId = null;

    function deleteVoucher(id) {
        deleteId = id;
        overlay.style.display = "block";
        document.getElementById("deletePopup").style.display = "block";
    }

    function closeDeletePopup() {
        deleteId = null;
        overlay.style.display = "none";
        document.getElementById("deletePopup").style.display = "none";
    }

    function confirmDelete() {
        if (!deleteId) return;

        fetch("deleteVoucher", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "id=" + encodeURIComponent(deleteId)
        })
            .then(res => res.json())
            .then(data => {
                closeDeletePopup();
                if (data.success) {
                    show(data.message);
                    setTimeout(() => location.reload(), 1200);
                } else {
                    alert(data.message, false);
                }
            })
            .catch(err => console.log(err));
    }

    let loaiSachChoices;
        let nxbChoices;

        document.addEventListener('DOMContentLoaded', function() {
            loaiSachChoices = new Choices('#loaisach', {
                removeItemButton: true,
                searchEnabled: true,
                placeholderValue: 'Chọn loại sách'
            });

            nxbChoices = new Choices('#nxb', {
                removeItemButton: true,
                searchEnabled: true,
                placeholderValue: 'Chọn nhà xuất bản'
            });

            function setupExclusiveAll(choicesInstance) {
                const el = choicesInstance.passedElement.element;
                let isUpdating = false;

                el.addEventListener('addItem', function(event) {
                    if (isUpdating) return;
                    const addedValue = event.detail.value;

                    if (addedValue === "") {
                        isUpdating = true;
                        choicesInstance.removeActiveItems();
                        choicesInstance.setChoiceByValue("");
                        isUpdating = false;
                    } else {
                        const selectedValues = choicesInstance.getValue(true);
                        if (selectedValues.includes("")) {
                            isUpdating = true;
                            choicesInstance.removeActiveItemsByValue("");
                            isUpdating = false;
                        }
                    }
                });

                el.addEventListener('removeItem', function() {
                    if (isUpdating) return;
                    const selectedValues = choicesInstance.getValue(true);
                    if (!selectedValues || selectedValues.length === 0) {
                        isUpdating = true;
                        choicesInstance.setChoiceByValue("");
                        isUpdating = false;
                    }
                });
            }

            setupExclusiveAll(loaiSachChoices);
            setupExclusiveAll(nxbChoices);
        });

        function getMultiSelectValues(selectElement) {
            const selectedOptions = Array.from(selectElement.selectedOptions).map(opt => opt.value);
            if (selectedOptions.length === 0 || selectedOptions.includes("")) return "";
            return selectedOptions.join(",");
        }

        function setChoicesValues(choiceInstance, valuesString) {
            choiceInstance.removeActiveItems();
            if (valuesString && valuesString !== "null" && valuesString.trim() !== "") {
                const values = valuesString.split(",").map(v => v.trim());
                choiceInstance.setChoiceByValue(values);
            } else {
                choiceInstance.setChoiceByValue("");
            }
        }

</script>
</body>
</html>