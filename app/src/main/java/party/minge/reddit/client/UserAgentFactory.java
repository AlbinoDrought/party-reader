package party.minge.reddit.client;

import net.dean.jraw.http.UserAgent;

/**
 * Created by sean on 06/07/17.
 */

public interface UserAgentFactory {
    UserAgent getUserAgent();
}
