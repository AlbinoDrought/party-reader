package party.minge.reddit;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joanzapata.iconify.widget.IconTextView;

import net.dean.jraw.ApiException;
import net.dean.jraw.models.OEmbed;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Thumbnails;
import net.dean.jraw.models.VoteDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.util.Locale;

import party.minge.reddit.client.Manager;
import party.minge.reddit.client.Upvoter;
import party.minge.reddit.client.VoteChangeListener;

@EViewGroup(R.layout.post_item)
public class PostItemView extends LinearLayout {
    @Bean
    protected Manager manager;

    @ColorRes(R.color.colorUpvote)
    protected int colorUpvote;

    @ColorRes(R.color.colorDownvote)
    protected int colorDownvote;

    @ColorRes(R.color.colorNoVote)
    protected int colorNoVote;

    @ViewById
    protected TextView txtPostTitle;

    @ViewById
    protected TextView txtPostSubtext;

    @ViewById
    protected TextView txtPostDate;

    @ViewById
    protected TextView txtPostComments;

    @ViewById
    protected ImageView imgPostThumbnail;

    @ViewById
    protected IconTextView btnUpvote;

    @ViewById
    protected IconTextView btnDownvote;

    @ViewById
    protected TextView txtScore;

    @Bean
    protected Upvoter<Submission> upvoter;

    protected Submission submission;

    public PostItemView(Context context) {
        super(context);
    }

    @AfterViews
    protected void initClickListeners() {
        this.imgPostThumbnail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PostItemView.this.viewPostResource();
            }
        });

        this.btnDownvote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PostItemView.this.upvoter.performVote(VoteDirection.DOWNVOTE);
            }
        });

        this.btnUpvote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PostItemView.this.upvoter.performVote(VoteDirection.UPVOTE);
            }
        });

        this.upvoter.setVoteChangeListener(new VoteChangeListener() {
            @Override
            public void onVoteChanged(VoteDirection voteDirection) {
                PostItemView.this.updateVoteColors(voteDirection);
            }
        });
    }

    public void bind(Submission submission) {
        this.submission = submission;

        this.txtPostTitle.setText(submission.getTitle());
        this.txtPostSubtext.setText(this.getPostSubtext(submission));
        this.txtPostDate.setText(submission.getCreated().toString());
        this.txtPostComments.setText(this.getPostComments(submission));
        this.txtScore.setText(String.format(Locale.US, "%d", submission.getScore()));

        this.upvoter.setUpvotable(submission);

        Thumbnails thumbnails = submission.getThumbnails();

        if (thumbnails != null) {
            Glide
                .with(this.getContext())
                .load(thumbnails.getSource().getUrl())
                .centerCrop()
                .placeholder(android.R.color.transparent)
                .crossFade()
                .into(this.imgPostThumbnail);
        } else {
            this.imgPostThumbnail.setImageResource(android.R.color.transparent);
        }
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

    @UiThread
    protected void updateVoteColors(VoteDirection voteDirection) {
        this.btnUpvote.setTextColor(voteDirection == VoteDirection.UPVOTE ? this.colorUpvote : this.colorNoVote);
        this.btnDownvote.setTextColor(voteDirection == VoteDirection.DOWNVOTE ? this.colorDownvote : this.colorNoVote);
    }

    private String getPostSubtext(Submission submission) {
        String subreddit = submission.getSubredditName();
        String submitter = submission.getAuthor();
        String domain = submission.getDomain();

        return String.format("to %s by %s (%s)", subreddit, submitter, domain);
    }

    private String getPostComments(Submission submission) {
        Integer comments = submission.getCommentCount();

        return comments + " " + (comments == 1 ? "comment" : "comments");
    }
}
