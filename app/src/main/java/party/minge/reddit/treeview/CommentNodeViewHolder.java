package party.minge.reddit.treeview;

import android.content.Context;
import android.view.View;

import com.unnamed.b.atv.model.TreeNode;

import net.dean.jraw.models.CommentNode;

import party.minge.reddit.CommentItemView;
import party.minge.reddit.CommentItemView_;

public class CommentNodeViewHolder extends TreeNode.BaseNodeViewHolder<CommentNode> {
    public CommentNodeViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, CommentNode value) {
        CommentItemView view = CommentItemView_.build(this.context);

        view.bind(value);

        return view;
    }
}
