<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MemberOffer</title>
    <link rel="stylesheet" href="assets/css/user.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css/UuDaiThanhVien.css">
</head>
<body>
<div class="page-wrapper">
    <c:import url="/user/headerUser.jsp"></c:import>
    <div class="content">
        <div class="container">
            <div class="menuUser">
                <div class="nameUser">
                    <div class="anh"><i class="fa-solid fa-user"></i></div>
                    <div class="name"> Lê Vân Trường</div>
                    <div class="bacThanhVien">Thành viên bạc</div>
                    <div class="point">10000 point</div>
                </div>
                <div class="menuMain">
                    <a href="user-hoSoCaNhan.jsp" class="menu ttcn"><i class="fa-regular fa-user"></i><span>Thông tin cá nhân</span><i
                            class="fa-solid fa-arrow-down"></i></a>
                    <div class="menuInfor">
                        <a href="user-hoSoCaNhan.jsp" class="title prof"><span>Hồ sơ cá nhân</span></a>
                        <a href="user-address.jsp" class="title address"><span>Sổ địa chỉ</span></a>
                        <a href="user-changePassword.jsp" class="title passw"><span>Đổi mật khẩu</span></a>
                        <a href="user_UuDaiThanhVien.html" class="title member"><span>Ưu đãi thành viên</span></a>
                    </div>
                    <a href="user-myOrders.jsp" class="menu donhang"><i class="fa-solid fa-receipt"></i><span>Đơn hàng của tôi</span></a>
                    <a href="ViVoucher.jsp" class="menu Voucher"><i class="fa-solid fa-ticket"></i></i>
                        <span>Ví voucher</span></a>
                    <a href="user-thongbao.jsp" class="menu thongbao"><i class="fa-regular fa-bell"></i><span>Thông báo</span></a>
                    <a href="user-spYeuThich.jsp" class="menu spYeuThich"><i
                            class="fa-regular fa-heart"></i><span>Sản phẩm yêu thích</span></a>
                </div>
                <div class="btDangXuat">
                    <a href="login.jsp" class="dangXuat">Đăng xuất</a>
                </div>
            </div>
            <div class="box-content">
                <h2>Ưu đãi thành viên</h2>
                <div class="info-container">
                    <div class="info-box">
                        <h3>Ưu đãi của bạn</h3>
                        <div class="info-content">
                            <div class="info-item">
                                <p>Point hiện có</p>
                                <span>0</span>
                            </div>
                            <div class="info-item">
                                <p>Freeship hiện có</p>
                                <span>0 lần</span>
                            </div>
                        </div>
                    </div>

                    <div class="info-box">
                        <h3>Thành tích năm 2025</h3>
                        <div class="info-content">
                            <div class="info-item">
                                <p>Số đơn hàng</p>
                                <span>0 đơn hàng</span>
                            </div>
                            <div class="info-item">
                                <p>Đã thanh toán</p>
                                <span>0 Đ</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="member-rank-table">
                    <h2>Quyền lợi theo bậc thành viên</h2>
                    <table>
                        <thead>
                        <tr>
                            <th>Bậc thành viên</th>
                            <th>Điểm cần đạt</th>
                            <th>Quà sinh nhật</th>
                            <th>Ưu đãi freeship / mã giảm giá</th>
                            <th>Tỉ lệ tích Point</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr class="silver">
                            <td><span class="tsilver">Hạng Bạc</span></td>
                            <td>0 – 29,999 điểm</td>
                            <td>Không</td>
                            <td>Không</td>
                            <td><b>0,5%</b></td>
                        </tr>
                        <tr class="gold">
                            <td><span class="tgold">Hạng Vàng</span></td>
                            <td>30,000 – 99,999 điểm</td>
                            <td>Voucher giảm 35%</td>
                            <td>Không freeship</td>
                            <td><b>1%</b></td>
                        </tr>
                        <tr class="diamond">
                            <td><span class="tdiamond">Hạng Kim Cương</span></td>
                            <td>≥ 100,000 điểm</td>
                            <td>Voucher giảm 35%</td>
                            <td>Freeship toàn quốc</td>
                            <td><b>2%</b></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>
    <footer class="footer">
        <div class="wave-container">
            <svg
                    viewBox="0 0 120 15"
                    xmlns="http://www.w3.org/2000/svg"
                    preserveAspectRatio="none"
            >
                <path
                        d="M0,10
                C10,15 20,5 30,10
                C40,15 50,5 60,10
                C70,15 80,5 90,10
                C100,15 115,5 120,10
                L120,20 0,20 Z"
                ></path>
            </svg>
        </div>
        <div class="footer-container">
            <div class="footer-column">
                <h3>Liên hệ chúng tôi</h3>
                <a href="#"><i class="fa-solid fa-phone"></i> 0981566177</a>
                <a href="#"
                ><i class="fa-brands fa-facebook-messenger"></i> Chat trực tiếp</a
                >
            </div>

            <div class="footer-column">
                <h3>Dịch vụ khách hàng</h3>
                <a href="user-myOrders.jsp">Theo dõi đơn hàng</a>
                <a href="user-hoSoCaNhan.jsp">Tài khoản</a>
                <a href="returnPolicy.jsp">Chính sách đổi trả</a>

            </div>

            <div class="footer-column">
                <h3>Đối tác</h3>
                <a href="NhaPhanPhoi.jsp">Nhà phân phối</a>
                <a href="dsSanPham.jsp">Sách của chúng tôi</a>
            </div>

            <div class="footer-column">
                <h3>Bảo mật</h3>
                <a href="PrivatePolicy.jsp">Chính sách bảo mật</a>
                <a href="DieuKhoanSuDung.jsp">Điều khoản sử dụng</a>
            </div>
        </div>
        <div class="footer-bottom">
            <p>Copyright ©. All Rights Reserved.</p>
        </div>
    </footer>
</div>
</body>
</html>