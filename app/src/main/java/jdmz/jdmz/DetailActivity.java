package jdmz.jdmz;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static jdmz.jdmz.CAUMZConfig.GET_DETAIL_URL;

public class DetailActivity extends AppCompatActivity {

    ViewPager viewpager;
    RecyclerView recyclerView;
    DetailRecyclerViewAdapter adapter;
    FloatingActionButton fab;
    TextView category, location, delivery, operateTime, telText, bestMenu, address, likeText;
    LinearLayout likeLayout;
    ImageView mapBtn, likeBtn;

    String title;
    int com_id;
    CompanyInfo companyInfo;
    ArrayList<TextMenuInfo> textMenus;
    ArrayList<ImageMenuInfo> imageMenus;
    DetailPagerAdapter viewpagerAdapter;
    int currentPage;
    int likeFlag;
    boolean islikechange;
    GetDetailAsync async;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //가게명
        title = getIntent().getStringExtra("title");
        com_id = getIntent().getIntExtra("id", 0);

        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        async = new GetDetailAsync();
        async.execute();
    }


    void initView(){
        fab = (FloatingActionButton)findViewById(R.id.detail_fab);
        recyclerView = (RecyclerView)findViewById(R.id.detail_textmenu);
        viewpager = (ViewPager)findViewById(R.id.detail_viewpager);

        category = (TextView)findViewById(R.id.detail_category);
        location = (TextView)findViewById(R.id.detail_location);
        delivery = (TextView)findViewById(R.id.detail_delivery);
        operateTime = (TextView)findViewById(R.id.detail_operate_time);
        telText = (TextView)findViewById(R.id.detail_tel);
        bestMenu = (TextView)findViewById(R.id.detail_best);
        mapBtn = (ImageView)findViewById(R.id.detail_mapBtn);
        address = (TextView)findViewById(R.id.detail_address);
        likeText = (TextView)findViewById(R.id.detail_like);
        likeBtn = (ImageView)findViewById(R.id.detail_likeBtn);
        likeLayout = (LinearLayout)findViewById(R.id.detail_like_layout);
        operateTime = (TextView)findViewById(R.id.detail_operate_time);

        companyInfo = new CompanyInfo();
        textMenus = new ArrayList<>();
        imageMenus = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        adapter = new DetailRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        likeFlag = 0;
        currentPage = 1;
        islikechange = false;

        viewpagerAdapter = new DetailPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewpagerAdapter);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_phone:

                if(companyInfo.getTel()!=null && companyInfo.getTel()!=""){
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", companyInfo.getTel(), null)));
                }
                return false;
        }
        return false;
    }


    class DetailPagerAdapter extends FragmentStatePagerAdapter {

        public DetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DetailPagerFragment fragment = new DetailPagerFragment().newInstance(imageMenus.get(position), position, imageMenus.size());
            return fragment;
        }

        @Override
        public int getCount() {
            return imageMenus.size();
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }


    public class DetailRecyclerViewAdapter
            extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder>  {



        public DetailRecyclerViewAdapter() {

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View mView;
            TextView title, subtitle, price;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                title = (TextView)view.findViewById(R.id.detail_menu_title);
                subtitle = (TextView)view.findViewById(R.id.detail_menu_subtitle);
                price = (TextView)view.findViewById(R.id.detail_menu_price);
            }

        }

        @Override
        public DetailRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout = R.layout.detail_menu_item;
            View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final DetailRecyclerViewAdapter.ViewHolder holder, final int position) {
            //title, subtitle, price;
            holder.title.setText(textMenus.get(position).getTitle());
            if(textMenus.get(position).getSubtitle()!="null" && textMenus.get(position).getSubtitle()!=""){
                holder.subtitle.setText(textMenus.get(position).getSubtitle());
            }else{
                holder.subtitle.setVisibility(View.GONE);
            }
            holder.price.setText(textMenus.get(position).getPrice());
        }


        @Override
        public int getItemCount() {
            return textMenus.size();
        }

    }


    private class GetDetailAsync extends AsyncTask<String, Void, Boolean> {

        ArrayList<TextMenuInfo> tmpTextMenus = new ArrayList<>();
        ArrayList<ImageMenuInfo> tmpImgMenus = new ArrayList<>();

        @Override
        protected Boolean doInBackground(String... params) {

            // URL 입력x
            String urlText = GET_DETAIL_URL;
            Response response = null;           //  응답 객체
            OkHttpClient client;                //  클라이언트->서버 객체
            try {
                // ********************* Server Connect ************************* Internet permission 꼭 manifests에 넣을것
                // single tone으로 client->sever 생성

                client = OkHttpInitSingtonManager.getOkHttpClient();
                // 서버요청
                RequestBody requestBody = new FormBody.Builder()
                        .add("com_id", com_id+"")
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
                    return false;
                } else {

                    JSONObject data = jobject.getJSONObject("data");
                    JSONObject info = data.getJSONObject("info");
                    companyInfo = new CompanyInfo(info);

                    JSONArray tmenu = data.getJSONArray("tmenu");
                    for(int i =0; i<tmenu.length();i++){
                        TextMenuInfo tmp = new TextMenuInfo(tmenu.getJSONObject(i));
                        //Log.d("text", data.length()+"//"+data.getJSONObject(i).toString());
                        tmpTextMenus.add(tmp);
                    }

                    JSONArray imenu = data.getJSONArray("imenu");
                    for(int j =0; j<imenu.length();j++){
                        ImageMenuInfo itmp = new ImageMenuInfo(imenu.getJSONObject(j));
                        tmpImgMenus.add(itmp);
                    }

                    response.body().close();
                    return true;
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


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                if(tmpTextMenus.size()>0){
                    textMenus = tmpTextMenus;
                    adapter.notifyDataSetChanged();
                }

                if(tmpImgMenus.size()>0){
                    imageMenus = tmpImgMenus;
                    viewpagerAdapter.notifyDataSetChanged();
                }


                fab.setOnClickListener(mOnClickListener);

                category.setText(companyInfo.getCategory());
                location.setText(companyInfo.getLocation());
                if(companyInfo.getDelivery()==1){
                    delivery.setVisibility(View.VISIBLE);
                }
                operateTime.setText(companyInfo.getOperate_time());
                telText.setText(companyInfo.getTel());
                bestMenu.setText(companyInfo.getBest_menu());
                address.setText(companyInfo.getAddress());

                mapBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+companyInfo.getAddress());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });

                likeText.setText(companyInfo.getLike()+"");

                if(PropertyManager.getInstance().getLiked().indexOf(com_id)>=0){
                    likeBtn.setImageResource(R.drawable.likered);
                    likeFlag = 1;

                }else {
                    likeFlag = 0;
                }

                likeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(likeFlag==0){
                            //좋아요
                            PropertyManager.getInstance().addLiked(com_id);
                            likeBtn.setImageResource(R.drawable.likered);
                            likeFlag = 1;
                            new LikeThread().likeCompnay(com_id);
                            int like = Integer.parseInt(likeText.getText().toString());
                            likeText.setText((like+1)+"");
                            islikechange = !islikechange;
                        }else if(likeFlag==1){
                            //좋아요 취소
                            PropertyManager.getInstance().deleteLiked(com_id);
                            likeBtn.setImageResource(R.drawable.likebtn);
                            likeFlag = 0;
                            new LikeThread().dislikeCompnay(com_id);
                            int like = Integer.parseInt(likeText.getText().toString());
                            likeText.setText((like-1)+"");
                            islikechange = !islikechange;
                        }
                    }
                });


            }else{
                Toast.makeText(getApplicationContext(), "데이터를 불러올수 없습니다", Toast.LENGTH_SHORT).show();
            }
        }

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomDialog dialog = new CustomDialog(DetailActivity.this);
            dialog.setData(title, com_id);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
        }
    };

    @Override
    protected void onDestroy() {
        if(async!=null){
            async.cancel(true);
        }
        super.onDestroy();

    }

}
