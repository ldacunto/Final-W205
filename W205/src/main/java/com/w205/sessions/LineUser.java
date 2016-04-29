package com.w205.sessions;


public class LineUser implements Comparable<LineUser>
{

	private Long time;
    private String line;
    
    public LineUser(String line)
    {
        time = null;
        this.line = null;
        this.line = line;
        time = Long.valueOf(Long.parseLong(line.split("\\t")[0]));
    }

    public int compareTo(LineUser o)
    {
        return Long.compare(time.longValue(), o.time.longValue());
    }

    public String getLine()
    {
        return line;
    }
    
}
