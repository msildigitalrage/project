package com.kithara;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Locations {
	public static String mountPath;
	public static String projectPath;
	
	public void searchLocations(){
		
		try{
			  Process p2= Runtime.getRuntime().exec( "sudo chmod -R 777 "+mountPath);
			  p2.waitFor();
		  }
		  catch(IOException | InterruptedException e1){
			  System.out.println("sudo chmod -R 777 problem");
		  }
			System.out.println(mountPath);
			System.out.println(projectPath);

		try {			
			//Connection with Database Locations
			Class.forName("org.sqlite.JDBC");
			Connection conLocations = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locations.db");
			Statement statementL =conLocations.createStatement();
			statementL.executeUpdate("drop table if exists myLocations;");
			statementL.executeUpdate("create table myLocations (id,timestamp,latitude,longitude,app,event);");
			System.out.println("Connection Succesful");
			
			//Connection with Database CommonData	
			Connection conCommonData = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement statementC = conCommonData.createStatement();
			
			//Start Collector
			//init
			int cv = 0;
			String timestamp,latitude,longitude,app,event;
			String sql;
			//---Viber
			ResultSet rsViber = statementC.executeQuery("SELECT timestamp,latitude,longitude,interlocutor,message,type FROM viber WHERE latitude !='0' AND type='1'");
			app = "Viber";
			while (rsViber.next()) {
				cv++;
				timestamp = rsViber.getString("timestamp");
				latitude=rsViber.getString("latitude");
				longitude=rsViber.getString("longitude");
				event = "Communicated with " +rsViber.getString("interlocutor") + ". Message of the event " +rsViber.getString("message")+ ".";
				//System.out.println(cv+". "+ timestamp + " " + latitude + "|" + longitude + " Application: Twitter, event: " + event);
				
				sql= "INSERT INTO myLocations VALUES ("+cv+",\'"+timestamp+"\',\'"+latitude+"\',\'"+longitude+"\',\'"+app+"\',\'"+event+"\');";
				statementL.executeUpdate(sql);
				conLocations.setAutoCommit(false);
			    conLocations.commit();
			}
			//WhatsUp
			ResultSet rsWhatsUp = statementC.executeQuery("SELECT contactWith,whoSend,timeStamp,latitude,longitude FROM whatsUp WHERE latitude !='0.0' AND whoSend='Sent'");
			app = "Whats Up";
			while (rsWhatsUp.next()) {
				cv++;
				timestamp = rsWhatsUp.getString("timestamp");
				latitude=rsWhatsUp.getString("latitude");
				longitude=rsWhatsUp.getString("longitude");
				event = "Communicated with " +rsWhatsUp.getString("contactWith");
				//System.out.println(cv+". "+ timestamp + " " + latitude + "|" + longitude + " Application: WhatsUp, event: " + event);
				
				sql= "INSERT INTO myLocations VALUES ("+cv+",\'"+timestamp+"\',\'"+latitude+"\',\'"+longitude+"\',\'"+app+"\',\'"+event+"\');";
				statementL.executeUpdate(sql);
				conLocations.setAutoCommit(false);
			    conLocations.commit();
			}
			//twitter
			//readtmpTwitter, find ids that send position from device
			 ResultSet rs = statementL.executeQuery("select * from tmpTwitter;");
			 String []tmpIDS = null; int c = 0;
			 String t = null;
			 while(rs.next()){			 
				 t = rs.getString("id");
				 //System.out.println(t);
				 c++;
			 }
			 tmpIDS = new String [c];
			 rs = statementL.executeQuery("select * from tmpTwitter;");
			 int b = c-1;
			 c--;
			 while(rs.next()){
				 t = rs.getString("id");
				 //System.out.println(t + c);
				 tmpIDS [c] = t;
				 System.out.println(tmpIDS[c]+" "+c);
				 c--;
			 }
			
			 for (int v = 0; v < tmpIDS.length; v++) {
				 //System.out.println(b+ "" +v);
				 ResultSet rstwitter = statementC.executeQuery("SELECT author_id,content,created,latitude,longitude FROM twitter WHERE latitude !='null' AND author_id ='"+tmpIDS [v]+"'");
				 System.out.println(tmpIDS [v]);		

			app = "Twitter";

			while (rsWhatsUp.next()) {
				cv++;
				timestamp = rstwitter.getString("created");
				latitude=rstwitter.getString("latitude");
				longitude=rstwitter.getString("longitude");
				event = "Message Sent " +rstwitter.getString("content");
				//System.out.println(cv+". "+ timestamp + " " + latitude + "|" + longitude + " Application: Twitter, event: " + event);
				//----------need to fix the ids

				sql= "INSERT INTO myLocations VALUES ("+cv+",\'"+timestamp+"\',\'"+latitude+"\',\'"+longitude+"\',\'"+app+"\',\'"+event+"\');";
				statementL.executeUpdate(sql);
				conLocations.setAutoCommit(false);
			    conLocations.commit();
			}
				}
			//close DB's connection
			conCommonData.close();
			conLocations.close();
		} catch (Exception e) {
			System.out.println("Connection with DB failed");
			// TODO: handle exception
		}
		
		mapCreator();
	}
public void mapCreator() {		
		
		//ypothetic positions
		double[] lat = new double []{50.375457,51.511214,53.479324};
		double[] lon = new double []{-4.142656,-0.119824,-2.248485};
		String[] titleEvent = new String []{"Event A","Event B","Event C"};
		String[] contentEvent = new String [] {"Information about the Event A","Information about the Event B","Information about the Event C"};
			
		//number of locations
		int max = 2;
		
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try {
			fWriter = new FileWriter("map.html");
		    writer = new BufferedWriter(fWriter);
		    //--html top
		    writer.write("<!DOCTYPE html>\n<html>\n<head>\n<title>Locations</title>\n");
		    writer.write("<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\n<meta charset=\"utf-8\">");
		    writer.write("\n<style>\nhtml, body, #map-canvas { height: 100%;margin: 0px;padding: 0px} \n</style>\n");
		    writer.write("<script src=\"https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false\"></script>");
		    //----------	
		    
		    //------------JavaScript ------------
		    
		    //-- Start Script
		    writer.write("\n<script>");	
		    writer.write("\nvar line;");
			writer.write(
					"\nfunction initialize() {\n");
			
			
		    //--addCoordinates
		    writer.write("var coordinates = [");
		    for (int i = 0; i <= max; i++) {
			    writer.write("\nnew google.maps.LatLng("+lat[i]+","+ lon[i]+")");
				if(i<=max-1){    
				    writer.write(",");	
				}
				
			}
		    writer.write("\n];");

		    
		    //--mapOptions
			writer.write(
					  "\nvar mapOptions = {"+
					    "\nzoom: 6,"+
					    //"\ncenter: myLatlng,"+
					    "\ncenter: new google.maps.LatLng("+lat[0]+","+ lon[0]+"),"+
					    "\nmapTypeId: google.maps.MapTypeId.ROADMAP"+
					  "\n}");

		    //--map
		    writer.write(
					  "\nvar map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);");
		    //--infoWindow
	    	writer.write("\nvar ");
		    for (int i = 0; i <= max; i++) {
			    writer.write("infowindow"+i+"");	
				if(i<=max-1){  
			    writer.write(",");
				}
			}
			writer.write(";");
		    //--addMarks-------------------
			for (int i = 0; i <= max; i++) {
		    writer.write("\nvar marker"+i+" = new google.maps.Marker({position: coordinates["+i+"],map: map,title:\""+titleEvent[i]+"\"});");
		    	writer.write("\ninfowindow"+i+" = new google.maps.InfoWindow({\ncontent:'<div>"+ contentEvent[i]+"</div>'\n});");
			}
			    
		    //--addListener
			for (int i = 0; i <= max; i++) {
		    writer.write("\ngoogle.maps.event.addListener(marker"+i+", 'click', function() {"+"\ninfowindow"+i+".open(map,marker"+i+");"+"\n});");
			}
		    
		    //--------------Set Polyline Simple-------
		    
		    //--lineSymbol setting
		    writer.write("\nvar lineSymbol = {\npath: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,\nscale: 2,\nstrokeColor: '#00FF00'\n};");
		    
		    //--addPathLine
		    writer.write("\nline = new google.maps.Polyline({\n"+
		    "path: coordinates,\nicons: [{\nicon: lineSymbol,\noffset: '100%'\n}],\ngeodesic: true, \nstrokeColor: '#FF0000',\nstrokeOpacity: 1.0, \nstrokeWeight: 2\n});");
		    writer.write("\nline.setMap(map);");
		   
		    //--call animationFunction
		    writer.write("\nanimate();");
		    //--close function initialize()
		    writer.write("\n}");
		   
		    writer.write("\nfunction animate() {\nvar count = 0;\nwindow.setInterval(function() {\ncount = (count + 1) % 200;\nvar icons = line.get('icons');\nicons[0].offset = (count / 2) + '%';\nline.set('icons', icons);\n}, 20);\n}");
		    		    
		    //--DomListener
		    writer.write(		
					"\ngoogle.maps.event.addDomListener(window, 'load', initialize);");
		    //--closeScript
		    writer.write(	
					   "\n</script>");
		    //--javaScript Ends
		    
		    //--html bottom
		    writer.write("\n</head>\n<body>\n<div id=\"map-canvas\"></div>\n</body>\n</html>");
		    //writer.newLine(); //this is not actually needed for html files - can make your code more readable though 
		    //-------------
		    writer.close(); //make sure you close the writer object 
			
		   //temp open map.html
		   //Runtime.getRuntime().exec("google-chrome /home/iceman/Project/MapCreator/map.html");
		   //Runtime.getRuntime().exec("gedit /home/iceman/Project/MapCreator/map.html");
		} catch (IOException e) {
			System.out.println("Something Went Wrong..");
			e.printStackTrace();
		}	
	}
}
