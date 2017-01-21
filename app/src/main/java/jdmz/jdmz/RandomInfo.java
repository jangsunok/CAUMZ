package jdmz.jdmz;

import org.json.JSONObject;

/**
 * Created by jang on 2017. 1. 5..
 */

public class RandomInfo {

    int id;
    String title;
    String location;
    String category;
    String best_menu;
    int delivery;
    String imageTitle;
    //String subtitle;
    String image;
    String writer;

    public RandomInfo(JSONObject jsonObject){
        id = jsonObject.optInt("id", 0);
        title = jsonObject.optString("title", "");
        location = jsonObject.optString("location", "");
        category = jsonObject.optString("category", "");
        best_menu = jsonObject.optString("best_menu", "");
        delivery = jsonObject.optInt("delivery", 0);

        imageTitle = jsonObject.optString("menu_title", "");
        //subtitle = jsonObject.optString("menu_subtitle", "");
        image = jsonObject.optString("menu_image", "");
        writer = jsonObject.optString("menu_writer", "");

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public String getBest_menu() {
        return best_menu;
    }

    public int getDelivery() {
        return delivery;
    }

    public String getImageTitle() {
        return imageTitle;
    }

//    public String getSubtitle() {
//        return subtitle;
//    }

    public String getImage() {
        return image;
    }

    public String getWriter() {
        return writer;
    }
}
