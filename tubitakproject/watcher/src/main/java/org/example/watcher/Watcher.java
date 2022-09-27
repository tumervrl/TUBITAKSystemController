package org.example.watcher;

import org.jgroups.*;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;
import org.jgroups.util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Watcher extends ReceiverAdapter {
    JChannel channel;
    String user_name="PC1";
    RspList rsp_list;
    RpcDispatcher disp;
    private View lastView;
    boolean checkSystemController;
    String currentHostName = "";






    HashMap<Address, Boolean> clients = new HashMap<org.jgroups.Address, Boolean>();

    private void start() throws Exception {
        channel=new JChannel().setReceiver(this);
        channel.setDiscardOwnMessages(true);
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

                channel.send(msg);
                System.out.println(channel.getReceivedMessages());

//                send(msg);
                getReceive(msg);

            }
            catch(Exception e) {
            }
            //getReceive(msg);

        }

    }
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
        //System.out.println(new_view.getMembers());
        if (new_view.getMembers().toString().contains(currentHostName)){
            System.out.println("contain");
        }
        else {
            System.out.println("does not contain");
        }

//        if (lastView == null) {
//            System.out.println("Received initial view:");
//            new_view.forEach(System.out::println);
//        } else {
//            System.out.println("Received new view.");
//
//            List<Address> newMembers = View.newMembers(lastView, new_view);
//            System.out.println("New members: ");
//            newMembers.forEach(System.out::println);
//
//            List<Address> exMembers = View.leftMembers(lastView, new_view);
//            System.out.println("Exited members:");
//            exMembers.forEach(System.out::println);
//        }
//
//        lastView = new_view;

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
    public static int print(int number) throws Exception {
        return number * 2;
    }
    public void getReceive(Message msg){
        MethodCall call= null;

        try {
            call = new MethodCall(getClass().getMethod("print", int.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        RequestOptions opts=new RequestOptions(ResponseMode.GET_ALL, 5000);

        disp=new RpcDispatcher(channel, this);
        //control();



        for(int i=0; i < 10; i++) {
            control();
            Util.sleep(100);

            try {
                rsp_list=disp.callRemoteMethods(null, "print", new Object[]{i}, new Class[]{int.class}, opts);

            } catch (Exception e) {
                e.printStackTrace();
            }
            call.setArgs(i);
            try {
                rsp_list=disp.callRemoteMethods(null, call, opts);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            Set set = rsp_list.entrySet();
//            for (){
//                System.out.println((Object)Entry.getClass().getSimpleName());
//            }

/*            Iterator iterator = set.iterator();
            while (iterator.hasNext()){
                System.out.println(iterator.next());
                System.out.println((Object)iterator.next().getClass().getSimpleName());

            }*/

            if (!clients.containsKey(msg.getObject())){
                clients.put(msg.getSrc(),true);
                System.out.println(clients);

            }

            currentHostName =msg.getSrc().toString();
//            System.out.println(currentHostName);

            //control();
            //System.out.println(); ////////////////////////////
            System.out.println("ALICILAR: " + rsp_list);

            //checkSystem();

        }

        channel.close();
        disp.stop();

    }
    public void control(){
        if (!clients.isEmpty() ){

            System.out.println("true");

        }
        else{
            System.out.println("EKLENMEDİ");
        }
    }
/*
    public boolean checkSystem(){
        if (!clients.isEmpty()){
            loggedInController.controlCircle1.setOpacity(0.3);
            System.out.println("true");
            return true;
        }
        else {
          //  System.out.println("false");
            return false;

        }
    }
*/


    public void receive(Message msg) {
       // System.out.println(clients);

        System.out.println(msg.getSrc() + ": " + msg.getObject());

        }


    public static void main(String[] args) throws Exception {
        new Watcher().start();

    }
}
