package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {

        ByteArrayOutputStream response = new ByteArrayOutputStream(); //skapa en ByteArrayOutputStream för att spara det som hämats från servern
        Socket clientSocket = new Socket(hostname, port);            // initsiera en socket
        InputStream inFromServer = clientSocket.getInputStream();   //InputStream för att ta emot data från server
        OutputStream outToServer = clientSocket.getOutputStream(); // Samma som ovan fast skicka

        outToServer.write(toServerBytes); // skickar data från toServerBytes till outputStream

        byte[] buffer = new byte[1024]; // alokera minnes buffer

        for (int length = inFromServer.read(buffer); length > 0; length = inFromServer.read(buffer)) { // loopar för att skriva den data vi tar emot till byteArrayOutputStream
            response.write(buffer, 0, buffer.length);
        }

        clientSocket.close();
        return response.toByteArray();

    }

    }



