package softdesign.com.devintensive.data.managers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import softdesign.com.devintensive.ui.utils.ConstantManager;
import softdesign.com.devintensive.ui.utils.DevintensiveApp;

/**
 * Created by qbai on 28.06.2016.
 */
public class PreferencesManager {

    public static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_MAIL_KEY,
            ConstantManager.USER_VK_KEY, ConstantManager.USER_GIT_KEY, ConstantManager.USER_BIO_KEY};

    private SharedPreferences mSharedPreferences;

    public PreferencesManager(){
        this.mSharedPreferences = DevintensiveApp.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for(int i = 0; i < USER_FIELDS.length; i++){
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> loadProfileData(){
        List<String> userFields = new ArrayList<String>();
        userFields.add(mSharedPreferences.getString(USER_FIELDS[0],"null"));
        userFields.add(mSharedPreferences.getString(USER_FIELDS[1],"null"));
        userFields.add(mSharedPreferences.getString(USER_FIELDS[2],"null"));
        userFields.add(mSharedPreferences.getString(USER_FIELDS[3],"null"));
        userFields.add(mSharedPreferences.getString(USER_FIELDS[4],"null"));
        return userFields;
    }
}
