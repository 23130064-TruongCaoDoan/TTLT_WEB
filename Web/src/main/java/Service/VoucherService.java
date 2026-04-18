package Service;

import Cart.Cart;
import Cart.CartItem;
import dao.VoucherDao;
import model.Book;
import model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class VoucherService {
    private VoucherDao voucherDao = new VoucherDao();
    private UserService userService = new UserService();

    public boolean addVoucher(String code, String description, int conditionPrice, String conditionBook, String conditionPublisher, String start_date, String end_date, int usage, double value, String type) {
        return voucherDao.addVoucher(code, description, conditionPrice, conditionBook, conditionPublisher, start_date, end_date, usage, value, type);
    }

    public List<Voucher> getListVoucher() {
        return voucherDao.getVoucherList();
    }

    //    public List<Voucher> getListVoucherSortTime(String type) {
//        return voucherDao.getListVoucherSortTime(type);
//    }
//
//    public static void main(String[] args) {
//        VoucherDao voucherDao = new VoucherDao();
//        for (Voucher voucher : voucherDao.listVoucherDiscountUser(31)) {
//            System.out.println(voucher.toString());
//        }
//        System.out.println(voucherDao.deleteVoucher(5));
//    }
//
//    public List<Voucher> getListVoucherSortType(String type) {
//        return voucherDao.getListVoucherSortType(type);
//    }

    public List<Voucher> filterVoucher(String keyword, String type, String time) {
        return voucherDao.filterVoucher(keyword, type, time);
    }

    public boolean deleteVoucher(int id) {
        return voucherDao.deleteVoucher(id);
    }

    public boolean updateVoucher(int id, String code, String description, int conditionPrice, String conditionBook, String conditionPublisher, String startDate, String endDate, int usageLimit, double valuee, String type) {
        return voucherDao.updateVoucher(id, code, description, conditionPrice, conditionBook, conditionPublisher, startDate, endDate, usageLimit, valuee, type);
    }

    public boolean isValidVoucher(String code) {
        List<Voucher> vouchers = voucherDao.getVoucherList();
        for (Voucher v : vouchers) {
            if (v.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public void insertVoucherForAll(String code) {
        if (!isValidVoucher(code)) return;
        List<Integer> listId = userService.getAllUserIds();
        int voucherId = voucherDao.getVoucherIdByCode(code);
        voucherDao.insertVoucherForAll(voucherId, listId);
    }

    public void insertVoucherForUsers(String code, String userIdsRaw) {

        if (!isValidVoucher(code)) return;
        if (userIdsRaw == null || userIdsRaw.isBlank()) return;

        List<Integer> userIds = new ArrayList<>();

        for (String s : userIdsRaw.split(",")) {
            s = s.trim();
            if (!s.matches("KH\\d+")) {
                continue;
            }
            int id = Integer.parseInt(s.substring(2));
            if (userService.existsById(id)) {
                userIds.add(id);
            }
        }


        if (!userIds.isEmpty()) {
            int voucherId = voucherDao.getVoucherIdByCode(code);
            voucherDao.insertVoucherForUsers(voucherId, userIds);
        }
    }

    public List<Voucher> listVoucherDiscountUser(int id) {
        return voucherDao.listVoucherDiscountUser(id);
    }

    public List<Voucher> listVoucherShipUser(int id) {
        return voucherDao.listVoucherShipUser(id);
    }

    public List<Voucher> filterVoucherValid(Cart cart, double cartTotal, List<Voucher> listVoucher) {
        List<Voucher> result = new ArrayList<>();
        if (cart == null || listVoucher == null) return result;

        for (Voucher v : listVoucher) {

            if (cartTotal < v.getConditionPrice()) continue;
            if (v.getUsage_limit() <= 0) continue;

            boolean hasConditionBook =
                    v.getConditionBook() != null && !v.getConditionBook().trim().isEmpty();
            boolean hasConditionPublisher =
                    v.getConditionPublisher() != null && !v.getConditionPublisher().trim().isEmpty();

            boolean valid = true;

            for (CartItem item : cart.getItems()) {
                Book b = item.getBook();

                if (hasConditionBook &&
                        !matchCondition(v.getConditionBook(), b.getType())) {
                    valid = false;
                    break;
                }

                if (hasConditionPublisher &&
                        !matchCondition(v.getConditionPublisher(), b.getPublisher())) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                result.add(v);
            }
        }
        return result;
    }


    private boolean matchCondition(String condition, String value) {
        if (condition == null || condition.trim().isEmpty()) return true;
        if (value == null || value.trim().isEmpty()) return false;
        String[] arr = condition.split(",");
        for (String c : arr) {
            if (value.equalsIgnoreCase(c.trim())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        VoucherService voucherService = new VoucherService();
        List<Voucher> lvoucher = voucherService.listVoucherDiscountUser(31);
        System.out.println(lvoucher.toString());
    }


    public Voucher getById(int voucherId) {
        for (Voucher v : voucherDao.getVoucherList()) {
            if (v.getId() == voucherId) {
                return v;
            }
        }
        return null;
    }

    public boolean isVoucherValid(Cart cart, Voucher v) {
        if (cart == null || v == null) return false;

        if (cart.getTotalBill() < v.getConditionPrice()) return false;
        if (v.getUsage_limit() <= 0) return false;

        boolean hasBookCond =
                v.getConditionBook() != null && !v.getConditionBook().trim().isEmpty();
        boolean hasPubCond =
                v.getConditionPublisher() != null && !v.getConditionPublisher().trim().isEmpty();

        for (CartItem item : cart.getItems()) {
            Book b = item.getBook();

            if (hasBookCond &&
                    !matchCondition(v.getConditionBook(), b.getType())) {
                return false;
            }

            if (hasPubCond &&
                    !matchCondition(v.getConditionPublisher(), b.getPublisher())) {
                return false;
            }
        }
        return true;
    }


    public boolean voucherExists(String voucherParam) {
        return voucherDao.voucherExists(voucherParam);
    }

    public void tangVoucher(List<Integer> listU, String voucherParam) {
        voucherDao.tangVoucher(listU, voucherParam);
    }

    public int countValidVouchers() {
        return voucherDao.countValidVouchers();
    }

    public int countExpiredVouchers() {
        return voucherDao.countExpiredVouchers();
    }
}
