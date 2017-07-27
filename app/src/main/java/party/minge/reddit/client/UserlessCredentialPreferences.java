package party.minge.reddit.client;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface UserlessCredentialPreferences {
    String uniqueUUID();
}
