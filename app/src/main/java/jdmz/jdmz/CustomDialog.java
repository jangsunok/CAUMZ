package jdmz.jdmz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by jang on 2016. 12. 30..
 */

public class CustomDialog extends Dialog {
    Context mContext;
    TextView modify, photo;
    ImageButton closeBtn;
    String title;
    int com_id;



    public CustomDialog(Context context) {
        super(context);
        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        mContext = context;

        setContentView(R.layout.detail_dialog);


        closeBtn = (ImageButton)findViewById(R.id.dialog_close);
        modify = (TextView)findViewById(R.id.dialog_modify);
        photo = (TextView)findViewById(R.id.dialog_photo);

        closeBtn.setOnClickListener(mClickListener);
        modify.setOnClickListener(mClickListener);
        photo.setOnClickListener(mClickListener);

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
                case R.id.dialog_modify:
                    Intent intent = new Intent(mContext, ModifyActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("id", com_id);
                    mContext.startActivity(intent);
                    break;
                case R.id.dialog_photo:
                    Intent intent2 = new Intent(mContext, PhotoRequestActivity.class);
                    intent2.putExtra("title", title);
                    intent2.putExtra("id", com_id);
                    mContext.startActivity(intent2);
                    break;
            }
        }
    };

}