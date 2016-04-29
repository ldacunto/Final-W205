package com.w205.utils;


public abstract class Writer
{
	
    public Writer() throws Exception
    {
    }

    public synchronized void open(String fileName) throws Exception
    {
    }

    public synchronized void open(String fileName, boolean append) throws Exception
    {
    }
    
    public synchronized void write(String line) throws Exception
    {
    }

    public synchronized void close() throws Exception
    {
    }

    public synchronized boolean isOpen()
    {
    	return false;
    }
}
