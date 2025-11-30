import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NewsServer {

    private final int port;
    private final NewsRepository repository = new NewsRepository();
    private final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    public NewsServer(int port) {
        this.port = port;
        preloadSampleNews();
    }

    private void preloadSampleNews() {
        repository.addNews(new NewsItem(LocalDate.now(), "Добро пожаловать в новостной сервер!"));
        repository.addNews(new NewsItem(LocalDate.now(), "Первая тестовая новость."));
    }

    public void registerClient(ClientHandler handler) {
        clients.add(handler);
    }

    public void unregisterClient(ClientHandler handler) {
        clients.remove(handler);
    }

    public void broadcastNews(NewsItem item) {
        synchronized (clients) {
            for (ClientHandler ch : clients) {
                ch.sendNews(item);
            }
        }
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("NewsServer started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, repository, this);
                Thread t = new Thread(handler);
                t.start();
            }
        }
    }

    public static void main(String[] args) {
        int port = 5555;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        NewsServer server = new NewsServer(port);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
