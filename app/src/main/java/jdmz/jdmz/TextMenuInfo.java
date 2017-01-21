package jdmz.jdmz;

import org.json.JSONObject;

/**
 * Created by jang on 2016. 12. 21..
 */

public class TextMenuInfo {
    String title;
    String subtitle;
    String price;

    public TextMenuInfo(){

    }

    public TextMenuInfo(JSONObject jsonObject){
        title = jsonObject.optString("title", "");
        subtitle = jsonObject.optString("subtitle", "");
        price = jsonObject.optString("price", "");

    }


    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPrice() {
        return price;
    }
}
