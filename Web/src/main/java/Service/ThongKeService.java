package Service;

import DTO.BookWithSoldDTO;
import DTO.RevenueDTO;
import DTO.UserWithTotalSpentDTO;
import dao.ThongKeDao;
import model.Book;

import java.time.LocalDate;
import java.util.List;

public class ThongKeService {
    private final ThongKeDao dao = new ThongKeDao();


    public double getTotalRevenue(LocalDate from, LocalDate to) {
        return dao.getTotalRevenue(from, to);
    }
    public double getTotalRevenue(String year) {
        return dao.getTotalRevenue(year);
    }

    public List<UserWithTotalSpentDTO> getTop10Users(LocalDate from, LocalDate to) {
        return dao.getTop10UsersWithTotalSpent(from, to);
    }
    public List<UserWithTotalSpentDTO> getTop10Users(String year) {
        return dao.getTop10UsersWithTotalSpent(year);
    }
    public UserWithTotalSpentDTO getTopCustomer(LocalDate from, LocalDate to) {
        return dao.getTopCustomer(from,to).orElse(null);
    }
    public UserWithTotalSpentDTO getTopCustomer(String year) {
        return dao.getTopCustomer(year).orElse(null);
    }

    public BookWithSoldDTO getBestSeller(LocalDate from, LocalDate to) {
        return dao.getBestSeller(from,to).orElse(null);
    }
    public BookWithSoldDTO getBestSeller(String year) {
        return dao.getBestSeller(year).orElse(null);
    }

    public BookWithSoldDTO getWorstSeller(LocalDate from, LocalDate to) {
        return dao.getWorstSeller(from, to).orElse(null);
    }
    public BookWithSoldDTO getWorstSeller(String year) {
        return dao.getWorstSeller(year).orElse(null);
    }


    public List<BookWithSoldDTO> getTop10Books(LocalDate from, LocalDate to) {
        return dao.getTop10Books(from,to);
    }
    public List<BookWithSoldDTO> getTop10Books(String year) {
        return dao.getTop10Books(year);
    }
    public List<RevenueDTO> getRevenueChart(LocalDate from, LocalDate to) {
        return dao.getRevenueChart(from,to);
    }
    public List<RevenueDTO> getRevenueChart(String year) {
        return dao.getRevenueChart(year);
    }
    public List<String> getListYear(){
        return dao.listYears();
    }
    public int getTotalSoldProducts(LocalDate from, LocalDate to) {
        return  dao.getTotalSoldProducts(from, to);
    }
    public int getTotalSoldProductsAllTime() {
        return dao.getTotalSoldProductsAllTime();
    }
    public int getTotalSoldProducts(String year) {
        return dao.getTotalSoldProducts(year);
    }
    public int getTotalStock() {
        return dao.getTotalStock();
    }
    public  int getOutOfStockCount() {
        return dao.getOutOfStockCount();
    }
    public List<Book> getOutOfStockBooks() {
        return dao.getOutOfStockBooks();
    }
}
