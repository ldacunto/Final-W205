package com.w205.sessions;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.w205.utils.FileAccessFactory;
import com.w205.utils.Reader;
import com.w205.utils.Writer;

public class SplitterByUser
{

	private String dest = null;
	
    public SplitterByUser()
    {
    }

    public void fileSplitter(String fileInput) throws Exception
    {
    	Reader reader = FileAccessFactory.createReader(fileInput);
        String line = "";
        HashMap<String, ArrayList<String>> users = new HashMap<String, ArrayList<String>>();
        int c = 0;
        while(line != null) 
        {
            line = reader.next();
            if(line != null)
            {
            	c++;
                String tmp[] = line.split("\\t");
                ArrayList<String> data = (ArrayList<String>)users.get(tmp[1]);
                if(data == null)
                {
                    data = new ArrayList<String>();
                    users.put(tmp[1], data);
                }
                data.add(line);
                if (c%10000 == 0)
                	System.out.println(c);
            }
        }
        reader.close();
        
        for(String id: users.keySet())
        {
        	Writer writer = FileAccessFactory.createWriter();
            writer.open(dest + File.separator  + id, true);
            ArrayList<String> data = (ArrayList<String>)users.get(id);
            
            for(String s: data)
            	writer.write(s);

            writer.close();
        }

    }

    public void execute(String source, String dest) throws Exception
    {
    	FileAccessFactory.createDirectoryIfNotPresent(dest);
        this.dest = dest;
        ArrayList<String> files = FileAccessFactory.listFiles(source, true);
        for(String file: files)
        {
            System.out.println("Processing: " + file);
            fileSplitter(file);
        }

    }
}
