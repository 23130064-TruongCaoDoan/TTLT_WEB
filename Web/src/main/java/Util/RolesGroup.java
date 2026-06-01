package Util;

import java.util.Set;

public final class RolesGroup {
    public static final Set<Integer> STATISTICAL_ROLE = Set.of(1,2,3,4,5);
    public static final Set<Integer> SALES_ROLE = Set.of(1,2,3);
    public static final Set<Integer> USER_MANAGER_ROLE = Set.of(1,2);
    public static final Set<Integer> PRODUCT_MANAGER_ROLE = Set.of(1,2,3,4,5);
    public static final Set<Integer> IMPORT_ROLE =Set.of(1,2,4);
    public static final Set<Integer> ALL_STAFF_ROLE =Set.of(1,2,3,4,5,6);

    public static boolean canViewStatistic(int role) {
        return STATISTICAL_ROLE.contains(role);
    }

    public static boolean canSale(int role) {
        return SALES_ROLE.contains(role);
    }

    public static boolean canManageUser(int role) {
        return USER_MANAGER_ROLE.contains(role);
    }

    public static boolean canManageProduct(int role) {
        return PRODUCT_MANAGER_ROLE.contains(role);
    }

    public static boolean canImport(int role) {
        return IMPORT_ROLE.contains(role);
    }

    public static boolean isStaff(int role) {
        return ALL_STAFF_ROLE.contains(role);
    }

    public static boolean isAdmin(int role) {
        return role == 1;
    }

    public static boolean isManager(int role) {
        return role == 2;
    }
}

