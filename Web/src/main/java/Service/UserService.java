package Service;

import DTO.UserWithTotalSpentDTO;
import Util.EmailSender;
import Util.PasswordUtil;
import dao.UserDao;
import model.User;
import org.springframework.cglib.core.Local;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserDao userDao = new UserDao();

    public User findUser(String user) {
        return userDao.finduser(user);
    }



    public boolean checkPass(User user, String password) {
        PasswordUtil passwordUtil = new PasswordUtil();
        return passwordUtil.checkPassword(password, user.getPassword_hash());
    }

    public boolean checkExit(String email) {

        if (findUser(email) != null) {
            return true;
        }
        return false;

    }

    public void addUser(String fullname, String email, String password) {
        userDao.addUser(fullname, email, password);
    }
    public void addUser(String fullname, String email) {
        userDao.addUser(fullname, email);
    }
    public void addUser(int id,String fullname) {
        userDao.addUser(id,fullname);
    }

    public void updatePass(String email, String password) {
        if (checkExit(email)) {
            userDao.updatePass(email,password);
        }
    }

    public boolean checkRole(User user) {
        return userDao.checkRole(user.getEmail());
    }

    public boolean isValidPassword(String password) {
        String regex = "^(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(regex);
    }
    public List<Integer> getAllUserIds(){
        List<User> ls = userDao.getListUser();
        List<Integer> rs = new ArrayList<>();
        for (User u : ls){
            rs.add(u.getId());
        }
        return rs;
    }
    public User getUserById(int id){
        return userDao.findUserById(id);
    }
    public List<UserWithTotalSpentDTO> getUserWithTotalSpent(String q, String stock) {
        return userDao.getUserWithTotalSpent(q,stock);
    }

    public boolean existsById(int id) {
        return userDao.existsById(id);
    }

    public List<Integer> getUserPoint(int minPoint) {
        return userDao.getUserIdsByMinPoint(minPoint);
    }
    public void updateProfile(int id, String name, String phone, String email,boolean sex, LocalDate birthday, String avatar) {
        userDao.updateProfile(id, name, phone, email, sex, birthday, avatar);
    }
    public boolean updateEmail(int id, String email) {
        return userDao.updateEmail(id, email);
    }

    public void tichDiem(int userId, double finalTotal) {
        userDao.tichDiem(userId,finalTotal);
    }

    public void updateDiem(int userId, int pointUsed) {
        userDao.updateDiem(userId,pointUsed);
    }

    public boolean changePassword(int id, String newPassword) {
        return userDao.changePassword(id, newPassword);
    }

    public boolean updateRole(int userId, boolean role) {
        return userDao.updateRole(userId, role);
    }
    public boolean updateStatus(int userId, boolean status) {
        EmailSender  emailSender = new EmailSender();
        String email = userDao.findUserById(userId).getEmail();
        String message="";
        if (status) {
            message="Tài khoản của bạn đã được mở";
        }
        else{
            message="Tài khoản của bạn đã bị khóa";
        }
        emailSender.sendSimpleEmail(email,"THÔNG BÁO TRẠNG THÁI HOẠT ĐỘNG CỦA TÀI KHOẢN",message);
        return userDao.updateStatus(userId, status);
    }
    public void addUserByAdmin(String fullname, String email, String password, int role, int status) {
        userDao.addUserByAdmin(fullname, email, password, role, status);
    }

    public void updateAvatar(Integer userId, String avatarUrl) {
        userDao.updateAvatar(userId,avatarUrl);
    }

    public boolean updateName(int userId, String value) {
        return userDao.updateName(userId,value);
    }

    public boolean updatePhone(int userId, String value) {
        return userDao.updatePhone(userId,value);
    }

    public boolean updateBirthDay(int userId, String value) {
        return userDao.updateBirthDay(userId,value);
    }

    public boolean checkExitByID(int id) {
        if (findUserByID(id) != null) {
            return true;
        }
        return false;

    }

    public User findUserByID(int id) {
        return userDao.findUserById(id);
    }
}
