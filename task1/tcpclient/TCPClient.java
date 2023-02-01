package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    
    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        Socket clientSocket = new Socket(hostname, port);
        OutputStream outToServer = clientSocket.getOutputStream();
        outToServer.write(toServerBytes);

        InputStream inFromServer = clientSocket.getInputStream();
        ByteArrayOutputStream response = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inFromServer.read(buffer)) != -1) {
            response.write(buffer, 0, length);
        }

        clientSocket.close();
        return response.toByteArray();

    }
}
