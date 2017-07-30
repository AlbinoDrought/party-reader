package party.minge.reddit.client;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import in.uncod.android.bypass.Bypass;

@EBean(scope = EBean.Scope.Singleton)
public class MarkdownParser {
    @RootContext
    protected Context rootContext;

    protected Bypass bypass;

    @AfterInject
    protected void init() {
        this.bypass = new Bypass(this.rootContext);
    }

    public CharSequence parseMarkdown(String markdown) {
        return this.bypass.markdownToSpannable(markdown);
    }
}
