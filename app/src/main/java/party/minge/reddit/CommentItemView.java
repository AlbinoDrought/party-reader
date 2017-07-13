package party.minge.reddit;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.dean.jraw.models.Comment;
import net.dean.jraw.models.CommentNode;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.comment_item)
public class CommentItemView extends LinearLayout {
    protected CommentNode commentNode;

    @ViewById
    protected TextView txtCommentAuthor;

    @ViewById
    protected TextView txtCommentScore;

    @ViewById
    protected TextView txtCommentTime;

    @ViewById
    protected TextView txtCommentText;

    @ViewById
    protected ListView lstSubComments;

    @Bean
    protected CommentListAdapter commentListAdapter;

    public CommentItemView(Context context) {
        super(context);
    }

    public void bind(CommentNode commentNode) {
        this.commentNode = commentNode;

        Comment c = commentNode.getComment();

        this.txtCommentAuthor.setText(c.getAuthor());
        this.txtCommentScore.setText(c.getScore() + " points");
        this.txtCommentTime.setText(c.getCreated().toString());

        // TODO: markdown?
        this.txtCommentText.setText(c.getBody());

        this.commentListAdapter.setCommentNode(commentNode);
        this.lstSubComments.setAdapter(this.commentListAdapter);
    }
}
