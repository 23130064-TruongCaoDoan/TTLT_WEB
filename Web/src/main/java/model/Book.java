package model;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

public class Book {
    private int id;
    private String bookCode;
    private String title;
    private Integer authorId;
    private String author;
    private int price;
    private int priceDiscounted;
    private int priceImport;
    private String type;
    private int age;
    private String coverImgUrl;
    private String description;
    private String publisher;
    private String provider;
    private int publishedDate;
    private double weight;
    private String bookSize;
    private int pagesNumber;
    private String format;
    private boolean isSell;
    private String add_date;
    private int quantitySold;
    private int stock;


    public Book() {
    }

    public Book(int id, String bookCode, String title, Integer authorId, String author, int price, int priceDiscounted, String type, int age, String coverImgUrl, String description, String publisher, String provider, int publishedDate, double weight, String bookSize, int pagesNumber, String format, boolean isSell, String add_date, int quantitySold, int stock) {
        this.id = id;
        this.bookCode = bookCode;
        this.title = title;
        this.authorId = authorId;
        this.author = author;
        this.price = price;
        this.priceDiscounted = priceDiscounted;
        this.type = type;
        this.age = age;
        this.coverImgUrl = coverImgUrl;
        this.description = description;
        this.publisher = publisher;
        this.provider = provider;
        this.publishedDate = publishedDate;
        this.weight = weight;
        this.bookSize = bookSize;
        this.pagesNumber = pagesNumber;
        this.format = format;
        this.isSell = isSell;
        this.add_date = add_date;
        this.quantitySold = quantitySold;
        this.stock = stock;
    }

    public boolean getIsSell() {
        return isSell;
    }
    public void setIsSell(boolean isSell) {
        this.isSell = isSell;
    }
    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBookCode() { return bookCode; }
    public void setBookCode(String bookCode) { this.bookCode = bookCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getPriceDiscounted() { return priceDiscounted; }
    public void setPriceDiscounted(int priceDiscounted) { this.priceDiscounted = priceDiscounted; }
    public int getPriceImport() { return priceImport; }
    public void setPriceImport(int priceImport) { this.priceImport = priceImport; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getCoverImgUrl() { return coverImgUrl; }
    public void setCoverImgUrl(String coverImgUrl) { this.coverImgUrl = coverImgUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public int  getPublishedDate() { return publishedDate; }
    public void setPublishedDate(int publishedDate) {this.publishedDate = publishedDate;}

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getBookSize() { return bookSize; }
    public void setBookSize(String bookSize) { this.bookSize = bookSize; }

    public int getPagesNumber() { return pagesNumber; }
    public void setPagesNumber(int pagesNumber) { this.pagesNumber = pagesNumber; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public boolean getSell() { return isSell; }
    public void setSell(boolean isSell) { this.isSell = isSell; }

    public String getAdd_date() { return add_date; }
    public void setAdd_date(String add_date) { this.add_date = add_date; }

    public int getQuantitySold() { return quantitySold; }
    public void setQuantitySold(int quantitySold) { this.quantitySold = quantitySold; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }


    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", model='" + bookCode + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", priceDiscounted=" + priceDiscounted +
                ", type='" + type + '\'' +
                ", age=" + age +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", description='" + description + '\'' +
                ", publisher='" + publisher + '\'' +
                ", provider='" + provider + '\'' +
                ", publishedDate=" + publishedDate +
                ", weight=" + weight +
                ", bookSize='" + bookSize + '\'' +
                ", pagesNumber=" + pagesNumber +
                ", format='" + format + '\'' +
                ", sell=" + isSell +
                ", add_date=" + add_date +
                ", quantitySold=" + quantitySold +
                ", stock=" + stock +
                '}';
    }
}

