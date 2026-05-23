package model;

public class ImportOrderDetails {
    private int id;
    private int importOrderId;
    private int bookId;
    private String title;
    private String coverImgUrl;
    private int quantity;
    private int priceImport;
    private int subtotal;

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getPriceImport() {
        return priceImport;
    }

    public void setPriceImport(int priceImport) {
        this.priceImport = priceImport;
    }

    public int getImportOrderId() {
        return importOrderId;
    }

    public void setImportOrderId(int importOrderId) {
        this.importOrderId = importOrderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
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

    @Override
    public String toString() {
        return "ImportOrderDetails{" +
                "id=" + id +
                ", importOrderId=" + importOrderId +
                ", bookId=" + bookId +
                ", title='" + title + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", quantity=" + quantity +
                ", priceImport=" + priceImport +
                ", subtotal=" + subtotal +
                '}';
    }
}
