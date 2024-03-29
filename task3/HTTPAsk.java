import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import tcpclient.TCPClient;

public class HTTPAsk {
 public static void main(String[] args) throws Exception {
       Integer port = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(port);

        boolean shutdown = false;
        Integer timeout = null; // milliseconds
        Integer limit = null;
        String hostname = null;
        byte[] data = new byte[0];
	  while(true) {
        Socket socket = serverSocket.accept();
        try (
       OutputStream out = socket.getOutputStream();
       InputStream in = socket.getInputStream()){

            byte[] request = new byte[1024];
            int read = in.read(request);
            String requestString = new String(request, 0, read);
            StringBuilder errorCheck = new StringBuilder();

            String[] lines = requestString.split("\r\n");
            String[] words = lines[0].split(" ");
            String[] parameters = words[1].split("[?&]");

 
            if (!requestString.contains("ask")) {
                System.out.println("test ask");
                errorCheck.append("HTTP/1.1 404 Not Found\r\n\r\n");
                out.write(errorCheck.toString().getBytes());
                continue;
                }
            if (!requestString.contains("GET") || !requestString.contains("hostname")) {
                System.out.println("elis");
                errorCheck.append("HTTP/1.1 400 Bad Request\r\n");
                out.write(errorCheck.toString().getBytes());
                continue;
            }
    for (int i = 1; i < parameters.length; i++ ) {
        String[] parameter = parameters[i].split("=");
        switch (parameter[0]) {
            case "hostname":
                hostname = parameter[1];
                break;
            case "port":
                port = Integer.parseInt(parameter[1]);
                break;
            case "limit":
                limit = Integer.parseInt(parameter[1]);
                break;
            case "string":
                data = parameter[1].getBytes();
                break;
            case "timeout":
                timeout = Integer.parseInt(parameter[1]);
                break;
            case "shutdown":
                shutdown = Boolean.parseBoolean(parameter[1]);
                break;

            default:
                break;
        }
    }

        TCPClient tcpClient = new TCPClient(shutdown, timeout, limit);
        byte[] toClientBytes = tcpClient.askServer(hostname, port, data);
        ByteArrayOutputStream serverOutput = new ByteArrayOutputStream();
        serverOutput.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
        serverOutput.write(toClientBytes);
        byte[] responseHeader = serverOutput.toByteArray();
        out.write(responseHeader);
            }
            catch(Exception e){
                System.out.println("Exception thrown: " + e);
        }
    }
 }
 }


