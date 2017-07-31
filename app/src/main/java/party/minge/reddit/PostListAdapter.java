package party.minge.reddit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.SubredditPaginator;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EBean
public class PostListAdapter extends BaseAdapter {
    @RootContext
    protected Context context;

    protected List<Submission> posts;

    protected SubredditPaginator paginator;

    public void setPaginator(SubredditPaginator paginator) {
        this.paginator = paginator;
        this.posts = new ArrayList<>();
        // automatically populate with posts
        this.gotoNextPage();
    }

    public void gotoNextPage() {
        if (!this.hasMore()) return;

        // array and list juggling is done because:
        // paginator.next returns listings,
        // and listings cannot be modified
        Listing<Submission> newPages = this.paginator.next();

        for (Submission page : newPages) {
            this.posts.add(page);
        }
    }

    public boolean hasMore() {
        return this.paginator.hasNext();
    }

    @Override
    public int getCount() {
        return this.posts.size();
    }

    @Override
    public Submission getItem(int i) {
        return this.posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PostItemView postItemView;

        if (view == null) {
            postItemView = PostItemView_.build(context);
        } else {
            postItemView = (PostItemView)view;
        }

        postItemView.bind(this.getItem(i));

        return postItemView;
    }
}
