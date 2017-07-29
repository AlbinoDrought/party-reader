package party.minge.reddit.menu.items;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import org.androidannotations.annotations.EViewGroup;

import party.minge.reddit.LoginActivity_;
import party.minge.reddit.R;
import party.minge.reddit.menu.MenuItem;

@EViewGroup(R.layout.menu_item_login)
public class Login extends LinearLayout implements MenuItem {
    public Login(Context context) {
        super(context);
    }

    public View getView() {
        return this;
    }

    public void onClick() {
        LoginActivity_.intent(this.getContext()).start();
    }
}
