package com.auda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AreasLocations {
	public static String mountPath;
	public static String projectPath;
	String mapTable[][];//will hold possible locations
	/*
	Setting up thresh, will return events(without default stored location)... having a location based on events
	that happened near others that have stored location.
	*/
	long thresh = 300;//millisec
	
	public void thresh(){
		//give thresh
		final JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		final JTextField threshText = new JTextField(String.valueOf(thresh),20);
		final JButton setThresh = new JButton("Set Thresh");
		panel.add(threshText);
		panel.add(setThresh);
		frame.add(panel);
		frame.requestFocus();
        frame.setTitle("Set Thresh (msecs)");
        frame.pack();
        frame.setVisible(true);
		setThresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(threshText.getText().isEmpty()){
				System.out.println("Empty Thresh");	
				}else{
					thresh = Long.parseLong(threshText.getText().toString());
					init();	
				}
				frame.setVisible(false);
			}
		});
		
		
	}
	public void init(){
		//Connection with Database Locations
		try {
			
//----------Event with TimeStamp and Location----------------
			
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locations.db");
			Statement statement =con.createStatement();
			ResultSet rs;
			rs = statement.executeQuery("SELECT Count(*) AS total FROM myLocations");
			int k = 0;
			while(rs.next()){
				k = rs.getInt("total");
			}
			System.out.println("number of events with locations"+k);
			rs = statement.executeQuery("select * from myLocations ORDER BY timestamp");
			String timestamp,latitude,longitude; String table[][] = null;
			table = new String[k][4];
			int i = 0; 
			String idLocTable;//id of event on myLocations Table
			while (rs.next()) {//take every event that has location and timestamp
				timestamp = rs.getString("timestamp");
				latitude = rs.getString("latitude");
				longitude = rs.getString("longitude");
				idLocTable = rs.getString("id");
				table[i][0] = timestamp;
				table[i][1] = latitude;
				table[i][2] = longitude;
				table[i][3] = idLocTable;
				i++;
			}
			
//----------Event with TimeStamp and No Location----------------
			
			Connection con2 = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"tempDb.db");
			Statement statement2 = con2.createStatement();
			ResultSet rs2;
			rs2 = statement2.executeQuery("SELECT Count(*) AS t FROM timeline");
			int kk = 0;
			while(rs2.next()){
				kk = rs2.getInt("t");
			}
			System.out.println("number of events with no location"+kk);
			rs2 = statement2.executeQuery("SELECT * FROM timeline");
			String timestamp2,app; int num=0;String table2[][] = null;table2 = new String[kk][4];
			String message = null; String numberName = null;
			while(rs2.next()){
				message = rs2.getString("message");
				numberName = rs2.getString("numberName");
				timestamp2 = rs2.getString("timestamp");
				app = rs2.getString("application");	
				table2[num][0]= Integer.toString(num+1);
				table2[num][1] = timestamp2;
				table2[num][2] = app;
				table2[num][3] = numberName + " " + message;
				num++;
			}
			//System.out.println("Final: "+ num);
			//table has loc and timestamps that exist
			//table2 has timestamp without locations
			long have,isIt,up,down; 
			String latitudeMap = null;//--
			String longitudeMap = null;//--
			String idOnTempDB = null;
			String timeStampOnLocationsDB = null;
			String idOnLocationDB = null;
			String info = null;
			Connection conThresh = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locationsThresh.db");
			Statement stateThresh =conThresh.createStatement();
			//ResultSet rsThresh;
			stateThresh.executeUpdate("drop table if exists thresh;");
			stateThresh.executeUpdate("create table thresh (idOnTempDB,latitudeMap,longitudeMap,timeStampOnLocationsDB,idOnLocationDB,info);");
			System.out.println("Threshold has been set in "+ thresh + " milliseconds before "+thresh +" msec and after "+thresh+"msec.");
			con.close();
			con2.close();
			int bb = 0;
			for (int j = 0; j < table.length; j++) {//48
				have = Long.valueOf(table[j][0]);
				up = have + thresh;
				down = have - thresh;	
				for (int j2 = 0; j2 < table2.length; j2++) {//690
					isIt = Long.valueOf(table2[j2][1]);
					if((isIt > down) && (isIt < up)){
					bb++;//System.out.println("........"+bb);
					idOnTempDB = table2[j2][0];
					timeStampOnLocationsDB = table[j][0];
					latitudeMap = table[j][1];
					longitudeMap = table[j][2];
					idOnLocationDB = table[j][3];
					//update
					info = table2[j][3];
					stateThresh.executeUpdate("INSERT INTO thresh VALUES ("+idOnTempDB+",\'"+latitudeMap+"\',\'"+longitudeMap+"\',\'"+timeStampOnLocationsDB+"\',\'"+idOnLocationDB+"\',\'"+info+"\');");
					conThresh.setAutoCommit(false);
				    conThresh.commit();
					}else{}
				}
			}
			System.out.println("........"+bb);
			conThresh.close();
			mapCreator();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("init/compare process failed");
			e.printStackTrace();
		}
		
	}
	private void mapCreator() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conThresh = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locationsThresh.db");
			Statement stateThresh =conThresh.createStatement();
			ResultSet rsThresh,rsCount;
			rsCount = stateThresh.executeQuery("select count (*) as tt from thresh");
			int count = rsCount.getInt("tt");
			System.out.println("Possible Locations Founded "+count);
			rsThresh = stateThresh.executeQuery("select * from thresh");
			int k = 0;
			String a;
			String b,c; String d,e;
			mapTable = new String[count][5];
			
			while(rsThresh.next()){//count possible locations for events
				a = rsThresh.getString("idOnTempDB");
				b = rsThresh.getString("latitudeMap");
				c = rsThresh.getString("longitudeMap");
				d = rsThresh.getString("idOnLocationDB");
				e = rsThresh.getString("info");
				if(k>0){//avoid duplicate events that are stored in threshDB, not to view them in the map
					if(a.equals(mapTable[k-1][0])){
						//System.out.println("This is duplicated.Not added to map");
					}else{
						mapTable[k][0]=a;mapTable[k][1]=b;mapTable[k][2]=c;mapTable[k][3]=d;mapTable[k][4]=e;
						k++;
					}
				}else{//for the first time
					mapTable[k][0]=a;mapTable[k][1]=b;mapTable[k][2]=c;mapTable[k][3]=d;mapTable[k][4]=e;
					k++;
				}	
				
			}
			System.out.println("Possible Locations>"+k);
			//create PossibleMap
			//number of locations
			int max = k-1;

			FileWriter fWriter = null;
			BufferedWriter writer = null;
				fWriter = new FileWriter(projectPath+"mapPossible.html");
			    writer = new BufferedWriter(fWriter);
			    //--html top
			    writer.write("<!DOCTYPE html>\n<html>\n<head>\n<title>Possible Locations with thresh: "+thresh+"msec</title>\n");
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
				    writer.write("\nnew google.maps.LatLng("+mapTable[i][1]+","+ mapTable[i][2]+")");
					if(i<=max-1){    
					    writer.write(",");	
					}	
				}
			    writer.write("\n];");
  
			    //--mapOptions
				writer.write(
						  "\nvar mapOptions = {"+
						    "\nzoom: 6,"+
						    "\ncenter: new google.maps.LatLng("+mapTable[0][1]+","+ mapTable[0][2]+"),"+
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
				for (int i = 0; i <=max; i++) {
						System.out.println("duplicate on" + (i+1));
						System.out.println("added ");
			    writer.write("\nvar marker"+i+" = new google.maps.Marker({position: coordinates["+i+"],map: map,draggable:true,title:\"Event ID "+mapTable[i][0]+"(stored on tempDB)\"});");
			    writer.write("\ninfowindow"+i+" = new google.maps.InfoWindow({\ncontent:'<div>The location of the event "+mapTable[i][4] +" on tempDB with ID: "+mapTable[i][0]+" is based on location.db with record ID: "+ mapTable[i][3]+"</div>'\n});");
				}
				    
			    //--addListener
				for (int i = 0; i <= max; i++) {
			    writer.write("\ngoogle.maps.event.addListener(marker"+i+", 'click', function() {"+"\ninfowindow"+i+".open(map,marker"+i+");"+"\n});");
				}
			    writer.write("\n}");
    
			    //--DomListener
			    writer.write(		
						"\ngoogle.maps.event.addDomListener(window, 'load', initialize);");
			    //--closeScript
			    writer.write(	
						   "\n</script>");
			    //--javaScript Ends
			    
			    //--html bottom
			    writer.write("\n</head>\n<body>\n<p>Thresh value: "+thresh+"msec</p>\n<div id=\"map-canvas\"></div>\n</body>\n</html>");
			    //writer.newLine(); //this is not actually needed for html files - can make your code more readable though 
			    //-------------
			    writer.close(); //make sure you close the writer object 
				
			   //temp open map.html
			   Runtime.getRuntime().exec("google-chrome /"+projectPath+"/mapPossible.html");


		} catch (Exception ex) {
			System.out.println("to pulaki tsiou");
			ex.printStackTrace();
		}

		
	}
}
