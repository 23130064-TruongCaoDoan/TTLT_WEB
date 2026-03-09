package Service;

import DTO.BookWithSoldDTO;
import DTO.RevenueDTO;
import DTO.UserWithTotalSpentDTO;
import dao.ThongKeDao;

import java.util.List;

public class ThongKeService {
    private final ThongKeDao dao = new ThongKeDao();


    public double getTotalRevenue() {
        return dao.getTotalRevenue();
    }

    public List<UserWithTotalSpentDTO> getTop10Users() {
        return dao.getTop10UsersWithTotalSpent();
    }
    public UserWithTotalSpentDTO getTopCustomer() {
        return dao.getTopCustomer().orElse(null);
    }

    public BookWithSoldDTO getBestSeller() {
        return dao.getBestSeller().orElse(null);
    }

    public BookWithSoldDTO getWorstSeller() {
        return dao.getWorstSeller().orElse(null);
    }

    public List<BookWithSoldDTO> getTop10Books() {
        return dao.getTop10Books();
    }
    public List<RevenueDTO> getRevenue(String type) {
        return dao.getRevenue(type);
    }

}
