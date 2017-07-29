package party.minge.reddit.client;

import android.util.Log;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.auth.RefreshTokenHandler;
import net.dean.jraw.auth.TokenStore;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.managers.AccountManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class Manager {
    @Bean(ResFactory.class)
    protected UserAgentFactory userAgentFactory;

    @Bean(ResFactory.class)
    protected CredentialFactory credentialFactory;

    @Bean(UserlessCredentialFactory.class)
    protected CredentialFactory anonymousCredentialFactory;

    protected RedditClient redditClient;

    protected AuthenticationManager authenticationManager;

    protected AccountManager accountManager;

    @Bean(PreferenceTokenStore.class)
    protected TokenStore tokenStore;

    @AfterInject
    public void afterInject() {
        this.redditClient = new RedditClient(this.userAgentFactory.getUserAgent());
        this.authenticationManager = AuthenticationManager.get();
        this.authenticationManager.init(this.redditClient, new RefreshTokenHandler(this.tokenStore, this.redditClient));
        this.accountManager = new AccountManager(this.redditClient);
    }

    /**
     * Anonymously authenticates if required.
     * If already authenticated, does not re-authenticate.
     *
     * @param authenticationCallback Methods to call on success or failure.
     */
    public void authenticateIfRequired(AuthenticationCallback authenticationCallback) {
        AuthenticationState state = AuthenticationManager.get().checkAuthState();

        if (state == AuthenticationState.READY) {
            authenticationCallback.onSuccessfulAuthentication();
            return;
        }

        if (state == AuthenticationState.NEED_REFRESH) {
            try {
                this.authenticationManager.refreshAccessToken(this.getCredentials());
                authenticationCallback.onSuccessfulAuthentication();
            } catch (Exception ex) {
                authenticationCallback.onFailedAuthentication(ex);
            }

            return;
        }

        try {
            OAuthData authData = this.getClient().getOAuthHelper().easyAuth(this.getAnonymousCredentials());
            this.getClient().authenticate(authData);

            authenticationCallback.onSuccessfulAuthentication();
        } catch (Exception ex) {
            authenticationCallback.onFailedAuthentication(ex);
        }
    }

    public RedditClient getClient() {
        return this.redditClient;
    }

    public AccountManager getAccountManager() {
        return this.accountManager;
    }

    public AuthenticationManager getAuthenticationManager() {
        return this.authenticationManager;
    }

    public Credentials getCredentials() {
        return this.credentialFactory.getCredentials();
    }

    public Credentials getAnonymousCredentials() {
        return this.anonymousCredentialFactory.getCredentials();
    }
}
