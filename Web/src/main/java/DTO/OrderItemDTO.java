package DTO;

public class OrderItemDTO {

    private int bookId;
    private String title;
    private String coverImgUrl;
    private int priceDiscounted;
    private int quantity;
    private double subtotal;
    private boolean isReviewed;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public int getPriceDiscounted() {
        return priceDiscounted;
    }

    public void setPriceDiscounted(int priceDiscounted) {
        this.priceDiscounted = priceDiscounted;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public boolean isReviewed() {
        return isReviewed;
    }
    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", priceDiscounted=" + priceDiscounted +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}
