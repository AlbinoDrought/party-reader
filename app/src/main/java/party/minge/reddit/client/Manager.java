package party.minge.reddit.client;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.RefreshTokenHandler;
import net.dean.jraw.auth.TokenStore;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class Manager {
    @Bean(ResUserAgentFactory.class)
    protected UserAgentFactory userAgentFactory;

    protected RedditClient redditClient;

    protected AuthenticationManager authenticationManager;

    @Bean(PreferenceTokenStore.class)
    protected TokenStore tokenStore;

    @AfterInject
    public void afterInject() {
        this.redditClient = new RedditClient(this.userAgentFactory.getUserAgent());
        this.authenticationManager = AuthenticationManager.get();
        this.authenticationManager.init(this.redditClient, new RefreshTokenHandler(this.tokenStore, this.redditClient));
    }

    public RedditClient getClient() {
        return this.redditClient;
    }

    public AuthenticationManager getAuthenticationManager() {
        return this.authenticationManager;
    }
}
