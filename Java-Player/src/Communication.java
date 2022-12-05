import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Communication {    
    int port;
    ServerSocket server;
    Socket client;

    Communication(int port){
        this.port = port;
        try{
            server = new ServerSocket(port);
            client = server.accept();
        } catch (IOException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int listen(){
        System.out.println("Recebendo do Cliente...");
        int entry = -1;
        try{
            BufferedReader inClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            entry = inClient.read();
        }catch (IOException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entry;
    }

    public void respond(Integer content){
        try{
            System.out.println("Enviando para o cliente...");
            DataOutputStream dos = new DataOutputStream(this.client.getOutputStream());
            dos.flush();
            dos.writeInt(content);
        }catch (IOException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close(){
        try {
            this.server.close();
        }catch (IOException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}