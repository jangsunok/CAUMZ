package jdmz.jdmz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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

import static jdmz.jdmz.CAUMZConfig.SEARCH_COMPANY_URL;

/**
 * Created by jang on 2017. 1. 3..
 */

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchRecyclerViewAdapter adapter;
    ArrayList<CompanyInfo> companyInfo;
    String[] items = {};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        companyInfo = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        adapter = new SearchRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        final EditText editText = (EditText) findViewById(R.id.search_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    new SearchAsync().execute(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(editText.getText().length()>0){
                        new SearchAsync().execute(editText.getText().toString());
                    }
                    return true;
                } else
                    return false;

            }
        });

        MobileAds.initialize(getApplicationContext(), getString(R.string.admob));

        AdView mAdView = (AdView) findViewById(R.id.search_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    public class SearchRecyclerViewAdapter
            extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

        public SearchRecyclerViewAdapter() {

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View mView;
            TextView title, bestMenu, delivery, like;
            ImageView likeBtn;
            LinearLayout likeLayout;
            int likeFlag;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                title = (TextView)view.findViewById(R.id.list_title);
                bestMenu = (TextView)view.findViewById(R.id.list_best);
                delivery = (TextView)view.findViewById(R.id.list_delivery);
                like = (TextView)view.findViewById(R.id.list_like);
                likeBtn = (ImageView)view.findViewById(R.id.list_like_img);
                likeLayout = (LinearLayout)view.findViewById(R.id.list_like_layout);
                likeFlag = 0;

            }

        }

        @Override
        public SearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout = R.layout.comlist_item;

            View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            SearchRecyclerViewAdapter.ViewHolder viewHolder = new SearchRecyclerViewAdapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final SearchRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.title.setText(companyInfo.get(position).getTitle());
            holder.bestMenu.setText(companyInfo.get(position).getBest_menu());
            if(companyInfo.get(position).getDelivery()==1){
                holder.delivery.setVisibility(View.VISIBLE);
            }else {
                holder.delivery.setVisibility(View.GONE);
            }

            //like
            final int com_id = companyInfo.get(position).getId();
            if(PropertyManager.getInstance().getLiked().indexOf(com_id)>=0){
                holder.likeBtn.setImageResource(R.drawable.likered);
                holder.likeFlag = 1;
            }else {
                holder.likeFlag = 0;
            }

            Log.d("search", "like="+companyInfo.get(position).getLike()+"");
            holder.like.setText(companyInfo.get(position).getLike()+"");
            holder.likeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                    if(holder.likeFlag==0){
                        //좋아요
                        PropertyManager.getInstance().addLiked(com_id);
                        holder.likeBtn.setImageResource(R.drawable.likered);
                        holder.likeFlag = 1;
                        new LikeThread().likeCompnay(com_id);
                        int like = Integer.parseInt(holder.like.getText().toString());
                        holder.like.setText((like+1)+"");
                    }else if(holder.likeFlag==1){
                        //좋아요 취소
                        PropertyManager.getInstance().deleteLiked(com_id);
                        holder.likeBtn.setImageResource(R.drawable.likebtn);
                        holder.likeFlag = 0;
                        new LikeThread().dislikeCompnay(com_id);
                        int like = Integer.parseInt(holder.like.getText().toString());
                        holder.like.setText((like-1)+"");
                    }


                }
            });

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                    intent.putExtra("title", companyInfo.get(position).getTitle());
                    intent.putExtra("id", companyInfo.get(position).getId());
                    startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return companyInfo.size();
        }

    }

    private class SearchAsync extends AsyncTask<String, Void, ArrayList<CompanyInfo>> {



        @Override
        protected ArrayList<CompanyInfo> doInBackground(String... params) {

            // URL 입력
            String urlText = SEARCH_COMPANY_URL;
            Response response = null;           //  응답 객체
            OkHttpClient client;                //  클라이언트->서버 객체
            try {
                // ********************* Server Connect ************************* Internet permission 꼭 manifests에 넣을것
                // single tone으로 client->sever 생성

                client = OkHttpInitSingtonManager.getOkHttpClient();
                // 서버요청
                RequestBody requestBody = new FormBody.Builder()
                        .add("keyword", params[0])
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
                ArrayList<CompanyInfo> ret = new ArrayList<>();
                if (jobject.getBoolean("error")) {
                    //에러
                    return null;
                } else {
                    JSONArray data = jobject.getJSONArray("data");

                    for(int i =0; i<data.length();i++){
                        CompanyInfo in = new CompanyInfo(data.getJSONObject(i));
                        //Log.d("text", data.length()+"//"+data.getJSONObject(i).toString());
                        ret.add(in);
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
        protected void onPostExecute(ArrayList<CompanyInfo> datas) {
            super.onPostExecute(datas);
            if(datas!=null && datas.size()>0){
                companyInfo = datas;
                adapter.notifyDataSetChanged();
            }else {
                //Toast.makeText(getContext(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
            }
        }

    }
}