package party.minge.reddit;

import android.app.Activity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import party.minge.reddit.client.Manager;

/*@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
    @ViewById
    protected TextView txtUsername;

    @Bean(Manager.class)
    protected Manager manager;

    @AfterViews
    protected void afterViews() {
        String text;

        if (this.manager.getClient().isAuthenticated()) {
            text = this.manager.getClient().getAuthenticatedUser();
        } else {
            text = "not logged in";
        }

        this.txtUsername.setText(text);
    }

    @Click
    public void btnLogin() {
        LoginActivity_.intent(this).start();
    }

    @Click
    public void btnShowFrontpage() {
        SubredditActivity_.intent(this).start();
    }
}*/
