package party.minge.reddit;


import android.content.Context;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joanzapata.iconify.widget.IconButton;
import com.joanzapata.iconify.widget.IconTextView;

import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Thumbnails;
import net.dean.jraw.models.VoteDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.util.Locale;

import party.minge.reddit.client.DialogReplyListener;
import party.minge.reddit.client.Manager;
import party.minge.reddit.client.MarkdownParser;
import party.minge.reddit.client.Replier;
import party.minge.reddit.client.Upvoter;
import party.minge.reddit.client.VoteChangeListener;

@EViewGroup(R.layout.detailed_post_item)
public class DetailedPostItemView extends LinearLayout {
    @Bean
    protected Manager manager;

    @ViewById
    protected TextView txtPostTitle;

    @ViewById
    protected TextView txtPostSubtext;

    @ViewById
    protected TextView txtPostVotes;

    @ViewById
    protected TextView txtPostSubreddit;

    @ViewById
    protected TextView txtPostDate;

    @ViewById
    protected TextView txtPostComments;

    @ViewById
    protected TextView txtPostBody;

    @ViewById
    protected ImageView imgPostThumbnail;

    @Bean
    protected MarkdownParser markdownParser;

    @ViewById
    protected IconButton btnUpvote;

    @ViewById
    protected IconButton btnDownvote;

    @ViewById
    protected IconButton btnReply;

    @Bean
    protected Upvoter<Submission> upvoter;

    @Bean
    protected Replier<Submission> replier;

    @ColorRes(R.color.colorUpvote)
    protected int colorUpvote;

    @ColorRes(R.color.colorDownvote)
    protected int colorDownvote;

    @ColorRes(R.color.colorNoVote)
    protected int colorNoVote;

    private int colorScore;

    protected Submission submission;

    public DetailedPostItemView(Context context) {
        super(context);
    }

    @AfterViews
    protected void initClickListeners() {
        this.colorScore = this.txtPostVotes.getCurrentTextColor();

        this.imgPostThumbnail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailedPostItemView.this.viewPostResource();
            }
        });

        this.txtPostSubreddit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SubredditActivity_.intent(DetailedPostItemView.this.getContext())
                        .subreddit(DetailedPostItemView.this.submission.getSubredditName())
                        .start();
            }
        });

        this.upvoter.setVoteChangeListener(new VoteChangeListener() {
            @Override
            public void onVoteChanged(VoteDirection voteDirection) {
                DetailedPostItemView.this.setVoteDirection(voteDirection);
            }
        });

        this.btnUpvote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailedPostItemView.this.upvoter.performVote(VoteDirection.UPVOTE);
            }
        });

        this.btnDownvote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailedPostItemView.this.upvoter.performVote(VoteDirection.DOWNVOTE);
            }
        });

        this.btnReply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Submission submission = DetailedPostItemView.this.submission;
                DetailedPostItemView.this.replier.replyTo(submission);
            }
        });

        this.replier.setReplyListener(new DialogReplyListener(this.getContext(), "Error replying to post") {
            @Override
            public void onSuccess(String id) {
                String submissionId = DetailedPostItemView.this.submission.getId();
                SubmissionDetailActivity_.intent(DetailedPostItemView.this.getContext())
                        .submissionId(submissionId)
                        .start();
            }
        });

        this.txtPostBody.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @UiThread
    protected void setVoteDirection(VoteDirection voteDirection) {
        int dvColor = this.colorNoVote;
        int uvColor = this.colorNoVote;
        int gColor = this.colorScore;

        switch (voteDirection) {
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
        this.txtPostVotes.setTextColor(gColor);
    }

    public void bind(Submission submission) {
        this.submission = submission;

        this.txtPostTitle.setText(submission.getTitle());
        this.txtPostSubtext.setText(this.getPostSubtext(submission));
        this.txtPostDate.setText(DateUtils.getRelativeTimeSpanString(submission.getCreated().getTime()));
        this.txtPostComments.setText(this.getPostComments(submission));
        this.txtPostSubreddit.setText(this.getSubredditText(submission));
        this.txtPostVotes.setText(this.getPostScoreText(submission));

        String selfText = submission.getSelftext();

        if (selfText != null && selfText.length() > 0) {
            this.txtPostBody.setText(this.markdownParser.parseMarkdown(selfText));
            this.txtPostBody.setVisibility(VISIBLE);
        } else {
            this.txtPostBody.setVisibility(GONE);
        }

        Thumbnails thumbnails = submission.getThumbnails();

        if (thumbnails != null) {
            this.imgPostThumbnail.setVisibility(VISIBLE);
            Glide
                .with(this.getContext())
                .load(thumbnails.getSource().getUrl())
                .centerCrop()
                .placeholder(android.R.color.transparent)
                .crossFade()
                .into(this.imgPostThumbnail);
        } else {
            this.imgPostThumbnail.setVisibility(GONE);
            this.imgPostThumbnail.setImageResource(android.R.color.transparent);
        }

        this.upvoter.setUpvotable(submission);
    }

    private void viewPostResource() {
        if (this.submission.isSelfPost()) {
            // self posts have no link, ignore
            return;
        }

        ExternalWebResourceActivity_.intent(this.getContext())
                .url(this.submission.getUrl())
                .domain(this.submission.getDomain())
                .start();
    }

    private String getPostScoreText(Submission submission) {
        Double upvoteRatio = submission.getUpvoteRatio() * 100;

        return String.format(Locale.US, "%d points (%d%% upvoted)", submission.getScore(), upvoteRatio.intValue());
    }

    private String getPostSubtext(Submission submission) {
        String submitter = submission.getAuthor();
        String domain = submission.getDomain();

        return String.format("by %s (%s)", submitter, domain);
    }

    private String getSubredditText(Submission submission) {
        return String.format("r/%s", submission.getSubredditName());
    }

    private String getPostComments(Submission submission) {
        Integer comments = submission.getCommentCount();

        return comments + " " + (comments == 1 ? "comment" : "comments");
    }
}
