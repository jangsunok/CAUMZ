package jdmz.jdmz;

import org.json.JSONObject;

/**
 * Created by jang on 2016. 12. 21..
 */

public class ImageMenuInfo {
    String title;
    String subtitle;
    String image;
    String writer;

    public ImageMenuInfo(){

    }

    public ImageMenuInfo(JSONObject jsonObject){
        title = jsonObject.optString("menu_title", "");
        subtitle = jsonObject.optString("menu_subtitle", "");
        image = jsonObject.optString("menu_image", "");
        writer = jsonObject.optString("menu_writer", "");

    }


    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getImage() {
        return image;
    }

    public String getWriter() {
        return writer;
    }
}
