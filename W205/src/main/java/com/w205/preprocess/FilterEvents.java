package com.w205.preprocess;

import java.io.File;
import java.util.ArrayList;

import com.w205.utils.FileAccessFactory;
import com.w205.utils.Reader;
import com.w205.utils.Writer;

public class FilterEvents
{

    public FilterEvents()
    {
    }

    public void processPath(String source, String dest) throws Exception
    {
    	FileAccessFactory.createDirectoryIfNotPresent(dest);
    	ArrayList<String> files = FileAccessFactory.listFiles(source, false);
		Writer writer = FileAccessFactory.createWriter();
        for(String file: files)
        {
            System.out.println("Processing: " + file);
            writer.open(dest  + File.separator + file);
            Reader reader = FileAccessFactory.createReader(source  + File.separator + file);
            for(String line = ""; line != null;)
            {
                line = reader.next();
                if(line != null && !line.contains("{\"error\":\"stored - not found\"}") && !line.contains("{\"error\":\"jedis - empty response\"}") && !line.contains("NOMATCH") && (line.contains("com.discoball.aphone") || line.contains("com.perk.livetv.aphone") || line.contains("com.perk.livetv.iphone")))
                    writer.write(line);
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
