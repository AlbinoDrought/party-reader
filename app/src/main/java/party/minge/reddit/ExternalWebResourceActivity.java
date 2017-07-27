package party.minge.reddit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_external_web_resource)
public class ExternalWebResourceActivity extends Activity {
    @ViewById
    protected WebView webview;

    @Extra
    protected String url;

    @AfterViews
    @UiThread
    @SuppressLint("SetJavaScriptEnabled")
    protected void visitUrl() {
        this.webview.setWebChromeClient(new WebChromeClient());

        // the default webview client will occasionally open the browser
        // we don't want this - we want content to be displayed,
        // by default, in the app.
        this.webview.setWebViewClient(new WebViewClient());

        // allow js
        this.webview.getSettings().setJavaScriptEnabled(true);

        this.webview.loadUrl(this.url);
    }
}
