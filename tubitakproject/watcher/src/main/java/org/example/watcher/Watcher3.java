package org.example.watcher;
import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.blocks.*;
import org.jgroups.util.RspList;
import org.jgroups.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Watcher3 {
    JChannel channel;
    RpcDispatcher disp;
    RspList rsp_list;
    String        props="/home/bela/udp.xml"; //????????????????
    public int print(int number) {
        System.out.println("print(" + number + ")");
        return number * 2;
    }
    public void baglanti(){
        System.out.println("BAGLANTİ");
    }

    public void start() throws Exception {
        channel=new JChannel(props);
        RequestOptions opts=new RequestOptions(ResponseMode.GET_ALL, 5000);
        MethodCall call=new MethodCall(getClass().getMethod("print", int.class));


        disp=new RpcDispatcher(channel,this);
        channel.connect("RpcDispatcherTestGroup");
        for(int i=0; i < 100; i++) {
            Util.sleep(1000);
            //rsp_list=disp.callRemoteMethod(null,"print", new Integer(i),opts,0);
            rsp_list=disp.callRemoteMethods(null, "print", new Object[]{i}, new Class[]{int.class}, opts);
            call.setArgs(i);
            rsp_list=disp.callRemoteMethods(null,call,opts);
            System.out.println("Responses: " + rsp_list);
        }
        channel.close();
    }

    public static void main(String[] args) {
        try {
            new Watcher3().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
