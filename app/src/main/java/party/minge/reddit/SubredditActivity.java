package party.minge.reddit;

import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.SubredditPaginator;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import party.minge.reddit.client.AuthenticationCallback;
import party.minge.reddit.client.Manager;
import party.minge.reddit.menu.SidebarMenuAdapter;

import static android.view.View.GONE;

@EActivity(R.layout.activity_subreddit)
public class SubredditActivity extends Activity {
    @Extra
    protected String subreddit;

    @Bean
    protected Manager manager;

    protected SubredditPaginator paginator;

    @Bean
    protected PostListAdapter postListAdapter;

    @ViewById
    protected ListView lvSubredditPosts;

    @ViewById
    protected DrawerLayout layoutDrawer;

    protected ActionBarDrawerToggle drawerListener;

    @ViewById
    protected ListView lvDrawer;

    @ViewById
    protected ProgressBar loader;

    @Bean
    protected SidebarMenuAdapter sidebarMenuAdapter;

    @ViewById
    protected SwipeRefreshLayout grpSwipeRefresh;

    @AfterInject
    protected void createPaginator() {
        this.paginator = new SubredditPaginator(this.manager.getClient());

        if (this.subreddit != null && this.subreddit.length() > 0) {
            this.paginator.setSubreddit(this.subreddit);
        }
    }

    @AfterViews
    protected void setupDrawer() {
        this.layoutDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        this.lvDrawer.setAdapter(this.sidebarMenuAdapter);
        this.lvDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SubredditActivity.this.sidebarMenuAdapter.getItem(i).onClick();
            }
        });

        this.drawerListener = new ActionBarDrawerToggle(
                this,
                this.layoutDrawer,
                R.string.drawerOpen,
                R.string.drawerClose
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                SubredditActivity.this.sidebarMenuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                SubredditActivity.this.sidebarMenuAdapter.notifyDataSetChanged();
            }
        };

        this.layoutDrawer.setDrawerListener(this.drawerListener);

        this.grpSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SubredditActivity.this.fetchPosts();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.drawerListener.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @AfterViews
    @Background
    protected void authenticateIfRequired() {
        this.manager.authenticateIfRequired(new AuthenticationCallback() {
            @Override
            public void onSuccessfulAuthentication() {
                SubredditActivity.this.fetchPosts();
            }

            @Override
            public void onFailedAuthentication(Exception e) {
                Log.e("subreddit-error", e.toString());
            }
        });
    }

    @Background
    protected void fetchPosts() {
        this.paginator.reset();
        this.postListAdapter.setPaginator(this.paginator);
        this.bindAdapter();
    }

    @UiThread
    protected void bindAdapter() {
        this.grpSwipeRefresh.setRefreshing(false);
        this.loader.setVisibility(View.GONE);
        this.lvSubredditPosts.setVisibility(View.VISIBLE);

        this.getActionBar().setTitle(this.getSubredditName());

        this.lvSubredditPosts.setAdapter(this.postListAdapter);
        this.lvSubredditPosts.setOnScrollListener(new EndlessScrollListener(10) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                boolean hasMore = SubredditActivity.this.postListAdapter.hasMore();

                if (hasMore) {
                    // background
                    SubredditActivity.this.nextPage();
                }

                return hasMore;
            }
        });
        this.lvSubredditPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Submission submission = SubredditActivity.this.postListAdapter.getItem(i);
                SubredditActivity.this.showSubmissionDetails(submission);
            }
        });
    }

    private String getSubredditName() {
        if (this.subreddit == null || this.subreddit.length() <= 0) {
            return "Front Page";
        } else {
            return this.subreddit;
        }
    }

    protected void showSubmissionDetails(Submission submission) {
        SubmissionDetailActivity_.intent(this)
                .submissionId(submission.getId())
                .start();
    }

    @Background
    protected void nextPage() {
        this.postListAdapter.gotoNextPage();;
        this.forceRefreshList();
    }

    @UiThread
    protected void forceRefreshList() {
        this.postListAdapter.notifyDataSetChanged();
    }
}
