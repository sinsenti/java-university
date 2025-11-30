import java.time.LocalDate;

public class NewsItem {
    private final LocalDate date;
    private final String text;

    public NewsItem(LocalDate date, String text) {
        this.date = date;
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return date + ";" + text;
    }
}
