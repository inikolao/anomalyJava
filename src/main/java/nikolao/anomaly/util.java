/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nikolao.anomaly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import us.jubat.anomaly.AnomalyClient;
import us.jubat.classifier.LabeledDatum;
import us.jubat.common.Datum;

/**
 *
 * @author nikolao {Ioannis Nikolaou AM 4504 mailto:Gianhspc@gmail.com}
 * 15/3/2015 Created{First write}
 */
public class util {
    public static client clsf;
    public static matrixPrt servermatching;
    public static List<String> portMatrix;
    public static String host;
    public static String distro;
    public static String[] getFileNames()
    {
        File folder = new File("trainDat");
        int i=0;
        File[] listOfFiles = folder.listFiles();
        String[] filenames = new String [(listOfFiles.length-1)];
        System.out.println("Reading available Train sets...");
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if(file.getName().contains("all")!=true)
                {
                    filenames [i]=file.getName();
                    System.out.println(file.getName());
                    i++;}
            }
        }
        System.out.println("OK....!");
        return filenames;
    }
    public static int getNumFileNames()
    {
        File folder = new File("trainDat");
        int i=0;
        File[] listOfFiles = folder.listFiles();
        //System.out.println("Counting available Train sets...");
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if(file.getName().contains("all")!=true)
                {
                    i++;}
            }
        }
        System.out.println("OK....!");
        return i;
    }
    public static List<String> getmatching(String[] filenames,int[] port)
    {
        List<String> matrix = null;
        return matrix;
    }
    public static void setmatching(String filename,int port,matrixPrt prt)
    {
        prt.SetNewMember(filename,port);
        String s=filename+","+Integer.toString(port);
        nikolao.anomaly.util.portMatrix.add(s);
    }
    public static List<String> getmatching()
    {
        return nikolao.anomaly.util.portMatrix;
    }
    public static AnomalyClient openClient(client clt) throws UnknownHostException{
        util.clsf=clt;
        AnomalyClient client = new AnomalyClient(clt.getHost(),clt.getPort(),clt.getName(),clt.getTimeout());
        return client;
    }
    public static void trainCl(String fileName,AnomalyClient client)throws FileNotFoundException, IOException{
        List<Datum> trainData;
        trainData = new ArrayList();
        String[] elemval;
        int i=0;
        String line;
        fileName="trainDat/"+fileName;
        System.out.println("Start training....");
        System.out.println("Server: "+fileName+"");
        //for (final String line : readLines(fileName)) {
        final BufferedReader br = new BufferedReader(new FileReader(fileName));
        while ((line = br.readLine()) != null) {
            //lines.add(line);
            i++;
            System.out.println("file line: "+i+" line contents: "+line+"");
            System.out.println("Sending Datum....");
            client.add(makeNumberDatum("value", Double.parseDouble(line)));
            //client.
            System.out.println("");
        }
        //add(makeNumberDatum("value", Double.parseDouble(elemval2 [1])))
        System.out.println("OK");
        System.out.println(client.getStatus());
        System.out.println("OK");
        System.out.println("Starting Service....");
    }
    public static Datum makeDatum(String key,double value) {
        return new Datum().addNumber(key, value);
    }
    public static Datum makeNumberDatum(final String label, double reading) {
        return new Datum().addNumber(label, reading);
    }
    public static LabeledDatum makeTrainDatum(String label, String key, double value) {
        return new LabeledDatum(label, makeDatum(key,value));
    }
    
    public static List<String> readLines(final String fileName) throws IOException {
        final List<String> lines;
        lines = new ArrayList();
        String line = "";
        final BufferedReader br = new BufferedReader(new FileReader(fileName));
        while ((line = br.readLine()) != null) {
            lines.add(line);
            //System.out.println(line);
        }
        br.close();
        return lines;
    }
     public static List<Float> getScore(double value,String serverName) throws UnknownHostException{//thelw polous client
    
        int offset = 1000;
        int[] ports;
        serverName=serverName+".dat";
        String[] serverNames;
        int size,i;
        //boolean asfn1,asfn2;
        List<Float> scores=new ArrayList();
        AnomalyClient client;
        serverNames=util.servermatching.getFilename();
        ports=util.servermatching.getPort();
        size=util.servermatching.getSize();
        i=0;
        do{
        i++;
        }while(serverNames [i].contains(serverName)!=true);
        
        client myclient= new client(host,ports[i],serverName,10);
        client=openClient(myclient);
        float normalValueScore;
        normalValueScore = client.calcScore(makeNumberDatum("value", value));
        float abnormalValueScore;
        abnormalValueScore = client.calcScore(makeNumberDatum("value", (value+offset)));
        //Assert.assertFalse(Float.isInfinite(normalValueScore));
        //Assert.assertTrue(Float.isInfinite(abnormalValueScore));
        scores.add(normalValueScore);
        scores.add(abnormalValueScore);
        return scores;
    }
    public static boolean checkPort(String host,int port) throws UnknownHostException
    { //otan einai available epistrefei true
        boolean result = true;
        try {
            System.out.println("Checking Port : "+port);
            (new Socket(host, port)).close();
            
            Socket s=new Socket(host, port);
            System.out.println("listening on port: " + s.getLocalPort());
            System.out.println("listening on port: " + s.getPort());
            s.close();
            System.out.println("Port is Taken!");
        // Successful connection means the port is taken.
            result = false;
            }
        catch(IOException e) {
        // Could not connect.
            System.out.println("Port is free!");
            }
        
        return result;
    }
    public static void createServerScript() throws IOException, InterruptedException
    {
        String distro = null;
        String  s="";
        int i=0;
        Properties props = System.getProperties();
         System.out.println("Os name:"+props.getProperty("os.name"));
        System.out.println("Os version:"+props.getProperty("os.version"));
        String[] cmd = {"/bin/sh", "-c", "cat /etc/*-release" };
        Process releaseDs=Runtime.getRuntime().exec(cmd);
        releaseDs.waitFor();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(releaseDs.getInputStream()));
        System.out.println("Reading distro version.....");
        while ((s = stdInput.readLine()) != null) {
           //System.out.println(s);//get distro<--name
           i++;
           if(i==1)
           {
               //get version name
               distro=s;
               
           break;
           }
        }
        stdInput.close();
        PrintWriter writer = new PrintWriter("anom.sh", "UTF-8");
        Runtime term = null;
        Process clasif = null;
        if(distro.contains("Ubuntu"))
        {//writing script and execute
            util.distro="Ubuntu";
            System.out.println("Disto : Ubuntu");
            writer.println("#!/bin/bash");
            writer.println("source /opt/jubatus/profile");
            writer.println("jubaanomaly --configpath jubaanomaly-config.json --rpc-port $1&");
            writer.close();
            //runing server
            System.out.println("Creating server Script.....");
            term.getRuntime().exec("chmod 775 anom.sh");
            //clasif=term.getRuntime().exec("/bin/bash clasf.sh "+port+" >server2.log");
            //clasif=term.getRuntime().exec("/bin/bash anom.sh "+port+"");
            //System.out.println("Script startred....");
            //term.getRuntime().exec("rm clasf.sh");
        }
        else if(distro.contains("Slackware"))
        {//writing script and execute
            util.distro="Slackware";
            System.out.println("Disto : Slackware");
            writer.println("#!/bin/bash");
            writer.println("source /root/local/share/jubatus/jubatus.profile");
            writer.println("jubaanomaly --configpath jubaanomaly-config.json --rpc-port $1&");
            writer.close();
            //start runing server
            System.out.println("Creating server Script.....");
            term.getRuntime().exec("chmod 775 anom.sh");
            //clasif=term.getRuntime().exec("sh anom.sh "+port+"");
            //clasif.waitFor();
            //term.getRuntime().exec("rm clasf.sh");
        }
        /*
        BufferedReader stdError = new BufferedReader(new InputStreamReader(clasif.getErrorStream()));
        //errors
        BufferedReader stdInput2 = new BufferedReader(new InputStreamReader(clasif.getInputStream()));
        s="";
        PrintWriter log = new PrintWriter("server.log", "UTF-8");
        while ((s = stdInput2.readLine()) != null) {
           log.println(s);           
        }
        stdInput2.close();
        log.close();
        clasif.destroy();
        /*s="";
        System.out.println("Keeping log file in \"server.log\" for the good starting......");
            while ((s = stdError.readLine()) != null) {
                if(s!=null)
                {
                    log.println(s);
                    System.out.println("Error. check \"server.log\" file for more.");
                }
                else
                {
                    
                    
                    System.out.println("ALL OK!");
                    break;
                }
            }
        stdError.close();*/
    }
    public static void startServer(int port) throws IOException{
        Runtime term = null;
        Process anom=null;
        System.out.println("Executing server Script on port "+port+".....");
        if(util.distro.contains("Slackware")==true)
        {
            anom = term.getRuntime().exec("sh anom.sh "+port+"");
        }
        else{
            anom=term.getRuntime().exec("/bin/bash anom.sh "+port+">server2.log");
        }
        BufferedReader stdError = new BufferedReader(new InputStreamReader(anom.getErrorStream()));
        //errors
        BufferedReader stdInput2 = new BufferedReader(new InputStreamReader(anom.getInputStream()));
        String s="";
        PrintWriter log = new PrintWriter("server.log", "UTF-8");
        /*while ((s = stdInput2.readLine()) != null) {
           log.println(s);           
        }
        stdInput2.close();
        log.close();
        //anom.destroy();*/
        while ((s = stdError.readLine()) != null) {
                if(s!=null)
                {
                    log.println(s);
                    System.out.println("Error. check \"server.log\" file for more.");
                }
                else
                {
                    
                    
                    System.out.println("ALL OK!");
                    break;
                }
            }
        stdError.close();//*/
    }
}
