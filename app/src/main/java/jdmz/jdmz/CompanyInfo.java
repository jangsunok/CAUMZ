package jdmz.jdmz;

import org.json.JSONObject;

/**
 * Created by jang on 2016. 12. 21..
 */

public class CompanyInfo {
    int id;
    String title;
    String location;
    String address;
    String category;
    String tel;
    String operate_time;
    String best_menu;
    String image;
    int like;
    int delivery;
    Boolean isliked;

    public CompanyInfo(){

    }

    public CompanyInfo(JSONObject jsonObject){
        id = jsonObject.optInt("id", 0);
        title = jsonObject.optString("title", "");
        location = jsonObject.optString("location", "");
        address = jsonObject.optString("address", "");
        category = jsonObject.optString("category", "");
        tel = jsonObject.optString("tel", "");
        operate_time = jsonObject.optString("operate_time", "");
        best_menu = jsonObject.optString("best_menu", "");
        image = jsonObject.optString("image", "");
        like = jsonObject.optInt("like_count", 0);
        delivery = jsonObject.optInt("delivery", 0);

    }


    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public String getTel() {
        return tel;
    }

    public String getOperate_time() {
        return operate_time;
    }

    public String getBest_menu() {
        return best_menu;
    }

    public String getImage() {
        return image;
    }

    public int getLike() {
        return like;
    }

    public int getDelivery() {
        return delivery;
    }

    public int getId() {
        return id;
    }

    public Boolean getIsliked() {
        return isliked;
    }

    public void setIsliked(Boolean isliked) {
        this.isliked = isliked;
    }
}
