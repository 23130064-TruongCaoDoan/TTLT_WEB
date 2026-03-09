<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>MoMo - Mô phỏng thanh toán</title>
    <link rel="stylesheet" href="style.css" />
    <link rel="stylesheet" href="assets/css/giaLapMoMo.css">
</head>
<body>
<div class="wrap">
    <div class="topbar">
        <div class="brand">MoMo</div>
        <div class="brand-title">Cổng thanh toán MoMo</div>
    </div>
    <div class="content">
        <!-- Thông tin đơn hàng -->
        <div class="left">
            <div class="info-title">Thông tin đơn hàng</div>

            <div class="info-row">
                <div class="label">Nhà cung cấp</div>
                <div class="supplier">
                    <span class="val">SachThieuNhiChoBe</span>
                </div>
            </div>

            <div class="info-row">
                <div class="label">Mã đơn hàng</div>
                <div class="val" id="orderShort">25110906365010379...</div>
            </div>

            <div class="info-row">
                <div class="label">Mô tả</div>
                <div class="val">Thanh toán cho đơn hàng #25110906365010379</div>
            </div>

            <div class="info-row">
                <div class="label">Số tiền</div>
                <div class="amount" id="amountView">164.300đ</div>
            </div>

            <div class="expire-box">
                <div>Đơn hàng sẽ hết hạn sau:</div>
                <div class="expire-time">
                    <div class="expire-pill"><span id="mm">09</span><div>Phút</div></div>
                    <div class="expire-pill"><span id="ss">21</span><div>Giây</div></div>
                </div>
            </div>

            <a href="#" class="back-link" onclick="window.location.href='home.jsp'">Quay về</a>
        </div>

        <!-- QR MoMo -->
        <div class="right">
            <div class="title">Quét mã QR để thanh toán</div>
            <div class="qr-wrap">
                <div class="qr">
                    <img id="qrImg" src="assets/img/QR/qrMoMo.jpg" alt="QR giả lập MoMo">
                </div>
            </div>
            <div class="hint">
                Sử dụng <strong>App MoMo</strong> hoặc ứng dụng camera hỗ trợ QR code để quét mã<br>
                <small>Gặp khó khăn khi thanh toán? <span class="link">Xem hướng dẫn</span></small>
            </div>
        </div>
    </div>
</div>

<script src="script.js"></script>
</body>
</html>
