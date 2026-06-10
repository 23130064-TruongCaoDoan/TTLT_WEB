package dao;

import DTO.UserWithTotalSpentDTO;
import model.Role;
import model.User;
import org.jdbi.v3.core.result.ResultIterable;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    public void addUser(int id,String fullname) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("insert into USER(id,name,role,status, point) values(:id,:username,:role,:status,:point)").bind("id",id).bind("username", fullname).bind("role",0).bind("status",1).bind("point",0).execute()
        );
    }

    public void updatePass(String email,String password) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("update `user` set password_hash=:password where email = :email").bind("password", password).bind("email",email).execute()
        );
    }

    public int checkRole(String email) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT role FROM USER where email=:email")
                        .bind("email", email)
                        .mapTo(Integer.class).one()
        );
    }

    public List<UserWithTotalSpentDTO> getUserWithTotalSpent(String q, String sortStock, String roleFilter, String statusFilter) {
        StringBuilder sql = new StringBuilder("""
            SELECT u.id, u.name, u.email, u.point, u.role, COALESCE(SUM(o.total_amount), 0) AS total_spent, u.status 
            FROM user u 
            LEFT JOIN orders o ON u.id = o.user_id 
            WHERE 1=1 
        """);

        if (q != null && !q.trim().isEmpty()) {
            sql.append(" AND (u.name LIKE :q OR u.email LIKE :q) ");
        }

        if (roleFilter != null && !roleFilter.isEmpty()) {
            sql.append(" AND u.role = :roleFilter ");
        }

        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND u.status = :statusFilter ");
        }

        sql.append(" GROUP BY u.id, u.name, u.email, u.point, u.role, u.status ");

        if (sortStock != null && !sortStock.isEmpty()) {
            switch (sortStock) {
                case "pAsc": sql.append(" ORDER BY u.point ASC "); break;
                case "pDesc": sql.append(" ORDER BY u.point DESC "); break;
                case "mAsc": sql.append(" ORDER BY total_spent ASC "); break;
                case "mDesc": sql.append(" ORDER BY total_spent DESC "); break;
                default: sql.append(" ORDER BY u.name ASC "); break;
            }
        } else {
            sql.append(" ORDER BY u.name ASC ");
        }

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());

            if (q != null && !q.trim().isEmpty()) {
                query.bind("q", "%" + q.trim() + "%");
            }
            if (roleFilter != null && !roleFilter.isEmpty()) {
                query.bind("roleFilter", Integer.parseInt(roleFilter));
            }
            if (statusFilter != null && !statusFilter.isEmpty()) {
                query.bind("statusFilter", Integer.parseInt(statusFilter));
            }

            return query.mapToBean(UserWithTotalSpentDTO.class).list();
        });
    }

    public User findUserById(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT u.*, r.role_name
                                From USER u
                                INNER JOIN ROLES r ON r.id = u.role
                                WHERE u.id=:id
                                """)
                        .bind("id", id)
                        .mapToBean(User.class)
                        .findFirst()
                        .orElse(null)
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
    public void updateProfile(int id, String name, String phone, String email, boolean sex, LocalDate birthday, String avatar) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE user SET name = :name, phone=:phone, email=:email,sex=:sex, birthday=:birthday, avatar=:avatar WHERE id = :id")
                .bind("id", id)
                .bind("name", name)
                .bind("phone", phone)
                .bind("email", email)
                .bind("sex", sex)
                .bind("birthday", birthday)
                .bind("avatar", avatar)
                .execute()
                );
    }
    public boolean updateEmail(int id, String email) {
       return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE user SET email=:email WHERE id = :id")
                        .bind("id", id)
                        .bind("email", email)
                        .execute()>0

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
                handle.createUpdate("update `user` set point =:pointUsed where id=:id")
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

    public boolean updateRole(int userId, int  role) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE user SET role = :role WHERE id = :id")
                        .bind("role", role)
                        .bind("id", userId)
                        .execute()>0
        );
    }
    public boolean updateStatus(int userId, boolean  status) {
        String sql = "UPDATE user SET status = :status WHERE id = :id";

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("id", userId)
                        .execute()>0
        );
    }

    public void addUserByAdmin(String fullname, String email, String password, int role, int status) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("insert into USER(name, email, password_hash, role, status, point) values(:username, :email, :password, :role, :status, :point)")
                        .bind("username", fullname)
                        .bind("email", email)
                        .bind("password", password)
                        .bind("role", role)
                        .bind("status", status)
                        .bind("point", 0)
                        .execute()
        );
    }

    public void updateAvatar(Integer userId, String avatarUrl) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE user SET avatar=:avatar WHERE id = :id").bind("avatar", avatarUrl).bind("id",userId).execute()
        );
    }

    public boolean updateName(int userId, String value) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE user SET name= :name  WHERE id= :id")
                        .bind("name",value)
                        .bind("id",userId)
                        .execute()>0
                );
    }

    public boolean updatePhone(int userId, String value) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE user SET phone= :phone  WHERE id= :id")
                        .bind("phone",value)
                        .bind("id",userId)
                        .execute()>0
        );
    }

    public boolean updateBirthDay(int userId, String value) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE user SET birthday= :birthday  WHERE id= :id")
                        .bind("birthday",value)
                        .bind("id",userId)
                        .execute()>0
        );
    }
     public List<Role> getAllRoles() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM ROLES")
                        .mapToBean(Role.class)
                        .list()
        );
     }
     public List<User> getAllEmployeeImportProduct() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT id, name
                        FROM USER
                        WHERE role IN (1,2,4)
                        """)
                        .mapToBean(User.class)
                        .list()
        );
     }

    public Timestamp getBlockUntil(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT order_block_until FROM user WHERE id=:id")
                        .bind("id",id)
                        .mapTo(Timestamp.class).one()
                );
    }
}
