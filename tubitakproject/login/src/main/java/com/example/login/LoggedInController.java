package com.example.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
//import org.example.watcher.Watcher;
//import org.example.watcher.Watcher3;
import javafx.stage.Stage;
import org.jgroups.*;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;
import org.jgroups.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController extends ReceiverAdapter implements Initializable{
    @FXML
    private Button button_logout;
    @FXML
    private Button checkPanel;
    @FXML
    private Label label_control;
    @FXML
    public Circle controlCircle1= new Circle();
    @FXML
    public Circle controlCircle2= new Circle();
    @FXML
    public Circle controlCircle3= new Circle();

    //Group root = new Group();
    HelloController helloController = new HelloController();
    //Watcher watcher = new Watcher();
   // Circle controlCircle =new Circle();
    JChannel channel;
    String user_name="PC1";
    RspList rsp_list;
    RpcDispatcher disp;
    Thread  t = new Thread();
    private View lastView;
    boolean checkSystemController=false;

    HashMap<Address, Boolean> clients = new HashMap<Address, Boolean>();

    public void start() throws Exception {
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

                channel.send(msg);
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
        Circle circle = new Circle();

        MethodCall call= null;
        try {
            call = new MethodCall(getClass().getMethod("print", int.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        RequestOptions opts=new RequestOptions(ResponseMode.GET_ALL, 5000);

        disp=new RpcDispatcher(channel, this);

        for(int i=0; i < 10; i++) {

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
            //control();
            System.out.println("ALICILAR: " + rsp_list);

            //checkSystem();
            if (!rsp_list.isEmpty() ){
                checkSystemController=true;
                System.err.println("true");
            }
            else if (rsp_list.isEmpty()){
                checkSystemController=false;
                System.err.println("EKLENMEDİ");
            }


        }
        channel.close();
        disp.stop();
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
        new LoggedInController().start();

    }


/*    public void controller(boolean checkSystemController){
        if (checkSystemController){
            controlCircle1.setOpacity(0.3);
        }

    }*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent,"hello-view.fxml","Log In!",null);

            }
        });
        checkPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!checkSystemController){
                    Platform.runLater(() -> {
                        try {

                            controlCircle1.setFill(Color.RED);
                            controlCircle2.setFill(Color.RED);
                            controlCircle3.setFill(Color.RED);
                            System.out.println("RENK KIRMIZI");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                }
                if(checkSystemController){
                    Platform.runLater(() -> {
                        try {
                            controlCircle1.setFill(Color.GREEN);
                            controlCircle2.setFill(Color.GREEN);
                            controlCircle3.setFill(Color.GREEN);
                            System.out.println("RENK YEŞİL");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        });




    }
    public void setUserInformation(String username){
        label_control.setText("Welcome"+"  " + username + "!");
        if (username.equals("Tümer")){
            label_control.setText("Welcome"+"  " +"Admin "+ username + "!");
        }

    }

}
