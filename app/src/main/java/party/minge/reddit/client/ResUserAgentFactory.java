package party.minge.reddit.client;

import android.content.Context;

import net.dean.jraw.http.UserAgent;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;

@EBean
public class ResUserAgentFactory implements UserAgentFactory {
    @StringRes
    protected String redditUsername;

    @StringRes
    protected String platform;

    @StringRes
    protected String version;

    protected String packageName;

    @RootContext
    public void setRootContext(Context context) {
        this.packageName = context.getPackageName();
    }

    @Override
    public UserAgent getUserAgent() {
        return UserAgent.of(this.platform, this.packageName, this.version, this.redditUsername);
    }
}
