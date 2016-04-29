package com.w205.malicious;

import java.util.*;

public class Content
{

	private ArrayList<ContentLine> content = new ArrayList<ContentLine>();
    private static final long THREASHOLD = 0x493e0L;
    private long totalTime = 0L;
    private long teoreticalTime = 0L;
    private String mostWatched = "";
    private int totalMatches = 0;
    private int totalMostWatchedMatches = -1;
    private HashMap<String, Integer> matches = new HashMap<String, Integer>();
    
    public Content()
    {
    }

    public void addLine(String line)
    {
        content.add(new ContentLine(line));
    }

    public void calculateTime()
    {
        totalTime = 0L;
        for(int i = 0; i < content.size() - 1; i++)
        {
            if(!((ContentLine)content.get(i)).type.equals("DVR"))
            {
                String key = (new StringBuilder(String.valueOf(((ContentLine)content.get(i)).type))).append("_").append(((ContentLine)content.get(i)).data1).toString();
                Integer counter = (Integer)matches.get(key);
                if(counter == null)
                {
                    matches.put(key, Integer.valueOf(1));
                } 
                else
                {
                    counter = Integer.valueOf(counter.intValue() + 1);
                    matches.put(key, counter);
                }
            }
            long d = ((ContentLine)content.get(i + 1)).timestamp - ((ContentLine)content.get(i)).timestamp;
            if(d <= THREASHOLD)
                totalTime += d;
        }

        totalMatches = content.size();
        teoreticalTime = ((ContentLine)content.get(content.size() - 1)).timestamp - ((ContentLine)content.get(0)).timestamp;
    }

    public boolean is24Hours()
    {
        if(teoreticalTime > 0x1687d280L)
        {
            double percentage = (double)totalTime / (double)teoreticalTime;
            return percentage > 0.80000000000000004D;
        } 
        else
        {
            return false;
        }
    }

    public long getTotalTime()
    {
        return totalTime;
    }

    public long getTeoreticalTime()
    {
        return teoreticalTime;
    }

    public boolean isAlwaysSameContent()
    {
        double threashold = 0.69999999999999996D;
        boolean result = false;
        if(mostWatched.startsWith("LIVE_"))
            return false;
        if(mostWatched.startsWith("AD_"))
        {
            result = totalMostWatchedMatches > 250;
            return result;
        }
        if(mostWatched.startsWith("TVSERIE_"))
            threashold = 0.40000000000000002D;
        if(mostWatched.startsWith("MOVIE_"))
            threashold = 0.40000000000000002D;
        if(teoreticalTime > 0x7829b80L)
        {
            double percentage = (double)totalMostWatchedMatches / (double)totalMatches;
            result = percentage > threashold && totalTime > 0x2255100L;
        }
        return result;
    }

    public String getMostViewContent()
    {
        return mostWatched;
    }

    public int getTotalMatches()
    {
        return totalMatches;
    }

    public int getTotalMostWatchedMatches()
    {
        return totalMostWatchedMatches;
    }

    public void calculateContent()
    {
        for(Iterator<String> iterator = matches.keySet().iterator(); iterator.hasNext();)
        {
            String key = (String)iterator.next();
            Integer c = (Integer)matches.get(key);
            if(c.intValue() > totalMostWatchedMatches)
            {
                totalMostWatchedMatches = c.intValue();
                mostWatched = key;
            }
        }

    }

    public String getRelevantIPAddress()
    {
        return null;
    }

    public int size()
    {
        return content.size();
    }

    public ContentLine getContent(int pos)
    {
        return (ContentLine)content.get(pos);
    }
}
