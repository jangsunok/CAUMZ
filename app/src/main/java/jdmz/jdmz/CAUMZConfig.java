package jdmz.jdmz;

import okhttp3.MediaType;

/**
 * Created by jang on 2016. 12. 18..
 */

public class CAUMZConfig {


    public static final String GET_COMPANY_URL = "http://52.78.48.230/getCompany.php";
    public static final String GET_DETAIL_URL = "http://52.78.48.230/getCompanyDetail.php";
    public static final String REQUEST_MODIFY_URL ="http://52.78.48.230/RequestModify.php";
    public static final String REQUEST_PHOTO_URL = "http://52.78.48.230/RequestPhoto.php";
    public static final String SEARCH_COMPANY_URL = "http://52.78.48.230/searchCompany.php";
    public static final String SET_LIKE_URL = "http://52.78.48.230/setLike.php";
    public static final String SET_DISLIKE_URL = "http://52.78.48.230/setDislike.php";
    public static final String GET_RANDOM_URL = "http://52.78.48.230/getRandom.php";

    public static final int CAMERA_DATA = 10000;
    public static final int GALLARY_DATA = 20000;
    public static final int REQUEST_DETAIL = 100;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1233;
    public static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1203;

    public static final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");

    public static final String CATEGORY_ALL = "전체";
    public static final String CATEGORY_CHICKEN = "치킨";
    public static final String CATEGORY_HANSIK = "한식";
    public static final String CATEGORY_YANGSIK = "양식";
    public static final String CATEGORY_BUNSIK = "분식";

}
