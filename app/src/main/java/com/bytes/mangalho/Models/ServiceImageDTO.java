package com.bytes.mangalho.Models;

import java.io.Serializable;

public class ServiceImageDTO implements Serializable {

    String image_id = "";
    String service_provider_id = "";
    String gallery_image = "";
    String created_at = "";

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getService_provider_id() {
        return service_provider_id;
    }

    public void setService_provider_id(String service_provider_id) {
        this.service_provider_id = service_provider_id;
    }

    public String getGallery_image() {
        return gallery_image;
    }

    public void setGallery_image(String gallery_image) {
        this.gallery_image = gallery_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
