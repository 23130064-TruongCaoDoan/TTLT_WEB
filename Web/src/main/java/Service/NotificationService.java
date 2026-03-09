package Service;

import dao.NotificationDAO;
import model.Notification;

import java.util.List;

public class NotificationService {

    private NotificationDAO notificationDAO = new NotificationDAO();

    public void sendToUsers(String title, String content, String[] userCodes) {

        if (userCodes == null || userCodes.length == 0) {
            throw new IllegalArgumentException("No user selected");
        }

        for (String code : userCodes) {
            Notification n = new Notification();
            int id = Integer.parseInt(code);
            n.setUserId(id);
            n.setTitle(title);
            n.setNoti(content);

            notificationDAO.insert(n);
        }
    }
    public void sendNoti(int userId, String title, String content) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setTitle(title);
        n.setNoti(content);
        notificationDAO.insert(n);
    }

    public List<Notification> getUserNotifications(int userId) {
        return notificationDAO.findByUser(userId);
    }
}

