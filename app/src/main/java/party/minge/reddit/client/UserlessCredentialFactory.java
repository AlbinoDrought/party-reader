package party.minge.reddit.client;

import net.dean.jraw.http.oauth.Credentials;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.UUID;

@EBean
public class UserlessCredentialFactory implements CredentialFactory {
    @StringRes
    protected String oauthClientId;

    @Pref
    protected UserlessCredentialPreferences_ preferences;

    @Override
    public Credentials getCredentials() {
        if (!this.preferences.uniqueUUID().exists()) {
            this.preferences.uniqueUUID().put(UUID.randomUUID().toString());
        }

        UUID uuid = UUID.fromString(this.preferences.uniqueUUID().get());

        // use the same uuid per install
        return Credentials.userlessApp(this.oauthClientId, uuid);
    }
}
