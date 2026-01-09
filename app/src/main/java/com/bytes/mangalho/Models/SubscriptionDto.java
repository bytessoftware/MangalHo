package com.bytes.mangalho.Models;

import java.io.Serializable;

public class SubscriptionDto implements Serializable {
    String id = "";
    String order_id = "";
    String txn_id = "";
    String user_id = "";
    String subscription_type = "";
    String price = "";
    String packages_id = "";
    String end_subscription_date = "";
    String created_at = "";
    String subscription_title = "";
    String subscription_name = "";
    String days = "";
    String subscription_start_date = "";
    String subscription_end_date = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(String subscription_type) {
        this.subscription_type = subscription_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPackages_id() {
        return packages_id;
    }

    public void setPackages_id(String packages_id) {
        this.packages_id = packages_id;
    }

    public String getEnd_subscription_date() {
        return end_subscription_date;
    }

    public void setEnd_subscription_date(String end_subscription_date) {
        this.end_subscription_date = end_subscription_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSubscription_title() {
        return subscription_title;
    }

    public void setSubscription_title(String subscription_title) {
        this.subscription_title = subscription_title;
    }

    public String getSubscription_name() {
        return subscription_name;
    }

    public void setSubscription_name(String subscription_name) {
        this.subscription_name = subscription_name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getSubscription_start_date() {
        return subscription_start_date;
    }

    public void setSubscription_start_date(String subscription_start_date) {
        this.subscription_start_date = subscription_start_date;
    }

    public String getSubscription_end_date() {
        return subscription_end_date;
    }

    public void setSubscription_end_date(String subscription_end_date) {
        this.subscription_end_date = subscription_end_date;
    }
}