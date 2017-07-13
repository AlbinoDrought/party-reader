package party.minge.reddit;

import android.app.Activity;
import android.widget.ListView;

import net.dean.jraw.models.CommentNode;
import net.dean.jraw.models.Submission;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import party.minge.reddit.client.Manager;

@EActivity(R.layout.activity_submission_detail)
public class SubmissionDetailActivity extends Activity {
    @Extra
    protected String submissionId;

    @Bean
    protected Manager manager;

    @ViewById
    protected ListView lstComments;

    @Bean
    protected CommentListAdapter commentListAdapter;

    @AfterViews
    @Background
    protected void afterViews() {
        this.commentListAdapter.setCommentNode(this.getCommentNode());
        this.setAdapter();
    }

    @UiThread
    protected void setAdapter() {
        this.lstComments.setAdapter(this.commentListAdapter);
    }

    protected CommentNode getCommentNode() {
        Submission submission = this.manager.getClient().getSubmission(this.submissionId);

        return submission.getComments();
    }
}
