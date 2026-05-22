package Service;

import DTO.HistoryImportDTO;
import dao.HistoryImportDao;
import model.ImportOrderDetails;

import java.util.List;

public class HistoryImportService {
    private HistoryImportDao hid = new HistoryImportDao();
    public List<HistoryImportDTO> getHistoryImportList(){
        return hid.getHistoryImportList();
    }
    public List<ImportOrderDetails> getImportOrderDetailById(int id){
        return hid.getImportOrderDetails(id);
    }
}
