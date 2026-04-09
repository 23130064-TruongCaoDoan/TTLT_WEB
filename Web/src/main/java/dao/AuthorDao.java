package dao;

import model.Author;

import java.util.List;

public class AuthorDao extends BaseDao {

    public List<Author> getAll() {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT id, name FROM authors ORDER BY name ASC")
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
}
