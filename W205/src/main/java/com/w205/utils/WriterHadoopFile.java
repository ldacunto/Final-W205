package com.w205.utils;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class WriterHadoopFile extends Writer
{
	private BufferedWriter bw = null;
    private FileSystem fs = null;
    private FSDataOutputStream out = null;
    
    public WriterHadoopFile() throws Exception
    {
    	super();
    	fs = com.w205.Configuration.getInstance().getFileSystem();
    }

    public synchronized void open(String fileName) throws Exception
    {
        out = fs.create(new Path(fileName), true);
        bw = new BufferedWriter(new OutputStreamWriter(out));
    }

    public synchronized void open(String fileName, boolean append) throws Exception
    {
    	Path p = new Path(fileName);
    	
    	if (!append)
    		out = fs.create(p, true);
    	else
    	{
    		if (fs.exists(p))
    			out = fs.append(p);
    		else
    			out = fs.create(p, true);
    	}
        bw = new BufferedWriter(new OutputStreamWriter(out));	
    }
    
    
    public synchronized void write(String line) throws Exception
    {
        bw.write((new StringBuilder(String.valueOf(line))).append("\n").toString());
        //bw.flush();
    }
    
    public synchronized void close() throws Exception
    {
    	try{bw.flush();}catch(Exception exception) {}
    	try{out.flush();}catch(Exception exception) {}
    	try{bw.close();}catch(Exception exception) {}
        try{out.close();}catch(Exception exception) {}
        bw = null;
        out = null;
    }

    public synchronized boolean isOpen()
    {
        return bw != null;
    }   
}
