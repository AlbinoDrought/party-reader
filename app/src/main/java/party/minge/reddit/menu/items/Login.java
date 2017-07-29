package party.minge.reddit.menu.items;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import party.minge.reddit.LoginActivity_;
import party.minge.reddit.menu.MenuItem;

@EBean
public class Login implements MenuItem {
    @RootContext
    protected Context rootContext;

    public int getId() {
        return 0;
    }

    public String getText() {
        return "Login";
    }

    public String getIcon() {
        return "{md-person}";
    }

    public void onClick() {
        LoginActivity_.intent(this.rootContext).start();
    }
}
