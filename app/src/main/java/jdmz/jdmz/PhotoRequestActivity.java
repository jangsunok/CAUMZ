package jdmz.jdmz;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static jdmz.jdmz.CAUMZConfig.CAMERA_DATA;
import static jdmz.jdmz.CAUMZConfig.GALLARY_DATA;
import static jdmz.jdmz.CAUMZConfig.IMAGE_MIME_TYPE;
import static jdmz.jdmz.CAUMZConfig.MY_PERMISSIONS_REQUEST_READ_CONTACTS;
import static jdmz.jdmz.CAUMZConfig.MY_PERMISSIONS_REQUEST_READ_STORAGE;
import static jdmz.jdmz.CAUMZConfig.REQUEST_PHOTO_URL;

public class PhotoRequestActivity extends AppCompatActivity {

    EditText titleView, subtitleView, writerView;
    ImageView addPhoto;
    int com_id;
    Uri uri;
    String title;
    int imgFlag;
    PhotoCustomDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        title = getIntent().getStringExtra("title");
        com_id = getIntent().getIntExtra("id", 0);

        getSupportActionBar().setTitle("사진 제보");
        getSupportActionBar().setSubtitle(title);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgFlag = 0;
        titleView = (EditText)findViewById(R.id.photo_title);
        subtitleView = (EditText)findViewById(R.id.photo_subtitle);
        writerView = (EditText)findViewById(R.id.photo_writer);

        addPhoto = (ImageView)findViewById(R.id.photo_addPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(PhotoRequestActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

                    // 이 권한을 필요한 이유를 설명해야하는가?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(PhotoRequestActivity.this,Manifest.permission.CAMERA)) {

                        // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                        // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다

                    } else {

                        ActivityCompat.requestPermissions(PhotoRequestActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

                    }
                }

                if (ContextCompat.checkSelfPermission(PhotoRequestActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    // 이 권한을 필요한 이유를 설명해야하는가?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(PhotoRequestActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                        // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다

                    } else {

                        ActivityCompat.requestPermissions(PhotoRequestActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_STORAGE);

                        // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

                    }
                }



                dialog = new PhotoCustomDialog(PhotoRequestActivity.this);
                dialog.setData(title, com_id);
                dialog.setOnPhotoDialogListener(new PhotoCustomDialog.photoDialogListener() {
                    @Override
                    public void getFromCamera() {
                        ClickedCamera();
                    }

                    @Override
                    public void getFromGallery() {
                        ClickedGallery();
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
            }
        });


    }

    void ClickedCamera() {
        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent_camera, CAMERA_DATA);
    }

    void ClickedGallery() {
        Intent intent_gallery = new Intent(Intent.ACTION_PICK);
        intent_gallery.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent_gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent_gallery, GALLARY_DATA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
            case MY_PERMISSIONS_REQUEST_READ_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK && intent != null) {

            Bundle extras = intent.getExtras();
            Matrix matrix = new Matrix();
            switch (requestCode) {
                case CAMERA_DATA:
                    uri = intent.getData();
                    addPhoto.setImageURI(uri);
                    imgFlag = 1;
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                break;
                case GALLARY_DATA:
                    uri = intent.getData();
                    Glide.with(PhotoRequestActivity.this)
                            .load(uri).asBitmap()
                            .into(addPhoto);
                    imgFlag = 1;
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        } else{
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                if(titleView.getText().length()>0 && subtitleView.getText().length()>0
                        && writerView.getText().length()>0 &&imgFlag==1){
                    new uploadPhotoAsync(com_id, titleView.getText().toString(), subtitleView.getText().toString()
                            , writerView.getText().toString(), getRealPathFromURI(PhotoRequestActivity.this,uri)).execute();
                }else {
                    Toast.makeText(PhotoRequestActivity.this, "입력이 올바르지 않습니다", Toast.LENGTH_LONG).show();
                }

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



    private class uploadPhotoAsync extends AsyncTask<String, Void, Boolean> {

        String company_id, title, subtitle, writer, uploadfile;


        public uploadPhotoAsync(int com_id, String title, String subtitle, String writer, String uploadfile){
            company_id = com_id+"";
            this.title = title;
            this.subtitle = subtitle;
            this.writer = writer;
            this.uploadfile = uploadfile;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            // URL 입력
            String urlText = REQUEST_PHOTO_URL;

            Response response = null;           //  응답 객체
            OkHttpClient client;                //  클라이언트->서버 객체
            try {
                // ********************* Server Connect ************************* Internet permission 꼭 manifests에 넣을것
                // single tone으로 client->sever 생성



                File file = new File(uploadfile);
                client = OkHttpInitSingtonManager.getOkHttpClient();
                // 서버요청
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("company_id", company_id)
                        .addFormDataPart("title", title)
                        .addFormDataPart("subtitle", subtitle)
                        .addFormDataPart("writer", writer)
                        .addFormDataPart("image", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file))
                        .build();



                Request request = new Request.Builder()
                        .url(String.format(urlText))
                        .post(requestBody)
                        .build();


                response = client.newCall(request).execute();       // 서버 응답
                String text = response.body().string();             // String으로 받음

                // ********************* Json Parsing *************************
                //Log.d("text", "text="+text);

                JSONObject jobject = new JSONObject(text);          //JSONObject 생성
                if (jobject.getBoolean("error")) {
                    //에러
                    return false;
                } else {
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
                Toast.makeText(getApplicationContext(), "요청이 완료 되었습니다", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }else{
                Toast.makeText(getApplicationContext(), "업로드에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

}
