package party.minge.reddit;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

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
import party.minge.reddit.treeview.SubmissionTreeNodeAdapter;

@EActivity(R.layout.activity_submission_detail)
public class SubmissionDetailActivity extends Activity {
    @Extra
    protected String submissionId;

    protected Submission submission;

    @Bean
    protected Manager manager;

    @Bean
    protected SubmissionTreeNodeAdapter adapter;

    @ViewById
    protected LinearLayout grpMain;

    protected TreeNode treeNode;

    @AfterViews
    protected void hideActionBar() {
        this.getActionBar().hide();
    }

    @AfterViews
    @Background
    protected void init() {
        this.submission = this.manager.getClient().getSubmission(this.submissionId);
        this.treeNode = this.adapter.fromSubmission(submission);

        this.setAdapter();
    }

    @UiThread
    protected void setAdapter() {
        AndroidTreeView treeView = new AndroidTreeView(this, this.treeNode);
        this.grpMain.addView(treeView.getView());
    }
}
