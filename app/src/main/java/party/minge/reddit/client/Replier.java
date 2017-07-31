package party.minge.reddit.client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import net.dean.jraw.models.Contribution;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import party.minge.reddit.SubredditActivity_;
import party.minge.reddit.menu.items.ViewSubreddit;

@EBean
public class Replier<T extends Contribution>  {
    @RootContext
    protected Context rootContext;

    @Bean
    protected Manager manager;

    protected ReplyListener replyListener = new NopReplyListener();

    public void setReplyListener(ReplyListener replyListener) {
        this.replyListener = replyListener;
    }

    public void replyTo(final T thing) {
        final EditText editText = new EditText(this.rootContext);

        editText.setHint("Message");

        new AlertDialog.Builder(this.rootContext)
                .setTitle("Reply")
                .setView(editText)
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Replier.this.replyWith(thing, editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing
                    }
                })
                .show();
    }

    @Background
    protected void replyWith(T thing, String message) {
        try {
            String result = this.manager.getAccountManager().reply(thing, message);
            this.replyListener.onSuccess(result);
        } catch (Exception ex) {
            this.replyListener.onFailure(ex);
        }
    }

    private class NopReplyListener implements ReplyListener {
        public void onSuccess(String id) {

        }

        public void onFailure(Exception ex) {

        }
    }

    public interface ReplyListener {
        void onSuccess(String id);
        void onFailure(Exception ex);
    }
}
