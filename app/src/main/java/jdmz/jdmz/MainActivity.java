package jdmz.jdmz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Context mContext;

    TextView random, mainGate, middleGate, backGate, school, heuksuk, market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        mainGate = (TextView) findViewById(R.id.main_maingate);
        middleGate = (TextView) findViewById(R.id.main_middlegate);
        backGate = (TextView) findViewById(R.id.main_backgate);
        school = (TextView) findViewById(R.id.main_school);
        heuksuk = (TextView) findViewById(R.id.main_heuksuk);
        market = (TextView) findViewById(R.id.main_market);
        random = (TextView)findViewById(R.id.main_random);


        mainGate.setOnClickListener(this);
        middleGate.setOnClickListener(this);
        backGate.setOnClickListener(this);
        school.setOnClickListener(this);
        heuksuk.setOnClickListener(this);
        market.setOnClickListener(this);
        random.setOnClickListener(this);

        MobileAds.initialize(getApplicationContext(), getString(R.string.admob));
        AdView mAdView = (AdView) findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_maingate:
                Intent i1 = new Intent(MainActivity.this, CompanyListActivity.class);
                i1.putExtra("location", "정문");
                startActivity(i1);
                break;
            case R.id.main_middlegate:
                Intent i2 = new Intent(MainActivity.this, CompanyListActivity.class);
                i2.putExtra("location", "중문");
                startActivity(i2);
                break;
            case R.id.main_backgate:
                Intent i3 = new Intent(MainActivity.this, CompanyListActivity.class);
                i3.putExtra("location", "후문");
                startActivity(i3);
                break;
            case R.id.main_school:
                Intent i4 = new Intent(MainActivity.this, CompanyListActivity.class);
                i4.putExtra("location", "교내");
                startActivity(i4);
                break;
            case R.id.main_heuksuk:
                Intent i5 = new Intent(MainActivity.this, CompanyListActivity.class);
                i5.putExtra("location", "흑석역");
                startActivity(i5);
                break;
            case R.id.main_market:
                Intent i6 = new Intent(MainActivity.this, CompanyListActivity.class);
                i6.putExtra("location", "흑석시장");
                startActivity(i6);
                break;
            case R.id.main_random:
                Intent i7 = new Intent(MainActivity.this, RandomActivity.class);
                startActivity(i7);
//                Toast.makeText(MainActivity.this, "준비중입니다", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent  = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu) | true;
    }

    public static Context getMyContext(){
        return mContext;
    }


}
