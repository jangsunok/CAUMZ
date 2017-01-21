package jdmz.jdmz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by jang on 2017. 1. 5..
 */

public class RandomPagerFragment extends Fragment {

    int id, delivery;
    String title, location, category, best_menu, menu_title, menu_subtitle, menu_writer, menu_image;

    public RandomPagerFragment() {

    }

    public static RandomPagerFragment newInstance(RandomInfo randomInfo) {
        RandomPagerFragment fragment = new RandomPagerFragment();
        Bundle args = new Bundle();
        args.putInt("id", randomInfo.getId());
        args.putString("title", randomInfo.getTitle());
        args.putString("location", randomInfo.getLocation());
        args.putString("category", randomInfo.getCategory());
        args.putString("best_menu", randomInfo.getBest_menu());
        args.putInt("delivery", randomInfo.getDelivery());

        args.putString("menu_title", randomInfo.getImageTitle());
        //args.putString("menu_subtitle", randomInfo.getSubtitle());
        args.putString("menu_image", randomInfo.getImage());
        args.putString("menu_writer", randomInfo.getWriter());

        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.random_item, container, false);
        id = getArguments().getInt("id");
        title = getArguments().getString("title");
        location = getArguments().getString("location");
        category = getArguments().getString("category");
        best_menu = getArguments().getString("best_menu");
        delivery = getArguments().getInt("delivery");

        menu_title = getArguments().getString("menu_title");
        //menu_subtitle = getArguments().getString("menu_subtitle");
        menu_image = getArguments().getString("menu_image");
        menu_writer = getArguments().getString("menu_writer");



        ImageView imageView = (ImageView)v.findViewById(R.id.random_viewpager_image);
        TextView imgTitleView = (TextView)v.findViewById(R.id.random_viewpager_imgtitle);
        //TextView subtitleView = (TextView)v.findViewById(R.id.random_viewpager_subtitle);
        TextView writerView = (TextView)v.findViewById(R.id.random_viewpager_writer);

        TextView comTitleView = (TextView)v.findViewById(R.id.random_viewpager_title);
        TextView categoryView = (TextView)v.findViewById(R.id.random_viewpager_category);
        TextView locationView = (TextView)v.findViewById(R.id.random_viewpager_location);
        TextView deliveryView = (TextView)v.findViewById(R.id.random_viewpager_delivery);
        TextView bestmenuView = (TextView)v.findViewById(R.id.random_viewpager_best);

        ImageView gradation = (ImageView)v.findViewById(R.id.random_viewpager_gradation);

        if(menu_image!=""){
            Glide.with(getContext()).load(menu_image).into(imageView);
            imgTitleView.setText(menu_title);
            writerView.setText("by."+menu_writer);
        }else {
            gradation.setVisibility(View.GONE);
        }

        if(delivery==1){
            deliveryView.setVisibility(View.VISIBLE);
        }else {
            deliveryView.setVisibility(View.GONE);
        }
        comTitleView.setText(title);
        categoryView.setText(category);
        locationView.setText(location);
        bestmenuView.setText(best_menu);

        CardView cardView = (CardView)v.findViewById(R.id.random_viewpager_cardview);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });

        return v;

    }

}
