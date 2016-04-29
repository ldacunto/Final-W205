package com.w205.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class ReadHadoopFileLineByLine extends Reader
{
	private BufferedReader br = null;
    private FileSystem fs = null;
    private FSDataInputStream fstream = null;
    
    public ReadHadoopFileLineByLine(String fileName) throws Exception
    {
    	super(fileName);
    	fs = com.w205.Configuration.getInstance().getFileSystem();
        fstream = fs.open(new Path(fileName));
        DataInputStream in = new DataInputStream(fstream);
        br = new BufferedReader(new InputStreamReader(in, "UTF8"));
    }

    public synchronized String next()
    {
        try
        {
            String strLine = br.readLine();
            return strLine;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public void close() throws Exception
    {
        try{br.close();}catch(Exception exception) {}
        try{fstream.close();}catch(Exception exception) {}
        br = null;
        fs = null;
        fstream = null;
    }
}
