package dao;

import DTO.HistoryImportDTO;
import model.ImportOrderDetails;

import java.util.List;

public class HistoryImportDao extends BaseDao{
    public List<HistoryImportDTO> getHistoryImportList(){
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT o.id, o.provider, o.import_date , o.total_amount, o.note, u.name as employeeName
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

    public static void main(String[] args) {
       HistoryImportDao historyImportDao = new HistoryImportDao();

    }
}
