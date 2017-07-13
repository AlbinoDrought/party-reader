package party.minge.reddit;

import android.app.Activity;
import android.widget.ListView;

import net.dean.jraw.paginators.SubredditPaginator;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import party.minge.reddit.client.Manager;

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

    @AfterInject
    protected void createPaginator() {
        this.paginator = new SubredditPaginator(this.manager.getClient());

        if (this.subreddit != null && this.subreddit.length() > 0) {
            this.paginator.setSubreddit(this.subreddit);
        }
    }

    @AfterViews
    @Background
    protected void fetchPosts() {
        this.postListAdapter.setPaginator(this.paginator);
        this.bindAdapter();
    }

    @UiThread
    protected void bindAdapter() {
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
