package com.amallya.twittermvvm.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.amallya.twittermvvm.BuildConfig;
import com.amallya.twittermvvm.ViewModelFactory;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.ui.base.BaseActivity;
import com.amallya.twittermvvm.ui.tweets.TweetsFragment;
import com.amallya.twittermvvm.utils.CircularTransform;
import com.amallya.twittermvvm.R;
import com.amallya.twittermvvm.models.User;
import com.amallya.twittermvvm.utils.Consts;
import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amallya.twittermvvm.utils.analytics.Events.USER_VISITED;


public class MainActivity extends BaseActivity
implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.nav_view) NavigationView navigationView;

    TextView tvNavHeader1, tvNavHeader2;
    ImageView ivNavHeader;
    LinearLayout lvNavHeader;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_list);
        ButterKnife.bind(this);
        setViews();
        setupFragment(savedInstanceState);
        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        viewModel =
                ViewModelProviders.of(this, factory).get(MainViewModel.class);
        observeViewModel(viewModel);
        Amplitude.getInstance().initialize(this, BuildConfig.AMPLITUDE_TOKEN).enableForegroundTracking(getApplication());
        Amplitude.getInstance().logEvent(USER_VISITED);
    }

    private void observeViewModel(MainViewModel mainViewModel){
        mainViewModel.getUserCredentialObservable().observe(this, response -> handleResponse(response));
        mainViewModel.getAccessTokenClearedObservable().observe(this, val -> finish());
    }

    @Override
    public void handleSuccess(Response response){
        User user = (User)response.getData();
        processUserCred(user);
    }

    @Override
    public void handleError(Response response){
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.drawer_layout),
                getString(R.string.error_msg), Snackbar.LENGTH_SHORT);
        mySnackbar.show();
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
        tvNavHeader1.setText(user.getName());
        tvNavHeader2.setText(getResources().getString(R.string.screen_name, user.getScreenName()));
        Picasso.with(this)
                .load(user.getProfileImageUrl())
                .transform(new CircularTransform())
                .into(ivNavHeader);
        setNavigationHeaderImage(user);
    }

    private void setNavigationHeaderImage(User user){
        Glide.with(MainActivity.this).load(user.getBannerUrl()).asBitmap().into(new SimpleTarget<Bitmap>(Consts.NAV_HEADER_WIDTH, Consts.NAV_HEADER_HEIGHT) {
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
            viewModel.clearAccessTokens();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}