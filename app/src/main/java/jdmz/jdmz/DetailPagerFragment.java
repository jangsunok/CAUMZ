package jdmz.jdmz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by jang on 2017. 1. 3..
 */

public class DetailPagerFragment extends Fragment {

    String title, subtitle, writer, image;
    int position, max;

    public DetailPagerFragment() {

    }

    public static DetailPagerFragment newInstance(ImageMenuInfo info, int position, int max) {
        DetailPagerFragment fragment = new DetailPagerFragment();
        Bundle args = new Bundle();
        args.putString("title", info.getTitle());
        args.putString("subtitle", info.getSubtitle());
        args.putString("writer", info.getWriter());
        args.putString("image", info.getImage());
        args.putInt("position", position);
        args.putInt("max", max);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.detail_viewpager, container, false);
        title = getArguments().getString("title");
        subtitle = getArguments().getString("subtitle");
        writer = getArguments().getString("writer");
        image = getArguments().getString("image");
        position = getArguments().getInt("position");
        max = getArguments().getInt("max");

        TextView titleView = (TextView)v.findViewById(R.id.detail_viewpager_title);
        TextView subtitleView = (TextView)v.findViewById(R.id.detail_viewpager_subtitle);
        TextView writerView = (TextView)v.findViewById(R.id.detail_viewpager_writer);
        ImageView imageView = (ImageView)v.findViewById(R.id.detail_viewpager_image);
        TextView indexView = (TextView)v.findViewById(R.id.detail_viewPager_index);

        titleView.setText(title);
        subtitleView.setText(subtitle);
        writerView.setText("by."+writer);
        indexView.setText((position+1)+"/"+max);
        Glide.with(getContext()).load(image).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FullScreenImageActivity.class);
                intent.putExtra("url", image);
                startActivity(intent);
            }
        });

        return v;

    }

}
