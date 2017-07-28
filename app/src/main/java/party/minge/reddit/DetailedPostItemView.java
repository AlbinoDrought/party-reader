package party.minge.reddit;


import android.content.Context;
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
    }

    public void bind(Submission submission) {
        this.submission = submission;

        this.txtPostTitle.setText(submission.getTitle());
        this.txtPostSubtext.setText(this.getPostSubtext(submission));
        this.txtPostDate.setText(submission.getCreated().toString());
        this.txtPostComments.setText(this.getPostComments(submission));
        this.txtPostVotes.setText(String.format(Locale.US, "%d points (%d%% upvoted)", submission.getScore(), submission.getUpvoteRatio().intValue()));
        this.txtPostSubreddit.setText(submission.getSubredditName());
        this.txtPostBody.setText(submission.getSelftext());

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
