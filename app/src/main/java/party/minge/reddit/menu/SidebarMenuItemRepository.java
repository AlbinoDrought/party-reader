package party.minge.reddit.menu;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import party.minge.reddit.menu.items.Login_;

@EBean
public class SidebarMenuItemRepository {
    @RootContext
    protected Context rootContext;

    public MenuItem[] getMenuItems() {
        return new MenuItem[] {
                Login_.build(this.rootContext),
        };
    }

    public int getLength() {
        return this.getMenuItems().length;
    }
}
