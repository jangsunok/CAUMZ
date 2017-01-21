package jdmz.jdmz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static jdmz.jdmz.CAUMZConfig.REQUEST_MODIFY_URL;


public class ModifyActivity extends AppCompatActivity {

    EditText editText;
    int com_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        String title = getIntent().getStringExtra("title");
        com_id = getIntent().getIntExtra("id", 0);

        getSupportActionBar().setTitle("수정 요청");
        getSupportActionBar().setSubtitle(title);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editText = (EditText)findViewById(R.id.modify_edittext);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                if(editText.getText().length()>0){
                    requestModify(com_id, editText.getText().toString());
                }
                //Toast.makeText(ModifyActivity.this, com_id+"/"+editText.getText(), Toast.LENGTH_LONG).show();
                return false;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    void requestModify(final int com_id, final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // URL 입력
                String urlText = REQUEST_MODIFY_URL;
                Response response = null;           //  응답 객체
                OkHttpClient client;                //  클라이언트->서버 객체
                try {
                    // ********************* Server Connect ************************* Internet permission 꼭 manifests에 넣을것
                    // single tone으로 client->sever 생성

                    client = OkHttpInitSingtonManager.getOkHttpClient();
                    // 서버요청
                    RequestBody requestBody = new FormBody.Builder()
                            .add("company_id", com_id+"")
                            .add("request", text)
                            .build();

                    Request request = new Request.Builder()
                            .url(String.format(urlText))
                            .post(requestBody)
                            .build();

                    response = client.newCall(request).execute();       // 서버 응답
                    String text = response.body().string();             // String으로 받음

                    // ********************* Json Parsing *************************
                    Log.d("text", text);
                    JSONObject jobject = new JSONObject(text);          //JSONObject 생성
                    if (jobject.getBoolean("error")) {
                        //에러
                    } else {
                        response.body().close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifyActivity.this, "요청이 접수되었습니다", Toast.LENGTH_SHORT).show();
                                editText.setText("");
                            }
                        });
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
