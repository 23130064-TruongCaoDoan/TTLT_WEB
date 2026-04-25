package Service;

import dao.ReturnRequestDao;
import model.ReturnRequest;

import java.util.List;

public class ReturnRequestService {

    private ReturnRequestDao returnRequestDao = new ReturnRequestDao();

    public boolean insertRequest(int orderId, int userId, String reason, String proofImage) {
        return returnRequestDao.insertRequest(orderId, userId, reason, proofImage);
    }

    public boolean updateStatus(int requestId, String status, String rejectReason) {
        return returnRequestDao.updateStatus(requestId, status, rejectReason);
    }

    public ReturnRequest getById(int id) {
        return returnRequestDao.getById(id);
    }

    public List<ReturnRequest> getAllRequests() {
        return returnRequestDao.getAllRequests();
    }
}