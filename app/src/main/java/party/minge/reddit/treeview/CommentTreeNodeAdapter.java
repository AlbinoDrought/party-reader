package party.minge.reddit.treeview;

import android.content.Context;

import com.unnamed.b.atv.model.TreeNode;

import net.dean.jraw.models.CommentNode;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;


@EBean
public class CommentTreeNodeAdapter  {
    @RootContext
    protected Context context;

    public TreeNode fromCommentNode(CommentNode node)
    {
        TreeNode myNode = new TreeNode(node);

        myNode.setExpanded(true);
        myNode.setViewHolder(new CommentNodeViewHolder(this.context));

        List<CommentNode> children = node.getChildren();

        for (CommentNode child : children) {
            myNode.addChild(this.fromCommentNode(child));
        }

        return myNode;
    }
}
