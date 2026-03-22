package Service;

import Cart.Cart;
import Cart.CartItem;
import dao.AuthorDao;
import dao.BookDao;
import jakarta.servlet.http.Part;
import model.Book;
import Service.UploadService;

import java.util.*;
import java.util.stream.Collectors;

public class BookService {
    private BookDao hd = new BookDao();
    private AuthorDao ad = new AuthorDao();
    private UploadService uploadService = new UploadService();

    public List<Book> getBooksDiscounted() {
        return hd.getBooksDiscounted();
    }

    public List<Book> getAllBooksDiscounted(int limit, int offset) {
        return hd.getAllBooksDiscounted(limit, offset);
    }

    public Book getBooksById(int id) {
        return hd.getBookById(id);
    }

    public List<Book> getBookRecommendInDetail(String type) {
        return hd.getBookRecommendInDetail(type);
    }

    public List<Book> getBooksNew() {
        return hd.getBooksNew();
    }

    public List<Book> getAllBooksNew(int limit, int offset) {
        return hd.getAllBooksNew(limit, offset);
    }

    public List<Book> getAllBooks(int limit, int offset) {
        return hd.getAllBooks(limit, offset);
    }

    public List<Book> getAllBooks() {
        return hd.getAllBooks();
    }

    public int countBooks() {
        return hd.countBooks();
    }

    public void addBook(Map<String, String[]> params, Part mainImage,
                        List<Part> detailImages) throws Exception {

        // ===== STRING =====
        String code = params.get("code")[0];
        String title = params.get("title")[0];
        String type = params.get("type")[0];
        String publisher = params.get("publisher")[0];
        String bookSize = params.get("size")[0];
        String format = params.get("format")[0];
        String description = params.get("description")[0];
        String provider = params.get("provider")[0];

        // ===== INT / DOUBLE (CÓ DEFAULT) =====
        int authorId = Integer.parseInt(params.get("author_id")[0]);
        int stock = Integer.parseInt(params.get("stock")[0]);
        int pagesNumber = Integer.parseInt(params.get("pageNumber")[0]);
        int age = Integer.parseInt(params.get("age")[0]);

        int price = Integer.parseInt(params.get("price")[0]);

        String priceDiscountStr = params.get("price_discounted")[0];
        int priceDiscounted = (priceDiscountStr == null || priceDiscountStr.isBlank())
                ? price
                : Integer.parseInt(priceDiscountStr);

        String weightStr = params.get("weight")[0];
        double weight = (weightStr == null || weightStr.isBlank())
                ? 0
                : Double.parseDouble(weightStr);

        // ===== DATE =====
        String startDate = params.get("startDate")[0];
        int publishedYear = Integer.parseInt(startDate.substring(0, 4));

        // ===== ẢNH BÌA =====
        String coverImgUrl =
                uploadService.upload(mainImage, "books/main");
        // ===== ẢNH CHI TIÊT =====
        List<String> detailImgUrls =
                uploadService.uploadMultiple(detailImages, "books/detail");

        // ===== SET BOOK =====
        Book book = new Book();
        book.setBookCode(code);
        book.setTitle(title);
        book.setAuthorId(authorId);
        book.setStock(stock);
        book.setType(type);
        book.setPublisher(publisher);
        book.setProvider(provider);
        book.setWeight(weight);
        book.setBookSize(bookSize);
        book.setPagesNumber(pagesNumber);
        book.setFormat(format);
        book.setDescription(description);
        book.setPublishedDate(publishedYear);
        book.setCoverImgUrl(coverImgUrl);
        book.setPrice(price);
        book.setPriceDiscounted(priceDiscounted);
        book.setQuantitySold(0);
        book.setAge(age);
        book.setIsSell(true);

        hd.insert(book, detailImgUrls);
    }


    public List<Book> findListBook(String search, int limit, int offset) {
        return hd.findListBook(search, limit, offset);
    }

    public List<Book> getBookByEvent(int limit, int offset, int id) {
        return hd.getAllBookByEvent(limit, offset, id);
    }

    public int countBooksBySearch(String search) {
        return hd.countBooksBySearch(search);
    }

    public int countBooksDiscounted() {
        return hd.countBooksDiscounted();
    }

    public int countBooksNew() {
        return hd.countBooksNew();
    }

    public int countBooksByEvent(int id) {
        return hd.countBooksByEvent(id);
    }

    public List<String> getAllBookTypes() {
        return hd.getAllBookTypes();

    }

    public List<Book> searchAndFilter(String q, String type, String stock) {
        if (q != null && q.isBlank()) q = null;
        if (type != null && type.isBlank()) type = null;
        if (stock != null && stock.isBlank()) stock = null;

        return hd.searchAndFilter(q, type, stock);
    }

    public void updateBook(int id,
                           Map<String, String[]> params,
                           Part mainImage,
                           List<Part> detailImages) throws Exception {

        Book old = hd.getBookById(id);
        if (old == null) return;

        Book incoming = buildBookFromParams(new Book(), params);
        incoming.setId(id);

        if (!old.getBookCode().equals(incoming.getBookCode()))
            old.setBookCode(incoming.getBookCode());

        if (!old.getTitle().equals(incoming.getTitle()))
            old.setTitle(incoming.getTitle());

        if (old.getAuthorId() != incoming.getAuthorId())
            old.setAuthorId(incoming.getAuthorId());

        if (old.getPrice() != incoming.getPrice())
            old.setPrice(incoming.getPrice());

        if (old.getPriceDiscounted() != incoming.getPriceDiscounted())
            old.setPriceDiscounted(incoming.getPriceDiscounted());

        if (!old.getType().equals(incoming.getType()))
            old.setType(incoming.getType());

        if (old.getAge() != incoming.getAge())
            old.setAge(incoming.getAge());

        if (old.getStock() != incoming.getStock())
            old.setStock(incoming.getStock());

        if (!old.getPublisher().equals(incoming.getPublisher()))
            old.setPublisher(incoming.getPublisher());

        if (!old.getProvider().equals(incoming.getProvider()))
            old.setProvider(incoming.getProvider());

        if (!old.getFormat().equals(incoming.getFormat()))
            old.setFormat(incoming.getFormat());

        if (!old.getDescription().equals(incoming.getDescription()))
            old.setDescription(incoming.getDescription());

        if (old.getPagesNumber() != incoming.getPagesNumber())
            old.setPagesNumber(incoming.getPagesNumber());

        if (Double.compare(old.getWeight(), incoming.getWeight()) != 0)
            old.setWeight(incoming.getWeight());

        if (!old.getBookSize().equals(incoming.getBookSize()))
            old.setBookSize(incoming.getBookSize());

        if (incoming.getPublishedDate() != 0 &&
                old.getPublishedDate() != incoming.getPublishedDate()) {
            old.setPublishedDate(incoming.getPublishedDate());
        }


        if (mainImage != null && mainImage.getSize() > 0) {
            String newCover = uploadService.upload(mainImage, "books/main");
            old.setCoverImgUrl(newCover);
        }

        hd.update(old);

        if (detailImages != null && !detailImages.isEmpty()) {
            hd.deleteDetailImages(id);
            List<String> urls = uploadService.uploadMultiple(detailImages, "books/detail");
            hd.insertDetailImages(id, urls);
        }
    }

    private Book buildBookFromParams(Book book, Map<String, String[]> p) {
        book.setBookCode(p.get("code")[0]);
        book.setTitle(p.get("title")[0]);
        book.setAuthorId(Integer.parseInt(p.get("author_id")[0]));
        book.setPrice(Integer.parseInt(p.get("price")[0]));

        String pd = p.get("price_discounted")[0];
        book.setPriceDiscounted(
                (pd == null || pd.isBlank())
                        ? book.getPrice()
                        : Integer.parseInt(pd)
        );

        book.setType(p.get("type")[0]);
        book.setAge(Integer.parseInt(p.get("age")[0]));
        book.setStock(Integer.parseInt(p.get("stock")[0]));
        book.setPublisher(p.get("publisher")[0]);
        book.setProvider(p.get("provider")[0]);
        book.setFormat(p.get("format")[0]);
        book.setDescription(p.get("description")[0]);
        book.setPagesNumber(Integer.parseInt(p.get("pageNumber")[0]));
        book.setWeight(
                p.get("weight")[0].isBlank() ? 0 :
                        Double.parseDouble(p.get("weight")[0])
        );
        book.setBookSize(p.get("size")[0]);

        String startDate = p.get("startDate")[0];
        if (startDate != null && !startDate.isBlank()) {
            book.setPublishedDate(Integer.parseInt(startDate.substring(0, 4)));
        }

        return book;
    }

    public void insertFavoriteBook(int BookId, int userId) {
        hd.insertFavouriteBook(BookId, userId);
    }

    public List<Book> getFavouriteBook(int userId) {
        return hd.getFavouriteBook(userId);
    }
    public List<Book> getAllFavouriteBook(int limit, int offset) {
        return hd.getAllFavouriteBook(limit, offset);
    }
    public List<Book> getFavouriteBook() {
        return hd.getFavouriteBook();
    }
    public int countBookFavourite() {
        return hd.countBookFavourite();
    }


    public void deleteFavouriteBook(int bookId, int userId) {
        hd.deleteFavouriteBook(bookId, userId);
    }

    public boolean isFavouriteBook(int bookId, int userId) {
        return hd.isFavouriteBook(bookId, userId);
    }

    private Set<String> splitToSet(String value) {
        if (value == null || value.trim().isEmpty()) return Set.of();
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private Set<Integer> splitToIntSet(String value) {
        if (value == null || value.trim().isEmpty()) return Set.of();
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
    public boolean isBookAvailable(int bookId) {
        return hd.isBookAvailable(bookId);
    }




    public List<Book> filterBooksForEvent(
            List<Book> allBooks,
            String typeBookApply,
            String authorApply,
            String publisherApply,
            String ageApply
    ) {
        Set<String> typeSet = splitToSet(typeBookApply);
        Set<Integer> authorSet = splitToIntSet(authorApply);
        Set<String> publisherSet = splitToSet(publisherApply);
        Set<Integer> ageSet = splitToIntSet(ageApply);

        return allBooks.stream().filter(book -> {

            boolean match = false;


            if (!typeSet.isEmpty() && typeSet.contains(book.getType())) {
                match = true;
            }

            if (!authorSet.isEmpty() && authorSet.contains(book.getAuthorId())) {
                match = true;
            }

            if (!publisherSet.isEmpty() && publisherSet.contains(book.getPublisher())) {
                match = true;
            }

            if (!ageSet.isEmpty() && ageSet.contains(book.getAge())) {
                match = true;
            }

            return match;
        }).toList();
    }
    public List<String> getImgDetails(int bookId) {
        return hd.getImgDetails(bookId);
    }



    public void updateDiscountBook(List<Book> listBookEvent, double value) {
        hd.updateDiscountBook(listBookEvent,value);

    }

    public void updateStock(Cart cart) {
        for (CartItem item : cart.getItems()) {
            hd.updateStock(item.getBook(),item.getQuantity());
            if (item.getBook().getStock()==item.getQuantity()){
                hd.setUpdatSeld(item.getBook().getId());
            }
        }
    }

    public void updateQuantity(Cart cart) {
        for (CartItem item : cart.getItems()) {
            hd.updateQuantity(item.getBook(),item.getQuantity());
        }
    }

    public int getStockByBookId(int bookId) {
        return hd.getStockByBookId(bookId);
    }

    public int countBooksByCategoryAndAge(String category, int ageFrom, int ageTo) {
        return hd.countByCategoryAndAge(
                category, ageFrom, ageTo
        );
    }

    public List<Book> getBooksByCategoryAndAge(String category, int ageFrom, int ageTo, int limit, int offset) {
        return hd.findByCategoryAndAge(
                category, ageFrom, ageTo, limit, offset
        );
    }

    public List<String> getAllCategories() {
        return hd.getAllCategories();
    }

    public List<String> getAllAuthors() {
        return ad.getAllAuthors();
    }

    public List<String> getAllPublishers() {
        return hd.getAllPublishers();
    }

    public List<Integer> getAllYears() {
        return hd.getAllYears();
    }

    public List<String> getSuggest(String keyword) {
        if (keyword == null || keyword.trim().isEmpty())
            return new ArrayList<>();

        return hd.getSuggest(keyword.trim());
    }
}
