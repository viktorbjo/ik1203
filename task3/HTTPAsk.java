import java.net.*;
import java.io.*;

public class HTTPAsk {
    public static void main( String[] args) {

        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
       
        while (true) {
            Socket clientSocket = serverSocket.accept(); // Wait for incoming connections
           
            // Read input from client
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String request = reader.readLine();

            // Parse input
            String[] requestParts = request.split("[ ?&]"); // Split request on spaces, question marks and ampersands
            String hostname = null;
            int portNum = 0;
            String command = null;
            String[] parameters = new String[0];
            for (int i = 0; i < requestParts.length; i++) {
                if (requestParts[i].equals("hostname")) {
                    hostname = requestParts[i + 1];
                } else if (requestParts[i].equals("port")) {
                    portNum = Integer.parseInt(requestParts[i + 1]);
                } else if (requestParts[i].equals("string")) {
                    command = requestParts[i + 1];
                } else if (requestParts[i].equals("verbose")) {
                    parameters = new String[] { "verbose" };
                }
            }
           
            // Call TCPClient and get response
            TCPClient tcpClient = new TCPClient(false, 5000, 1024);
            byte[] responseBytes = new byte[0];
            try {
                responseBytes = tcpClient.askServer(hostname, portNum, command.getBytes("UTF-8"));
            } catch (IOException e) {
                responseBytes = "HTTP/1.1 404 Not Found\r\n\r\n".getBytes("UTF-8");
            }
           
            // Send response to client
            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes("UTF-8"));
            outputStream.write(responseBytes);
           
            // Close socket and streams
            outputStream.close();
            inputStream.close();
            reader.close();
            clientSocket.close();
        }
    }
}
