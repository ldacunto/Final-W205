package com.w205.utils;

import java.io.File;
import java.util.ArrayList;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import com.w205.Configuration;

// Manager of the file system that allows to easily test the code in a Windows/Linux environment
// and in a HDFS
public class FileAccessFactory
{
	
    public static Reader createReader(String fileName) throws Exception
    {
    	if (Configuration.HADOOP)
    		return new ReadHadoopFileLineByLine(fileName);
    	else
    		return new ReadFileLineByLine(fileName);
    }

    public static Writer createWriter() throws Exception
    {
    	if (Configuration.HADOOP)
    		return new WriterHadoopFile();
    	else
    		return new WriterFile();
    }

    public static void createDirectoryIfNotPresent(String path) throws Exception
    {
    	if (Configuration.HADOOP)
    	{
    		FileSystem fs = Configuration.getInstance().getFileSystem();
    		Path pathHadoop = new Path(path);
    		if (!fs.exists(pathHadoop))
    		{
    			fs.mkdirs(pathHadoop);
    		}
    	}
    	else
    	{
    		File f = new File(path);
            if(!f.exists())
                f.mkdirs();
    	}
    }
    
    
    public static ArrayList<String> listFiles(String path, boolean complete) throws Exception
    {
    	if (Configuration.HADOOP)
    	{
    		FileSystem fs = Configuration.getInstance().getFileSystem();
    		Path pathHadoop = new Path(path);
    		RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(pathHadoop, false);
    		ArrayList<String> list = new ArrayList<String>();
    	    while(fileStatusListIterator.hasNext())
    	    {
    	        LocatedFileStatus fileStatus = fileStatusListIterator.next();
    	        if (complete)
    	        	list.add(fileStatus.getPath().toString());
    	        else
    	        	list.add(fileStatus.getPath().getName().toString());
    	    }
    	    return list;
    	}
    	else
    	{
	    	File file = new File(path);
	        File list[] = file.listFiles();
	        ArrayList<String> l = new ArrayList<String>();
	        for(File f: list)
	        {
	        	if (complete)
	        		l.add(f.getAbsolutePath());
	        	else
	        		l.add(f.getName());
	        }
	        return l;
    	}
    }
}
