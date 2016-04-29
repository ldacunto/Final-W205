package com.w205.sessions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.Callable;

import com.w205.utils.FileAccessFactory;
import com.w205.utils.Reader;
import com.w205.utils.Writer;

public class SingleFileOrderer implements Callable<String>
{

	private String source;
    private String file;
    private String dest;
    
    public SingleFileOrderer(String source, String file, String dest) throws Exception
    {
        this.source = "";
        this.file = "";
        this.dest = "";
        this.file = file;
        this.source = source;
        this.dest = dest;
    }

    public String call()
    {
        try
        {
            ArrayList<LineUser> lines = new ArrayList<LineUser>();
            Reader reader = FileAccessFactory.createReader((new StringBuilder(String.valueOf(source))).append(File.separator).append(file).toString());
            for(String line = ""; line != null;)
            {
                line = reader.next();
                if(line != null)
                    lines.add(new LineUser(line));
            }

            reader.close();
            Collections.sort(lines);
            Writer writer = FileAccessFactory.createWriter();
            writer.open(dest + File.separator + file);
            LineUser l;
            for(Iterator<LineUser> iterator = lines.iterator(); iterator.hasNext(); writer.write(l.getLine()))
                l = (LineUser)iterator.next();

            writer.close();
            return file;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
