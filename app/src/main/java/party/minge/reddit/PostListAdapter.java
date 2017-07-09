package party.minge.reddit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.dean.jraw.models.Submission;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class PostListAdapter extends BaseAdapter {
    @RootContext
    protected Context context;

    protected List<Submission> posts;

    public void setPosts(List<Submission> posts) {
        this.posts = posts;
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
