package jdmz.jdmz;

import android.app.Activity;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by jang on 2017. 1. 5..
 */

public class FullScreenImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreenimage);

        String url = getIntent().getStringExtra("url");
        PhotoView photoView = (PhotoView) findViewById(R.id.full_image);
        Glide.with(this).load(url).thumbnail(0.1f).into(photoView);


    }
}
