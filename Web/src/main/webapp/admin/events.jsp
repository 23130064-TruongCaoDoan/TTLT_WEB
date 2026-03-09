<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Event</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css_admin/events.css">
    <link rel="stylesheet" href="assets/css_admin/admin.css">
    <link rel="stylesheet" href="assets/css_admin/notifySuccess.css">
</head>
<body>
<main>
    <c:import url="headerAdmin.jsp"></c:import>
    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp"></c:import>
        <div class="event-container">
            <h2>Quản lý Sự kiện</h2>
            <form method="get" action="Event">
                <div class="function">
                    <button id="addEvent" type="button">Thêm sự kiện</button>
                    <div class="timkiem">
                        <input type="text"
                               class="search"
                               name="q"
                               placeholder="Tìm kiếm sự kiện"
                               value="${param.q}">
                        <button type="submit" class="buttonSearch">Tìm kiếm</button>
                    </div>
                </div>

                <div class="event-list">
                    <div class="title">
                        <h3>Danh sách sự kiện</h3>
                        <select class="locEvent" name="sortDate" onchange="this.form.submit()">
                            <option value="all">Tất cả</option>
                            <option value="asc" ${param.sortDate == 'asc' ? 'selected' : ''}>
                                Sự kiện cũ nhất
                            </option>
                            <option value="desc" ${param.sortDate == 'desc' ? 'selected' : ''}>
                                Sự kiện mới nhất
                            </option>
                        </select>
                    </div>
                </div>
            </form>
            <div class="event-list">
                <div class="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã sự kiện</th>
                            <th>Tiêu đề</th>
                            <th>Giá trị</th>
                            <th>Thời gian</th>
                            <th>Hoạt động</th>
                            <th>Loại sách áp dụng</th>
                            <th>Chỉnh sửa</th>
                        </tr>
                        </thead>
                        <tbody id="eventTable">
                        <c:forEach var="event" items="${listEvent}">
                            <tr>
                                <td>${event.eventCode}</td>
                                <td>${event.title}</td>
                                <td>${event.value}%</td>
                                <td>${event.getStartDateFormatted()} - ${event.getEndDateFormatted()}</td>
                                <td>
                                    <c:if test="${event.isActive()}">
                                        <span class="status active">Đang diễn ra</span>
                                    </c:if>
                                    <c:if test="${!event.isActive()}">
                                        <span class="status inactive">Đã kết thúc</span>
                                    </c:if>
                                </td>
                                <td>${event.getApplyConditionSummary()}</td>
                                <td>
                                    <i class="fa-solid fa-pen sua"
                                       data-id="${event.id}"
                                       data-code="${fn:escapeXml(event.eventCode)}"
                                       data-title="${fn:escapeXml(event.title)}"
                                       data-img="${fn:escapeXml(event.imgUrl)}"
                                       data-value="${event.value}"
                                       data-start="${event.startDate}"
                                       data-end="${event.endDate}"
                                       data-type="${fn:escapeXml(event.type_book_apply)}"
                                       data-publisher="${fn:escapeXml(event.pulisher_apply)}"
                                       data-author="${fn:escapeXml(event.author_apply)}"
                                       data-voucher="${event.voucher_code}"
                                       data-special="${event.special_voucher}"
                                       data-point="${event.min_point}"
                                       data-age="${fn:escapeXml(event.age_apply)}"
                                       onclick="editEvent(this)">
                                    </i>


                                    <i class="fa-solid fa-trash xoa"
                                       onclick="deleteEvent(${event.id})">
                                    </i>
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
        <p>Bạn có chắc chắn muốn xóa sự kiện này không?</p>
        <div class="delete-actions">
            <button class="btn-delete" onclick="confirmDelete()">Xóa</button>
            <button class="btn-cancel" onclick="closeDeletePopup()">Hủy</button>
        </div>
    </div>
    <form id="eventForm" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label>Mã sự kiện</label>
            <input type="text" name="code" id="eventCode" placeholder="Nhập mã sự kiện">
            <span class="error"></span>
        </div>

        <div class="form-group">
            <label>Tiêu đề</label>
            <input type="text" name="title" id="eventTitle" placeholder="Tiêu đề sự kiện">
            <span class="error"></span>
        </div>

        <div class="form-group">
            <label>Giá trị (%)</label>
            <input type="number" name="giatri" id="eventValue" placeholder="30">
            <span class="error"></span>
        </div>

        <div class="form-group-inline">
            <div>
                <label>Ngày bắt đầu</label>
                <input type="date" name="startdate" id="startDate">
                <span class="error"></span>
            </div>
            <div>
                <label>Ngày kết thúc</label>
                <input type="date" name="enddate" id="endDate">
                <span class="error"></span>
            </div>
        </div>

        <div class="form-group anh">
            <label>Ảnh sự kiện</label>
            <input type="file" name="image" id="eventImage" accept="image/*">
            <input type="hidden" name="oldImg" id="oldImg">
            <span class="error"></span>
        </div>

        <div class="form-group book-type-group">
            <label>Loại sách áp dụng</label>

            <div class="book-type-box">
                <label class="book-type-item">
                    <input type="checkbox" id="tomau" name="typeBook" value="Sách tô màu">
                    <span>Sách tô màu</span>
                    <span class="error"></span>
                </label>

                <label class="book-type-item">
                    <input type="checkbox" name="typeBook" id="giaoduc" value="Sách giáo dục">
                    <span>Sách giáo dục</span>
                    <span class="error"></span>
                </label>

                <label class="book-type-item">
                    <input type="checkbox" name="typeBook" id="sachanh" value="Sách ảnh">
                    <span>Sách ảnh</span>
                    <span class="error"></span>
                </label>

                <label class="book-type-item">
                    <input type="checkbox" name="typeBook" id="truyentranh" value="Truyện tranh">
                    <span>Truyện tranh</span>
                    <span class="error"></span>
                </label>
            </div>
        </div>

        <div class="form-group">
            <label>Loại sách theo độ tuổi đc áp dụng</label>
            <input id="ageBook" name="age" placeholder="Nhập các tên loại, cách nhau bằng dấu phẩy...">
            <span class="error"></span>
        </div>
        <div class="form-group">
            <label>Sách của tác giả được áp dụng đc áp dụng</label>
            <input id="authorBook" name="author" placeholder="Nhập các tên loại, cách nhau bằng dấu phẩy...">
            <span class="error"></span>
        </div>
        <div class="form-group">
            <label>Nhà xuất bản áp dụng</label>
            <input id="publisher" name="pulisher" placeholder="Nhập các tên nhà xuất bảng, cách nhau bằng dấu phẩy...">
            <span class="error"></span>
        </div>
        <div class="form-group">
            <label>Mã voucher tặng chung</label>
            <input type="text" id="voucherCode" name="voucher" placeholder="Nhập mã voucher">
            <span class="error"></span>
        </div>

        <div class="form-group-inline">
            <div>
                <label>Các mã voucher dành riêng</label>
                <input type="text" id="specialVoucher" name="v"
                       placeholder="Nhập các mã voucher (cách nhau bằng dấu phẩy)">
                <span class="error"></span>
            </div>
            <div>
                <label>Điều kiện point</label>
                <input type="number" id="minPoint" name="point" placeholder="Điềm từ...">
                <span class="error"></span>
            </div>
        </div>

        <button type="submit" class="btn-save">Lưu sự kiện</button>
    </form>
</main>
<script>
    let mode = "add";

    let editId = null;
    const overlay = document.getElementById("overlay");
    const add = document.getElementById("addEvent")
    const sua = document.querySelector(".sua")
    const popup = document.getElementById("eventForm");
    const form = document.getElementById("eventForm");


    overlay.addEventListener('click', () => {
        overlay.style.display = "none";
        popup.style.display = "none";
        closeDeletePopup();
    });
    add.addEventListener('click', () => {
        mode = "add";
        editId = null;
        code.readOnly = false;
        form.reset();

        overlay.style.display = "block";
        popup.style.display = "block";
    })
    const code = document.getElementById("eventCode");
    const title = document.getElementById("eventTitle");
    const eValue = document.getElementById("eventValue");
    const sd = document.getElementById("startDate");
    const ed = document.getElementById("endDate");
    const img = document.getElementById("eventImage");
    const tomau = document.getElementById("tomau");
    const giaoduc = document.getElementById("giaoduc");
    const sachanh = document.getElementById("sachanh");
    const truyentranh = document.getElementById("truyentranh");
    const voucher = document.getElementById("voucherCode");
    const spVoucher = document.getElementById("specialVoucher");
    const minPoint = document.getElementById("minPoint");
    const age = document.getElementById("ageBook");
    const authorBook = document.getElementById("authorBook");
    const bookCodes = document.getElementById("publisher");


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


    form.addEventListener("submit", function (e) {
        e.preventDefault();
        let hasError = false;

        // Mã sự kiện
        if (code.value.trim() === "") {
            setError(code, "Vui lòng nhập mã sự kiện");
            hasError = true;
        } else clearError(code);

        // Tiêu đề
        if (title.value.trim() === "") {
            setError(title, "Vui lòng nhập tiêu đề sự kiện");
            hasError = true;
        } else clearError(title);

        // Giá trị
        const valueInput = document.getElementById("eventValue");
        if (valueInput.value === "" || valueInput.value <= 0) {
            setError(valueInput, "Giá trị phải lớn hơn 0");
            hasError = true;
        } else clearError(valueInput);

        // Ngày
        if (!sd.value) {
            setError(sd, "Vui lòng chọn ngày bắt đầu");
            hasError = true;
        } else clearError(sd);

        if (!ed.value) {
            setError(ed, "Vui lòng chọn ngày kết thúc");
            hasError = true;
        } else clearError(ed);

        if (sd.value && ed.value && sd.value > ed.value) {
            setError(sd, "Ngày bắt đầu phải nhỏ hơn ngày kết thúc");
            setError(ed, "Ngày kết thúc phải lớn hơn ngày bắt đầu");
            hasError = true;
        }

        // Ảnh: chỉ bắt buộc khi ADD
        if (mode === "add" && img.files.length === 0) {
            setError(img, "Vui lòng chọn ảnh sự kiện");
            hasError = true;
        } else {
            clearError(img);
        }

        // ===== Điều kiện áp dụng (ít nhất 1 trong 4) =====
        const checkedBooks = document.querySelectorAll('input[name="typeBook"]:checked');
        const hasTypeBook = checkedBooks.length > 0;
        const hasPublisher = bookCodes.value.trim() !== "";
        const hasAge = age.value.trim() !== "";
        const hasAuthor = authorBook.value.trim() !== "";

        if (!hasTypeBook && !hasPublisher && !hasAge && !hasAuthor) {

            // show lỗi ở publisher (1 chỗ đại diện)
            setError(bookCodes,
                "Vui lòng chọn ít nhất 1 điều kiện (Loại sách / NXB / Độ tuổi / Tác giả)"
            );

            // OPTIONAL: bôi đỏ thêm nhóm checkbox cho dễ nhìn
            document.querySelectorAll('input[name="typeBook"]').forEach(cb => {
                cb.closest('.book-type-item').style.border = '1px solid red';
            });

            hasError = true;

        } else {
            clearError(bookCodes);

            document.querySelectorAll('.book-type-item').forEach(item => {
                item.style.border = '';
            });
        }


        if (minPoint.value !== "" && minPoint.value < 0) {
            setError(minPoint, "Point phải >= 0");
            hasError = true;
        } else clearError(minPoint);

        if (!hasError) {
            if (mode === "add") {
                addEvent();
            } else {
                updateEvent();
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

    function editEvent(el) {
        mode = "edit";
        editId = el.dataset.id;

        code.value = el.dataset.code;
        code.readOnly = true;

        title.value = el.dataset.title;
        eValue.value = el.dataset.value;
        sd.value = el.dataset.start;
        ed.value = el.dataset.end;

        voucher.value = el.dataset.voucher || "";
        spVoucher.value = el.dataset.special || "";
        minPoint.value = el.dataset.point || "";

        age.value = el.dataset.age || "";
        authorBook.value = el.dataset.author || "";
        bookCodes.value = el.dataset.publisher || "";

        document.getElementById("oldImg").value = el.dataset.img || "";

        img.value = "";

        document.querySelectorAll('input[name="typeBook"]').forEach(cb => cb.checked = false);
        if (el.dataset.type) {
            el.dataset.type.split(",").forEach(t => {
                const cb = document.querySelector(`input[value="${t.trim()}"]`);
                if (cb) cb.checked = true;
            });
        }

        overlay.style.display = "block";
        popup.style.display = "block";
    }


    function updateEvent() {
        const data = new FormData(form);
        data.append("id", editId);

        fetch("updateEvent", {
            method: "POST",
            body: data
        })
            .then(res => res.json())
            .then(data => {
                overlay.style.display = "none";
                popup.style.display = "none";

                if (data.success) {
                    show(data.message);
                    form.reset();
                    setTimeout(() => location.reload(), 1500);
                } else {
                    show(data.message, false);
                }
            })
            .catch(err => console.log(err));
    }


    function addEvent() {
        const data = new FormData(form);

        fetch("addEvent", {
            method: "POST",
            body: data
        })
            .then(res => res.json())
            .then(data => {
                overlay.style.display = "none";
                popup.style.display = "none";

                if (data.success) {
                    show(data.message);
                    form.reset();
                    setTimeout(() => location.reload(), 1500);
                } else {
                    show(data.message, false);
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

    function deleteEvent(id) {
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

        fetch("deleteEvent?id=" + deleteId)
            .then(res => res.json())
            .then(data => {
                closeDeletePopup();
                if (data.success) {
                    show(data.message);
                    setTimeout(() => location.reload(), 1200);
                } else {
                    show(data.message, false);
                }
            });
    }


</script>
</body>
</html>