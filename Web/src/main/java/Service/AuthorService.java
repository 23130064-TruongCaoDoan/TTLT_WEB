package Service;

import dao.AuthorDao;
import model.Author;

import java.util.List;

public class AuthorService {
    private AuthorDao authorDao = new AuthorDao();

    public List<Author> getAllAuthors() {
        return authorDao.getAll();
    }


}
