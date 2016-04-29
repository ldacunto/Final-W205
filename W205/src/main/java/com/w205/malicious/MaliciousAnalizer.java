package com.w205.malicious;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.concurrent.Callable;

import com.w205.utils.FileAccessFactory;
import com.w205.utils.Reader;


public class MaliciousAnalizer implements Callable<String>
{
	private String source = "";
	private String id = "";
	
    public MaliciousAnalizer(String source, String id)
    {
        this.source = source;
        this.id = id;
    }

    private void addUser(Connection con, String id, ContentLine line, int calls, String type, String mostwatchedcontent, int totaltime, 
            			int mostwatchedmateches) throws Exception
    {
        String query = "insert into users (userid, ipaddress, lon, lat,time_first, calls,type, mostwatchedcontent, totaltime, mostwatchedmateches) values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, id);
        pstmt.setString(2, line.ipaddress);
        pstmt.setString(3, line.log);
        pstmt.setString(4, line.lat);
        pstmt.setTimestamp(5, new Timestamp(line.timestamp));
        pstmt.setInt(6, calls);
        pstmt.setString(7, type);
        pstmt.setString(8, mostwatchedcontent);
        pstmt.setInt(9, totaltime);
        pstmt.setInt(10, mostwatchedmateches);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public String call()
    {
        Connection con;
        Statement st;
        String url;
        String user;
        String password;
        con = null;
        st = null;
        url = "jdbc:postgresql://localhost/postgres";
        user = "postgres";
        password = "postgres";
        try
        {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            Reader reader = FileAccessFactory.createReader(source + id);
            String line = "";
            Content content = new Content();
            while(line != null) 
            {
                line = reader.next();
                if(line != null)
                    content.addLine(line);
            }
            reader.close();
            content.calculateTime();
            content.calculateContent();
            if(content.is24Hours())
                addUser(con, id, content.getContent(0), content.size(), "24H", content.getMostViewContent(), (int)content.getTotalTime(), content.getTotalMostWatchedMatches());
            else if(content.isAlwaysSameContent())
                addUser(con, id, content.getContent(0), content.size(), "SAME CONTENT", content.getMostViewContent(), (int)content.getTotalTime(), content.getTotalMostWatchedMatches());
            else
                addUser(con, id, content.getContent(0), content.size(), "GOOD", content.getMostViewContent(), (int)content.getTotalTime(), content.getTotalMostWatchedMatches());
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        try{st.close();}catch(Exception exception){}
        try{con.close();}catch(Exception exception){}
        
        return source + id;
    }

    
}
