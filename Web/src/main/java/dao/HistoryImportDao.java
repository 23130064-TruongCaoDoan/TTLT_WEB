package dao;

import DTO.HistoryImportDTO;
import model.ImportOrderDetails;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Query;

import java.time.LocalDate;
import java.util.List;

public class HistoryImportDao extends BaseDao{
    public List<HistoryImportDTO> getHistoryImportList(){
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT o.*, u.name as employeeName
                        FROM IMPORT_ORDERS o
                        INNER JOIN USER u ON o.employee_id_import = u.id
                        """)
                        .mapToBean(HistoryImportDTO.class)
                        .list()

        );
    }
    public List<ImportOrderDetails> getImportOrderDetails(int id){
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT od.id, od.import_order_id, od.book_id, b.title, b.cover_img_url, od.quantity, od.price_import, od.subtotal
                        FROM IMPORT_ORDERS o
                        INNER JOIN IMPORT_ORDER_DETAILS od on o.id = od.import_order_id
                        INNER JOIN BOOKS b ON od.book_id = b.id
                        WHERE o.id = :id
                        """)
                        .bind("id",id)
                        .mapToBean(ImportOrderDetails.class)
                        .list()
        );
    }
    public List<HistoryImportDTO> getHistoryImportDTOSearch(String importId, String employeeId, String fromDate, String toDate){
        StringBuilder sql = new StringBuilder("""
                SELECT o.*, u.name as employeeName
                FROM IMPORT_ORDERS o
                INNER JOIN USER u ON o.employee_id_import = u.id
                WHERE 1=1
                """);
        if(importId!=null && !importId.isBlank()) {
            sql.append(" AND o.id = :importId");
        }
        if(employeeId!=null && !employeeId.isBlank()) {
            sql.append(" AND o.employee_id_import = :employeeId");
        }
        if(fromDate!=null && !fromDate.isBlank()) {
            sql.append(" AND DATE(o.import_date) >= :fromDate");
        }
        if(toDate!=null && !toDate.isBlank()) {
            sql.append(" AND DATE(o.import_date) <= :toDate");
        }
        sql.append(" ORDER BY o.import_date DESC");
        try (Handle handle = getJdbi().open()) {
            Query query = handle.createQuery(sql.toString());
            if (importId != null && !importId.isBlank()) {
                query.bind("importId", importId);
            }

            if (employeeId != null && !employeeId.isBlank()) {
                query.bind("employeeId", employeeId);
            }

            if (fromDate != null && !fromDate.isBlank()) {
                query.bind("fromDate", fromDate);
            }

            if (toDate != null && !toDate.isBlank()) {
                query.bind("toDate", toDate);
            }

            return query.mapToBean(HistoryImportDTO.class).list();
        }
    }

    public static void main(String[] args) {
       HistoryImportDao historyImportDao = new HistoryImportDao();
        System.out.println(historyImportDao.getHistoryImportDTOSearch("5","","",""));

    }
}
