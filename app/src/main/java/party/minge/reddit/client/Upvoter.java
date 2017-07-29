package party.minge.reddit.client;

import android.util.Log;

import net.dean.jraw.ApiException;
import net.dean.jraw.models.Thing;
import net.dean.jraw.models.VoteDirection;
import net.dean.jraw.models.attr.Votable;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class Upvoter<T extends Thing & Votable> {
    @Bean
    protected Manager manager;

    private T upvotable;

    private VoteChangeListener voteChangeListener;

    private VoteDirection currentVoteDirection = VoteDirection.NO_VOTE;

    private boolean currentlyVoting = false;

    public void setUpvotable(T thing) {
        this.upvotable = thing;

        this.setVoteDirection(thing.getVote());
    }

    protected void setVoteDirection(VoteDirection voteDirection) {
        this.currentVoteDirection = voteDirection;

        if (this.voteChangeListener != null) {
            this.voteChangeListener.onVoteChanged(voteDirection);
        }
    }

    public void setVoteChangeListener(VoteChangeListener voteChangeListener) {
        this.voteChangeListener = voteChangeListener;
    }

    @Background
    public void performVote(VoteDirection voteDirection) {
        if (this.currentlyVoting) {
            return;
        }

        this.currentlyVoting = true;

        VoteDirection oldDirection = this.currentVoteDirection;

        if (voteDirection == oldDirection) {
            voteDirection = VoteDirection.NO_VOTE;
        }

        // update vote on ui instantly, reverting if it fails
        this.setVoteDirection(voteDirection);

        try {
            this.manager.getAccountManager().vote(this.upvotable, voteDirection);
        } catch (ApiException ex) {
            Log.e("vote", ex.toString());
            this.setVoteDirection(oldDirection);
        }

        this.currentlyVoting = false;
    }
}
