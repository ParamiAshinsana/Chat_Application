package lk.ijse.chatapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Clients extends Thread{
    private ArrayList<Clients> clientsArrayList;
    Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Clients(Socket socket, ArrayList<Clients> clientsArrayList) throws IOException {
        this.socket = socket;
        this.clientsArrayList = clientsArrayList;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                if (msg.equalsIgnoreCase( "exit")) {
                    break;
                }
                for (Clients cl : clientsArrayList) {
                    cl.writer.println(msg);
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }
}
