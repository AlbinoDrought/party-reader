package party.minge.reddit.client;

import net.dean.jraw.http.oauth.Credentials;

public interface CredentialFactory {
    Credentials getCredentials();
}
