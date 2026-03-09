
    document.addEventListener('DOMContentLoaded', function () {
    const hoten = document.getElementById('hoten');
    const hotenError = document.getElementById('hotenError');
    const btnSave = document.querySelector('.btn-save');

    function showError(message) {
    hotenError.textContent = message || 'Vui lòng nhập họ và tên.';
    hotenError.style.display = 'block';
    hoten.classList.add('input-error');
}

    function hideError() {
    hotenError.style.display = 'none';
    hoten.classList.remove('input-error');
}

    btnSave.addEventListener('click', function (e) {
    const value = hoten.value.trim();
    if (!value) {
    e.preventDefault();
    showError('Họ và tên không được để trống.');
    hoten.focus();
    return;
}
    if (value.length < 2) {
    e.preventDefault();
    showError('Vui lòng nhập đầy đủ họ và tên (ít nhất 2 ký tự).');
    hoten.focus();
    return;
}
    hideError();
});
    hoten.addEventListener('input', function () {
    if (hoten.value.trim()) hideError();
});

    // optional: validate on blur để warning sớm
    hoten.addEventListener('blur', function () {
    if (!hoten.value.trim()) showError('Họ và tên không được để trống.');
});
});
    // thay đổi số điện thoại và email
    const overlay = document.getElementById("overlay");
    const popup = document.getElementById("thaydoDT");
    const popupE = document.getElementById("thaydoiEmail");
    const cancelBtns=document.querySelectorAll(".cancel");
    const changeBtn = document.getElementById("thayDoiDT");
    const changeBtnE = document.getElementById("thayDoiE");


    changeBtn.addEventListener('click', (e) => {
    e.preventDefault();
    overlay.style.display = "block";
    popup.style.display = "block";
});
    changeBtnE.addEventListener('click', (e) => {
    e.preventDefault();
    overlay.style.display = "block";
    popupE.style.display = "block";
});
    cancelBtns.forEach(btn => {
    btn.addEventListener('click', () => {
        overlay.style.display = "none";
        popup.style.display = "none";
        popupE.style.display = "none";
    });
});

    overlay.addEventListener('click', () => {
    overlay.style.display = "none";
    popup.style.display = "none";
    popupE.style.display = "none";
});
