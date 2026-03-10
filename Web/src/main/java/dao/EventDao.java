package dao;

import jakarta.servlet.http.Part;
import model.Book;
import model.Event;

import java.util.List;

public class EventDao extends BaseDao {
    public Event getEventById(int eventId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM events WHERE id = :id")
                        .bind("id", eventId)
                        .mapToBean(Event.class)
                        .findOne()
                        .orElse(null)
        );
    }


    public List<Event> getListEvent() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM events WHERE start_date <= NOW() AND end_date >= NOW() ORDER BY start_date DESC")
                        .mapToBean(Event.class)
                        .list()
        );
    }
    public List<Event> getActiveEventsOrderByStartDateAsc() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                SELECT *
                FROM events
                WHERE start_date <= NOW()
                  AND end_date >= NOW()
                ORDER BY start_date ASC
            """)
                        .mapToBean(Event.class)
                        .list()
        );
    }
    public List<Book> getBooksByEventId(int eventId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                SELECT b.*
                FROM books b
                JOIN event_books eb ON b.id = eb.book_id
                WHERE eb.event_id = :eventId
            """)
                        .bind("eventId", eventId)
                        .mapToBean(Book.class)
                        .list()
        );
    }




    public List<Event> getListEventALl() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM events ORDER BY start_date DESC")
                        .mapToBean(Event.class)
                        .list()
        );
    }



    public boolean deleteEvent(int id) {
        return getJdbi().inTransaction(handle -> {
            int count1 = handle.createUpdate(
                    "DELETE FROM event_books WHERE event_id = :id"
            ).bind("id", id).execute();

            int count = handle.createUpdate(
                    "DELETE FROM events WHERE id = :id"
            ).bind("id", id).execute();

            return count > 0;
        });
    }

    public boolean addEvent(
            String code,
            String coverImgUrl,
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
            String age,
            List<Book> listBookEvent
    ) {
        return getJdbi().withHandle(handle -> {

            // 1. INSERT EVENT + LẤY event_id
            Integer eventId = handle.createUpdate("""
                                INSERT INTO events(
                                    event_code, img_url, title, value, start_date, end_date,
                                    type_book_apply, pulisher_apply, author_apply,
                                    voucher_code, special_voucher, min_point, age_apply
                                ) VALUES (
                                    :event_code, :img_url, :title, :value, :start_date, :end_date,
                                    :type_book_apply, :pulisher_apply, :author_apply,
                                    :voucher_code, :special_voucher, :min_point, :age_apply
                                )
                            """)
                    .bind("event_code", code)
                    .bind("img_url", coverImgUrl)
                    .bind("title", title)
                    .bind("value", value)
                    .bind("start_date", startDate)
                    .bind("end_date", endDate)
                    .bind("type_book_apply", typeBookApply)
                    .bind("pulisher_apply", pulisher)
                    .bind("author_apply", author)
                    .bind("voucher_code", voucher)
                    .bind("special_voucher", specialVoucher)
                    .bind("min_point", minPoint)
                    .bind("age_apply", age)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();

            if (eventId == null) {
                return false;
            }

            for (Book b : listBookEvent) {
                handle.createUpdate("""
                                    INSERT INTO event_books(event_id, book_id)
                                    VALUES (:event_id, :book_id)
                                """)
                        .bind("event_id", eventId)
                        .bind("book_id", b.getId())
                        .execute();
            }

            return true;
        });
    }


    public boolean existsByCode(String code) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT COUNT(*) FROM events WHERE event_code = :code"
                        )
                        .bind("code", code)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public boolean updateEvent(
            String code,
            String coverImgUrl,
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
            String age,
            List<Book> listBookEvent
    ) {
        return getJdbi().inTransaction(handle -> {

            Integer eventId = handle.createQuery(
                            "SELECT id FROM events WHERE event_code = :code"
                    )
                    .bind("code", code)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);

            if (eventId == null) {
                return false;
            }

            int updated = handle.createUpdate("""
                            UPDATE events SET
                                img_url = :img_url,
                                title = :title,
                                value = :value,
                                start_date = :start_date,
                                end_date = :end_date,
                                type_book_apply = :type_book_apply,
                                pulisher_apply = :pulisher_apply,
                                author_apply = :author_apply,
                                voucher_code = :voucher_code,
                                special_voucher = :special_voucher,
                                min_point = :min_point,
                                age_apply = :age_apply
                            WHERE event_code = :code
                            """)
                    .bind("img_url", coverImgUrl)
                    .bind("title", title)
                    .bind("value", value)
                    .bind("start_date", startDate)
                    .bind("end_date", endDate)
                    .bind("type_book_apply", typeBookApply)
                    .bind("pulisher_apply", pulisher)
                    .bind("author_apply", author)
                    .bind("voucher_code", voucher)
                    .bind("special_voucher", specialVoucher)
                    .bind("min_point", minPoint)
                    .bind("age_apply", age)
                    .bind("code", code)
                    .execute();

            if (updated == 0) {
                return false;
            }

            handle.createUpdate(
                            "DELETE FROM event_books WHERE event_id = :event_id"
                    )
                    .bind("event_id", eventId)
                    .execute();

            for (Book b : listBookEvent) {
                handle.createUpdate("""
                                INSERT INTO event_books(event_id, book_id)
                                VALUES (:event_id, :book_id)
                                """)
                        .bind("event_id", eventId)
                        .bind("book_id", b.getId())
                        .execute();
            }

            return true;
        });
    }

    public Event getEventByCode(String code) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM events WHERE event_code = :code"
                        )
                        .bind("code", code)
                        .mapToBean(Event.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public List<Event> searchAndFilter(String q, String stock) {
        return getJdbi().withHandle(handle -> {

            StringBuilder sql = new StringBuilder(
                    "SELECT * " +
                            "FROM events "
            );

            if (q != null) {
                sql.append(" WHERE (title LIKE :q OR event_code LIKE :q OR type_book_apply LIKE :q OR pulisher_apply LIKE :q  OR age_apply LIKE :q)");
            }
            if ("asc".equals(stock)) {
                sql.append(" ORDER BY end_date ASC");
            } else if ("desc".equals(stock)) {
                sql.append(" ORDER BY end_date DESC");
            } else {
                sql.append(" ORDER BY end_date DESC");
            }
            var query = handle.createQuery(sql.toString());

            if (q != null) {
                query.bind("q", "%" + q + "%");
            }
            return query.mapToBean(Event.class).list();
        });
    }
}
