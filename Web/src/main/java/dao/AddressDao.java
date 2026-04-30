package dao;

import model.Address;
import model.Book;

import java.util.List;

public class AddressDao extends BaseDao{
    public void insertAddress(Address address) {
        getJdbi().useHandle(handle -> {
            handle.createUpdate(
                            "INSERT INTO ADDRESS (user_id, name, phone, city, districts, ward, specificAddress, is_default) " +
                                    "VALUES (:userId, :name, :phone, :city,:districts,:ward, :specificAddress, :isDefault)"
                    )
                    .bind("userId", address.getUserId())
                    .bind("name", address.getName())
                    .bind("phone", address.getPhone())
                    .bind("city", address.getCity())
                    .bind("districts", address.getDistricts())
                    .bind("ward", address.getWard())
                    .bind("specificAddress", address.getSpecificAddress())
                    .bind("isDefault", address.getIsDefault() ? 1 : 0)
                    .execute();
        });
    }

    public List<Address> getAddress(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM ADDRESS WHERE user_id = :userId ORDER BY ID")
                        .bind("userId", userId)
                        .mapToBean(Address.class)
                        .list()

        );
    }
    public Address getAddressById(int id) {
        return  getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM ADDRESS WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Address.class)
                        .findOne()
                        .orElse(null)
        );
    }
    public void deleteAddress(int id) {
        getJdbi().useHandle(handle -> {
            handle.createUpdate("DELETE FROM ADDRESS WHERE id = :id")
                    .bind("id", id)
                    .execute();
        });
    }
    public void updateAddress(Address address) {
        getJdbi().useHandle(handle -> {
           handle.createUpdate(" UPDATE address SET name= :name, phone= :phone, city= :city,districts=:districts, ward= :ward, specificAddress= :specificAddress, is_default= :isDefault WHERE id = :id")
                   .bind("id", address.getId())
                   .bind("name", address.getName())
                   .bind("phone", address.getPhone())
                   .bind("city", address.getCity())
                   .bind("districts", address.getDistricts())
                   .bind("ward", address.getWard())
                   .bind("specificAddress", address.getSpecificAddress())
                   .bind("isDefault", address.getIsDefault() ? 1 : 0)
                   .execute();
        });
    }
    public boolean isDefaultAddress(int addressId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                            SELECT 1
                            FROM address
                            WHERE id = :id AND is_default = 1
                            LIMIT 1
                        """)
                        .bind("id", addressId)
                        .mapTo(Integer.class)
                        .findFirst()
                        .isPresent()
        );
    }
    public void setDefaultAddress(int addressId, int userId) {
        resetDefaultAddress(userId);
        getJdbi().useHandle(handle -> {
           handle.createUpdate("UPDATE address SET is_default=1 WHERE id = :id")
                   .bind("id", addressId)
                    .execute();
        });
    }
    public void resetDefaultAddress(int userId) {
        getJdbi().useHandle(handle -> {
            handle.createUpdate("UPDATE address SET is_default=0 WHERE user_id = :userId")
            .bind("userId", userId)
             .execute();

        });
    }

    public static void main(String[] args) {
        AddressDao dao = new AddressDao();
        Address  address = new Address();
        System.out.println(dao.getAddress(1));
        List<Address> addresses = dao.getAddressOfUser(36);
        for (Address address1 : addresses) {
            System.out.println(address1);
        }

    }

    public List<Address> getAddressOfUser(int id) {
        return getJdbi().withHandle(handle ->
            handle.createQuery("SELECT * FROM ADDRESS WHERE user_id = :id")
                    .bind("id", id)
                    .mapToBean(Address.class).list()
        );
    }

    public void deleteAddressOfUser(int uid, int id) {
        getJdbi().useHandle(handle -> {
           handle.createUpdate("DELETE FROM ADDRESS WHERE id = :id AND user_id = :userId")
                   .bind("id", id)
                   .bind("userId", uid)
                   .execute();
        });
    }
}
