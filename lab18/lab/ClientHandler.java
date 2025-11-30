import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final NewsRepository repository;
    private final NewsServer server;
    private PrintWriter out;

    public ClientHandler(Socket socket, NewsRepository repository, NewsServer server) {
        this.socket = socket;
        this.repository = repository;
        this.server = server;
    }

    @Override
    public void run() {
        System.out.println("Client connected: " + socket.getRemoteSocketAddress());
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), "UTF-8"));
        ) {
            out = new PrintWriter(socket.getOutputStream(), true);
            server.registerClient(this);

            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.equalsIgnoreCase("QUIT")) {
                    out.println("OK Bye");
                    break;
                } else if (line.equalsIgnoreCase("GET_TODAY")) {
                    handleGetDate(LocalDate.now());
                } else if (line.startsWith("GET_DATE")) {
                    handleGetDateCommand(line);
                } else if (line.startsWith("NEW ")) {
                    handleNewCommand(line.substring(4));
                } else {
                    out.println("ERROR Unknown command");
                }
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } finally {
            server.unregisterClient(this);
            try {
                socket.close();
            } catch (IOException ignored) {}
            System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
        }
    }

    private void handleGetDateCommand(String line) {
        String[] parts = line.split("\\s+", 2);
        if (parts.length < 2) {
            out.println("ERROR Usage: GET_DATE yyyy-MM-dd");
            return;
        }
        try {
            LocalDate date = LocalDate.parse(parts[1]);
            handleGetDate(date);
        } catch (DateTimeParseException e) {
            out.println("ERROR Bad date format, use yyyy-MM-dd");
        }
    }

    private void handleGetDate(LocalDate date) {
        for (NewsItem item : repository.getByDate(date)) {
            out.println("NEWS " + item.toString());
        }
        out.println("OK EndOfList");
    }

    private void handleNewCommand(String payload) {
        int sep = payload.indexOf(';');
        if (sep <= 0) {
            out.println("ERROR Usage: NEW yyyy-MM-dd;text");
            return;
        }
        String dateStr = payload.substring(0, sep).trim();
        String text = payload.substring(sep + 1).trim();
        try {
            LocalDate date = LocalDate.parse(dateStr);
            NewsItem item = new NewsItem(date, text);
            repository.addNews(item);
            server.broadcastNews(item);
            out.println("OK NewsAdded");
        } catch (DateTimeParseException e) {
            out.println("ERROR Bad date format, use yyyy-MM-dd");
        }
    }

    public void sendNews(NewsItem item) {
        if (out != null) {
            out.println("NEWS " + item.toString());
        }
    }
}
