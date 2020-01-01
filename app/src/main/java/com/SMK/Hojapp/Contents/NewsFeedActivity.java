package com.SMK.Hojapp.Contents;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.SMK.Hojapp.Chat.ChatRoomListActivity;
import com.SMK.Hojapp.Contents.Fragment.*;
import com.SMK.Hojapp.Profile.ProfileActivity;
import com.SMK.Hojapp.R;

import java.util.ArrayList;
import java.util.List;
/*
 * 로그인 액티비티 이후에 나오는 타임라인, 마켓 등을 포함하는 액티비티
 */
public class NewsFeedActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "NewsFeedActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        findViewById(R.id.searchButton).setOnClickListener(this);
        findViewById(R.id.chatButton).setOnClickListener(this);
        findViewById(R.id.notiButton).setOnClickListener(this);
        findViewById(R.id.profileButton).setOnClickListener(this);
        findViewById(R.id.writeButton).setOnClickListener(this);

        // [데이터 초기화 시작]
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // [데이터 초기화 완료]

        // [탭 레이아웃과 뷰페이저 연결]
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TimeLine(), "타임라인");
        adapter.addFrag(new Chitchat(), "잡담/이야기");
        adapter.addFrag(new Market(), "사고팔기");
        adapter.addFrag(new Realty(), "방 구하기");
        adapter.addFrag(new Job(), "알바 구하기");
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
        }
    }

}
