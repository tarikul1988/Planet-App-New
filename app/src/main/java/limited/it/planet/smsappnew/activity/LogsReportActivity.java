package limited.it.planet.smsappnew.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import limited.it.planet.smsappnew.R;
import limited.it.planet.smsappnew.util.Constant;
import limited.it.planet.smsappnew.util.FontCustomization;
import limited.it.planet.smsappnew.util.SessionClear;
import me.drakeet.materialdialog.MaterialDialog;

import static limited.it.planet.smsappnew.util.SaveValueSharedPreference.saveBoleanValueSharedPreferences;

public class LogsReportActivity extends DemoActivity {
    Toolbar toolbar;
    private ProgressDialog progressBar;
    AlertDialog alertDialog;
    WebView webView;
    //ImageView imgvHome;
    MaterialDialog alert;;
    String logsReportAPICall = "";
    public static String TAG = "cookie";
    public static String TAG_PRO = "progress";
    TextView txvHeadlineToolbar;
    FontCustomization fontCustomization;

    SessionClear sessionClear;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_report);
        toolbar = (Toolbar)findViewById(R.id.toolbar_logs_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                Intent intent = new Intent(LogsReportActivity.this,ReportDashboard.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(LogsReportActivity.this);
            }
        });


        initViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initViews(){

        sessionClear = new SessionClear(LogsReportActivity.this);
        logsReportAPICall = Constant.logsReportAPI;
        webView = (WebView) findViewById(R.id.webview);
        txvHeadlineToolbar = (TextView)findViewById(R.id.txv_headline_sms);
        fontCustomization = new FontCustomization(LogsReportActivity.this);

        alertDialog = new AlertDialog.Builder(this).create();


        // listDialogue();
        txvHeadlineToolbar.setTypeface(fontCustomization.getTexgyreHerosRegular());


//        progressBar = ProgressDialog.show(LogsReportActivity.this,
//                getString(R.string.progress_please_wait), getString(R.string.progress_loading));
//        progressBar.setCancelable(true);

        startProgress();
        loadLogsWebView();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void loadLogsWebView(){
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Log.i(TAG, "Processing webview url click...");
                Log.d("My Webview", url);
                // linearLayoutWarning.setVisibility(View.VISIBLE);
                // viewDialog.showDialog(AllReportActivity.this,"This Service is not Available for this account.");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {

//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//
//
//                }
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
             //   progressBar.isShowing();

            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Log.e(TAG, "Error: " + description);
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                        "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });

                alertDialog.show();
            }
        });


        webView.loadUrl(logsReportAPICall);
    }
    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logs, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Sudip: 20170212
        switch (item.getItemId()) {

            case R.id.logs_logout:

                clearCookies(LogsReportActivity.this);
                saveBoleanValueSharedPreferences("islogin",false,LogsReportActivity.this);

                Intent intent = new Intent(LogsReportActivity.this,LoginActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(LogsReportActivity.this);
                break;


            default:
                return super.onOptionsItemSelected(item);
        }


        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            // Log.d(C.TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            //Log.d(C.TAG, "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    private void startProgress() {

        progressON("Loading....");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF();
            }
        }, 3500);

    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        sessionClear.clearSessionWhenPause();
//    }
}
