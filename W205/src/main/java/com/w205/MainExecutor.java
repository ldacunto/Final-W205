package com.w205;

import java.io.File;

import com.w205.malicious.MaliciousAnalizerMain;
import com.w205.preprocess.FilterEvents;
import com.w205.preprocess.Normalizer;
import com.w205.preprocess.PrivacyProtector;
import com.w205.sessions.OrderByTimeUserFiles;
import com.w205.sessions.SplitterByUser;
import com.w205.utils.FileAccessFactory;

public class MainExecutor
{

    public MainExecutor()
    {
    }

    private static void printInstructions()
    {
        System.out.println("MainExecutor syntax: command souce {dest}");
        System.out.println("command list");
        System.out.println("-filter     :filter from the files only the events of match and related to the app of interest");
        System.out.println("-privacy    :replace sensitive information on the files to protect the privacy of the users");
        System.out.println("-norm       :normalize the files in order to analyze them");
        System.out.println("-split      :split the data in different files, one for each user");
        System.out.println("-order      :order temporarly the data into the files");
        System.out.println("-malicious  :find the malicious users");
        System.exit(0);
    }

    public static void main(String argv[]) throws Exception
    {
        if(argv == null || argv.length < 2)
            printInstructions();
        String command = argv[0].toLowerCase();
        String source = argv[1];
        String dest = "";
        
        if(!source.endsWith(File.separator))
            source = (new StringBuilder(String.valueOf(source))).append(File.separator).toString();
        
        if(argv.length == 3)
        {
            dest = argv[2];
            if(!dest.endsWith(File.separator))
                dest = (new StringBuilder(String.valueOf(dest))).append(File.separator).toString();
            
            FileAccessFactory.createDirectoryIfNotPresent(dest);
            
        }
        if(command.equals("-filter"))
        {
            FilterEvents filter = new FilterEvents();
            filter.execute(source, dest);
        } 
        else if(command.equals("-privacy"))
        {
            PrivacyProtector protector = new PrivacyProtector();
            protector.execute(source, dest);
        } 
        else if(command.equals("-norm"))
        {
            Normalizer normalizer = new Normalizer();
            normalizer.execute(source, dest);
        } 
        else if(command.equals("-split"))
        {
            SplitterByUser splitter = new SplitterByUser();
            splitter.execute(source, dest);
        } 
        else if(command.equals("-order"))
        {
            OrderByTimeUserFiles ordered = new OrderByTimeUserFiles();
            ordered.execute(source, dest);
        } 
        else if(command.equals("-malicious"))
        {
            MaliciousAnalizerMain malicious = new MaliciousAnalizerMain();
            malicious.execute(source);
        } 
        else
        {
            printInstructions();
        }
    }
}
