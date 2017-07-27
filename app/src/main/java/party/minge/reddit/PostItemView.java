package party.minge.reddit;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.dean.jraw.models.OEmbed;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Thumbnails;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.post_item)
public class PostItemView extends LinearLayout {
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

    protected Submission submission;

    public PostItemView(Context context) {
        super(context);
    }

    public void bind(Submission submission) {
        this.submission = submission;

        this.txtPostTitle.setText(submission.getTitle());
        this.txtPostSubtext.setText(this.getPostSubtext(submission));
        this.txtPostDate.setText(submission.getCreated().toString());
        this.txtPostComments.setText(this.getPostComments(submission));

        // TODO: set thumbnail
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

        this.imgPostThumbnail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PostItemView.this.viewPostResource();
            }
        });
    }

    private void viewPostResource() {
        if (this.submission.isSelfPost()) {
            // self posts have no link, ignore
            return;
        }

        ExternalWebResourceActivity_.intent(this.getContext()).url(this.submission.getUrl()).start();
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
