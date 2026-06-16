package dao;

import model.Author;
import model.Book;
import model.CommentView;
import model.RatingStartView;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Update;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookDao extends BaseDao {
    public List<String> getAllCategories() {
        return getJdbi().withHandle(handle ->
            handle.createQuery("select distinct type from BOOKS ORDER BY type ASC")
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
                                        COUNT(b.id) DESC,
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
                                "SELECT b.id, b.book_code, b.title, a.name AS author, b.price, b.price_discounted AS priceDiscounted, b.stock, b.type, b.age, b.cover_img_url FROM books b LEFT JOIN authors a ON b.author_id = a.id WHERE b.is_sell = 1")
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


    public void insert(List<Book> books, List<List<String>> allDetailImages, int employeeId) {
        getJdbi().useTransaction(h -> {
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                List<String> detailImages = allDetailImages.get(i);
                Integer bookIdOld = findBookByBookCode(h,book.getBookCode());

                    if(bookIdOld!=null){
                        h.createUpdate("""
                            UPDATE books
                            SET stock = stock + :quantity
                            WHERE id = :bookId
                            """)
                                .bind("quantity", book.getStock())
                                .bind("bookId", bookIdOld)
                                .execute();
                    }
                    else{
                        int bookId = h.createUpdate(
                                        "INSERT INTO books (" +
                                                "book_code, title, author_id, price, price_discounted,price_import, type, age, " +
                                                "cover_img_url, description, publisher, provider, published_date, " +
                                                "weight, book_size, pages_number, format, is_sell, add_date, quantity_sold, stock" +
                                                ") VALUES (" +
                                                ":bookCode, :title, :authorId, :price, :priceDiscounted, :priceImport,:type, :age, " +
                                                ":coverImgUrl, :description, :publisher, :provider, :publishedDate, " +
                                                ":weight, :bookSize, :pagesNumber, :format, :isSell, CURDATE(), :quantitySold, :stock" +
                                                ")"
                                )
                                .bind("bookCode", book.getBookCode())
                                .bind("title", book.getTitle())
                                .bind("authorId", book.getAuthorId())
                                .bind("price", book.getPrice())
                                .bind("priceDiscounted", book.getPriceDiscounted())
                                .bind("priceImport",book.getPriceImport())
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
                                .bind("isSell", 1)
                                .bind("quantitySold", book.getQuantitySold())
                                .bind("stock", book.getStock())
                                .executeAndReturnGeneratedKeys("id")
                                .mapTo(int.class)
                                .one();

                        for (String img : detailImages) {
                            h.createUpdate(
                                            "INSERT INTO book_image_details (book_id, img_url) VALUES (:bookId, :img)"
                                    )
                                    .bind("bookId", bookId)
                                    .bind("img", "assets/img/books/" + img)
                                    .execute();
                        }
                    }
            }
            insertHistoryBookImport(h,books, employeeId);
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

    public List<Book> searchAndFilter(String q, String type, String stock, int status) {

        return getJdbi().withHandle(handle -> {

            StringBuilder sql = new StringBuilder(
                    "SELECT b.*, a.name AS author " +
                            "FROM books b " +
                            "LEFT JOIN authors a ON b.author_id = a.id " +
                            "WHERE 1=1"
            );

            if (q != null) {
                sql.append(" AND (b.title LIKE :q OR a.name LIKE :q OR b.type LIKE :q)");
            }

            if (type != null) {
                sql.append(" AND b.type = :type");
            }
            if (status == 0) {
                sql.append(" AND b.is_sell = 0");
            }else {
                sql.append(" AND b.is_sell = 1");
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

    public void update(int bookId, Map<String, Object> changes) {
        StringBuilder sql = new StringBuilder("UPDATE BOOKS SET ");
        for (var entry : changes.entrySet()) {
            sql.append(entry.getKey())
                    .append("=:")
                    .append(entry.getKey())
                    .append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE id = :bookId");
        getJdbi().useHandle(handle ->{
            Update update = handle.createUpdate(sql.toString());
            update.bind("bookId", bookId);
            for (var entry : changes.entrySet()) {
                update.bind(entry.getKey(), entry.getValue());
            }
            update.execute();
        });
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
    public void updateQuantityOrderCancelled(int bookId, int quantity) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("""
                                UPDATE BOOKS
                                SET stock = stock + :quantity , quantity_sold = quantity_sold - :quantity
                                WHERE id=:id
                        """)
                        .bind("quantity", quantity)
                        .bind("id", bookId)
                        .execute()
        );
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
                handle.createQuery("SELECT distinct publisher FROM BOOKS ORDER BY publisher ASC")
                        .mapTo(String.class).list()
                );
    }

    public List<Integer> getAllYears() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT distinct published_date FROM BOOKS ORDER BY published_date DESC ")
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
            sql.append("LEFT JOIN authors a ON b.author_id = a.id ");
            sql.append("WHERE b.is_sell = 1 ");

            if (keyword != null && !keyword.isBlank()) {
                sql.append("AND (b.title LIKE :keyword OR a.name LIKE :keyword) ");
            }
            if (type == 1) sql.append("AND b.price_discounted > 0 ");
            if (type == 2) sql.append("AND b.add_date IS NOT NULL ");
            if (type == 3) sql.append("AND b.id IN (SELECT book_id FROM favourite_books) ");
            if (type == 4 && idEvent > 0) sql.append("AND e.id = :idEvent ");
            if (category != null && !category.isBlank()) sql.append("AND FIND_IN_SET(b.type, :category) > 0 ");
            if (author != null && !author.isBlank()) sql.append("AND FIND_IN_SET(a.name, :author) > 0 ");
            if (publisher != null && !publisher.isBlank()) sql.append("AND FIND_IN_SET(b.publisher, :publisher) > 0 ");
            if (age != null && !age.isBlank()) {
                String[] parts = age.split("-");
                sql.append("AND b.age BETWEEN :ageFrom AND :ageTo ");
            }
            if (maxPrice != null && !maxPrice.isBlank()) {
                sql.append("AND b.price_discounted <= :maxPrice ");
            }
            if (year != null && !year.isBlank()) {
                sql.append("AND YEAR(b.published_date) = :year ");
            }

            var query = handle.createQuery(sql.toString());
            if (keyword != null && !keyword.isBlank()) query.bind("keyword", "%" + keyword + "%");
            if (type == 4 && idEvent > 0) query.bind("idEvent", idEvent);
            if (category != null && !category.isBlank()) query.bind("category", category);
            if (author != null && !author.isBlank()) query.bind("author", author);
            if (publisher != null && !publisher.isBlank()) query.bind("publisher", publisher);
            if (age != null && !age.isBlank()) {
                String[] parts = age.split("-");
                query.bind("ageFrom", Integer.parseInt(parts[0]));
                query.bind("ageTo", Integer.parseInt(parts[1]));
            }
            if (maxPrice != null && !maxPrice.isBlank()) query.bind("maxPrice", Integer.parseInt(maxPrice));
            if (year != null && !year.isBlank()) query.bind("year", Integer.parseInt(year));

            return query.mapTo(int.class).one();
        });
    }

    public List<Book> getBooksUniversal(String keyword, int type, int idEvent, String category, String author, String publisher, String age, String maxPrice, String year, int pageSize, int offset) {
        return getJdbi().withHandle(handle -> {
            StringBuilder sql = new StringBuilder("SELECT DISTINCT b.* FROM books b ");
            if (type == 4 || idEvent > 0) {
                sql.append("INNER JOIN event_books eb ON eb.book_id = b.id ");
                sql.append("INNER JOIN events e ON e.id = eb.event_id ");
            }
            sql.append("LEFT JOIN authors a ON b.author_id = a.id ");
            sql.append("WHERE b.is_sell = 1 ");

            if (keyword != null && !keyword.isBlank()) {
                sql.append("AND (b.title LIKE :keyword OR a.name LIKE :keyword) ");
            }
            if (type == 1) sql.append("AND b.price_discounted > 0 ");
            if (type == 2) sql.append("AND b.add_date IS NOT NULL ");
            if (type == 3) sql.append("AND b.id IN (SELECT book_id FROM favourite_books) ");
            if (type == 4 && idEvent > 0) sql.append("AND e.id = :idEvent ");
            if (category != null && !category.isBlank()) sql.append("AND FIND_IN_SET(b.type, :category) > 0 ");
            if (author != null && !author.isBlank()) sql.append("AND FIND_IN_SET(a.name, :author) > 0 ");
            if (publisher != null && !publisher.isBlank()) sql.append("AND FIND_IN_SET(b.publisher, :publisher) > 0 ");
            if (age != null && !age.isBlank()) {
                String[] parts = age.split("-");
                sql.append("AND b.age BETWEEN :ageFrom AND :ageTo ");
            }
            if (maxPrice != null && !maxPrice.isBlank()) {
                sql.append("AND b.price_discounted <= :maxPrice ");
            }
            if (year != null && !year.isBlank()) {
                sql.append("AND b.published_date= :year ");
            }

            sql.append("ORDER BY b.add_date DESC ");
            sql.append("LIMIT :limit OFFSET :offset ");

            var query = handle.createQuery(sql.toString());
            if (keyword != null && !keyword.isBlank()) query.bind("keyword", "%" + keyword + "%");
            if (type == 4 && idEvent > 0) query.bind("idEvent", idEvent);
            if (category != null && !category.isBlank()) query.bind("category", category);
            if (author != null && !author.isBlank()) query.bind("author", author);
            if (publisher != null && !publisher.isBlank()) query.bind("publisher", publisher);
            if (age != null && !age.isBlank()) {
                String[] parts = age.split("-");
                query.bind("ageFrom", Integer.parseInt(parts[0]));
                query.bind("ageTo", Integer.parseInt(parts[1]));
            }
            if (maxPrice != null && !maxPrice.isBlank()) query.bind("maxPrice", Integer.parseInt(maxPrice));
            if (year != null && !year.isBlank()) query.bind("year", Integer.parseInt(year));

            query.bind("limit", pageSize);
            query.bind("offset", offset);

            return query.mapToBean(Book.class).list();
        });
    }

    public int countSearchAndFilter(String q, String type, int status) {

        return getJdbi().withHandle(handle -> {
            StringBuilder sql = new StringBuilder(
                    "SELECT COUNT(*) FROM books b " +
                    "LEFT JOIN authors a ON b.author_id = a.id " +
                    "WHERE 1=1"
            );
            if (q != null && !q.trim().isEmpty()) {
                sql.append(" AND (b.title LIKE :q OR a.name LIKE :q OR b.type LIKE :q)");
            }
            if (type != null && !type.trim().isEmpty()) {
                sql.append(" AND b.type = :type");
            }
            if (status == 0) {
                sql.append(" AND b.is_sell = 0");
            }else {
                sql.append(" AND b.is_sell = 1");
            }
            var query = handle.createQuery(sql.toString());
            if (q != null && !q.trim().isEmpty()) query.bind("q", "%" + q + "%");
            if (type != null && !type.trim().isEmpty()) query.bind("type", type);
            return query.mapTo(int.class).one();
        });
    }

    public List<Book> searchAndFilterPaginated(String q, String type, String stock, int limit, int offset,  int status) {
        return getJdbi().withHandle(handle -> {
            StringBuilder sql = new StringBuilder(
                    "SELECT b.*, a.name AS author FROM books b " +
                    "LEFT JOIN authors a ON b.author_id = a.id " +
                    "WHERE 1=1"
            );
            if (q != null && !q.trim().isEmpty()) {
                sql.append(" AND (b.title LIKE :q OR a.name LIKE :q OR b.type LIKE :q)");
            }
            if (type != null && !type.trim().isEmpty()) {
                sql.append(" AND b.type = :type");
            }
            if (status == 0) {
                sql.append(" AND b.is_sell = 0");
            }else {
                sql.append(" AND b.is_sell = 1");
            }
            if ("asc".equals(stock)) {
                sql.append(" ORDER BY b.stock ASC");
            } else if ("desc".equals(stock)) {
                sql.append(" ORDER BY b.stock DESC");
            } else {
                sql.append(" ORDER BY b.add_date DESC, b.id DESC");
            }
            sql.append(" LIMIT :limit OFFSET :offset");

            var query = handle.createQuery(sql.toString());
            if (q != null && !q.trim().isEmpty()) query.bind("q", "%" + q + "%");
            if (type != null && !type.trim().isEmpty()) query.bind("type", type);
            query.bind("limit", limit);
            query.bind("offset", offset);

            return query.mapToBean(Book.class).list();
        });
    }
    public void insertHistoryBookImport(Handle handle, List<Book> books, int employeeId) {
            Book book = books.get(0);
            String provider = book.getProvider();
            double totalPrice = 0;
            for (Book b : books) {
                totalPrice += b.getPriceImport()*b.getStock();
            }

           int importOrderId = handle.createUpdate("""
                INSERT INTO import_orders(provider, import_date, total_amount, note, employee_id_import)
                VALUES(:provider, NOW(), :totalAmount, :note, :employeeIdImport)
                """)
                    .bind("provider", provider)
                    .bind("totalAmount", totalPrice)
                    .bind("note", "sách mới về")
                    .bind("employeeIdImport", employeeId)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class)
                    .one();
            for (int i = 0; i <books.size(); i++) {
                Book b = books.get(i);
                int bookId = findBookByBookCode(handle,b.getBookCode());
                int subtotal = b.getPriceImport()*b.getStock();
                handle.createUpdate("""
                INSERT INTO import_order_details(import_order_id,book_id,quantity, price_import,subtotal)
                VALUES(:importOrderId,:bookId,:quantity,:priceImport,:subtotal)
                """)
                        .bind("importOrderId", importOrderId)
                        .bind("bookId", bookId)
                        .bind("quantity", b.getStock())
                        .bind("priceImport", b.getPriceImport())
                        .bind("subtotal", subtotal)
                        .execute();
            }
    }
    public Integer findBookByBookCode(Handle h, String bookCode) {
        return h.createQuery("""
            SELECT id
            FROM books
            WHERE book_code = :bookCode
            """)
                .bind("bookCode", bookCode)
                .mapTo(Integer.class)
                .findOne()
                .orElse(null);
    }
    public List<String> getListBookCode(){
        return getJdbi().withHandle(handle ->
            handle.createQuery("""
                    SELECT book_code FROM BOOKS
                    """)
                    .mapTo(String.class)
                    .list()
        );
    }
    public double salesPercentageTheMostRecentImport(int bookId){
        return getJdbi().withHandle(handle -> {
            LocalDateTime latestRecentImportDate = handle.createQuery("""
                    SELECT o.import_date
                    FROM IMPORT_ORDERS o
                    INNER JOIN IMPORT_ORDER_DETAILS od ON o.id = od.import_order_id
                    WHERE od.book_id = :bookId
                    ORDER BY o.import_date DESC
                    LIMIT 1
                    """)
                    .bind("bookId", bookId)
                    .mapTo(LocalDateTime.class)
                    .findOne()
                    .orElse(null);
            if(latestRecentImportDate == null) return 0.0;

            Integer importedQuantity = handle.createQuery("""
                    SELECT COALESCE(SUM(od.quantity),0)
                    FROM IMPORT_ORDERS o
                    INNER JOIN IMPORT_ORDER_DETAILS od ON o.id = od.import_order_id
                    WHERE od.book_id = :bookId AND o.import_date = :importDate
                    """)
                    .bind("bookId", bookId)
                    .bind("importDate", latestRecentImportDate)
                    .mapTo(Integer.class)
                    .one();

            if (importedQuantity == 0) return 0.0;

            Integer soldQuantity = handle.createQuery("""
                        SELECT COALESCE(SUM(oi.quantity), 0)
                        FROM ORDERS o
                        INNER JOIN ORDER_ITEMS oi ON o.id = oi.order_id
                        WHERE oi.book_id = :bookId AND o.order_date >= :importDate AND o.status = 'COMPLETED'
                        """)
                            .bind("bookId", bookId)
                            .bind("importDate", latestRecentImportDate)
                            .mapTo(Integer.class)
                            .one();
            return Math.round(((double) soldQuantity / importedQuantity) * 10000) / 100.0 ;
        });

    }
    public static void main(String[] args) {
        BookDao dao = new BookDao();
        System.out.println(Math.round(((double) 55 / 102) * 10000) / 100.0);
    }

    public List<Integer> getAllAges() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT DISTINCT age FROM BOOKS ORDER BY age ASC")
                        .mapTo(Integer.class).list()
                );
    }

    public List<Author> getAllAuthorsOb() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT ID, NAME, BIO, BIRTHDAY FROM AUTHORS ORDER BY NAME ASC")
                        .mapToBean(Author.class).list()
                );
    }
}
