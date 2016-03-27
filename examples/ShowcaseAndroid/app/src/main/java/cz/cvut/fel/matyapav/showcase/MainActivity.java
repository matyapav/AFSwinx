package cz.cvut.fel.matyapav.showcase;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.enums.SupportedLanguages;
import cz.cvut.fel.matyapav.showcase.fragments.AbsenceManagementFragment;
import cz.cvut.fel.matyapav.showcase.fragments.AbsenceTypeManagementFragment;
import cz.cvut.fel.matyapav.showcase.fragments.CountriesFragment;
import cz.cvut.fel.matyapav.showcase.fragments.CreateAbsenceFragment;
import cz.cvut.fel.matyapav.showcase.fragments.LoginFragment;
import cz.cvut.fel.matyapav.showcase.fragments.MyAbsencesFragment;
import cz.cvut.fel.matyapav.showcase.fragments.ProfileFragment;
import cz.cvut.fel.matyapav.showcase.fragments.WelcomeFragment;
import cz.cvut.fel.matyapav.showcase.utils.ShowCaseUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);
        //set localization
        Localization.setContext(getThisActivity());
        Localization.setPathToStrings("cz.cvut.fel.matyapav.showcase");

        if(savedInstanceState != null && Localization.getCurrentLanguage() != null){
            Localization.changeLanguage(Localization.getCurrentLanguage());
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if(savedInstanceState == null) {

            Menu menu = navigationView.getMenu();
            menu.setGroupVisible(R.id.beforeLoginGroup, true);
            menu.setGroupVisible(R.id.afterLoginGroup, false);
            //set default login fragment
            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.mainLayout, loginFragment);
            tx.commit();
        }else{

           ShowCaseUtils.refreshCurrentFragment(getThisActivity());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // LOCALIZATION
        int id = item.getItemId();
        if(id == R.id.langCZ){
            Localization.changeLanguage(SupportedLanguages.CZ);

            System.err.println("Current locale: "+getThisActivity().getResources().getConfiguration().locale);
            restartActivity();
            }
        else if(id == R.id.langEN) {
            Localization.changeLanguage(SupportedLanguages.EN);
            System.err.println("Current locale: " + getThisActivity().getResources().getConfiguration().locale);
            restartActivity();
        }
        //do not call restart activity if new language was not set
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.loginForm) {
            fragmentClass = LoginFragment.class;
        }else if(id == R.id.supportedCountries){
            fragmentClass = CountriesFragment.class;
        } else if (id == R.id.userProfile) {
            fragmentClass = ProfileFragment.class;
        }else if( id == R.id.myAbsences) {
            fragmentClass = MyAbsencesFragment.class;
        } else if (id == R.id.absenceManagement) {
            fragmentClass = AbsenceManagementFragment.class;
        } else if (id == R.id.welcome) {
            Toast.makeText(this, Localization.translate("please.wait"), Toast.LENGTH_LONG).show();
            fragmentClass = WelcomeFragment.class;
        }else if (id == R.id.createAbsence) {
            fragmentClass = CreateAbsenceFragment.class;
        }else if (id == R.id.absenceTypeManagement){
            fragmentClass = AbsenceTypeManagementFragment.class;
        } else if(id == R.id.logout) {
            ShowCaseUtils.clearUserInPreferences(getThisActivity());
            Menu menu = ((NavigationView) findViewById(R.id.nav_view)).getMenu();
            menu.setGroupVisible(R.id.beforeLoginGroup, true);
            menu.setGroupVisible(R.id.afterLoginGroup, false);
            fragmentClass = LoginFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public MainActivity getThisActivity() {
        return this;
    }

    private void restartActivity(){
        Intent intent = getIntent();
        Bundle temp_bundle = new Bundle();
        onSaveInstanceState(temp_bundle);
        intent.putExtra("bundle", temp_bundle);
        finish();
        startActivity(intent);
    }


}
