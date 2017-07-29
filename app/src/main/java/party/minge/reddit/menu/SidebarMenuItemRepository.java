package party.minge.reddit.menu;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

import party.minge.reddit.client.Manager;
import party.minge.reddit.menu.items.Login_;
import party.minge.reddit.menu.items.Logout_;

@EBean
public class SidebarMenuItemRepository {
    @RootContext
    protected Context rootContext;

    @Bean
    protected Manager manager;

    public MenuItem[] getMenuItems() {
        ArrayList<MenuItem> items = new ArrayList<>();

        if (this.manager.getClient().isAuthenticated()) {
            // add logout button
            items.add(Logout_.getInstance_(this.rootContext));
        } else {
            items.add(Login_.getInstance_(this.rootContext));
        }

        MenuItem[] menuItems = new MenuItem[items.size()];
        return items.toArray(menuItems);
    }

    public int getLength() {
        return this.getMenuItems().length;
    }
}
