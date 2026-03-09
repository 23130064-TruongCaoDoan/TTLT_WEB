document.addEventListener("DOMContentLoaded", () => {
    const carousel = document.querySelector('.event-carousel');
    const slides = carousel.querySelectorAll('.slide');
    const nextBtn = carousel.querySelector('.next');
    const prevBtn = carousel.querySelector('.prev');

    let index = 0;
    const total = slides.length;

    function showSlide(i) {
        carousel.querySelector('.slides').style.transform = `translateX(-${i * 100}%)`;
    }

    nextBtn.addEventListener('click', () => {
        index = (index + 1) % total;
        showSlide(index);
    });

    prevBtn.addEventListener('click', () => {
        index = (index - 1 + total) % total;
        showSlide(index);
        console.log("bbb")
    });

    setInterval(() => {
        index = (index + 1) % total;
        showSlide(index);
    }, 3000);
});

function addToCart(bookId, quantity) {
    fetch("addItemShopping?bookId=" + bookId + "&quantity=" + quantity)
        .then(res => res.json())
        .then(data => {
            document.getElementById("totalItem").innerText = data.total;
            if (data.success) {
                show("Đã thêm vào giỏ hàng");
            } else {
                show("Không thể thêm do quá số lượng")
            }
        })
        .catch(err => console.log(err));
}

function show(message) {
    const toast = document.getElementById("toast");
    toast.innerText = message;
    toast.classList.add("show");
    setTimeout(() => {
        toast.classList.remove("show");
    }, 2000);
}