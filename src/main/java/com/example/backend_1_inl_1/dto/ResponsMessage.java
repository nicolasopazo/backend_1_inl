package com.example.backend_1_inl_1.dto;

import com.example.backend_1_inl_1.model.Customer;
import com.example.backend_1_inl_1.model.Item;

public final class ResponsMessage {

    public static final String NOT_A_NUMBER = "Please provide a valid number.";

    public static final String ORDER_COMPLETE = "Purchase complete.";
    public static final String ORDER_DELETED = "Order has been deleted.";
    public static final String ORDER_NOT_FOUND = "Order was not found.";
    public static final String PRODUCT_NOT_FOUND = "Product was not found.";
    public static final String CUSTOMER_NOT_FOUND = "Customer was not found.";
    public static final String NOTHING_FOUND = "Product and customer was not found.";

    public static final String EMAIL_IN_USE = "Email is already in use.";

    public static String productAdded(Item item) {
        return "Product '" + item.toString() + "' was added to the database.";
    }

    public static String productDeleted(Item item) {
        return "Product '" + item.toString() + "' was deleted from the database.";
    }

    public static String customerAdded(Customer customer) {
        return "Customer '" + customer.getName() + "' was added to the database.";
    }

    public static String customerDeleted(Customer customer) {
        return "Customer '" + customer.getName() + "' was deleted from the database.";
    }

}
