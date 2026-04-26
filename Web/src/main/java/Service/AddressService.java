package Service;

import dao.AddressDao;
import model.Address;

import java.util.List;

public class AddressService {
    private AddressDao addressDao = new AddressDao();
    public void insertAddress(Address address){
        addressDao.insertAddress(address);
    }
    public List<Address> getAddress(int userId){
        return addressDao.getAddress(userId);
    }
    public void deleteAddress(int id){
        addressDao.deleteAddress(id);
    }
    public void updateAddress(Address address){
        addressDao.updateAddress(address);
    }

    public Address getAddressById(int id) {
        return addressDao.getAddressById(id);
    }

    public static void main(String[] args) {
        AddressService addressService = new AddressService();
        List<Address> listAddress = addressService.getAddress(31);
        for(Address address:addressService.getAddress(31)){
            System.out.println(address.toString());
        }
        System.out.println(listAddress);
    }

    public boolean isDefaultAddress(int id) {
        return addressDao.isDefaultAddress(id);
    }
    public void setAddressDefault(int id,int userId) {
        addressDao.setDefaultAddress(id,userId);
    }

    public List<Address> getAddressOfUser(int id) {
        return addressDao.getAddressOfUser(id);
    }
}
