package cz.cvut.fel.matyapav.showcase.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import java.util.HashMap;

import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.showcase.R;
import cz.cvut.fel.matyapav.showcase.fragments.LoginFragment;

/**
 * Util methods used within showcase application
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class ShowCaseUtils {

    public static String PREFS_NAME = "preferences";
    public static int PRIVATE_MODE = 0;

    /**
     * Sets user in application preferences.
     * @param activity currently running activity
     * @param username user's username
     * @param password user's password
     */
    public static void setUserInPreferences(Activity activity, String username, String password){
        SharedPreferences mySharedPreferences= activity.getSharedPreferences(PREFS_NAME, PRIVATE_MODE); //0 is private mode
        SharedPreferences.Editor editor= mySharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     * Removes user from application preferences
     * @param activity currently running activity
     */
    public static void clearUserInPreferences(Activity activity){
        SharedPreferences mySharedPreferences = activity.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor= mySharedPreferences.edit();
        editor.remove("username");
        editor.remove("password");
        editor.commit();
    }

    /**
     * Gets user credentials from application preferences.
     * @param activity currently running activity
     * @return user credentials in hashmap
     */
    public static HashMap<String, String> getUserCredentials(Activity activity){
        SharedPreferences mySharedPreferences = activity.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        String username = mySharedPreferences.getString("username", null);
        String password = mySharedPreferences.getString("password", null);
        if(username != null && password != null){
            HashMap<String, String> result = new HashMap<>();
            result.put("username", username);
            result.put("password", password);
            return result;
        }
        return null;
    }

    /**
     * Gets user login from application preferences.
     * @param activity currently running acitivity
     * @return username of logged user
     */
    public static String getUserLogin(Activity activity){
        SharedPreferences mySharedPreferences = activity.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        String username = mySharedPreferences.getString("username", null);
        return username;
    }

    /**
     * Refreshes current fragment.
     * @param activity currently running acitvity
     */
    public static void refreshCurrentFragment(FragmentActivity activity){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment current = fragmentManager.findFragmentById(R.id.mainLayout);
        if(current instanceof LoginFragment){
            Menu menu = ((NavigationView) activity.findViewById(R.id.nav_view)).getMenu();
            menu.setGroupVisible(R.id.beforeLoginGroup, true);
            menu.setGroupVisible(R.id.afterLoginGroup, false);
        }
        fragmentManager.beginTransaction().detach(current).attach(current).commit();
    }

    /**
     * Converts dp units to pixel units.
     * @param dps number of dp units to convert
     * @param context current application context.
     * @return
     */
    public static int convertDpToPixels(int dps, Context context){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    /**
     * Shows dialog with building failed message.
     * @param activity currently running activity
     * @param e exception which occured
     */
    public static void showBuildingFailedDialog(Activity activity, Exception e){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(Localization.translate("error.building.failed"));
        alertDialog.setMessage(Localization.translate("error.reason")+" : " + e.getMessage());
        alertDialog.show();
    }

}
