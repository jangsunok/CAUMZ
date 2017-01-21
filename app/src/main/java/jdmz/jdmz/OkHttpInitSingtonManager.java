package jdmz.jdmz;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;



public class OkHttpInitSingtonManager {
    private static OkHttpClient okHttpClient;
    private static final int OKHTTP_INIT_VALUE = 20;
    static{
        okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                .readTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                .build();
    }
    public static OkHttpClient getOkHttpClient(){
        if( okHttpClient != null){
            return okHttpClient;
        }else{
            okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                    .readTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
}