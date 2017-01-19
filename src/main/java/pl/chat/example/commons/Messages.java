package pl.chat.example.commons;

import static pl.chat.example.commons.ServerProperties.SERVER_ADDRESS;
import static pl.chat.example.commons.ServerProperties.SERVER_PORT;

public class Messages {

    public static final String FRAME_TITLE_MESSAGE = "Simple multi-client chat application";
    public static final String CONNECTING_TO_MESSAGE = "Connecting to " + SERVER_ADDRESS + " on port " + SERVER_PORT + "...";
    public static final String COULD_NOT_CONNECT_TO_SERVER_MESSAGE = "\nCould not connect to Server";
    public static final String USER_NAME_VALIDATION_MESSAGE = "Name can not be empty and can not be longer than 20 characters";
    public static final String SERVER_LISTENING_ON_PORT_MESSAGE = "Server Listening on port : ";
    public static final String CONNECTED_TO_MESSAGE = "Connected to ";
    public static final String CONNECTION_CLOSED_BY_REMOTE_HOST_MESSAGE = "Connection closed by remote host.";

}
