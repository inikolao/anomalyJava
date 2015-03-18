/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nikolao.anomaly;

import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static nikolao.anomaly.util.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import us.jubat.anomaly.AnomalyClient;

/**
 *
 * @author nikolao {Ioannis Nikolaou AM 4504 mailto:Gianhspc@gmail.com}
 * 15/3/2015 Created{First write}
 */

@ComponentScan
@EnableAutoConfiguration
public class main {
    public static void main(String[] args) throws UnknownHostException, IOException, FileNotFoundException, InterruptedException {
    InetAddress thisIp = null;
   String filename="";
   String host="83.212.112.190";
   //String host="192.168.2.8";
   try{
   host=thisIp.getHostAddress().toString();}
   catch (NullPointerException ex){}
   int port=9200;
   String name="test";
   int timeout=10;
   client myclient= new client(host,port,name,timeout);
   boolean t;
   int i;
   int size;
   String [] nams;
   int [] ptrs;
   String [] files;
   matrixPrt mtport = new matrixPrt();
   util.servermatching=mtport;
   util.host=host;
   files = getFileNames();
   createServerScript();
   //i=0;
  for(i=0;i<getNumFileNames();i++){ 
        do{
        t=checkPort(host,port);
            if(t==true)
            {
                System.out.println("Starting "+(i+1)+" Server for "+files[i]+" on port "+port+"\n");
                startServer(port);
                mtport.SetNewMember(files [i], port);
                port++;
            }
            else
            {
            port++;
            }
        }while(t==false);
    }
  sleep(10000);
//open clients
    //client myclient= new client("83.212.112.190",9200,"test",10);
    size=mtport.getSize();
    nams=mtport.getFilename();
    ptrs=mtport.getPort();
    for(i=0;i<size;i++){
        myclient.setName(nams [i]);
        myclient.setPort(ptrs [i]);
        AnomalyClient cl=openClient(myclient);
        System.out.println(myclient.getHost());
        System.out.println(myclient.getPort());
        System.out.println(myclient.getName());
        trainCl(nams[i],cl);//train what
    }
    SpringApplication.run(main.class, args);
    }
}
