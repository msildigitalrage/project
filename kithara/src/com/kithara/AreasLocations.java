package com.kithara;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AreasLocations {
	public static String mountPath;
	public static String projectPath;
	
	public void init(){
		System.out.println("Areas user moves");
		
		//Connection with Database Locations
		try {

			//Event with TimeStamp and Location
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locations.db");
			Statement statement =con.createStatement();
			ResultSet rs;
			rs = statement.executeQuery("SELECT Count(*) AS total FROM myLocations");
			int k = 0;
			while(rs.next()){
				k = rs.getInt("total");
			}
			//System.out.println(k);
			rs = statement.executeQuery("select * from myLocations ORDER BY timestamp");
			String timestamp,latitude,longitude; String table[][] = null;
			table = new String[k][3];
			int i = 0;
			while (rs.next()) {//take every event that has location and timestamp
				timestamp = rs.getString("timestamp");
				latitude = rs.getString("latitude");
				longitude = rs.getString("longitude");
				table[i][0] = timestamp;
				table[i][1] = latitude;
				table[i][2] = longitude;
				
				//System.out.println(timestamp + " " + latitude + " " + longitude);
				i++;
			}
			//Event with TimeStamp and No Location
			Connection conSearch = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"tempDb.db");
			Statement stateSearch = conSearch.createStatement();
			ResultSet rsSearch;
			long threshHoldUp; String u = null;
			long threshHoldDown; String d = null;
			String msg = null;
			System.out.println(table.length);
			//--- find timestamps that are similar (in a spectrum of time 2 Hours) and save the location
			//1378242635733
			//

			for (int j = 0; j < table.length; j++) {
				threshHoldUp = 3600000 + Long.parseLong(table[j][0]);//1 hour to millisecond
				threshHoldDown = 3600000 - Long.parseLong(table[j][0]);//1 hour to millisecond
				u = Long.toString(threshHoldUp);
				d = Long.toString(threshHoldDown);
				//System.out.println(u);
				//select * from timeline where timestamp='1380659729327'
				rsSearch = stateSearch.executeQuery("select * from timeline WHERE timestamp = 1378242635733;");
				while(rsSearch.next()){
					timestamp = rsSearch.getString("timestamp");   //BETWEEN '"+u+"' AND '"+d+"'
					msg = rsSearch.getString("application") + " >> " + rsSearch.getString("message") ;
					
					System.out.println(j+" Near timestamp: " + table[j][0] + " found this " +timestamp + " with this " + msg);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("to pulaki");
			e.printStackTrace();
		}
		
	}
	
}
