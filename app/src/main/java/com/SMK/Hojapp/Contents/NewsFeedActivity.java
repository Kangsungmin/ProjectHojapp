package com.SMK.Hojapp.Contents;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.SMK.Hojapp.Chat.ChatRoomListActivity;
import com.SMK.Hojapp.Contents.Fragment.*;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.Profile.ProfileActivity;
import com.SMK.Hojapp.R;

import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/*
 * 로그인 액티비티 이후에 나오는 타임라인, 마켓 등을 포함하는 액티비티
 */
public class NewsFeedActivity extends AppCompatActivity implements View.OnClickListener{

    private SpinnerDialog spinnerDialog;
    private String TAG = "NewsFeedActivity";

    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GlobalData globalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_feed);

        globalData = (GlobalData) getApplicationContext().getApplicationContext();
        toolbar = findViewById(R.id.toolbar);
        findViewById(R.id.floatingActionButton).setOnClickListener(this);
        /* 뉴스피드 하단 버튼
        findViewById(R.id.searchButton).setOnClickListener(this);
        findViewById(R.id.chatButton).setOnClickListener(this);
        findViewById(R.id.notiButton).setOnClickListener(this);
        findViewById(R.id.profileButton).setOnClickListener(this);
        findViewById(R.id.writeButton).setOnClickListener(this);
        */

        // [데이터 초기화 시작]
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setSupportActionBar(toolbar);

        toolbar.setTitle("이곳에 위치정보가 표시됩니다.");
        ArrayList<String> locationList = new ArrayList<>();
        for(String city : globalData.getSydneyCityList())
        {
            locationList.add(city);
        }
        spinnerDialog = new SpinnerDialog(NewsFeedActivity.this, locationList, "라이더 검색 위치");
        // [데이터 초기화 완료]

        // [탭 레이아웃과 뷰페이저 연결]
        tabLayout.setupWithViewPager(viewPager);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                String locationName = globalData.getSydneyRegion(i);
                // 현재 위치 수정
                globalData.getAccount().setLocation(locationName);
                toolbar.setTitle(locationName);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.location_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.location_setting:
                // User chose the "Settings" item, show the app settings UI...
                //Toast.makeText(getApplicationContext(), "지역설정 버튼 클릭됨", Toast.LENGTH_LONG).show();
                spinnerDialog.showSpinerDialog();
                return true;
            default:
                return false;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DeliveryService(), "라이더 찾기");
        //adapter.addFrag(new TimeLine(), "타임라인");
        //adapter.addFrag(new Chitchat(), "잡담/이야기");
        //adapter.addFrag(new Market(), "사고팔기");
        //adapter.addFrag(new Realty(), "방 구하기");
        //adapter.addFrag(new Job(), "알바 구하기");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    // 액티비티가 시작할 때.
    @Override
    protected  void onStart() {
        Log.d(TAG, "start activity");
        super.onStart();
    }

    // 액티비티가 화면 상단으로 왔을 때.
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "event occurred.");

        int i = view.getId();
        switch (i) { // Open WriteContentsActivity
            case R.id.floatingActionButton :
            {
                Intent intent = new Intent(NewsFeedActivity.this, RegisterRiderActivity.class); startActivity(intent);
                break;
            }
            /*
            case  R.id.writeButton :
            {
                Intent intent = new Intent(NewsFeedActivity.this, WriteContentsActivity.class); startActivity(intent);
                break;
            }
            case R.id.chatButton:
            {
                Intent intent = new Intent(NewsFeedActivity.this, ChatRoomListActivity.class); startActivity(intent);
                break;
            }
            case R.id.profileButton:
            {
                Intent intent = new Intent(NewsFeedActivity.this, ProfileActivity.class); startActivity(intent);
                break;
            }
            case R.id.searchButton:
            {
                Toast.makeText(this, "준비중인 기능입니다.", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.notiButton:
            {
                Toast.makeText(this, "준비중인 기능입니다.", Toast.LENGTH_SHORT).show();
                break;
            }
             */
        }
    }

}
