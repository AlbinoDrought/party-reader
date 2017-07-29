package party.minge.reddit.menu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class SidebarMenuAdapter extends BaseAdapter {
    @Bean
    protected SidebarMenuItemRepository repository;

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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return this.getItem(i).getView();
    }
}
