package pl.chat.example.commons;

import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

public class Broadcaster {

    public static void broadcast(String message, PrintWriter out, List<PrintWriter> printers) {
        printers.stream()
                .filter(Objects::nonNull)
                .filter(printWriter -> printWriter != out)
                .forEach(printWriter -> printWriter.println(message));
    }
}
