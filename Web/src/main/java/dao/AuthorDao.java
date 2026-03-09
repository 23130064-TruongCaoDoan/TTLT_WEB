package dao;

import model.Author;

import java.util.List;

public class AuthorDao extends BaseDao {

    public List<Author> getAll() {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT id, name FROM authors")
                        .mapToBean(Author.class)
                        .list()
        );
    }

}
