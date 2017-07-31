package party.minge.reddit;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Spannable;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconButton;

import net.dean.jraw.models.Comment;
import net.dean.jraw.models.CommentNode;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.VoteDirection;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;
import org.androidannotations.annotations.res.DimensionRes;
import org.androidannotations.annotations.res.IntArrayRes;

import in.uncod.android.bypass.Bypass;
import party.minge.reddit.client.DialogReplyListener;
import party.minge.reddit.client.Manager;
import party.minge.reddit.client.MarkdownParser;
import party.minge.reddit.client.Replier;
import party.minge.reddit.client.Upvoter;
import party.minge.reddit.client.VoteChangeListener;

@EViewGroup(R.layout.comment_item)
public class CommentItemView extends LinearLayout {
    static final int MAX_DEPTH = 15;

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

    @ViewById
    protected IconButton btnUpvote;

    @ViewById
    protected IconButton btnDownvote;

    @ViewById
    protected IconButton btnReply;

    @Bean
    protected Upvoter<Comment> upvoter;

    @Bean
    protected Replier<Comment> replier;

    @ColorRes(R.color.colorUpvote)
    protected int colorUpvote;

    @ColorRes(R.color.colorDownvote)
    protected int colorDownvote;

    @ColorRes(R.color.colorNoVote)
    protected int colorNoVote;

    private int colorScore;

    @Bean
    protected MarkdownParser markdownParser;

    public CommentItemView(Context context) {
        super(context);
    }

    @AfterViews
    protected void init() {
        this.colorScore = this.txtCommentScore.getCurrentTextColor();

        this.upvoter.setVoteChangeListener(new VoteChangeListener() {
            @Override
            public void onVoteChanged(VoteDirection voteDirection) {
                CommentItemView.this.setVoteDirection(voteDirection);
            }
        });

        this.replier.setReplyListener(new DialogReplyListener(this.getContext(), "Error replying to comment") {
            @Override
            public void onSuccess(String id) {
                String submissionId = CommentItemView.this.commentNode.getComment().getSubmissionId();
                SubmissionDetailActivity_.intent(CommentItemView.this.getContext())
                        .submissionId(submissionId)
                        .start();
            }
        });

        this.btnUpvote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentItemView.this.upvoter.performVote(VoteDirection.UPVOTE);
            }
        });

        this.btnDownvote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentItemView.this.upvoter.performVote(VoteDirection.DOWNVOTE);
            }
        });

        this.btnReply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = CommentItemView.this.commentNode.getComment();
                CommentItemView.this.replier.replyTo(comment);
            }
        });

        this.txtCommentText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @UiThread
    protected void setVoteDirection(VoteDirection direction) {
        int dvColor = this.colorNoVote;
        int uvColor = this.colorNoVote;
        int gColor = this.colorScore;

        switch (direction) {
            case UPVOTE:
                uvColor = this.colorUpvote;
                gColor = this.colorUpvote;
                break;
            case DOWNVOTE:
                dvColor = this.colorDownvote;
                gColor = this.colorDownvote;
                break;
        }

        this.btnDownvote.setTextColor(dvColor);
        this.btnUpvote.setTextColor(uvColor);
        this.txtCommentScore.setTextColor(gColor);
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

        this.upvoter.setUpvotable(c);
    }

    private int getColorForDepth(int depth) {
        int modded = depth % this.colorsDepth.length;

        return this.colorsDepth[modded];
    }
}
