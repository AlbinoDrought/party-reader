package party.minge.reddit.menu.items;

import android.app.AlertDialog;
import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;

import party.minge.reddit.client.MarkdownParser;
import party.minge.reddit.menu.MenuItem;

@EBean
public class About implements MenuItem {
    @RootContext
    protected Context rootContext;

    @StringRes
    protected String about;

    @Bean
    protected MarkdownParser markdownParser;

    @Override
    public int getId() {
        return 31;//st degree
    }

    public String getText() {
        return "About";
    }

    public String getIcon() {
        return "{md-info}";
    }

    @Override
    public void onClick() {
        new AlertDialog.Builder(this.rootContext)
                .setTitle("About")
                .setMessage(this.markdownParser.parseMarkdown(this.about))
                .show();

    }
}
