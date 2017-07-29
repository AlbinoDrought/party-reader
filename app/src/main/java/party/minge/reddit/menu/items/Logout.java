package party.minge.reddit.menu.items;


import net.dean.jraw.http.oauth.OAuthHelper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import party.minge.reddit.client.Manager;
import party.minge.reddit.menu.MenuItem;

@EBean
public class Logout implements MenuItem {
    @Bean
    protected Manager manager;

    public int getId() {
        return 1;
    }

    public String getText() {
        return "Logout";
    }

    public String getIcon() {
        return "{md-person}";
    }

    @Background
    public void onClick() {
        this.manager.getClient().getOAuthHelper().revokeAccessToken(this.manager.getCredentials());
        this.manager.getClient().deauthenticate();
    }
}
