package tk.cavinc.smsclient.ui;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.services.SenserSmsService;
import tk.cavinc.smsclient.ui.fragments.HistoryFragment;
import tk.cavinc.smsclient.ui.fragments.MainFragment;
import tk.cavinc.smsclient.ui.fragments.MessageTxtFragment;
import tk.cavinc.smsclient.ui.fragments.PhoneListFragment;
import tk.cavinc.smsclient.ui.fragments.SettingFragment;
import tk.cavinc.smsclient.ui.fragments.ShortMsgTxtFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int REQUEST_WRITER = 219;
    private static final int REQUEST_SMS = 218;
    private DataManager mDataManager;
    private DrawerLayout mNavigationDrawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = DataManager.getInstance();
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setupToolbar();
        setupDrower();

        viewFragment(new MainFragment(),"MAIN");
    }

    private void setupDrower() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.main_tr);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setupToolbar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPermission();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_tr:
                viewFragment(new MainFragment(),"MAIN");
                navigationView.setCheckedItem(R.id.main_tr);
                break;
            case R.id.message_tr:
                viewFragment(new MessageTxtFragment(),"MESSAGE");
                navigationView.setCheckedItem(R.id.message_tr);
                break;
            case R.id.short_msg_tr:
                viewFragment(new ShortMsgTxtFragment(),"SHORT");
                navigationView.setCheckedItem(R.id.short_msg_tr);
                break;
            case R.id.history_tr:
                viewFragment(new HistoryFragment(),"HISTORY");
                navigationView.setCheckedItem(R.id.history_tr);
                break;
            case R.id.setting_tr:
                viewFragment(new SettingFragment(),"SETTING");
                navigationView.setCheckedItem(R.id.setting_tr);
                break;
            case R.id.phone_tr:
                viewFragment(new PhoneListFragment(),"PHONE");
                navigationView.setCheckedItem(R.id.phone_tr);
                break;
        }
        mNavigationDrawer.closeDrawer(GravityCompat.START);
        return false;
    }

    // устанавливаем фрагмент в контейнер
    private void viewFragment(Fragment fragment, String tag){
        FragmentTransaction trz = getFragmentManager().beginTransaction();
        trz.replace(R.id.content,fragment,tag);
        trz.commit();
    }

    private void setPermission(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITER);
        }

        /*
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},REQUEST_SMS);
        }
        */

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},REQUEST_SMS);
        }
    }

    // запуск сервиса
    public void onStartService(View v){
        //mDataManager.getDB().deleteAllQuery();
        Intent intent = new Intent(this, SenserSmsService.class);
        // тодо тут надо добавить прверку на версию
        //if (Build.VERSION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    // остановка сервиса
    public void onStopService(View v){
        Intent intent = new Intent(this,SenserSmsService.class);
        stopService(intent);
    }

}
