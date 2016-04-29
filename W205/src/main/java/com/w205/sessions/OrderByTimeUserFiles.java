package com.w205.sessions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.w205.utils.FileAccessFactory;

public class OrderByTimeUserFiles
{

    public OrderByTimeUserFiles()
    {
    }

    public void execute(String source, String dest) throws Exception
    {
    	FileAccessFactory.createDirectoryIfNotPresent(dest);
    	ArrayList<String> files = FileAccessFactory.listFiles(source, false);
    	
        int nrOfProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);
        CompletionService<String> cservice = new ExecutorCompletionService<String>(eservice);
        List<String> list = new ArrayList<String>();
        for(String file: files)
        {
            cservice.submit(new SingleFileOrderer(source, file, dest));
            list.add(file);
        }

        for(int i = 0; i < list.size(); i++)
        {    
        	try
            {
                String info = (String)cservice.take().get();
                System.out.println(info);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        eservice.shutdown();
    }
}
