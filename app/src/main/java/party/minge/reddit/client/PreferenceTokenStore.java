package party.minge.reddit.client;

import android.content.Context;
import android.content.SharedPreferences;

import net.dean.jraw.auth.NoSuchTokenException;
import net.dean.jraw.auth.TokenStore;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;

import party.minge.reddit.R;

@EBean
public class PreferenceTokenStore implements TokenStore {
    @RootContext
    protected Context context;

    @StringRes
    protected String tokenFile;

    protected SharedPreferences preferences;

    @AfterInject
    protected void afterInject() {
        this.preferences = this.context.getSharedPreferences(this.tokenFile, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isStored(String key) {
        return this.preferences.contains(key);
    }

    @Override
    public String readToken(String key) throws NoSuchTokenException {
        String token = this.preferences.getString(key, null);

        if (token == null) {
            throw new NoSuchTokenException("Token for key '" + key + "' does not exist");
        }

        return token;
    }

    @Override
    public void writeToken(String key, String token) {
        this.preferences.edit().putString(key, token).apply();
    }
}
