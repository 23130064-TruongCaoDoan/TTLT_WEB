package Service;

import DTO.HistoryImportDTO;
import dao.HistoryImportDao;

import java.util.List;

public class HistoryImportService {
    private HistoryImportDao hid = new HistoryImportDao();
    public List<HistoryImportDTO> getHistoryImportList(){
        return hid.getHistoryImportList();
    }
}
