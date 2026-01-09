package com.bytes.mangalho.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceDTO implements Serializable {
    String service_provider_id = "";
    String service_provider_name = "";
    String category_id = "";
    String about = "";
    String contact_detail = "";
    String address = "";
    String landmark = "";
    String latitude = "";
    String longitude = "";
    String service_offer = "";
    String pricing_info = "";
    String image = "";
    String banner_image = "";
    String language = "";
    String status = "";
    String created_at = "";
    String updated_at = "";
    ArrayList<ServiceImageDTO> service_imgs;

    public String getService_provider_id() {
        return service_provider_id;
    }

    public void setService_provider_id(String service_provider_id) {
        this.service_provider_id = service_provider_id;
    }

    public String getService_provider_name() {
        return service_provider_name;
    }

    public void setService_provider_name(String service_provider_name) {
        this.service_provider_name = service_provider_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getContact_detail() {
        return contact_detail;
    }

    public void setContact_detail(String contact_detail) {
        this.contact_detail = contact_detail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getService_offer() {
        return service_offer;
    }

    public void setService_offer(String service_offer) {
        this.service_offer = service_offer;
    }

    public String getPricing_info() {
        return pricing_info;
    }

    public void setPricing_info(String pricing_info) {
        this.pricing_info = pricing_info;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ArrayList<ServiceImageDTO> getService_imgs() {
        return service_imgs;
    }

    public void setService_imgs(ArrayList<ServiceImageDTO> service_imgs) {
        this.service_imgs = service_imgs;
    }
}
