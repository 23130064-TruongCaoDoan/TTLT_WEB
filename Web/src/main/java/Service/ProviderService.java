package Service;

import dao.ProviderDao;
import model.Provider;
import java.util.List;

public class ProviderService {
    private final ProviderDao providerDao = new ProviderDao();

    public List<Provider> getProviders(String q, int limit, int offset) {
        return providerDao.getProvidersPaginated(q, limit, offset);
    }

    public int countTotal(String q) {
        return providerDao.countTotal(q);
    }

    public boolean addProvider(Provider provider) {
        if (providerDao.existsByCode(provider.getCode())) {
            return false;
        }
        return providerDao.insert(provider);
    }

    public boolean updateProviderSelective(Provider provider) {
        return providerDao.updateSelective(provider);
    }

    public boolean deleteProvider(int id) {
        return providerDao.deleteSoft(id);
    }

    public Provider getById(int id) {
        return providerDao.getById(id);
    }

    public boolean existsByCode(String code) {
        return providerDao.existsByCode(code);
    }
}