package com.kafkadefinitiveguide.consumer.deserializer.customdeserializer;

/**
 * Customer Pojo.
 *
 * @author  Wuyi Chen
 * @date    06/03/2020
 * @version 1.0
 * @since   1.0
 */
public class Customer {
    private int    customerId;
    private String customerName;

    public Customer(int Id, String name) {
        this.customerId   = Id;
        this.customerName = name;
    }

    public int getId() {
        return customerId;
    }

    public String getName() {
        return customerName;
    }
}
