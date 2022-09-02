import javafx.application.Application;
import javafx.stage.Stage;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client2 extends ReceiverAdapter {
    JChannel channel;
    String user_name="PC3";
    public Boolean receive =true;



    private void start() throws Exception {
        channel=new JChannel().setReceiver(this);
        channel.connect("ChatCluster");
        eventLoop();
        channel.close();

    }

    private void eventLoop() {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.print("> "); System.out.flush();
                String line=in.readLine().toLowerCase();
                if(line.startsWith("quit") || line.startsWith("exit"))
                    break;
                line="[" + user_name + "] " + line;
                Message msg=new Message(null, line);
                channel.send(msg);

            }
            catch(Exception e) {
            }
        }
    }
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);


    }

    public void receive(Message msg) {
        System.out.println(msg.getSrc() + ": " + msg.getObject());

        while(receive){
            try {
                channel.send(null, user_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            receive=false;

        }


    }



    public static void main(String[] args) throws Exception {
        new Client2().start();
    }
}