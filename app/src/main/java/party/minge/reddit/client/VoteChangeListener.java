package party.minge.reddit.client;

import net.dean.jraw.models.VoteDirection;

public interface VoteChangeListener {
    void onVoteChanged(VoteDirection voteDirection);
}
