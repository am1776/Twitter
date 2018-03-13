package com.amallya.twittermvvm.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amallya.twittermvvm.ui.tweets.TweetsFragment;
import com.amallya.twittermvvm.utils.CircularTransform;
import com.amallya.twittermvvm.ui.compose.ComposeFragment;
import com.amallya.twittermvvm.utils.NetworkUtils;
import com.amallya.twittermvvm.R;
import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener, ComposeFragment.OnPostTweetListener {

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.nav_view) NavigationView navigationView;

    TextView tvNavHeader1, tvNavHeader2;
    ImageView ivNavHeader;
    LinearLayout lvNavHeader;
    private ComposeFragment composeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_list);
        ButterKnife.bind(this);
        setViews();
        setupFragment(savedInstanceState);
        final MainViewModel viewModel =
                ViewModelProviders.of(this).get(MainViewModel.class);
        observeViewModel(viewModel);
    }

    private void observeViewModel(MainViewModel mainViewModel){
        mainViewModel.getUserCredential().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    processUserCred(user);
                }
            }
        });
    }

    private void setupFragment(Bundle savedInstanceState){
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, new TweetsFragment(), "TAG").commit();
        }
    }

    private void setViews(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.home);
        setDrawerViews();
    }

    public void onFabClicked(View view){
        showComposeDialog();
    }

    private void setDrawerViews(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        lvNavHeader = ButterKnife.findById(hView, R.id.lv_nav_header);
        tvNavHeader1 = ButterKnife.findById(hView, R.id.tv_nav_header1);
        tvNavHeader2 = ButterKnife.findById(hView, R.id.tv_nav_header2);
        ivNavHeader = ButterKnife.findById(hView, R.id.iv_nav_header);
    }

    private void processUserCred(User user){
        RestApplication.setUser(user);
        tvNavHeader1.setText(user.getName());
        tvNavHeader2.setText(getResources().getString(R.string.screen_name, user.getScreenName()));
        Picasso.with(this)
                .load(user.getProfileImageUrl())
                .transform(new CircularTransform())
                .into(ivNavHeader);
        setNavigationHeaderImage(user);
    }

    private void setNavigationHeaderImage(User user){
        Glide.with(MainActivity.this).load(user.getBannerUrl()).asBitmap().into(new SimpleTarget<Bitmap>(500, 500) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    lvNavHeader.setBackground(drawable);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("result recieved "+requestCode+" "+resultCode);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            String draft = data.getStringExtra("drafts");
            System.out.println("draft recieved "+draft);
            composeDialog.setDraft(draft);
        }
        System.out.println("result recieved");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_sign_out) {
            RestApplication.getRestClient().clearAccessToken();
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        composeDialog = ComposeFragment.newInstance(RestApplication.getUser().getProfileImageUrl(), null);
        composeDialog.show(fm, getString(R.string.compose_dialog));
    }

    public void onTweetPosted(String newTweet){
        NetworkUtils.postTweets(RestApplication.getRestClient(), newTweet, drawer);
        //HomeFragment fragment = (HomeFragment)((CustomFragmentPagerAdapter)viewPager.getAdapter()).getRegisteredFragment(0);
        //fragment.postTweet(newTweet, loggedInUser);
    }
}