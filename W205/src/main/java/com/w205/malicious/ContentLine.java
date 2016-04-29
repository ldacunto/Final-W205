package com.w205.malicious;


public class ContentLine
{

    public ContentLine(String line)
    {
        timestamp = -1L;
        type = null;
        data1 = null;
        data2 = null;
        ipaddress = null;
        log = null;
        lat = null;
        String tmp[] = line.split("\\t");
        timestamp = Long.parseLong(tmp[0]);
        type = tmp[5];
        data1 = tmp[6];
        if(tmp.length > 7)
            data2 = tmp[7];
        ipaddress = tmp[2];
        log = tmp[3];
        lat = tmp[4];
    }

    public long timestamp;
    public String type;
    public String data1;
    public String data2;
    public String ipaddress;
    public String log;
    public String lat;
}
