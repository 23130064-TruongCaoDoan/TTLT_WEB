package dao;

import DTO.UserWithTotalSpentDTO;
import model.User;
import org.jdbi.v3.core.result.ResultIterable;

import java.time.LocalDate;
import java.util.List;

public class UserDao extends BaseDao {
    public void tichDiem(int userId, double finalTotal) {
        getJdbi().withHandle(handle -> (
                handle.createUpdate("update `user` set point= point + :finalTotal where id=:id")
                        .bind("finalTotal",(int)(finalTotal*0.05))
                        .bind("id",userId)
                        .execute()
                ));
    }

    public User finduser(String username) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("select * from USER where phone=:username OR email=:username")
                        .bind("username", username).mapToBean(User.class).findFirst().orElse(null)
        );
    }

    public List<User> getListUser() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM User")
                        .mapToBean(User.class)
                        .list()
        );
    }

    public void addUser(String fullname, String email, String password) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("insert into USER(name,email,password_hash,role,status, point) values(:username, :email, :password,:role,:status,:point)").bind("username", fullname).bind("email", email).bind("password", password).bind("role",0).bind("status",1).bind("point",0).execute()
        );
    }
    public void addUser(String fullname, String email) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("insert into USER(name,email,role,status, point) values(:username, :email,:role,:status,:point)").bind("username", fullname).bind("email", email).bind("role",0).bind("status",1).bind("point",0).execute()
        );
    }

    public void updatePass(String email,String password) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("update `user` set password_hash=:password where email = :email").bind("password", password).bind("email",email).execute()
        );
    }

    public boolean checkRole(String email) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT role FROM USER where email=:email")
                        .bind("email", email)
                        .mapTo(boolean.class).one()
        );
    }

    public List<UserWithTotalSpentDTO> getUserWithTotalSpent(String q, String stock) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT u.id, u.name, u.email, u.point, u.role,  COALESCE(SUM(o.total_amount), 0) AS total_spent, u.status FROM user u LEFT JOIN orders o ON u.id = o.user_id WHERE (:q IS NULL OR u.name LIKE CONCAT('%', :q, '%') OR u.email LIKE CONCAT('%', :q, '%')) GROUP BY u.id, u.name, u.email, u.point ORDER BY CASE WHEN :sort = 'pAsc'  THEN u.point END ASC, CASE WHEN :sort = 'pDesc' THEN u.point END DESC, CASE WHEN :sort = 'mAsc'  THEN total_spent END ASC, CASE WHEN :sort = 'mDesc' THEN total_spent END DESC;")
                        .bind("q",q)
                        .bind("sort",stock)
                        .mapToBean(UserWithTotalSpentDTO.class)
                        .list()
        );
    }

    public User findUserById(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("select * from `USER` where id=:id")
                        .bind("id", id).mapToBean(User.class).findFirst().orElse(null)
        );
    }

    public boolean existsById(int id) {

        String sql = "SELECT 1 FROM user WHERE id = :id LIMIT 1";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", id)
                        .mapTo(Integer.class)
                        .findFirst()
                        .isPresent()
        );
    }
    public void updateProfile(int id, String name, String phone, String email, boolean sex, LocalDate birthday) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE user SET name = :name, phone=:phone, email=:email,sex=:sex, birthday=:birthday WHERE id = :id")
                .bind("id", id)
                .bind("name", name)
                .bind("phone", phone)
                .bind("email", email)
                .bind("sex", sex)
                .bind("birthday", birthday)
                .execute()
                );
    }
    public void updateEmail(int id, String email) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE user SET email=:email WHERE id = :id")
                        .bind("id", id)
                        .bind("email", email)
                        .execute()

        );
    }

    public List<Integer> getUserIdsByMinPoint(int minPoint) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SElECT id FROM `user` where point >= :minpoint")
                        .bind("minpoint",minPoint)
                        .mapTo(Integer.class)
                        .list()
        );
    }

    public void updateDiem(int userId, double pointUsed) {
        getJdbi().withHandle(handle -> (
                handle.createUpdate("update `user` set point= point - :pointUsed where id=:id")
                        .bind("pointUsed",(pointUsed))
                        .bind("id",userId)
                        .execute()
        ));
    }

    public boolean changePassword(int id, String newPassword) {
        return getJdbi().withHandle(handle ->(
                handle.createUpdate("UPDATE `USER` set password_hash=:newPassword WHERE id=:id")
                        .bind("newPassword",newPassword)
                        .bind("id",id)
                        .execute()>0
                ));
    }

    public boolean checkPassword(int id, String oldPassword) {
        return getJdbi().withHandle(handle ->(
                handle.createQuery("SELECT Count(*) from `user` where id=:id and password_hash=:oldPassword ")
                        .bind("id",id)
                        .bind("oldPassword",oldPassword)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(0) > 0
                ));
    }

    public void updateRole(int userId, boolean  role) {
        String sql = "UPDATE user SET role = :role WHERE id = :id";

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("role", role)
                        .bind("id", userId)
                        .execute()
        );
    }
    public void updateStatus(int userId, boolean  status) {
        String sql = "UPDATE user SET status = :status WHERE id = :id";

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("id", userId)
                        .execute()
        );
    }
}
