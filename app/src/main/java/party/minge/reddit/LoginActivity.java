package party.minge.reddit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.net.URL;

import party.minge.reddit.client.Manager;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {
    @ViewById
    protected WebView webviewLogin;

    @Bean
    protected Manager manager;

    protected Credentials credentials;

    protected OAuthHelper helper;

    protected String[] scopes = new String[] {
        "read",
        "submit",
        "edit",
        "identity",
        "history",
        "save",
        "subscribe",
        "vote",
        "wikiread",
    };

    @AfterInject
    protected void init() {
        this.credentials = this.manager.getCredentials();
        this.helper = this.manager.getClient().getOAuthHelper();
    }

    @AfterViews
    protected void beginOAuth() {
        URL authUrl = this.helper.getAuthorizationUrl(this.credentials, true, true, this.scopes);

        this.webviewLogin.loadUrl(authUrl.toExternalForm());
        this.webviewLogin.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("code=")) {
                    LoginActivity.this.handleOAuthSuccess(url);
                } else if (url.contains("error=")) {
                    LoginActivity.this.handleOAuthError(url);
                }
            }
        });
    }

    @Background
    protected void handleOAuthSuccess(String url) {
        try {
            OAuthData resp = this.helper.onUserChallenge(url, this.credentials);
            this.manager.getClient().authenticate(resp);
            this.handleSuccess();
        } catch (OAuthException ex) {
            this.handleError(ex.getMessage());
        }
    }

    @Background
    protected void handleOAuthError(String error) {
        Log.d("login", error);
        this.handleError("OAuth failed");
    }

    @UiThread
    protected void handleSuccess() {
        String loginMessage = "Logged in as " + this.manager.getClient().getAuthenticatedUser();

        Toast.makeText(this, loginMessage, Toast.LENGTH_SHORT).show();

        this.finish();
    }

    @UiThread
    protected void handleError(String message) {
        Log.e("login", message);

        // TODO: A "proper" error message / screen
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        this.finish();
    }
}
