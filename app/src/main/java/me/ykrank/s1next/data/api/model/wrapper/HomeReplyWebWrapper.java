package me.ykrank.s1next.data.api.model.wrapper;

import androidx.annotation.NonNull;

import com.google.common.base.Objects;
import com.github.ykrank.androidtools.util.L;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import me.ykrank.s1next.data.api.model.HomeReply;
import me.ykrank.s1next.data.api.model.HomeThread;

/**
 * Created by ykrank on 2017/2/4.
 */

public class HomeReplyWebWrapper {
    private List<HomeReplyItem> replyItems;
    private boolean more;

    @NonNull
    public static HomeReplyWebWrapper fromHtml(String html) {
        HomeReplyWebWrapper wrapper = new HomeReplyWebWrapper();
        List<HomeReplyItem> replyItems = new ArrayList<>();
        try {
            Document document = Jsoup.parse(html);
            HtmlDataWrapper.Companion.preTreatHtml(document);
            Elements elements = document.select("#delform tr");
            for (int i = 1; i < elements.size(); i++) {
                Element element = elements.get(i);
                int childSize = element.children().size();
                if (childSize >= 5) {
                    HomeThread homeThread = HomeThread.Companion.fromHtmlElement(element);
                    if (homeThread != null) {
                        replyItems.add(homeThread);
                    }
                } else if (childSize >= 1) {
                    HomeReply homeReply = HomeReply.Companion.fromHtmlElement(element);
                    if (homeReply != null) {
                        replyItems.add(homeReply);
                    }
                }
            }
            //more
            Elements next = document.select("div.pg>a.nxt");
            wrapper.setMore(!next.isEmpty());
        } catch (Exception e) {
            L.report(e);
        }
        wrapper.setReplyItems(replyItems);
        return wrapper;
    }

    public List<HomeReplyItem> getReplyItems() {
        return replyItems;
    }

    public void setReplyItems(List<HomeReplyItem> replyItems) {
        this.replyItems = replyItems;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeReplyWebWrapper that = (HomeReplyWebWrapper) o;
        return more == that.more &&
                Objects.equal(replyItems, that.replyItems);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(replyItems, more);
    }

    @Override
    public String toString() {
        return "HomeReplyWebWrapper{" +
                "replyItems=" + replyItems +
                ", more=" + more +
                '}';
    }

    public interface HomeReplyItem {

    }
}
