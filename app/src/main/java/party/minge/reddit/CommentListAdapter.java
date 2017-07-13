package party.minge.reddit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.dean.jraw.models.CommentNode;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.SubredditPaginator;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class CommentListAdapter extends BaseAdapter {
    @RootContext
    protected Context context;

    protected CommentNode commentNode;

    public void setCommentNode(CommentNode commentNode) {
        this.commentNode = commentNode;
    }

    public boolean hasMore() {
        return this.commentNode.hasMoreComments();
    }

    @Override
    public int getCount() {
        return this.commentNode.getImmediateSize();
    }

    @Override
    public CommentNode getItem(int i) {
        return this.commentNode.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CommentItemView commentItemView;

        if (view == null) {
            commentItemView = CommentItemView_.build(context);
        } else {
            commentItemView = (CommentItemView)view;
        }

        commentItemView.bind(this.getItem(i));

        return commentItemView;
    }
}
