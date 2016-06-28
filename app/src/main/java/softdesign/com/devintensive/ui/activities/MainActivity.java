package softdesign.com.devintensive.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import softdesign.com.devintensive.R;
import softdesign.com.devintensive.data.managers.DataManager;
import softdesign.com.devintensive.ui.utils.ConstantManager;
import softdesign.com.devintensive.ui.utils.RoundingBitMap;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    protected CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;
    private ImageView mCallImg;
    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private boolean mCurrentEditMode = false;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserGit, mUserVk, mUserBio, mUserMail;
    private List<EditText> mUserInfoViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mUserBio = (EditText) findViewById(R.id.about_et);
        mUserVk = (EditText) findViewById(R.id.vk_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserGit = (EditText) findViewById(R.id.github_et);
        mUserPhone = (EditText) findViewById(R.id.phone_et);

        mUserInfoViews = new ArrayList<EditText>();

        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);
        mDataManager = DataManager.getInstance();


        setupToolBar();
        setupDrawer();
        saveUserInfoValue();
        loadUserInfoValue();


        if (savedInstanceState == null) {

        } else {
            mCurrentEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY);
            changeEditMode(mCurrentEditMode);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserInfoValue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                showSnackBar("Нажали на розовую кнопопочку");
                if (!mCurrentEditMode) {
                    changeEditMode(true);
                    mCurrentEditMode = true;
                } else {
                    changeEditMode(false);
                    mCurrentEditMode = false;
                    saveUserInfoValue();
                }
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);

    }

    private void runWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 5000);

    }

    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG)
                .show();
    }

    public void setupToolBar() {

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_line_weight_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        setupDrawerHeader(navigationView, getRoundBitmap(R.drawable.user_avatar),
                "Краев Владимир", "awatarmumu0@gmail.com");
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    showSnackBar(item.getTitle().toString());
                    item.setCheckable(true);
                    mDrawerLayout.closeDrawer(GravityCompat.START);

                    return false;
                }
            });
        }
    }

    //Если @param mode == true -> режим редактирования else -> режим просмотра
    private void changeEditMode(boolean mode) {

        if (mode) {
            mFab.setImageResource(R.drawable.ic_check_black_24dp);
            for (EditText et : mUserInfoViews) {
                et.setEnabled(true);
                et.setFocusable(true);
                et.setFocusableInTouchMode(true);
            }
        } else {
            mFab.setImageResource(R.drawable.ic_edit_black_24dp);
            for (EditText et : mUserInfoViews) {
                et.setEnabled(false);
                et.setFocusable(false);
                et.setFocusableInTouchMode(false);

            }

        }


    }

    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<String>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private Bitmap getRoundBitmap(int drawRes) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawRes);
        bitmap = RoundingBitMap.getRoundedBitmap(bitmap);
        return bitmap;
    }

    private void setupDrawerHeader(NavigationView navigationView, Bitmap avatar, String name, String email) {
        View view = navigationView.getHeaderView(0);
        if (avatar != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.user_avatar);
            imageView.setImageBitmap(avatar);
        }
        if (name != null) {
            TextView textView = (TextView) view.findViewById(R.id.user_name_txt);
            textView.setText(name);
        }
        if (email != null) {
            TextView textView = (TextView) view.findViewById(R.id.user_email_txt);
            textView.setText(email);
        }
    }
}
