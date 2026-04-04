package dao;

import model.Book;
import model.CommentView;

import java.sql.PreparedStatement;
import java.util.List;

public class BookDao extends BaseDao {
    public List<String> getAllCategories() {
        return getJdbi().withHandle(handle ->
            handle.createQuery("select distinct type from BOOKS")
                    .mapTo(String.class).list()
        );
    }

    public List<Book> getBooksDiscounted() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM BOOKS WHERE price_discounted > 0 AND is_sell=1 ORDER BY updated_at DESC")
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public List<Book> getBooksNew() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM BOOKS WHERE is_sell=1 ORDER BY add_date DESC")
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public Book getBookById(int bookId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT b.*, a.name AS author " +
                                        "FROM BOOKS b " +
                                        "INNER JOIN AUTHORS a ON b.author_id = a.id " +
                                        "WHERE b.id = :id AND b.is_sell = 1"
                        )
                        .bind("id", bookId)
                        .mapToBean(Book.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public List<Book> getBookRecommendInDetail(String type) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                    SELECT b.*
                                    FROM BOOKS b
                                    LEFT JOIN comments r 
                                           ON b.id = r.book_id 
                                          AND r.is_active = 1
                                    WHERE b.is_sell = 1
                                      AND b.type = :type
                                    GROUP BY 
                                        b.id, b.title, b.author_id, b.publisher,
                                        b.price, b.price_discounted, b.quantity_sold,
                                        b.published_date, b.stock, b.is_sell, b.type
                                    ORDER BY
                                        AVG(r.rating) DESC,
                                        COUNT(*) DESC,
                                        b.quantity_sold DESC,
                                        CASE 
                                            WHEN b.price_discounted > 0 THEN 1 
                                            ELSE 0 
                                        END DESC,
                                        b.published_date DESC
                                """)
                        .bind("type", type)
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public List<Book> getAllBooks(int limit, int offset) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM BOOKS WHERE is_sell=1 LIMIT :limit OFFSET :offset")
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public List<Book> getAllBooks() {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT b.id, b.book_code, b.title, a.name AS author, b.price, b.stock, b.type, b.age, b.cover_img_url FROM books b LEFT JOIN authors a ON b.author_id = a.id WHERE b.is_sell = 1")
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public int countBooks() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM BOOKS WHERE is_sell = 1")
                        .mapTo(int.class)
                        .one()
        );
    }

    public List<Book> findListBook(String search, int limit, int offset) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT b.* " +
                                        "FROM BOOKS b " +
                                        "JOIN AUTHORS a ON b.author_id = a.id " +
                                        "WHERE b.is_sell = 1 " +
                                        "AND (b.title LIKE :search OR a.name LIKE :search OR b.type LIKE :search OR a.name like :search) " +
                                        "LIMIT :limit OFFSET :offset"
                        )
                        .bind("search", "%" + search + "%")
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(Book.class)
                        .list()
        );
    }


    public void insert(Book book, List<String> detailImages) {
        getJdbi().useHandle(h -> {

            int bookId = h.createUpdate(
                            "INSERT INTO books (" +
                                    "book_code, title, author_id, price, price_discounted, type, age, " +
                                    "cover_img_url, description, publisher, provider, published_date, " +
                                    "weight, book_size, pages_number, format, is_sell, add_date, quantity_sold, stock" +
                                    ") VALUES (" +
                                    ":bookCode, :title, :authorId, :price, :priceDiscounted, :type, :age, " +
                                    ":coverImgUrl, :description, :publisher, :provider, :publishedDate, " +
                                    ":weight, :bookSize, :pagesNumber, :format, :isSell, CURDATE(), :quantitySold, :stock" +
                                    ")"
                    )
                    .bind("bookCode", book.getBookCode())
                    .bind("title", book.getTitle())
                    .bind("authorId", book.getAuthorId())
                    .bind("price", book.getPrice())
                    .bind("priceDiscounted", book.getPriceDiscounted())
                    .bind("type", book.getType())
                    .bind("age", book.getAge())
                    .bind("coverImgUrl", book.getCoverImgUrl())
                    .bind("description", book.getDescription())
                    .bind("publisher", book.getPublisher())
                    .bind("provider", book.getProvider())
                    .bind("publishedDate", book.getPublishedDate())
                    .bind("weight", book.getWeight())
                    .bind("bookSize", book.getBookSize())
                    .bind("pagesNumber", book.getPagesNumber())
                    .bind("format", book.getFormat())
                    .bind("isSell", book.getIsSell())          // 0 / 1
                    .bind("quantitySold", book.getQuantitySold())
                    .bind("stock", book.getStock())
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class)
                    .one();

            // ảnh chi tiết
            for (String img : detailImages) {
                h.createUpdate(
                                "INSERT INTO book_image_details (book_id, img_url) VALUES (:bookId, :img)"
                        )
                        .bind("bookId", bookId)
                        .bind("img", "assets/img/books/" + img)
                        .execute();
            }
        });
    }

    public List<Book> getAllBooksDiscounted(int limit, int offset) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM BOOKS WHERE price_discounted > 0 AND is_sell=1  ORDER BY updated_at DESC LIMIT :limit OFFSET :offset").bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public List<Book> getAllBooksNew(int limit, int offset) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM BOOKS WHERE is_sell=1 ORDER BY add_date DESC LIMIT :limit OFFSET :offset")
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public List<Book> getAllBookByEvent(int limit, int offset, int idEvent) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT DISTINCT b.* " +
                                "FROM BOOKS b " +
                                "INNER JOIN event_books eb ON eb.book_id = b.id " +
                                "INNER JOIN events e ON e.id = eb.event_id " +
                                "WHERE is_sell=1 AND e.id = :id " +
                                "ORDER BY add_date DESC LIMIT :limit OFFSET :offset")
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .bind("id", idEvent)
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public List<Book> getAllFavouriteBook(int limit, int offset) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT b.*
                                FROM books b
                                JOIN favourite_books f ON b.id = f.book_id
                                WHERE is_sell=1
                                GROUP BY b.id
                                ORDER BY COUNT(f.book_id) DESC
                                LIMIT :limit OFFSET :offset
                                """)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public int countBooksBySearch(String search) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT COUNT(*) " +
                                        "FROM BOOKS b " +
                                        "LEFT JOIN AUTHORS a ON b.author_id = a.id " +
                                        "WHERE b.is_sell = 1 " +
                                        "AND (b.title LIKE :search OR a.name LIKE :search OR b.type LIKE :search)"
                        )
                        .bind("search", "%" + search + "%")
                        .mapTo(int.class)
                        .one()
        );
    }


    public int countBooksDiscounted() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM BOOKS WHERE price_discounted > 0 AND is_sell=1")
                        .mapTo(int.class)
                        .one()
        );
    }

    public int countBooksNew() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM BOOKS WHERE is_sell=1 ORDER BY add_date DESC")
                        .mapTo(int.class)
                        .one()
        );
    }

    public int countBookFavourite() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM BOOKS WHERE id IN (SELECT DISTINCT book_id FROM favourite_books) AND is_sell=1")
                        .mapTo(int.class)
                        .one()
        );
    }

    public int countBooksByEvent(int idEvent) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(DISTINCT b.id) " +
                                "FROM BOOKS b " +
                                "INNER JOIN event_books eb ON eb.book_id = b.id " +
                                "INNER JOIN events e ON e.id = eb.event_id " +
                                "WHERE is_sell=1 AND e.id = :id ")
                        .bind("id", idEvent)
                        .mapTo(int.class)
                        .one()
        );
    }

    public List<String> getAllBookTypes() {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT DISTINCT type FROM books WHERE is_sell = 1"
                        )
                        .mapTo(String.class)
                        .list()
        );
    }

    public List<Book> searchAndFilter(String q, String type, String stock) {

        return getJdbi().withHandle(handle -> {

            StringBuilder sql = new StringBuilder(
                    "SELECT b.*, a.name AS author " +
                            "FROM books b " +
                            "LEFT JOIN authors a ON b.author_id = a.id " +
                            "WHERE b.is_sell = 1"
            );

            if (q != null) {
                sql.append(" AND (b.title LIKE :q OR a.name LIKE :q OR b.type LIKE :q)");
            }

            if (type != null) {
                sql.append(" AND b.type = :type");
            }

            if ("asc".equals(stock)) {
                sql.append(" ORDER BY b.stock ASC");
            } else if ("desc".equals(stock)) {
                sql.append(" ORDER BY b.stock DESC");
            } else {
                sql.append(" ORDER BY b.add_date DESC");
            }


            var query = handle.createQuery(sql.toString());

            if (q != null) {
                query.bind("q", "%" + q + "%");
            }
            if (type != null) {
                query.bind("type", type);
            }

            return query.mapToBean(Book.class).list();
        });
    }

    public void update(Book book) {
        getJdbi().useHandle(h ->
                h.createUpdate("""
                                    UPDATE books SET
                                        book_code = :code,
                                        title = :title,
                                        author_id = :authorId,
                                        price = :price,
                                        price_discounted = :priceDiscounted,
                                        type = :type,
                                        age = :age,
                                        cover_img_url = :cover,
                                        description = :description,
                                        publisher = :publisher,
                                        provider = :provider,
                                        published_date = :published,
                                        weight = :weight,
                                        book_size = :size,
                                        pages_number = :pages,
                                        format = :format,
                                        stock = :stock
                                    WHERE id = :id
                                """)
                        .bind("id", book.getId())
                        .bind("code", book.getBookCode())
                        .bind("title", book.getTitle())
                        .bind("authorId", book.getAuthorId())
                        .bind("price", book.getPrice())
                        .bind("priceDiscounted", book.getPriceDiscounted())
                        .bind("type", book.getType())
                        .bind("age", book.getAge())
                        .bind("cover", book.getCoverImgUrl())
                        .bind("description", book.getDescription())
                        .bind("publisher", book.getPublisher())
                        .bind("provider", book.getProvider())
                        .bind("published", book.getPublishedDate())
                        .bind("weight", book.getWeight())
                        .bind("size", book.getBookSize())
                        .bind("pages", book.getPagesNumber())
                        .bind("format", book.getFormat())
                        .bind("stock", book.getStock())
                        .execute()
        );
    }


    public void deleteDetailImages(int bookId) {
        getJdbi().useHandle(h ->
                h.createUpdate("DELETE FROM book_image_details WHERE book_id = :id")
                        .bind("id", bookId)
                        .execute()
        );
    }

    public void insertDetailImages(int bookId, List<String> urls) {
        getJdbi().useHandle(h -> {
            for (String url : urls) {
                h.createUpdate("""
                                    INSERT INTO book_image_details (book_id, img_url)
                                    VALUES (:id, :url)
                                """)
                        .bind("id", bookId)
                        .bind("url", url)
                        .execute();
            }
        });
    }

    public void insertFavouriteBook(int bookId, int userId) {
        getJdbi().useHandle(h -> {
            h.createUpdate("""
                            INSERT INTO favourite_books(book_id, user_id)
                            VALUES (:bookId, :userId)
                            """)
                    .bind("bookId", bookId)
                    .bind("userId", userId)
                    .execute();
        });
    }

    public List<Book> getFavouriteBook(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT *
                                FROM BOOKS
                                WHERE id IN (SELECT book_id FROM favourite_books WHERE user_id = :userId)
                                ORDER BY id DESC
                                """)
                        .bind("userId", userId)
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public List<Book> getFavouriteBook() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT b.*
                                FROM books b
                                JOIN favourite_books f ON b.id = f.book_id
                                where is_sell=1
                                GROUP BY b.id
                                ORDER BY COUNT(f.book_id) DESC;
                                """)
                        .mapToBean(Book.class)
                        .list()
        );
    }

    public void deleteFavouriteBook(int bookId, int userId) {
        getJdbi().useHandle(h -> {
            h.createUpdate("DELETE FROM favourite_books WHERE book_id = :bookId AND user_id = :userId")
                    .bind("bookId", bookId)
                    .bind("userId", userId)
                    .execute();
        });
    }

    public boolean isFavouriteBook(int bookId, int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                    SELECT 1
                                    FROM favourite_books
                                    WHERE book_id = :bookId
                                      AND user_id = :userId
                                    LIMIT 1
                                """)
                        .bind("bookId", bookId)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .findFirst()
                        .isPresent()
        );
    }

    public void updateDiscountBook(List<Book> listBookEvent, double value) {

        if (listBookEvent == null || listBookEvent.isEmpty()) {
            return;
        }

        if (value <= 0 || value >= 100) {
            throw new IllegalArgumentException("Giá trị giảm không hợp lệ");
        }

        getJdbi().useHandle(handle -> {

            var batch = handle.prepareBatch("""
                        UPDATE books
                        SET price_discounted = price * (1 - :value / 100),
                        updated_at = NOW()
                        WHERE id = :bookId
                    """);

            for (Book book : listBookEvent) {
                batch
                        .bind("value", value)
                        .bind("bookId", book.getId())
                        .add();
            }

            batch.execute();
        });
    }

    public List<String> getImgDetails(int bookId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT img_url FROM book_image_details WHERE book_id = :book_id"
                        )
                        .bind("book_id", bookId)
                        .mapTo(String.class)
                        .list()
        );
    }

    public void setUpdatSeld(int id) {
        getJdbi().withHandle(handle -> (
                handle.createUpdate("UPDATE books SET is_sell=0 where id=:id")
                        .bind("id", id)
                        .execute()
        ));
    }

    public void updateStock(Book book, int quantity) {
        getJdbi().withHandle(handle -> (
                handle.createUpdate("UPDATE books SET stock = stock - :stock where id=:id")
                        .bind("stock", quantity)
                        .bind("id", book.getId())
                        .execute()
        ));
    }

    public boolean isBookAvailable(int bookId) {

        String sql = "SELECT is_sell FROM books WHERE id = :id";

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("id", bookId)
                        .mapTo(Boolean.class)
                        .findOne()
                        .orElse(false)
        );
    }

    public void updateQuantity(Book book, int quantity) {
        getJdbi().withHandle(handle -> (
                handle.createUpdate("UPDATE books SET quantity_sold = quantity_sold + :quantity_sold where id=:id")
                        .bind("quantity_sold", quantity)
                        .bind("id", book.getId())
                        .execute()
        ));
    }

    public int getStockByBookId(int bookId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT stock FROM BOOKS WHERE id=:id")
                        .bind("id", bookId)
                        .mapTo(int.class)
                        .one()
        );
    }

    public int countByCategoryAndAge(String category, int ageFrom, int ageTo) {
        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*)
            FROM books
            WHERE is_sell = 1
              AND age BETWEEN :ageFrom AND :ageTo
        """);

        if (category != null) {
            sql.append(" AND type = :category ");
        }

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString())
                    .bind("ageFrom", ageFrom)
                    .bind("ageTo", ageTo);

            if (category != null) {
                query.bind("category", category);
            }

            return query.mapTo(Integer.class).one();
        });
    }

    public List<Book> findByCategoryAndAge(String category, int ageFrom, int ageTo, int limit, int offset) {
        StringBuilder sql = new StringBuilder("""
            SELECT *
            FROM books
            WHERE is_sell = 1
              AND age BETWEEN :ageFrom AND :ageTo
        """);

        if (category != null) {
            sql.append(" AND type = :category ");
        }

        sql.append(" ORDER BY add_date DESC ");
        sql.append(" LIMIT :limit OFFSET :offset ");

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString())
                    .bind("ageFrom", ageFrom)
                    .bind("ageTo", ageTo)
                    .bind("limit", limit)
                    .bind("offset", offset);

            if (category != null) {
                query.bind("category", category);
            }

            return query.mapToBean(Book.class).list();
        });
    }

    public List<String> getAllPublishers() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT distinct publisher FROM BOOKS")
                        .mapTo(String.class).list()
                );
    }

    public List<Integer> getAllYears() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT distinct published_date FROM BOOKS")
                        .mapTo(Integer.class).list()
        );
    }

    public List<String> getSuggest(String keyword) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                    SELECT DISTINCT title
                    FROM BOOKS
                    WHERE is_sell = 1
                      AND title LIKE :keyword
                    ORDER BY title
                    LIMIT 8
            """)
                        .bind("keyword", "%" + keyword + "%")
                        .mapTo(String.class)
                        .list()
        );
    }
    public List<String> getCategory(){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT DISTINCT type FROM BOOKS")
                        .mapTo(String.class).list());
    }

    public int countBooksUniversal(String keyword, int type, int idEvent, String category, String author, String publisher, String age, String maxPrice, String year) {
        return getJdbi().withHandle(handle -> {
            StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT b.id) FROM books b ");
            if (type == 4 || idEvent > 0) {
                sql.append("INNER JOIN event_books eb ON eb.book_id = b.id ");
                sql.append("INNER JOIN events e ON e.id = eb.event_id ");
            }
            sql.append("WHERE b.is_sell = 1 ");

            if (type == 1) sql.append("AND b.price_discounted > 0 ");
            if (type == 2) sql.append("AND b.add_date IS NOT NULL ");
            if (type == 3) sql.append("AND b.id IN (SELECT book_id FROM favourite_books) ");
            if (type == 4 && idEvent > 0) sql.append("AND e.id = :idEvent ");
            if (category != null && !category.isBlank()) sql.append("AND b.type = :category ");
            if (age != null && !age.isBlank()) {
                String[] parts = age.split("-");
                sql.append("AND b.age BETWEEN :ageFrom AND :ageTo ");
            }

            var query = handle.createQuery(sql.toString());
            if (type == 4 && idEvent > 0) query.bind("idEvent", idEvent);
            if (category != null && !category.isBlank()) query.bind("category", category);
            if (age != null && !age.isBlank()) {
                String[] parts = age.split("-");
                query.bind("ageFrom", Integer.parseInt(parts[0]));
                query.bind("ageTo", Integer.parseInt(parts[1]));
            }

            return query.mapTo(int.class).one();
        });
    }

    public List<Book> getBooksUniversal(String keyword, int type, int idEvent, String category, String author, String publisher, String age, String maxPrice, String year, int pageSize, int offset) {
    }
}
