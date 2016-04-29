package com.w205.preprocess;


import java.io.File;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.w205.utils.FileAccessFactory;
import com.w205.utils.Reader;
import com.w205.utils.Writer;

public class Normalizer
{

	public static final String TYPE_NOMATCH = "NOMATCH";
    public static final String TYPE_AD = "AD";
    public static final String TYPE_LIVETV = "LIVETV";
    public static final String TYPE_DVR = "DVR";
    public static final String TYPE_MOVIE = "MOVIE";
    public static final String TYPE_TVSERIE = "TVSERIE";
    
    public Normalizer()
    {
    }

    private String format(String data)
    {
        if(data == null)
            return "-";
        if("".equals(data))
            return "-";
        if("null".equals(data))
            return "-";
        else
            return data;
    }

    public void normalizeFile(String fileInput, String pathSource, Writer writer) throws Exception
    {
    	
        Reader reader = FileAccessFactory.createReader((new StringBuilder(String.valueOf(pathSource))).append(File.separator).append(fileInput).toString());
        for(String line = ""; line != null;)
        {
            line = reader.next();
            if(line != null)
            {
                String tmp[] = line.split("\\t");
                StringBuffer buffer = new StringBuffer();
                buffer.append(tmp[0]);
                buffer.append("\t");
                buffer.append(tmp[2]);
                buffer.append("\t");
                buffer.append(tmp[15]);
                buffer.append("\t");
                buffer.append(tmp[8]);
                buffer.append("\t");
                buffer.append(tmp[9]);
                buffer.append("\t");
                String type = "";
                String data1 = "";
                String data2 = "";
                JSONParser parser = new JSONParser();
                String json = tmp[38];
                JSONObject obj = (JSONObject)parser.parse(json);
                try
                {
                    if(json.contains("\"type\":\"ad\""))
                    {
                        type = "AD";
                        data1 = format((String)obj.get("brand"));
                    } 
                    else if(json.contains("\"type\":\"livetv\""))
                    {
                        type = "LIVETV";
                        data1 = format((String)obj.get("channel"));
                        data2 = format((String)obj.get("program"));
                    } 
                    else if(json.contains("\"type\":\"dvr\""))
                    {
                        type = "DVR";
                        data1 = format((String)obj.get("channel"));
                        data2 = format((String)obj.get("program"));
                    } 
                    else if(json.contains("\"g\":\"2\""))
                    {
                        type = "MOVIE";
                        obj = (JSONObject)((JSONArray)obj.get("v")).get(0);
                        data1 = format((String)obj.get("title"));
                    } 
                    else if(json.contains("\"g\":\"3\""))
                    {
                        type = "TVSERIE";
                        obj = (JSONObject)((JSONArray)obj.get("v")).get(0);
                        data1 = format((String)obj.get("title"));
                        data2 = (new StringBuilder("S")).append(format((String)obj.get("season"))).append("E").append(format((String)obj.get("episode"))).toString();
                    }
                }
                catch(Exception e)
                {
                    System.out.println(line);
                    e.printStackTrace();
                }
                buffer.append(type);
                buffer.append("\t");
                buffer.append(data1);
                buffer.append("\t");
                buffer.append(data2);
                buffer.append("\t");
                writer.write(buffer.toString());
            }
        }

        reader.close();
        
    }

    public void processPath(String pathSource, String dest, String output) throws Exception
    {
        ArrayList<String> files = FileAccessFactory.listFiles(pathSource, false);
        FileAccessFactory.createDirectoryIfNotPresent(dest);
    	
    	Writer writer = FileAccessFactory.createWriter();
        writer.open(dest + File.separator + output, true);
        
        for(String file: files)
        {
            System.out.println("Processing: " + file);
            normalizeFile(file, pathSource, writer);
        }
        writer.close();
    }

    public void execute(String source, String dest) throws Exception
    {
        processPath(source  + "20160308", dest, "20160308");
        processPath(source  + "20160309", dest, "20160309");
        processPath(source  + "20160310", dest, "20160310");
        processPath(source  + "20160311", dest, "20160311");
        processPath(source  + "20160312", dest, "20160312");
        processPath(source  + "20160313", dest, "20160313");
        processPath(source  + "20160314", dest, "20160314");
    }
}
