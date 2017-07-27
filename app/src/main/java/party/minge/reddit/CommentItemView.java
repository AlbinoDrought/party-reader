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
    // the two consts that follow this comment
    // were very precisely chosen ;)
    static final int MAX_DEPTH = 7;
    static final int PX_PER_DEPTH = 50;

    protected CommentNode commentNode;

    @ViewById
    protected LinearLayout grpMain;

    @ViewById
    protected TextView txtCommentAuthor;

    @ViewById
    protected TextView txtCommentScore;

    @ViewById
    protected TextView txtCommentTime;

    @ViewById
    protected TextView txtCommentText;

    public CommentItemView(Context context) {
        super(context);
    }

    public void bind(CommentNode commentNode) {
        this.commentNode = commentNode;

        Comment c = commentNode.getComment();

        int commentDepth = commentNode.getDepth();
        this.grpMain.setPadding(
                Math.min(commentDepth, MAX_DEPTH) * PX_PER_DEPTH,
                this.getPaddingTop(),
                this.getPaddingRight(),
                this.getPaddingBottom()
        );

        this.txtCommentAuthor.setText(c.getAuthor());
        this.txtCommentScore.setText(c.getScore() + " points");
        this.txtCommentTime.setText(c.getCreated().toString());

        // TODO: markdown?
        this.txtCommentText.setText(c.getBody());
    }
}
