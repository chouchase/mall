package com.chouchase.common;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

public class Const {
    public static final String CURRENT_USER = "current_user";
    public interface Role {
        int CUSTOMER = 1;
        int ADMIN = 2;
    }
    public interface Cart{
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        Integer checked = 1;
        Integer unChecked = 0;
    }
    public static final class ProductListOrderBy{
        public  static final Set<String> PRICE_ASC_DESC;
        static {
            PRICE_ASC_DESC = new HashSet<>();
            PRICE_ASC_DESC.add("price_desc");
            PRICE_ASC_DESC.add("price_asc");
        }
    }
    public  static final class ProductStatus{
        public static final int ON_SALE = 1;
        public static final int OFF_SALE = 0;
    }
}
