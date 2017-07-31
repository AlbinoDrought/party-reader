package party.minge.reddit.client;

import android.app.AlertDialog;
import android.content.Context;

abstract public class DialogReplyListener implements Replier.ReplyListener {
    private Context context;
    private String title;

    public DialogReplyListener(Context context, String title) {
        this.context = context;
        this.title = title;
    }

    @Override
    public void onFailure(Exception ex) {
        new AlertDialog.Builder(this.context)
                .setTitle(this.title)
                .setMessage(ex.getMessage())
                .show();
    }
}
