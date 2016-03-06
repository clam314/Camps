package com.clam.camps;

import android.app.ActivityManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.clam.camps.adapter.TabAdapter;
import com.clam.camps.fragment.BeenFragment;
import com.clam.camps.fragment.MainFragment;
import com.clam.camps.utils.LollipopBitmapMemoryCacheParamsSupplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mtoolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private TabLayout tabLayout;
    private FragmentManager fragmentManager;
    private Fragment mContent;
    private BeenFragment beenFragment;
    private MainFragment mainFragment;

    private void initFresco() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig
                .newBuilder(getApplicationContext())
                .setBitmapMemoryCacheParamsSupplier(new LollipopBitmapMemoryCacheParamsSupplier(activityManager))
                .build();

        Fresco.initialize(getApplicationContext(), imagePipelineConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fresco.initialize(this);
        initFresco();
        setContentView(R.layout.activity_main);
        initView();
        fragmentManager = getSupportFragmentManager();
        initFragment(savedInstanceState);
    }

    private void initView() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_ly);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);


        tabLayout = (TabLayout)findViewById(R.id.tabs_ly);

    }

    private void initFragment(Bundle savedInstanceState ){
        if(savedInstanceState == null){
            if(mainFragment == null){
                mainFragment = new MainFragment();
            }
            mContent = mainFragment;
            fragmentManager.beginTransaction().replace(R.id.fragment_main, mainFragment).commit();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.refresh:
                if(MainFragment.class == fragmentManager.findFragmentById(R.id.fragment_main).getClass()){
                    ((MainFragment)fragmentManager.findFragmentById(R.id.fragment_main)).refresh();
                }else if(BeenFragment.class == fragmentManager.findFragmentById(R.id.fragment_main).getClass()){
                    ((BeenFragment)fragmentManager.findFragmentById(R.id.fragment_main)).refresh();
                }
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);


        switch (item.getItemId()) {
            case R.id.homepage:
                if(mainFragment==null){
                    mainFragment = new MainFragment();
                }
                switchContent(mContent,mainFragment);
                Fresco.getImagePipeline().clearMemoryCaches();
                tabLayout.setVisibility(View.VISIBLE);

                break;
            case R.id.been:
                if(beenFragment == null){
                    beenFragment = new BeenFragment("福利",true);
                }
                switchContent(mContent,beenFragment);
                Fresco.getImagePipeline().clearMemoryCaches();
                tabLayout.setVisibility(View.GONE);
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    public void switchContent(Fragment from, Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.fragment_main, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

}


