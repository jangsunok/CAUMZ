package jdmz.jdmz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static jdmz.jdmz.CAUMZConfig.GET_RANDOM_URL;

/**
 * Created by jang on 2017. 1. 5..
 */

public class RandomActivity extends AppCompatActivity{
    ArrayList<RandomInfo> randomInfos;

    ViewPager viewPager;
    RandomPagerAdapter adapter;
    GetRandomListAsync randomListAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        getSupportActionBar().setTitle("오늘뭐먹지");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        randomInfos = new ArrayList<>();
        viewPager = (ViewPager)findViewById(R.id.random_viewpager);
        adapter = new RandomPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        // Disable clip to padding
        viewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        viewPager.setPadding(60, 0, 60, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        viewPager.setPageMargin(20);

        randomListAsync = new GetRandomListAsync();
        randomListAsync.execute();

    }


    class RandomPagerAdapter extends FragmentStatePagerAdapter {

        public RandomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            RandomPagerFragment fragment = new RandomPagerFragment().newInstance(randomInfos.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return randomInfos.size();
        }


    }

    private class GetRandomListAsync extends AsyncTask<String, Void, ArrayList<RandomInfo>> {
        @Override
        protected ArrayList<RandomInfo> doInBackground(String... params) {

            // URL 입력
            String urlText = GET_RANDOM_URL;
            Response response = null;           //  응답 객체
            OkHttpClient client;                //  클라이언트->서버 객체
            try {
                // single tone으로 client->sever 생성

                client = OkHttpInitSingtonManager.getOkHttpClient();
                // 서버요청
                Request request = new Request.Builder()
                        .url(String.format(urlText))
                        .build();

                response = client.newCall(request).execute();       // 서버 응답
                String text = response.body().string();             // String으로 받음

                // ********************* Json Parsing *************************
                //Log.d("text", text);
                JSONObject jobject = new JSONObject(text);          //JSONObject 생성
                ArrayList<RandomInfo> ret = new ArrayList<>();
                if (jobject.getBoolean("error")) {
                    //에러
                    return null;
                } else {
                    JSONArray data = jobject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        RandomInfo info = new RandomInfo(data.getJSONObject(i));
                        //Log.d("text", data.length()+"//"+data.getJSONObject(i).toString());
                        ret.add(info);
                    }
                    response.body().close();
                    return ret;
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


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<RandomInfo> datas) {
            super.onPostExecute(datas);
            if (datas != null && datas.size() > 0) {
                randomInfos = datas;
                adapter.notifyDataSetChanged();
            } else {
                //Toast.makeText(getContext(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        if(randomListAsync!=null){
            randomListAsync.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return false;
    }
}
