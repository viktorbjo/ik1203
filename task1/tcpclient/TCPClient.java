package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream(); //skapa en ByteArrayOutputStream för att spara det som hämats från servern
        Socket clientSocket = new Socket(hostname, port);            // initsiera en socket
        InputStream inFromServer = clientSocket.getInputStream();   //InputStream för att ta emot data från server
        OutputStream outToServer = clientSocket.getOutputStream(); // Samma som ovan fast skicka

        outToServer.write(toServerBytes); // skickar data från toServerBytes till outputStream

        byte[] staticBuffer = new byte[1024]; // alokera statisk minnes buffer
        int length;
        while((length = inFromServer.read(staticBuffer)) != -1){
            out.write(staticBuffer, 0, staticBuffer.length);
        }

        clientSocket.close(); //stäng av socket anslutningen
        return out.toByteArray();

    }

    }



