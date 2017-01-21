package jdmz.jdmz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import static jdmz.jdmz.CAUMZConfig.CATEGORY_ALL;
import static jdmz.jdmz.CAUMZConfig.GET_COMPANY_URL;

/**
 * Created by jang on 2016. 12. 17..
 */

public class ComListFragment extends Fragment {

    String location, title;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList<CompanyInfo> companyInfo;
    GetListAsync getListAsync;

    public ComListFragment() {

    }

    public static ComListFragment newInstance(String location, String title) {
        ComListFragment fragment = new ComListFragment();
        Bundle args = new Bundle();
        args.putString("location", location);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.comlist_viewpager, container, false);
        location = getArguments().getString("location");
        title = getArguments().getString("title");

        companyInfo = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.comlist_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        getListAsync = new GetListAsync();
        getListAsync.execute();
        return v;

    }

    @Override
    public void onDestroy() {
        if(getListAsync!=null){
            getListAsync.cancel(true);
        }
        super.onDestroy();
    }

    public class RecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        int TYPE_HEADER = 0;


        public RecyclerViewAdapter() {

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View mView;

            //header
            TextView total;

            //content
            TextView title, bestMenu, delivery, like;
            ImageView likeBtn;
            LinearLayout likeLayout;
            int likeFlag;


            public ViewHolder(View view, int viewType) {
                super(view);
                mView = view;

                if (viewType == TYPE_HEADER) {
                    total = (TextView) view.findViewById(R.id.comlist_count);

                } else {
                    title = (TextView) view.findViewById(R.id.list_title);
                    bestMenu = (TextView) view.findViewById(R.id.list_best);
                    delivery = (TextView) view.findViewById(R.id.list_delivery);
                    like = (TextView) view.findViewById(R.id.list_like);
                    likeBtn = (ImageView) view.findViewById(R.id.list_like_img);
                    likeLayout = (LinearLayout) view.findViewById(R.id.list_like_layout);
                }

            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout = 0;
            if (viewType == TYPE_HEADER) {
                layout = R.layout.comlist_header;
            } else {
                layout = R.layout.comlist_item;
            }

            View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(view, viewType);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (isPositionHeader(position)) {
                holder.total.setText(companyInfo.size() + "");
            } else {

                holder.title.setText(companyInfo.get(position - 1).getTitle());
                holder.bestMenu.setText(companyInfo.get(position - 1).getBest_menu());
                if (companyInfo.get(position - 1).getDelivery() == 1) {
                    holder.delivery.setVisibility(View.VISIBLE);
                } else {
                    holder.delivery.setVisibility(View.GONE);
                }

                //like
                final int com_id = companyInfo.get(position - 1).getId();
                if (PropertyManager.getInstance().getLiked().indexOf(com_id) >= 0) {
                    holder.likeBtn.setImageResource(R.drawable.likered);
                    holder.likeFlag = 1;
                } else {
                    holder.likeBtn.setImageResource(R.drawable.likebtn);
                    holder.likeFlag = 0;
                }

                holder.like.setText(companyInfo.get(position - 1).getLike() + "");
                holder.likeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //
                        if (holder.likeFlag == 0) {
                            //좋아요
                            PropertyManager.getInstance().addLiked(com_id);
                            holder.likeBtn.setImageResource(R.drawable.likered);
                            holder.likeFlag = 1;
                            new LikeThread().likeCompnay(com_id);
                            int like = Integer.parseInt(holder.like.getText().toString());
                            holder.like.setText((like + 1) + "");
                        } else if (holder.likeFlag == 1) {
                            //좋아요 취소
                            PropertyManager.getInstance().deleteLiked(com_id);
                            holder.likeBtn.setImageResource(R.drawable.likebtn);
                            holder.likeFlag = 0;
                            new LikeThread().dislikeCompnay(com_id);
                            int like = Integer.parseInt(holder.like.getText().toString());
                            holder.like.setText((like - 1) + "");
                        }


                    }
                });

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra("title", companyInfo.get(position - 1).getTitle());
                        intent.putExtra("id", companyInfo.get(position - 1).getId());
                        startActivity(intent);
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return companyInfo.size() + 1;
        }

        private boolean isPositionHeader(int position) {
            return position == TYPE_HEADER;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return 0;
            else
                return position;
        }

    }


    private class GetListAsync extends AsyncTask<String, Void, ArrayList<CompanyInfo>> {
        @Override
        protected ArrayList<CompanyInfo> doInBackground(String... params) {

            // URL 입력
            String urlText = GET_COMPANY_URL;
            Response response = null;           //  응답 객체
            OkHttpClient client;                //  클라이언트->서버 객체
            try {
                // ********************* Server Connect ************************* Internet permission 꼭 manifests에 넣을것
                // single tone으로 client->sever 생성

                client = OkHttpInitSingtonManager.getOkHttpClient();
                // 서버요청
                RequestBody requestBody;

                if (title == CATEGORY_ALL) {
                    requestBody = new FormBody.Builder()
                            .add("location", location)
                            .build();
                } else {
                    requestBody = new FormBody.Builder()
                            .add("location", location)
                            .add("category", title)
                            .build();
                }


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

                    for (int i = 0; i < data.length(); i++) {
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
            if (datas != null && datas.size() > 0) {
                companyInfo = datas;
                adapter.notifyDataSetChanged();
            } else {
                //Toast.makeText(getContext(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
