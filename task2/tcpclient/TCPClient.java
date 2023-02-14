package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    private final boolean shutdown;
    private final Integer timeout;
    private final Integer limit;

    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); //skapa en ByteArrayOutputStream för att spara det som hämats från servern
        Socket clientSocket = new Socket(hostname, port);            // initsiera en socket
        InputStream inFromServer = clientSocket.getInputStream();   //InputStream för att ta emot data från server
        OutputStream outToServer = clientSocket.getOutputStream(); // Samma som ovan fast skicka

        outToServer.write(toServerBytes); // skickar data från toServerBytes till outputStream

        byte[] staticBuffer = new byte[1024]; // alokera statisk minnes buffer
        int length;
        int totalLength = 0;
        clientSocket.setSoTimeout(timeout);

        while (true) {
            length = inFromServer.read(staticBuffer);
            if (length == -1) {
                break;
            }

            out.write(staticBuffer, 0, length);
            totalLength += length;

            if (limit != null && totalLength >= limit) {
                break;
            }
        }

        if (shutdown) {
            clientSocket.shutdownOutput();
        }

        clientSocket.close();
        return out.toByteArray();
    }
}