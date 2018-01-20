package com.yang.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private WebView webView;
	private Uri mUri;
	private String url;
	String mUrl1 = "file:///android_asset/html/attack_file.html";
	String mUrl2 = "file:///android_asset/html/test.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webView = findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JSInterface(), "jsInterface");
        /**
         * 设为false时，在4.1手机上，attack_file.html通过script访问file:///mnt/sdcard/11.txt显示找不到文件.
         */
		webView.getSettings().setAllowFileAccessFromFileURLs(false);
        /**
         * 即使setAllowFileAccessFromFileURLs为false，仍然可以通过跨域访问漏洞访问本应用的私有数据.
         */
        //webView.getSettings().setAllowFileAccess(false);
        /**
         * 设为false时，在4.1手机上，attack_file.html通过script可以访问file:///mnt/sdcard/11.txt内容.
         */
//		webView.getSettings().setAllowFileAccessFromFileURLs(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			    public boolean onJsAlert(WebView view, String url, String message,JsResult result) {
			    //Required functionality here
			    return super.onJsAlert(view, url, message, result);
			}
		});
//		webView.loadUrl(mUrl1);
//		webView.loadUrl(mUrl2);

        // 通过intent并提取里面的url并加载.
        Intent i = getIntent();
        if (i != null) {
            mUri = i.getData();
        }
        if (mUri != null) {
            url = mUri.toString();
        }
        if (url != null) {
            webView.loadUrl(url);
        }
    }
	
	
    class JSInterface {
	    @JavascriptInterface
        public String onButtonClick(String text) {
            final String str = text;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("leehong2", "onButtonClick: text = " + str);
                    Toast.makeText(getApplicationContext(), "onButtonClick: text = " + str, Toast.LENGTH_LONG).show();
                }
            });
            
            return "This text is returned from Java layer.  js text = " + text;
        }

        @JavascriptInterface
        public void onImageClick(String url, int width, int height) {
            final String str = "onImageClick: text = " + url + "  width = " + width + "  height = " + height;
            Log.i("leehong2", str);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
