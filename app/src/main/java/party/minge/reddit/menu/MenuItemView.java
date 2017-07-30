package party.minge.reddit.menu;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import party.minge.reddit.R;

@EViewGroup(R.layout.menu_item)
public class MenuItemView extends LinearLayout {
    @ViewById
    protected IconTextView txtIcon;

    @ViewById
    protected TextView txtName;

    public MenuItemView(Context context) {
        super(context);
    }

    public void bind(MenuItem menuItem) {
        this.txtIcon.setText(menuItem.getIcon());
        this.txtName.setText(menuItem.getText());
    }
}
