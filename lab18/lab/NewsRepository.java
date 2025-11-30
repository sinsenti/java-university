import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsRepository {
    private final List<NewsItem> items = Collections.synchronizedList(new ArrayList<>());

    public void addNews(NewsItem item) {
        items.add(item);
    }

    public List<NewsItem> getByDate(LocalDate date) {
        List<NewsItem> result = new ArrayList<>();
        synchronized (items) {
            for (NewsItem item : items) {
                if (item.getDate().equals(date)) {
                    result.add(item);
                }
            }
        }
        return result;
    }
}
