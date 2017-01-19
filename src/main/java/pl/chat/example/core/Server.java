package pl.chat.example.core;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static pl.chat.example.commons.Broadcaster.broadcast;
import static pl.chat.example.commons.Messages.CONNECTED_TO_MESSAGE;
import static pl.chat.example.commons.Messages.SERVER_LISTENING_ON_PORT_MESSAGE;
import static pl.chat.example.commons.ServerProperties.SERVER_PORT;

@Slf4j
public class Server extends Thread {

    private static List<String> names = new ArrayList<>();

    private static ArrayList<PrintWriter> printers = new ArrayList<>();

    private ServerSocket listener;

    private PrintWriter output;

    public Server() throws IOException {
        listener = new ServerSocket(SERVER_PORT);
    }

    public void run() {
        log.info(SERVER_LISTENING_ON_PORT_MESSAGE + listener.getLocalPort());
        while (true) {
            try {
                Socket clientSocket = listener.accept();
                log.info(CONNECTED_TO_MESSAGE + clientSocket.getRemoteSocketAddress());
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(clientSocket.getOutputStream(), true);
                new Read(input, output).start();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    class Read extends Thread {

        private BufferedReader reader;
        private PrintWriter printer;

        Read(BufferedReader reader, PrintWriter printer) {
            this.reader = reader;
            this.printer = printer;
        }

        public void run() {
            try {
                String message;
                String name = reader.readLine();
                printers.add(output);
                names.add(name);
                broadcast(name + " joined.", printer, printers);
                message = reader.readLine()
                                .trim();
                while (!reader.ready()) {
                    log.info(message);
                    while (message.equals("")) {
                        message = reader.readLine();
                    }
                    broadcast(message, printer, printers);
                    message = reader.readLine()
                                    .trim();
                }
                broadcast(name + " left", printer, printers);
                for (int i = 0; i != printers.size(); i++) {
                    if (printers.get(i)
                                .equals(printer)) {
                        printers.remove(i);
                        names.remove(i);
                    }
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }
}
