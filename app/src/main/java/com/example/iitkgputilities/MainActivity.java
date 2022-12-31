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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
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

    /***********************************************************************************************
     * Save credentials.
     **********************************************************************************************/
    public void save_erp(View view)
    {
        SharedPreferences preferences = getSharedPreferences("iitkgp-utilities-erp", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("uid", ((TextView)findViewById(R.id.uid_erp)).getText().toString());
        editor.putString("pw", ((TextView)findViewById(R.id.pw_erp)).getText().toString());
        editor.apply();
        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
    }

    /***********************************************************************************************
     * Open login page.
     **********************************************************************************************/
    public void proceed_erp(View view)
    {
        SharedPreferences preferences = getSharedPreferences("iitkgp-utilities-erp", MODE_PRIVATE);
        String uid = preferences.getString("uid", "");
        String pw = preferences.getString("pw", "");
        if(uid.isEmpty() || pw.isEmpty())
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Error");
            dialog.setMessage("User ID or password not provided");
            dialog.setPositiveButton("OK", null);
            dialog.show();
            return;
        }

        String login_url = "https://erp.iitkgp.ac.in/SSOAdministration/login.htm?requestedUrl=https://erp.iitkgp.ac.in/IIT_ERP3/home.htm";
        WebView wv = new WebView(MainActivity.this);
        setContentView(wv);
        WebSettings settings = wv.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        wv.loadUrl(login_url);

        // Type credentials.
        wv.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView wv, String url)
            {
                if(url.startsWith("https://erp.iitkgp.ac.in"))
                {
                    wv.loadUrl(url);
                    return true;
                }
                return false;
            }
            public void onPageFinished(WebView wv, String url)
            {
                if(!url.equals(login_url))
                {
                    return;
                }
                wv.loadUrl("javascript:(function() { document.getElementById('user_id').value = '" + uid + "'; ;})()");
                wv.loadUrl("javascript:(function() { document.getElementById('password').value = '" + pw + "'; ;})()");
                wv.requestFocus(View.FOCUS_DOWN);
                wv.loadUrl("javascript:document.getElementById('password').focus();");

                // Focusing away from the user ID entry causes a new entry to appear. Wait for some
                // time and focus on said entry.
                wv.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        wv.loadUrl("javascript:document.getElementById('answer').focus();");
                    }
                }, 1000);
            }
        });
    }

    /***********************************************************************************************
     * Save credentials.
     **********************************************************************************************/
    public void save_moodle(View view)
    {
        SharedPreferences preferences = getSharedPreferences("iitkgp-utilities-moodle", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("uid", ((TextView)findViewById(R.id.uid_moodle)).getText().toString());
        editor.putString("pw", ((TextView)findViewById(R.id.pw_moodle)).getText().toString());
        editor.apply();
        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
    }

    /***********************************************************************************************
     * Open login page.
     **********************************************************************************************/
    public void proceed_moodle(View view)
    {
        SharedPreferences preferences = getSharedPreferences("iitkgp-utilities-moodle", MODE_PRIVATE);
        String uid = preferences.getString("uid", "");
        String pw = preferences.getString("pw", "");
        if(uid.isEmpty() || pw.isEmpty())
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Error");
            dialog.setMessage("User ID or password not provided");
            dialog.setPositiveButton("OK", null);
            dialog.show();
            return;
        }

        String login_url = "http://kgpmoodlenew.iitkgp.ac.in/moodle/login/index.php";
        WebView wv = new WebView(MainActivity.this);
        setContentView(wv);
        WebSettings settings = wv.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        wv.loadUrl(login_url);

        // Type credentials.
        wv.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView wv, String url)
            {
                if(url.startsWith("http://kgpmoodlenew.iitkgp.ac.in"))
                {
                    wv.loadUrl(url);
                    return true;
                }
                return false;
            }
            public void onPageFinished(WebView wv, String url)
            {
                if(!url.equals(login_url))
                {
                    return;
                }
                wv.loadUrl("javascript:(function() { document.getElementById('username').value = '" + uid + "'; ;})()");
                wv.loadUrl("javascript:(function() { document.getElementById('password').value = '" + pw + "'; ;})()");
                wv.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        wv.loadUrl("javascript:document.getElementById('loginbtn').click();");
                    }
                }, 1000);
            }
        });
    }

    public void proceed_csemoodle(View view) {
        WebView wv = (WebView)findViewById(R.id.wv_csemoodle);
        WebSettings settings = wv.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        wv.loadUrl("https:www.google.com");
    }

    public void save_csemoodle(View view) {
    }
}