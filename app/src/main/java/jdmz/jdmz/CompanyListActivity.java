package jdmz.jdmz;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class CompanyListActivity extends AppCompatActivity {

    TabLayout tab;
    ViewPager viewpager;
    String location;
    String[] tabTitles = {"전체", "한식", "양식", "중식", "일식", "분식", "치킨", "카페", "술집", "기타"};
        //한식양식중식일식분식치킨카페술집

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companylist);

        location = getIntent().getStringExtra("location");

        getSupportActionBar().setTitle(location);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tab = (TabLayout)findViewById(R.id.comlist_tablayout);
        viewpager = (ViewPager)findViewById(R.id.comlist_viewpager);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tab.setupWithViewPager(viewpager);
        for (int i = 0; i < tabTitles.length; i++) {
            tab.getTabAt(i).setText(tabTitles[i]);
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.admob));

        AdView mAdView = (AdView) findViewById(R.id.list_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }



    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //탭 포지션에 따라서 데이터 다르게
            ComListFragment fragment = new ComListFragment().newInstance(location, tabTitles[position]);
            return fragment;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return false;
    }
}
