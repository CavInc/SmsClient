package tk.cavinc.smsclient.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.drawerlayout.widget.DrawerLayout;
import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.data.models.SimDataModel;
import tk.cavinc.smsclient.ui.fragments.HistoryFragment;
import tk.cavinc.smsclient.ui.fragments.MainFragment;
import tk.cavinc.smsclient.ui.fragments.MessageTxtFragment;
import tk.cavinc.smsclient.ui.fragments.PhoneListFragment;
import tk.cavinc.smsclient.ui.fragments.SettingFragment;
import tk.cavinc.smsclient.ui.fragments.ShortMsgTxtFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int REQUEST_WRITER = 219;
    private static final int REQUEST_SMS = 218;
    private static final String TAG = "MA";
    private static final int REQUEST_PHONE = 214;
    private DataManager mDataManager;
    private DrawerLayout mNavigationDrawer;
    private NavigationView navigationView;
    private ActionBar actionBar;

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
        actionBar = getSupportActionBar();
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
        //testDuoSim();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_tr:
                viewFragment(new MainFragment(),"MAIN");
                navigationView.setCheckedItem(R.id.main_tr);
                actionBar.setTitle("Оправка СМС");
                break;
            case R.id.message_tr:
                viewFragment(new MessageTxtFragment(),"MESSAGE");
                navigationView.setCheckedItem(R.id.message_tr);
                actionBar.setTitle("Сообщения");
                break;
            case R.id.short_msg_tr:
                viewFragment(new ShortMsgTxtFragment(),"SHORT");
                navigationView.setCheckedItem(R.id.short_msg_tr);
                actionBar.setTitle("Шорткаты");
                break;
            case R.id.history_tr:
                viewFragment(new HistoryFragment(),"HISTORY");
                navigationView.setCheckedItem(R.id.history_tr);
                actionBar.setTitle("История сообщений");
                break;
            case R.id.setting_tr:
                viewFragment(new SettingFragment(),"SETTING");
                navigationView.setCheckedItem(R.id.setting_tr);
                actionBar.setTitle("Настройки");
                break;
            case R.id.phone_tr:
                viewFragment(new PhoneListFragment(),"PHONE");
                navigationView.setCheckedItem(R.id.phone_tr);
                actionBar.setTitle("Телефоны");
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

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_PHONE);
        } else {
            testDuoSim();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE:
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // проверяем на 2 симочность
    private void testDuoSim(){
        /*
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Log.i("OmSai ", "Single or Dula Sim "+manager.getPhoneCount());

        Log.i("OmSai ", "Defualt device ID "+manager.getDeviceId());
        Log.i("OmSai ", "Single 1 "+manager.getDeviceId(0));
        Log.i("OmSai ", "Single 2 "+manager.getDeviceId(1));
        */

        SubscriptionManager subscriptionManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            subscriptionManager = SubscriptionManager.from(getApplicationContext());
            @SuppressLint("MissingPermission") List<SubscriptionInfo> activeSubscriptionInfoList
                    = subscriptionManager.getActiveSubscriptionInfoList();
            int simCount = activeSubscriptionInfoList.size();
            Log.d("MainActivity: ","simCount:" +simCount);
            mDataManager.getSimDataModel().clear();
            for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                Log.d("MainActivity: ","iccId :"+ subscriptionInfo.getIccId()+" , name : "+ subscriptionInfo.getDisplayName());
                Log.d(TAG,"SIM SLOT IDNDEX : "+subscriptionInfo.getSimSlotIndex());
                mDataManager.getSimDataModel().add(new SimDataModel(subscriptionInfo.getSimSlotIndex(),
                        subscriptionInfo.getDisplayName().toString()));
            }
        }
    }


}
