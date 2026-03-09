package Service;

import dao.EventDao;
import jakarta.servlet.http.Part;
import model.Book;
import model.Event;

import java.util.List;

public class EventService {
    private EventDao eventDao = new EventDao();
    private UploadService uploadService = new UploadService();

    public Event getEventById(int eventId) {
        return eventDao.getEventById(eventId);
    }

    public List<Event> getListEvent() {
        return eventDao.getListEvent();
    }

    public List<Event> getListEventALl() {
        return eventDao.getListEventALl();
    }

    public boolean deleteEvent(int id) {
        return eventDao.deleteEvent(id);
    }


    public boolean addEvent(String code, Part imagePart, String title, double value, String startDate, String endDate, String typeBookApply, String pulisher, String author, String voucher, String specialVoucher, int minPoint, String age) {
        String coverImgUrl = uploadService.upload(imagePart, "event/");
        BookService bookService = new BookService();
        List<Book> listBookEvent = bookService.filterBooksForEvent(bookService.getAllBooks(), typeBookApply, pulisher, author, age);
        bookService.updateDiscountBook(listBookEvent, value);
        return eventDao.addEvent(code, coverImgUrl, title, value, startDate, endDate, typeBookApply, pulisher, author, voucher, specialVoucher, minPoint, age, listBookEvent);
    }
    public List<Event> getActiveEventsOrderByStartDateAsc() {
        return eventDao.getActiveEventsOrderByStartDateAsc();
    }
    public void updatBookPriceForEvent() {
        BookService bookService = new BookService();
        for (Event event : getActiveEventsOrderByStartDateAsc()) {
            List<Book> listBookEvent = eventDao.getBooksByEventId(event.getId());
            bookService.updateDiscountBook(listBookEvent, event.getValue());
        }
    }



    public boolean existsByCode(String code) {
        return eventDao.existsByCode(code);
    }


    public boolean updateEvent(
            String code,
            Part imagePart,
            String title,
            double value,
            String startDate,
            String endDate,
            String typeBookApply,
            String pulisher,
            String author,
            String voucher,
            String specialVoucher,
            int minPoint,
            String age
    ) {

        Event oldEvent = eventDao.getEventByCode(code);
        if (oldEvent == null) {
            return false;
        }

        String coverImgUrl = oldEvent.getImgUrl();
        if (imagePart != null && imagePart.getSize() > 0) {
            coverImgUrl = uploadService.upload(imagePart, "event/");
        }

        BookService bookService = new BookService();

        List<Book> listBookEvent = bookService.filterBooksForEvent(
                bookService.getAllBooks(),
                typeBookApply,
                pulisher,
                author,
                age
        );

        bookService.updateDiscountBook(listBookEvent, value);

        return eventDao.updateEvent(
                code,
                coverImgUrl,
                title,
                value,
                startDate,
                endDate,
                typeBookApply,
                pulisher,
                author,
                voucher,
                specialVoucher,
                minPoint,
                age,
                listBookEvent
        );
    }

    public List<Event> searchAndFilter(String q, String stock) {
        if (q != null && q.isBlank()) q = null;
        if (stock != null && stock.isBlank()) stock = null;

        return eventDao.searchAndFilter(q, stock);
    }
}
