package com.w205;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;

public class Configuration 
{

	public static final boolean HADOOP = true;
	
	private static final Configuration instance = new Configuration();
		
	private FileSystem fs = null;
	
	private void load()
	{
		org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
		conf.set("fs.default.name","hdfs://127.0.0.1:8020/");
		conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		conf.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
		conf.set("dfs.replication", "1");
		try 
		{
			fs = FileSystem.get(conf);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private Configuration()
	{
		load();
	}
	public static Configuration getInstance()
	{
		return instance;
	}
	
	public FileSystem getFileSystem()
	{
		return fs;
	}
	
}
