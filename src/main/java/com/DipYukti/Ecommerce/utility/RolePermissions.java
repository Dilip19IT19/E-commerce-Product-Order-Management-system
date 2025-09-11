package com.DipYukti.Ecommerce.utility;

import com.DipYukti.Ecommerce.type.PermissionType;
import com.DipYukti.Ecommerce.type.RoleType;

import java.util.List;
import java.util.Map;

public class RolePermissions
{
    public static final Map<RoleType, List<PermissionType>> Role_Permissions_Map=Map.of(
            RoleType.ROLE_USER,List.of(
                    PermissionType.CREATE_ORDER,
                    PermissionType.READ_ORDER,
                    PermissionType.READ_CART,
                    PermissionType.READ_CATEGORY,
                    PermissionType.READ_PRODUCT,
                    PermissionType.UPDATE_ORDER,
                    PermissionType.UPDATE_CART,
                    PermissionType.DELETE_CART,
                    PermissionType.DELETE_ORDER,
                    PermissionType.CREATE_CART,
                    PermissionType.UPDATE_CUSTOMER,
                    PermissionType.READ_CUSTOMER,
                    PermissionType.DELETE_CUSTOMER,
                    PermissionType.CLEAR_CART,
                    PermissionType.CHANGE_PASSWORD
            ),
            RoleType.ROLE_ADMIN,List.of(
                    PermissionType.CREATE_CATEGORY,
                    PermissionType.UPDATE_CATEGORY,
                    PermissionType.READ_CATEGORY,
                    PermissionType.DELETE_CATEGORY,
                    PermissionType.CREATE_PRODUCT,
                    PermissionType.UPDATE_PRODUCT,
                    PermissionType.DELETE_PRODUCT,
                    PermissionType.READ_PRODUCT,
                    PermissionType.READ_CUSTOMER,
                    PermissionType.DELETE_CUSTOMER

            )
    );

    public static List<PermissionType> getPermissionsByRole(RoleType role) {
        return Role_Permissions_Map.getOrDefault(role, List.of());
    }



}
