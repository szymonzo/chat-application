package pl.chat.example;

import pl.chat.example.core.Server;

import java.io.IOException;

public class ServerRunner {

    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}
