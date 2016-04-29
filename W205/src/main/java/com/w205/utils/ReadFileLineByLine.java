package com.w205.utils;

import java.io.*;

public class ReadFileLineByLine extends Reader
{
	private BufferedReader br = null;
	
    public ReadFileLineByLine(String fileName) throws Exception
    {
    	super(fileName);
        FileInputStream fstream = new FileInputStream(fileName);
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
        br.close();
    }
}
