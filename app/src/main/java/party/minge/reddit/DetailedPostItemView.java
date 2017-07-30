package party.minge.reddit;


import android.content.Context;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joanzapata.iconify.widget.IconTextView;

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

import party.minge.reddit.client.Manager;
import party.minge.reddit.client.MarkdownParser;
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

    protected Submission submission;

    public DetailedPostItemView(Context context) {
        super(context);
    }

    @AfterViews
    protected void initClickListeners() {
        this.imgPostThumbnail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailedPostItemView.this.viewPostResource();
            }
        });

        this.txtPostBody.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void bind(Submission submission) {
        this.submission = submission;

        this.txtPostTitle.setText(submission.getTitle());
        this.txtPostSubtext.setText(this.getPostSubtext(submission));
        this.txtPostDate.setText(DateUtils.getRelativeTimeSpanString(submission.getCreated().getTime()));
        this.txtPostComments.setText(this.getPostComments(submission));
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
