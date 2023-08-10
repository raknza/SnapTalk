package com.example.snapchat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import com.example.snapchat.data.IMqttCallBack;
import com.example.snapchat.provider.DefaultModule;
import com.example.snapchat.provider.MqttClientProvider;
import com.example.snapchat.service.MqttForegroundService;
import com.example.snapchat.ui.authentication.LoginActivity;
import com.example.snapchat.ui.authentication.LoginViewModel;
import com.example.snapchat.utils.JwtInterceptor;
import com.example.snapchat.utils.JwtManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.EntryPointAccessors;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements IMqttCallBack {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavHeaderMainBinding navBinding;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_contact, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        init();


        Intent serviceIntent = new Intent(this, MqttForegroundService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private int notificationIdCounter = 0;
    private void showNotification(String topic, String message) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = notificationIdCounter++;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                .setSmallIcon(R.mipmap.snap)
                .setContentText(topic + " " + message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(notificationId, builder.build());
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
        JwtManager.removeJwt(getApplicationContext());
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActionSuccess(int action, IMqttToken asyncActionToken) {
        showNotification("連線", "大成公");
    }

    @Override
    public void onActionFailure(int action, IMqttToken asyncActionToken, Throwable exception) {
        showNotification("連線", "幹你娘大師敗");
    }

    @Override
    public void onActionFailure(int action, Exception e) {

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Log.e("mqtt", topic + ":" + message.toString());
        if(message.toString().equals("logout"))
            logout();
        showNotification(topic,message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    private void init(){
        navBinding();
        addJwtInterceptor();
        checkAuth();
        mainViewModel.getUserInfo(getApplicationContext());
        mainViewModel.getUser().observe(this, user-> {
            mqttInit(user.username, user.password);
        });
    }

    private void navBinding(){
        // header binding
        NavigationView navigationView = binding.navView;
        navBinding = NavHeaderMainBinding.bind(navigationView.getHeaderView(0));
        navBinding.setViewModel(mainViewModel);
        navBinding.setLifecycleOwner(this);
        navBinding.executePendingBindings();
        Glide.with(navBinding.avatar.getContext())
                .load(R.drawable.avatar_icon)
                .apply(RequestOptions.circleCropTransform())
                .into(navBinding.avatar);
        navBinding.executePendingBindings();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void addJwtInterceptor(){
        // add jwt for each request
        JwtInterceptor jwtInterceptor = (JwtInterceptor) EntryPointAccessors.fromApplication(
                        getApplicationContext(), DefaultModule.ProviderEntryPoint.class).getOkHttpClientProvider()
                .get().interceptors().get(0);
        jwtInterceptor.setToken(JwtManager.getJwtFromSharedPreferences(getApplicationContext()));
    }

    private void checkAuth(){
        // check if auth valid, logout when auth is invalid
        mainViewModel.getIsAuthValid().observe(this, auth -> {
            if(auth == false){
                logout();
            }
        });
        mainViewModel.checkAuth(getApplicationContext());
    }
    private void mqttInit(String username, String password){
        // mqtt connect
        MqttClientProvider mqttClientProvider = MqttClientProvider.getInstance();
        mqttClientProvider.init();
        mqttClientProvider.setIMqttCallBack(this);
        try {
            mqttClientProvider.connect(username,password);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        // subscribe user relations topic
        mqttClientProvider.subscribe(username);
    }

}