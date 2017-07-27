package party.minge.reddit.treeview;

import android.content.Context;

import com.unnamed.b.atv.model.TreeNode;

import net.dean.jraw.models.CommentNode;
import net.dean.jraw.models.Submission;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class SubmissionTreeNodeAdapter {
    @RootContext
    protected Context context;

    @Bean
    protected CommentTreeNodeAdapter commentTreeNodeAdapter;

    public TreeNode fromSubmission(Submission submission)
    {
        TreeNode rootNode = new TreeNode(null);

        TreeNode postNode = new TreeNode(submission);

        postNode.setViewHolder(new SubmissionNodeViewHolder(this.context));

        rootNode.addChild(postNode);

        List<CommentNode> comments = submission.getComments().getChildren();

        for (CommentNode comment : comments) {
            rootNode.addChild(this.commentTreeNodeAdapter.fromCommentNode(comment));
        }

        return rootNode;
    }
}
