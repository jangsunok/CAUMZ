package jdmz.jdmz;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static jdmz.jdmz.CAUMZConfig.SET_DISLIKE_URL;
import static jdmz.jdmz.CAUMZConfig.SET_LIKE_URL;


/**
 * Created by jang on 2017. 1. 3..
 */

public class LikeThread {

    public static void likeCompnay(final int com_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // URL 입력
                String urlText = SET_LIKE_URL;
                Response response = null;           //  응답 객체
                OkHttpClient client;                //  클라이언트->서버 객체
                try {
                    // ********************* Server Connect ************************* Internet permission 꼭 manifests에 넣을것
                    // single tone으로 client->sever 생성

                    client = OkHttpInitSingtonManager.getOkHttpClient();
                    // 서버요청
                    RequestBody requestBody = new FormBody.Builder()
                            .add("company_id", com_id+"")
                            .build();

                    Request request = new Request.Builder()
                            .url(String.format(urlText))
                            .post(requestBody)
                            .build();

                    response = client.newCall(request).execute();       // 서버 응답
                    String text = response.body().string();             // String으로 받음

                    // ********************* Json Parsing *************************
                    //Log.d("text", text);
                    JSONObject jobject = new JSONObject(text);          //JSONObject 생성
                    if (jobject.getBoolean("error")) {
                        //에러
                    } else {
                        response.body().close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (response != null) {
                        // 연결 해제
                        response.body().close();
                    }
                }

            }
        }).start();


    }

    public static void dislikeCompnay(final int com_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // URL 입력
                String urlText = SET_DISLIKE_URL;
                Response response = null;           //  응답 객체
                OkHttpClient client;                //  클라이언트->서버 객체
                try {
                    // ********************* Server Connect ************************* Internet permission 꼭 manifests에 넣을것
                    // single tone으로 client->sever 생성

                    client = OkHttpInitSingtonManager.getOkHttpClient();
                    // 서버요청
                    RequestBody requestBody = new FormBody.Builder()
                            .add("company_id", com_id+"")
                            .build();

                    Request request = new Request.Builder()
                            .url(String.format(urlText))
                            .post(requestBody)
                            .build();

                    response = client.newCall(request).execute();       // 서버 응답
                    String text = response.body().string();             // String으로 받음

                    // ********************* Json Parsing *************************
                    //Log.d("text", text);
                    JSONObject jobject = new JSONObject(text);          //JSONObject 생성
                    if (jobject.getBoolean("error")) {
                        //에러
                    } else {
                        response.body().close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (response != null) {
                        // 연결 해제
                        response.body().close();
                    }
                }

            }
        }).start();


    }
}
