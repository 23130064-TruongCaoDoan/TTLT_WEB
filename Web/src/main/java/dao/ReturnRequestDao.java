package dao;
import model.ReturnRequest;
import java.util.List;

public class ReturnRequestDao extends BaseDao{
    public boolean insertRequest(int orderId, int userId, String reason, String proofImage) {
        try {
            return getJdbi().withHandle(handle -> handle.createUpdate(
                            "INSERT INTO return_requests (order_id, user_id, reason, proof_image, status) " +
                                    "VALUES (:orderId, :userId, :reason, :proofImage, 'PENDING')"
                    )
                    .bind("orderId", orderId)
                    .bind("userId", userId)
                    .bind("reason", reason)
                    .bind("proofImage", proofImage)
                    .execute() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(int requestId, String status, String rejectReason) {
        return getJdbi().withHandle(handle -> handle.createUpdate(
                        "UPDATE return_requests SET status = :status, reject_reason = :rejectReason WHERE id = :id"
                )
                .bind("status", status)
                .bind("rejectReason", rejectReason)
                .bind("id", requestId)
                .execute() > 0);
    }

    public ReturnRequest getById(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("SELECT * FROM return_requests WHERE id = :id")
                .bind("id", id)
                .mapToBean(ReturnRequest.class)
                .findOne().orElse(null));
    }

    public List<ReturnRequest> getAllRequests() {
        return getJdbi().withHandle(handle -> handle.createQuery("SELECT * FROM return_requests ORDER BY created_at DESC")
                .mapToBean(ReturnRequest.class)
                .list());
    }
}
