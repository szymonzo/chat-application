package pl.chat.example.readers;

import lombok.AllArgsConstructor;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.io.BufferedReader;

import static pl.chat.example.commons.Messages.CONNECTION_CLOSED_BY_REMOTE_HOST_MESSAGE;

@AllArgsConstructor
public class ClientReader extends Thread {

    private BufferedReader input;

    private JTextArea textArea;

    public void run() {
        String message;
        while (true) {
            try {
                message = input.readLine();
                textArea.append("\n" + message);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, CONNECTION_CLOSED_BY_REMOTE_HOST_MESSAGE);
                System.exit(0);
            }
        }
    }
}
