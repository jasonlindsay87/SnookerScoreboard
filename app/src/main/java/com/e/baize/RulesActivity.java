package com.e.baize;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class RulesActivity extends AppCompatActivity {
    private WebView webViewRules;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_webview_layout);
        webViewRules = (WebView) findViewById(R.id.wvRules);
        webViewRules.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webViewRules.loadUrl("https://en.wikipedia.org/wiki/Rules_of_snooker#Gameplay");
    }

    @Override
    public void onBackPressed(){
        if(webViewRules.canGoBack()){
            webViewRules.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void bClose(View view) {
        Animate.animateButton(view.findViewById(R.id.btnBack));
        finish();
    }
}
