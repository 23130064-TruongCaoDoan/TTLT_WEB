package dao;

import DTO.HistoryImportDTO;

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

    public static void main(String[] args) {
       HistoryImportDao historyImportDao = new HistoryImportDao();
        System.out.println(historyImportDao.getHistoryImportList());
    }
}
