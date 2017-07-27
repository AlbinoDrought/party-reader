package party.minge.reddit.treeview;

import android.content.Context;
import android.view.View;

import com.unnamed.b.atv.model.TreeNode;

import net.dean.jraw.models.Submission;

import party.minge.reddit.PostItemView;
import party.minge.reddit.PostItemView_;

public class SubmissionNodeViewHolder extends TreeNode.BaseNodeViewHolder<Submission> {
    public SubmissionNodeViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, Submission value) {
        PostItemView view = PostItemView_.build(this.context);

        view.bind(value);

        return view;
    }
}
