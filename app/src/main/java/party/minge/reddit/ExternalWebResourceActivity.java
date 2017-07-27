package party.minge.reddit;

import android.app.Activity;
import android.webkit.WebView;

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
    protected void visitUrl() {
        this.webview.loadUrl(this.url);
    }
}
