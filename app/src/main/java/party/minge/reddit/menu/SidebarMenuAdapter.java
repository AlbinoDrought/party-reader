package party.minge.reddit.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class SidebarMenuAdapter extends BaseAdapter {
    @Bean
    protected SidebarMenuItemRepository repository;

    @RootContext
    protected Context rootContext;

    @Override
    public int getCount() {
        return this.repository.getLength();
    }

    @Override
    public MenuItem getItem(int i) {
        return this.repository.getMenuItems()[i];
    }

    @Override
    public long getItemId(int i) {
        return this.repository.getMenuItems()[i].getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = MenuItemView_.build(this.rootContext);
        }

        ((MenuItemView)view).bind(this.getItem(i));

        return view;
    }
}
