import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewsClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5555;
        if (args.length >= 1) host = args[0];
        if (args.length >= 2) port = Integer.parseInt(args[1]);

        try (
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), "UTF-8"));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader console = new BufferedReader(
                        new InputStreamReader(System.in, "UTF-8"))
        ) {
            System.out.println("Connected to news server " + host + ":" + port);
            System.out.println("Команды:");
            System.out.println("  GET_TODAY");
            System.out.println("  GET_DATE yyyy-MM-dd");
            System.out.println("  NEW yyyy-MM-dd;текст новости");
            System.out.println("  QUIT");

            Thread readerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println("[SERVER] " + line);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            readerThread.setDaemon(true);
            readerThread.start();

            String cmd;
            while ((cmd = console.readLine()) != null) {
                cmd = cmd.trim();
                if (cmd.isEmpty()) continue;
                out.println(cmd);
                if (cmd.equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
