package jdmz.jdmz;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by jang on 2016. 12. 30..
 */

public class PhotoCustomDialog extends Dialog {
    Context mContext;
    TextView camera, gallery;
    ImageButton closeBtn;
    String title;
    int com_id;
    photoDialogListener listener;

    interface photoDialogListener{
        void getFromCamera();
        void getFromGallery();
    }

    public void setOnPhotoDialogListener(photoDialogListener listener){
        this.listener = listener;
    }



    public PhotoCustomDialog(Context context) {
        super(context);
        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        mContext = context;

        setContentView(R.layout.photo_dialog);


        closeBtn = (ImageButton)findViewById(R.id.dialog_close);
        camera = (TextView)findViewById(R.id.dialog_camera);
        gallery = (TextView)findViewById(R.id.dialog_gallery);

        closeBtn.setOnClickListener(mClickListener);
        camera.setOnClickListener(mClickListener);
        gallery.setOnClickListener(mClickListener);

    }

    public void setData(String title, int com_id){
        this.title = title;
        this.com_id = com_id;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.dialog_close:
                    dismiss();
                    break;
                case R.id.dialog_camera:
                    listener.getFromCamera();
                    dismiss();
                    break;
                case R.id.dialog_gallery:
                    listener.getFromGallery();
                    break;
            }
        }
    };







}