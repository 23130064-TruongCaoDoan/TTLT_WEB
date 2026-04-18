package dao;

import model.Voucher;

import java.time.LocalDate;
import java.util.List;

public class VoucherDao extends BaseDao {
    public List<Voucher> getVoucherList() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM VOUCHER").mapToBean(Voucher.class).list()
        );
    }

    public boolean addVoucher(String code, String description, int conditionPrice, String conditionBook, String conditionPublisher, String start_date, String end_date, int usage_limit, double valuee, String type) {
        int i = getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO VOUCHER(code,description,conditionPrice,conditionBook,conditionPublisher,start_date,end_date, usage_limit, valuee,type) values(:code,:description,:conditionPrice,:conditionBook,:conditionPublisher,:start_date,:end_date,:usage_limit,:valuee,:type)")
                        .bind("code", code)
                        .bind("description", description)
                        .bind("conditionPrice", conditionPrice)
                        .bind("conditionBook", conditionBook)
                        .bind("conditionPublisher", conditionPublisher)
                        .bind("start_date", start_date)
                        .bind("end_date", end_date)
                        .bind("usage_limit", usage_limit)
                        .bind("valuee", valuee)
                        .bind("type", type)
                        .execute()
        );
        if (i > 0) {
            return true;
        }
        return false;
    }

//    public List<Voucher> getListVoucherSortTime(String type) {
//        return getJdbi().withHandle(handle ->
//                handle.createQuery("SELECT * FROM VOUCHER" +" ORDER BY created_at "+type).mapToBean(Voucher.class).list()
//        );
//    }
//
//    public List<Voucher> getListVoucherSortType(String type) {
//        return getJdbi().withHandle(handle ->
//                handle.createQuery("SELECT * FROM VOUCHER where type like :type ORDER BY created_at DESC").bind("type",type).mapToBean(Voucher.class).list()
//        );
//    }

    public List<Voucher> filterVoucher(String keyword, String type, String time) {
        StringBuilder sql = new StringBuilder("SELECT * FROM voucher WHERE 1=1 ");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (code LIKE :kw OR description LIKE :kw OR type LIKE :kw)");
        }

        if (type != null && !type.isEmpty()) {
            sql.append(" AND type = :type");
        }

        if ("new".equals(time)) {
            sql.append(" ORDER BY start_date DESC");
        } else if ("old".equals(time)) {
            sql.append(" ORDER BY start_date ASC");
        }

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.bind("kw", "%" + keyword + "%");
            }
            if (type != null && !type.isEmpty()) {
                query.bind("type", type);
            }

            return query.mapToBean(Voucher.class).list();
        });
    }

    public boolean deleteVoucher(int id) {
        return getJdbi().inTransaction(handle -> {

            // Có thì xóa, không có thì thôi
            handle.createUpdate(
                    "DELETE FROM voucher_user WHERE voucher_id = :id"
            ).bind("id", id).execute();

            int count = handle.createUpdate(
                    "DELETE FROM voucher WHERE id = :id"
            ).bind("id", id).execute();

            return count > 0;
        });
    }

    public boolean updateVoucher(int id, String code, String description, int conditionPrice, String conditionBook, String conditionPublisher, String startDate, String endDate, int usageLimit, double valuee, String type) {
        int i = getJdbi().withHandle(handle ->
                handle.createUpdate(
                                "UPDATE voucher SET " +
                                        "code = :code, " +
                                        "description = :description, " +
                                        "conditionPrice = :conditionPrice, " +
                                        "conditionBook = :conditionBook, " +
                                        "conditionPublisher = :conditionPublisher, " +
                                        "start_date = :start_date, " +
                                        "end_date = :end_date, " +
                                        "usage_limit = :usage_limit, " +
                                        "valuee = :valuee, " +
                                        "type = :type " +
                                        "WHERE id = :id"
                        )
                        .bind("id", id)
                        .bind("code", code)
                        .bind("description", description)
                        .bind("conditionPrice", conditionPrice)
                        .bind("conditionBook", conditionBook)
                        .bind("conditionPublisher", conditionPublisher)
                        .bind("start_date", startDate)
                        .bind("end_date", endDate)
                        .bind("usage_limit", usageLimit)
                        .bind("valuee", valuee)
                        .bind("type", type)
                        .execute()
        );
        if (i > 0) {
            return true;
        }
        return false;
    }

    public void insertVoucherForAll(int voucher_id, List<Integer> userIds) {

        String sql = "INSERT IGNORE INTO voucher_user (user_id, voucher_id) VALUES (:userId, :voucher_id)";
        getJdbi().useHandle(handle -> {
            var batch = handle.prepareBatch(sql);

            for (Integer id : userIds) {
                batch
                        .bind("userId", id)
                        .bind("voucher_id", voucher_id)
                        .add();
            }

            batch.execute();
        });
    }

    public void insertVoucherForUsers(int voucher_id, List<Integer> userIds) {

        String sql = """
                INSERT IGNORE INTO voucher_user (user_id, voucher_id)
                VALUES (:userId, :voucher_id)
                """;

        getJdbi().useHandle(handle -> {
            var batch = handle.prepareBatch(sql);

            for (Integer id : userIds) {
                batch.bind("userId", id)
                        .bind("voucher_id", voucher_id)
                        .add();
            }

            batch.execute();
        });
    }

    public int getVoucherIdByCode(String code) {
        String sql = """
                SELECT id
                FROM voucher
                WHERE code = :code
                LIMIT 1
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("code", code)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElseThrow(() ->
                                new IllegalArgumentException("Voucher không tồn tại: " + code)
                        )
        );
    }

    public List<Voucher> listVoucherDiscountUser(int id) {
        LocalDate today = LocalDate.now();

        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT v.* FROM voucher v
                                INNER JOIN voucher_user vu ON v.id = vu.voucher_id
                                WHERE 
                                  type='discount' 
                                  AND vu.user_id = :userId
                                  AND v.start_date <= :today
                                  AND v.end_date >= :today
                                  AND (v.usage_limit IS NULL OR v.usage_limit > 0)
                                ORDER BY v.end_date ASC, v.valuee DESC
                                """)
                        .bind("userId", id)
                        .bind("today", today)
                        .mapToBean(Voucher.class)
                        .list()
        );

    }

    public List<Voucher> listVoucherShipUser(int id) {
        LocalDate today = LocalDate.now();

        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT v.* FROM voucher v
                                INNER JOIN voucher_user vu ON v.id = vu.voucher_id
                                WHERE  type='ship'
                                  AND vu.user_id = :userId
                                  AND v.start_date <= :today
                                  AND v.end_date >= :today
                                  AND (v.usage_limit IS NULL OR v.usage_limit > 0)
                                ORDER BY v.end_date ASC, v.valuee DESC
                                """)
                        .bind("userId", id)
                        .bind("today", today)
                        .mapToBean(Voucher.class)
                        .list()
        );
    }

    public boolean voucherExists(String code) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                    SELECT COUNT(*) FROM voucher WHERE code = :code
                                """)
                        .bind("code", code)
                        .mapTo(int.class)
                        .one()
        ) > 0;
    }

    public void tangVoucher(List<Integer> listU, String voucherParam) {
        if (listU == null || listU.isEmpty()) return;

        getJdbi().useHandle(handle -> {
            Integer voucherId = handle.createQuery(
                            "SELECT id FROM voucher WHERE code = :code"
                    )
                    .bind("code", voucherParam)
                    .mapTo(Integer.class)
                    .one();

            org.jdbi.v3.core.statement.PreparedBatch batch =
                    handle.prepareBatch(
                            "INSERT INTO voucher_user(user_id, voucher_id) VALUES (:user_id, :voucher_id)"
                    );

            for (Integer userId : listU) {
                batch.bind("user_id", userId)
                        .bind("voucher_id", voucherId)
                        .add();
            }

            batch.execute();
        });
    }

    public int countValidVouchers() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM voucher WHERE usage_limit > 0 AND end_date >= CURDATE()")
                        .mapTo(Integer.class)
                        .one()
        );
    }

}
