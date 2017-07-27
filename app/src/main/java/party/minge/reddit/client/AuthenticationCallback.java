package party.minge.reddit.client;

public interface AuthenticationCallback {
    void onSuccessfulAuthentication();
    void onFailedAuthentication(Exception e);
}
