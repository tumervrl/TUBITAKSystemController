import com.sun.security.ntlm.Client;
import org.jgroups.*;
import org.jgroups.blocks.MessageDispatcher;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Watcher extends ReceiverAdapter {
    JChannel channel;
    String user_name="PC1";


    Map<Address, Boolean> clients = new HashMap<org.jgroups.Address, Boolean>();

    private void start() throws Exception {
        channel=new JChannel().setReceiver(this);
        channel.connect("ChatCluster");
        //channel.getState(null, 500);
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

                //channel.send(msg);
                send(msg);
            }
            catch(Exception e) {
            }
        }
    }
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);

    }
    public void send (Message msg) {
        Timer myTimer = new Timer();
        TimerTask gorev = new TimerTask() {
            @Override
            public void run() {
                try {
                    channel.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        myTimer.schedule(gorev,0,30000);

    }

    public void receive(Message msg) {
       // System.out.println(clients);
        if (!clients.containsKey(msg.getObject())){
            clients.put(msg.getSrc(),true);
        }
        System.out.println(msg.getSrc() + ": " + msg.getObject());
        System.out.println(clients);


        }





    public static void main(String[] args) throws Exception {
        new Watcher().start();

    }
}
