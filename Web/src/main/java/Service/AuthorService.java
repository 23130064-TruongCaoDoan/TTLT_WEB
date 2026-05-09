package Service;

import dao.AuthorDao;
import model.Author;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

public class AuthorService {
    private AuthorDao authorDao = new AuthorDao();

    public List<Author> getAllAuthors() {
        return authorDao.getAll();
    }

    public List<Author> searchAuthors(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAuthors();
        }
        return authorDao.search(keyword.trim());
    }

    public boolean addAuthor(String name, String birthday) {
        if (!isValidName(name)) {
            return false;
        }
        Author author = new Author();
        author.setName(name.trim());
        author.setBirthday(birthday);
        return authorDao.add(author);
    }

    public boolean updateAuthor(int id, String name, String birthday) {
        if (name != null && !name.trim().isEmpty() && !isValidName(name)) {
            return false;
        }
        if (birthday != null && !birthday.trim().isEmpty() && !isValidDate(birthday)) {
            return false;
        }
        return authorDao.update(id, name, birthday);
    }

    public boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        String regex = "^[\\p{L} \\s]+$";
        return Pattern.matches(regex, name.trim());
    }

    public boolean isValidDate(String date) {
        if (date == null || date.trim().isEmpty()) return false;
        try {
            LocalDate.parse(date.trim());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public int getTotalAuthors(String keyword) {
        return authorDao.getTotalAuthors(keyword);
    }

    public List<Author> getAuthorsByPage(String keyword, int page, int recordsPerPage) {
        int offset = (page - 1) * recordsPerPage; // Công thức tính vị trí bắt đầu
        return authorDao.getAuthorsByPage(keyword, offset, recordsPerPage);
    }
}
