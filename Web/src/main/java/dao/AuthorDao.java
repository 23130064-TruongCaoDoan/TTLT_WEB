package dao;

import model.Author;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AuthorDao extends BaseDao {

    public List<Author> getAll() {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT id, name, bio, birthday FROM authors ORDER BY id DESC")
                        .mapToBean(Author.class)
                        .list()
        );
    }

    public List<String> getAllAuthors() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("Select distinct name FROM AUTHORS")
                        .mapTo(String.class).list()
                );
    }

    public List<Author> search(String keyword) {
        String queryKeyword = "%" + keyword + "%";
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT id, name, bio, birthday FROM authors WHERE CAST(id AS CHAR) LIKE :kw OR name LIKE :kw ORDER BY id DESC")
                        .bind("kw", queryKeyword)
                        .mapToBean(Author.class)
                        .list()
        );
    }

    public boolean add(Author author) {
        String penName = author.getPenName()==null?author.getName():author.getPenName();
        return getJdbi().withHandle(h ->
                h.createUpdate("INSERT INTO authors(name, birthday, pen_name) VALUES(:name, :birthday, :penName)")
                        .bindBean(author)
                        .execute() > 0
        );
    }
    public Integer addAuthorFromFile(Author author) {
        String penName = author.getPenName()==null?author.getName():author.getPenName();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate localDate = LocalDate.parse(author.getBirthday(), formatter);
        Date sqlDate = Date.valueOf(localDate);

        return getJdbi().withHandle(handle ->
                handle.createUpdate("""
                            INSERT INTO authors(name,birthday,pen_name)
                            VALUES(:name,:birthday,:penName)
                        """)
                        .bind("name", author.getName())
                        .bind("birthday", sqlDate)
                        .bind("penName", penName)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }
    public Integer findAuthorByPenName(String name, String birthday, String penName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate localDate = LocalDate.parse(birthday, formatter);
        Date sqlDate = Date.valueOf(localDate);

        return  getJdbi().withHandle(handle ->
                handle.createQuery("""
                SELECT id FROM AUTHORS WHERE name=:name AND pen_name = :penName AND birthday = :birtday
                """)
                .bind("penName", penName)
                .bind("name", name)
                .bind("birtday", sqlDate)
                .mapTo(Integer.class)
                .findOne()
                .orElse(null)
        );
    }


    public boolean update(int id, String name, String birthday) {
        StringBuilder sql = new StringBuilder("UPDATE authors SET ");
        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasBirthday = birthday != null && !birthday.trim().isEmpty();

        if (!hasName && !hasBirthday) return false;

        if (hasName) sql.append("name = :name ");
        if (hasName && hasBirthday) sql.append(", ");
        if (hasBirthday) sql.append("birthday = :birthday ");

        sql.append("WHERE id = :id");

        return getJdbi().withHandle(h -> {
            var update = h.createUpdate(sql.toString()).bind("id", id);
            if (hasName) update.bind("name", name.trim());
            if (hasBirthday) update.bind("birthday", birthday.trim());
            return update.execute() > 0;
        });
    }

    public int getTotalAuthors(String keyword) {
        String queryKeyword = (keyword == null || keyword.trim().isEmpty()) ? "%" : "%" + keyword.trim() + "%";
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT COUNT(id) FROM authors WHERE (CAST(id AS CHAR) LIKE :kw OR name LIKE :kw) AND is_deleted = 0 ")
                        .bind("kw", queryKeyword)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<Author> getAuthorsByPage(String keyword, int offset, int limit) {
        String queryKeyword = (keyword == null || keyword.trim().isEmpty()) ? "%" : "%" + keyword.trim() + "%";
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT id, name, bio, birthday FROM authors WHERE (CAST(id AS CHAR) LIKE :kw OR name LIKE :kw) AND is_deleted = 0 ORDER BY id DESC LIMIT :limit OFFSET :offset")
                        .bind("kw", queryKeyword)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(Author.class)
                        .list()
        );
    }

    public boolean softDelete(int id) {
        return getJdbi().withHandle(h ->
                h.createUpdate("UPDATE authors SET is_deleted = 1 WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }
}
