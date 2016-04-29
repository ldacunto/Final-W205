package com.w205.preprocess;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.w205.utils.FileAccessFactory;
import com.w205.utils.Reader;
import com.w205.utils.Writer;

public class PrivacyProtector
{

    public PrivacyProtector()
    {
    }

    private String randomIP()
    {
        Random r = new Random();
        return (new StringBuilder(String.valueOf(r.nextInt(256)))).append(".").append(r.nextInt(256)).append(".").append(r.nextInt(256)).append(".").append(r.nextInt(256)).toString();
    }

    public void processPath(String source, String dest) throws Exception
    {
    	FileAccessFactory.createDirectoryIfNotPresent(dest);
    	ArrayList<String> files = FileAccessFactory.listFiles(source, false);
        Writer writer = FileAccessFactory.createWriter();
        HashMap<String, Integer> ids = new HashMap<String, Integer>();
        HashMap<String, String> ipAddress = new HashMap<String, String>();
        int currentId = -1;
        for(String file: files)
        {
            System.out.println("Processing: " + file);
            writer.open((new StringBuilder(String.valueOf(dest))).append(File.separator).append(file).toString());
            Reader reader = FileAccessFactory.createReader(source + File.separator + file);
            for(String line = ""; line != null;)
            {
                line = reader.next();
                if(line != null)
                {
                    String tmp[] = line.split("\\t");
                    Integer id = (Integer)ids.get(tmp[30]);
                    if(id == null)
                    {
                        currentId++;
                        ids.put(tmp[30], Integer.valueOf(currentId));
                        id = Integer.valueOf(currentId);
                    }
                    line = line.replace(tmp[30], (new StringBuilder()).append(id).toString());
                    line = line.replace(tmp[2], (new StringBuilder()).append(id).toString());
                    String ip = (String)ipAddress.get(tmp[15]);
                    if(ip == null)
                    {
                        ip = randomIP();
                        ipAddress.put(tmp[15], ip);
                    }
                    line = line.replace(tmp[15], ip);
                    writer.write(line);
                }
            }

            reader.close();
            writer.close();
        }
    }

    public void execute(String source, String dest) throws Exception
    {
    	processPath(source  + "20160308", dest + "20160308");
        processPath(source  + "20160309", dest + "20160309");
        processPath(source  + "20160310", dest + "20160310");
        processPath(source  + "20160311", dest + "20160311");
        processPath(source  + "20160312", dest + "20160312");
        processPath(source  + "20160313", dest + "20160313");
        processPath(source  + "20160314", dest + "20160314");
    }
}
