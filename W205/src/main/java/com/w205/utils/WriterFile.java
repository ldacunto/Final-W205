package com.w205.utils;

import java.io.*;

public class WriterFile extends Writer
{
	private BufferedWriter bw = null;
	
    public WriterFile() throws Exception
    {
    	super();
    }

    public synchronized void open(String fileName) throws Exception
    {
        bw = new BufferedWriter(new FileWriter(new File(fileName), false));
    }

    public synchronized void open(String fileName, boolean append) throws Exception
    {
    	bw = new BufferedWriter(new FileWriter(new File(fileName), append));
    }
    
    public synchronized void write(String line) throws Exception
    {
        bw.write((new StringBuilder(String.valueOf(line))).append("\n").toString());
        bw.flush();
    }

    public synchronized void close() throws Exception
    {
        bw.flush();
        bw.close();
        bw = null;
    }

    public synchronized boolean isOpen()
    {
        return bw != null;
    }
}
