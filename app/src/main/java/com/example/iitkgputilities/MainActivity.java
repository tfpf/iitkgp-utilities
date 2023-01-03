package com.example.iitkgputilities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iitkgputilities.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_moodle, R.id.nav_csemoodle)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // CSE Moodle.
    public void save_csemoodle(View view)
    {
        save(Constants.csemoodle, findViewById(R.id.uid_csemoodle), findViewById(R.id.pw_csemoodle));
    }
    public void proceed_csemoodle(View view)
    {
        proceed(Constants.csemoodle, findViewById(R.id.wv_csemoodle), "https://moodlecse.iitkgp.ac.in/moodle/login/index.php", "username", "password", "loginbtn", true);
    }

    // Institute Moodle.
    public void save_moodle(View view)
    {
        save(Constants.moodle, findViewById(R.id.uid_moodle), findViewById(R.id.pw_moodle));
    }
    public void proceed_moodle(View view)
    {
        proceed(Constants.moodle, findViewById(R.id.wv_moodle), "http://kgpmoodlenew.iitkgp.ac.in/moodle/login/index.php", "username", "password", "loginbtn", true);
    }

    // ERP.
    public void save_erp(View view)
    {
        save(Constants.erp, findViewById(R.id.uid_erp), findViewById(R.id.pw_erp));
    }
    public void proceed_erp(View view)
    {
        WebView wv = findViewById(R.id.wv_erp);
        proceed(Constants.erp, wv, "https://erp.iitkgp.ac.in/SSOAdministration/login.htm?requestedUrl=https://erp.iitkgp.ac.in/IIT_ERP3/home.htm", "user_id", "password", "loginFormSubmitButton", false);
        wv.postDelayed(() -> wv.loadUrl("javascript:document.getElementById('answer').focus();"), 3000);
    }

    /***********************************************************************************************
     * Save the user's credentials.
     *
     * @param name Name of the shared preferences file to write the credentials to.
     * @param uid_view View containing the user ID.
     * @param pw_view View containing the password.
     **********************************************************************************************/
    public void save(String name, TextView uid_view, TextView pw_view)
    {
        SharedPreferences preferences = getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("uid", uid_view.getText().toString());
        editor.putString("pw", pw_view.getText().toString());
        editor.apply();
        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
    }

    /***********************************************************************************************
     * Enter the user's credentials on the login page.
     *
     * @param name Name of the shared preferences file to read the credentials from.
     * @param wv View to load the login page in.
     * @param url Address of the login page.
     * @param uid_id HTML ID of the field to enter the user ID into.
     * @param pw_id HTML ID of the field to enter the password into.
     * @param btn_id HTML ID of the button which logs the user in.
     * @param log_in Whether or not to click the above button.
     **********************************************************************************************/
    public void proceed(String name, WebView wv, String url, String uid_id, String pw_id, String btn_id, boolean log_in)
    {
        SharedPreferences preferences = getSharedPreferences(name, MODE_PRIVATE);
        String uid = preferences.getString("uid", "");
        String pw = preferences.getString("pw", "");
        if(uid.isEmpty() || pw.isEmpty())
        {
            error_no_uid_pw();
            return;
        }

        load_page(wv, url);
        wv.setWebViewClient(new WebViewClient()
        {
            private boolean attempted = false;
            public boolean shouldOverrideUrlLoading(WebView wv, String url)
            {
                wv.loadUrl(url);
                return true;
            }
            public void onPageFinished(WebView wv, String url)
            {
                // Try to log in only once. A flurry of attempts may be flagged as a DoS attack.
                if(!attempted)
                {
                    attempted = true;
                    wv.loadUrl("javascript:(function() { document.getElementById('" + uid_id + "').value = '" + uid + "'; ;})()");
                    wv.loadUrl("javascript:(function() { document.getElementById('" + pw_id + "').value = '" + pw + "'; ;})()");

                    // The security question is not displayed on the ERP login page unless the user
                    // ID field is filled and the password field has focus.
                    wv.requestFocus(View.FOCUS_DOWN);
                    wv.loadUrl("javascript:document.getElementById('" + pw_id + "').focus();");
                    if(log_in)
                    {
                        wv.postDelayed(() -> wv.loadUrl("javascript:document.getElementById('" + btn_id + "').click();"), 1000);
                    }
                }
            }
        });
    }

    /***********************************************************************************************
     * Display an error about a missing user ID or password.
     **********************************************************************************************/
    public void error_no_uid_pw()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Error");
        dialog.setMessage("User ID or password not provided");
        dialog.setPositiveButton("OK", null);
        dialog.show();
    }

    /***********************************************************************************************
     * Load a webpage.
     *
     * @param wv View to load the webpage in.
     * @param url Address of the webpage.
     **********************************************************************************************/
    public void load_page(WebView wv, String url)
    {
        wv.setVisibility(View.VISIBLE);
        WebSettings settings = wv.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        wv.loadUrl(url);
    }
}
