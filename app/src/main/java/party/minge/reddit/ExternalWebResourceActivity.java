package party.minge.reddit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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
    protected String domain;

    @Extra
    protected String url;

    @AfterViews
    protected void showUp() {
        this.getActionBar().setDisplayShowHomeEnabled(false);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @AfterViews
    protected void useDomainAsTitle() {
        this.setTitle(this.domain);
    }

    @AfterViews
    @SuppressLint("SetJavaScriptEnabled")
    protected void visitUrl() {
        this.webview.setWebChromeClient(new WebChromeClient());

        // the default webview client will occasionally open the browser
        // we don't want this - we want content to be displayed,
        // by default, in the app.
        this.webview.setWebViewClient(new WebViewClient());

        // allow js
        WebSettings settings = this.webview.getSettings();

        // attempt to initially fit content without requiring zooms
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // allow zooming
        settings.setBuiltInZoomControls(true);

        // allow js
        settings.setJavaScriptEnabled(true);

        this.webview.loadUrl(this.url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // currently, only called when "up" is pressed.
        // close our intent when this occurs.
        this.finish();
        return true;
    }
}
