package com.example.snapchat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.databinding.NavHeaderMainBinding;
import com.example.snapchat.service.MessageReceiveService;
import com.example.snapchat.ui.authentication.LoginActivity;
import com.example.snapchat.utils.DataManager;
import com.example.snapchat.utils.JwtManager;
import com.google.android.material.navigation.NavigationView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.databinding.ActivityMainBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavHeaderMainBinding navBinding;
    MainViewModel mainViewModel;
    NavController navController;
    boolean init = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_contact, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        if(isServiceRunning(MessageReceiveService.class) == false) {
            Intent serviceIntent = new Intent(this, MessageReceiveService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            }
        }
        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onLogoutClick(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout){
            logout();
        }
    }

    private void logout(){
        init = false;
        JwtManager.removeJwt(getApplicationContext());
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void init(){
        JwtManager.addJwtInterceptor(getApplicationContext());
        DataManager.getInstance().fetchData(getApplicationContext());
        DataManager.getInstance().getUserLiveData().observe(this, user ->{
            if(user!= null) {
                init = true;
                navBinding();
            }
        });
    }
    private void navBinding(){
        // header binding
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) {
            navBinding = DataBindingUtil.bind(headerView);
        }
        if (navBinding != null) {
            navBinding.setViewModel(mainViewModel);
            navBinding.setLifecycleOwner(this);
            Glide.with(navBinding.avatar.getContext())
                    .load(DataManager.getInstance().getUserLiveData().getValue().avatar.isEmpty() ? R.drawable.avatar_icon : DataManager.getInstance().getUserLiveData().getValue().avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(navBinding.avatar);
            navBinding.executePendingBindings();
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if (getIntent() != null && getIntent().getStringExtra("openChatroom") != null){
            handleNotificationClick(getIntent());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleNotificationClick(intent);
    }

    private void handleNotificationClick(Intent intent) {
        if (intent != null && intent.getStringExtra("openChatroom") != null) {
            Bundle bundle = new Bundle();
            bundle.putString("openChatroom", intent.getStringExtra("openChatroom"));
            navController.navigate(R.id.nav_home, bundle);
        }
    }

    private void checkAuth(){
        // check if auth valid, logout when auth is invalid
        mainViewModel.getIsAuthValid().observe(this, auth -> {
            if(auth == false){
                Log.e("checkAuth","without check and logout");
                logout();
            }
        });
        Log.e("checkAuth","check auth");
        mainViewModel.checkAuth(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(init == true) {
            Log.e("checkAuth","resume check");
            checkAuth();
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}