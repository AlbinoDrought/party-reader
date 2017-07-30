package party.minge.reddit;

import android.content.Context;
import android.text.Spannable;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.dean.jraw.models.Comment;
import net.dean.jraw.models.CommentNode;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;
import org.androidannotations.annotations.res.DimensionRes;
import org.androidannotations.annotations.res.IntArrayRes;

import in.uncod.android.bypass.Bypass;
import party.minge.reddit.client.MarkdownParser;

@EViewGroup(R.layout.comment_item)
public class CommentItemView extends LinearLayout {
    static final int MAX_DEPTH = 7;

    protected CommentNode commentNode;

    @DimensionPixelSizeRes(R.dimen.comment_bar_size)
    protected int commentBarSize;

    @IntArrayRes
    protected int[] colorsDepth;

    @ViewById
    protected View vBar;

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

    @Bean
    protected MarkdownParser markdownParser;

    public CommentItemView(Context context) {
        super(context);
    }

    @AfterViews
    protected void init() {
        this.txtCommentText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void bind(CommentNode commentNode) {
        this.commentNode = commentNode;

        Comment c = commentNode.getComment();

        // no comment node is actually at 0 depth. that is the depth of the root node.
        // so, inset our comment depth by 1.
        int commentDepth = commentNode.getDepth() - 1;
        this.grpMain.setPadding(
                Math.min(commentDepth, MAX_DEPTH) * this.commentBarSize,
                this.getPaddingTop(),
                this.getPaddingRight(),
                this.getPaddingBottom()
        );

        if (commentDepth > 0) {
            this.vBar.setVisibility(VISIBLE);
            this.vBar.setBackgroundColor(this.getColorForDepth(commentDepth));
        } else {
            this.vBar.setVisibility(GONE);
        }

        this.txtCommentAuthor.setText(c.getAuthor());
        this.txtCommentScore.setText(c.getScore() + " points");
        this.txtCommentTime.setText(DateUtils.getRelativeTimeSpanString(c.getCreated().getTime()));

        this.txtCommentText.setText(this.markdownParser.parseMarkdown(c.getBody()));
    }

    private int getColorForDepth(int depth) {
        int modded = depth % this.colorsDepth.length;

        return this.colorsDepth[modded];
    }
}
