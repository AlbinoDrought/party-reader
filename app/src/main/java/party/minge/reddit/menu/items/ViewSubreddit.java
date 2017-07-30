package party.minge.reddit.menu.items;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import party.minge.reddit.SubredditActivity_;
import party.minge.reddit.menu.MenuItem;

@EBean
public class ViewSubreddit implements MenuItem {
    @RootContext
    protected Context rootContext;

    public int getId() {
        return 3;
    }

    public String getText() {
        return "Subreddit";
    }

    public String getIcon() {
        return "{md-forum}";
    }

    @UiThread
    public void onClick() {
        final EditText editText = new EditText(this.rootContext);

        editText.setHint("Subreddit name");

        new AlertDialog.Builder(this.rootContext)
            .setTitle("Go to subreddit")
            .setView(editText)
            .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SubredditActivity_.intent(ViewSubreddit.this.rootContext)
                            .subreddit(editText.getText().toString())
                            .start();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // nothing
                }
            })
            .show();
    }
}
