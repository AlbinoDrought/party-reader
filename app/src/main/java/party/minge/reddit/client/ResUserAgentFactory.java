package party.minge.reddit.client;

import android.content.Context;

import net.dean.jraw.http.UserAgent;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;

@EBean
public class ResUserAgentFactory implements UserAgentFactory {
    @StringRes
    private String redditUsername;

    @StringRes
    private String platform;

    @StringRes
    private String version;

    private String packageName;

    @RootContext
    public void setRootContext(Context context) {
        this.packageName = context.getPackageName();
    }

    @Override
    public UserAgent getUserAgent() {
        return UserAgent.of(this.platform, this.packageName, this.version, this.redditUsername);
    }
}
