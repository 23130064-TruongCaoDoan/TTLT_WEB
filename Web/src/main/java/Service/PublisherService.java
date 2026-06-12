package Service;

import dao.PublisherDao;
import model.Publisher;
import java.util.List;

public class PublisherService {
    private PublisherDao dao = new PublisherDao();

    public List<Publisher> searchAndPaginate(String query, int limit, int offset) {
        return dao.searchAndPaginate(query, limit, offset);
    }

    public int countSearch(String query) {
        return dao.countSearch(query);
    }

    public boolean isNameExists(String name) {
        return dao.isNameExists(name);
    }

    public Publisher getById(int id) {
        return dao.getById(id);
    }

    public boolean addPublisher(String code, String name, String address, String email, String phone) {
        Publisher p = new Publisher();
        p.setPublisherCode(code);
        p.setName(name);
        p.setAddress(address);
        p.setEmail(email);
        p.setPhone(phone);
        return dao.insertPublisher(p);
    }

    public boolean updatePublisher(Publisher p) {
        return dao.updatePublisher(p);
    }

    public boolean deletePublisher(int id) {
        return dao.deletePublisher(id);
    }
}