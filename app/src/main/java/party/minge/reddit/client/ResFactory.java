package party.minge.reddit.client;

import android.content.Context;

import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;

@EBean(scope = EBean.Scope.Singleton)
public class ResFactory implements UserAgentFactory, CredentialFactory {
    @StringRes
    protected String redditUsername;

    @StringRes
    protected String platform;

    @StringRes
    protected String version;

    protected String packageName;

    @StringRes
    protected String oauthClientId;

    @StringRes
    protected String oauthRedirectUrl;

    @RootContext
    public void setRootContext(Context context) {
        this.packageName = context.getPackageName();
    }

    @Override
    public UserAgent getUserAgent() {
        return UserAgent.of(this.platform, this.packageName, this.version, this.redditUsername);
    }

    @Override
    public Credentials getCredentials() {
        return Credentials.installedApp(this.oauthClientId, this.oauthRedirectUrl);
    }
}
