package cz.cvut.fel.matyapav.afandroid.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import java.util.Locale;

/**
 * This class is responsible for translations of strings. Gives user also ability to change language.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class Localization {

    private static String currentLanguage;
    private static String stringsPackage; //must be set externally
    private static Context context; //must be set externally

    /**
     * Translates given resource. Looks for the texts in specified package. If strings are not in root package
     * package must be defines using setStringsPackage method.
     *
     * @param resource key to be translated
     * @return Translation if key was found in localization texts, otherwise key is not translated and returned.
     */
    public static String translate(String resource){
        try {
            int id = context.getResources().getIdentifier(resource, "string", stringsPackage);
            System.err.println("Localization for id "+id+" is "+context.getResources().getString(id));
            return context.getResources().getString(id);
        }catch (Exception e){
            System.err.println("Localization text "+resource+" not found");
           // e.printStackTrace();
            return resource;
        }
    }

    /**
     * Changes language of application.
     *
     * @param lang Shortcut of language. Examples: English = en, Czech = cs etc.
     */
    public static void changeLanguage(String lang){
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang.toLowerCase());
        res.updateConfiguration(conf, dm);
        currentLanguage = lang;
    }

    public static String getCurrentLanguage(){
        return currentLanguage;
    }


    public static void setStringsPackage(String path){
        stringsPackage = path;
    }

    public static void setContext(Context ctx){
        context = ctx;
    }


}
