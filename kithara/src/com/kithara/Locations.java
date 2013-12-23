package com.kithara;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
public class Locations {
	public static String mountPath;
	public static String projectPath;
	

    
	public void collectingLocations(){
		
		//--------Permissions Start----------------------------------------
		
		try{
			  Process p2= Runtime.getRuntime().exec( "sudo chmod -R 777 "+mountPath);
			  p2.waitFor();
		  }
		  catch(IOException | InterruptedException e1){
			  System.out.println("sudo chmod -R 777 problem");
		  }
			System.out.println(mountPath);
			System.out.println(projectPath);
			
	   //--------DB Handling----------------------------------------
			
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
			
			//......Start Collecting Events with Locations and timeStamp 
			
			//init
			int cv = 0;
			String timestamp,latitude,longitude,app,event;
			String sql;
			
			
			//-------------------Viber----------------------------
			
			ResultSet rsViber = statementC.executeQuery("SELECT timestamp,latitude,longitude,interlocutor,message,type FROM viber WHERE latitude !='0' AND type='1'");
			app = "Viber"; 
			int psifialat = 0; 
			int psifialong = 0;
			while (rsViber.next()) {
				cv++;
				timestamp = rsViber.getString("timestamp");
				latitude=rsViber.getString("latitude");
				longitude=rsViber.getString("longitude");
				/*Starting Procedure to fix latitude,longitude format in Viber
				 * Values of lat and long in Viber are saved without dot.. (example 403038680 and not 40.3038680)
				*/
				psifialat = latitude.length();
				psifialong = longitude.length();
				//System.out.println(psifialat + "    " + psifialong);
				if(latitude.contains("-")){
					if(psifialat ==9){
						latitude = latitude.substring(0, 2) + "." + latitude.substring(1, latitude.length());
						
					}else{//psifialat=10
						latitude = latitude.substring(0, 3) + "." + latitude.substring(2, latitude.length());
					}
				}else{
					if(psifialat == 9){
						latitude = latitude.substring(0, 2) + "." + latitude.substring(2, latitude.length());
					}else{//psifialat=8
						latitude = latitude.substring(0, 1) + "." + latitude.substring(1, latitude.length());
					}
				}
				
				if(longitude.contains("-")){
					if(psifialat ==9){
						longitude = longitude.substring(0, 2) + "." + longitude.substring(1, longitude.length());
						
					}else{//psifialat=10
						longitude = longitude.substring(0, 3) + "." + longitude.substring(2, longitude.length());
					}
				}else{	
					if(psifialat == 9){
						longitude = longitude.substring(0, 2) + "." + longitude.substring(2, longitude.length());
					}else{//psifialat=8
						longitude = longitude.substring(0, 1) + "." + longitude.substring(1, longitude.length());
					}
				}
				
				event = "Communicated with " +rsViber.getString("interlocutor") + ". Message of the event " +rsViber.getString("message")+ ".";

				sql= "INSERT INTO myLocations VALUES ("+cv+",\'"+timestamp+"\',\'"+latitude+"\',\'"+longitude+"\',\'"+app+"\',\'"+event+"\');";
				statementL.executeUpdate(sql);
				conLocations.setAutoCommit(false);
			    conLocations.commit();
			}
			
			//----------------WhatsUp---------------------------
			
			ResultSet rsWhatsUp = statementC.executeQuery("SELECT contactWith,whoSend,timeStamp,latitude,longitude FROM whatsUp WHERE latitude !='0.0' AND whoSend='Sent'");
			app = "Whats Up";
			while (rsWhatsUp.next()) {
				cv++;
				timestamp = rsWhatsUp.getString("timestamp");
				latitude=rsWhatsUp.getString("latitude");
				longitude=rsWhatsUp.getString("longitude");
				event = "Communicated with " +rsWhatsUp.getString("contactWith");

				sql= "INSERT INTO myLocations VALUES ("+cv+",\'"+timestamp+"\',\'"+latitude+"\',\'"+longitude+"\',\'"+app+"\',\'"+event+"\');";
				statementL.executeUpdate(sql);
				conLocations.setAutoCommit(false);
			    conLocations.commit();
			}
			
			//------------------Twitter------------------------
			
			//readtmpTwitter, find ids that send position from device
			 ResultSet rs = statementL.executeQuery("select * from tmpTwitter;");
			 String []tmpIDS = null; int c = 0;
			 String t = null;
			 while(rs.next()){			 
				 t = rs.getString("id");
				 c++;
			 }
			 tmpIDS = new String [c];
			 rs = statementL.executeQuery("select * from tmpTwitter;");
			 c--;
			 while(rs.next()){
				 t = rs.getString("id");
				 tmpIDS [c] = t;
				 c--;
			 }
			
			 for (int v = 0; v < tmpIDS.length; v++) {
				 ResultSet rstwitter = statementC.executeQuery("SELECT author_id,content,created,latitude,longitude FROM twitter WHERE latitude !='null' AND author_id ='"+tmpIDS [v]+"'");	
			app = "Twitter";

			while (rsWhatsUp.next()) {
				cv++;
				timestamp = rstwitter.getString("created");
				latitude=rstwitter.getString("latitude");
				longitude=rstwitter.getString("longitude");
				event = "Message Sent " +rstwitter.getString("content");

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

	}
	public void mapOptions(){
        String line="";
	   	 try{
	   		  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	   		  Process p2= Runtime.getRuntime().exec( "find "+projectPath+" -name locations.db");
	   		  p2.waitFor();
	   		  reader=new BufferedReader(new InputStreamReader(p2.getInputStream()));
	   		  line=reader.readLine();
	   		  if(line == null || line==" "){//if db not exists ..create,collect data
	   			collectingLocations();
	   		  }
	   	  }
	   	  catch(IOException | InterruptedException e1){
	   		  
	   	  }
		//---Map Options
		final JFrame frame = new JFrame();
		JPanel panel = new JPanel();
	    ButtonGroup buttonGroup = new ButtonGroup();
	    final JRadioButton fullRadioButton = new JRadioButton("All Locations");
	    final JRadioButton specificRadioButton = new JRadioButton("Set Time Area");
	    final JButton from = new JButton("From");
	    final JButton to = new JButton("To");
	    JButton cancel = new JButton("Cancel");
	    fullRadioButton.setSelected(true);
		buttonGroup.add(fullRadioButton);
		buttonGroup.add(specificRadioButton);
        final JTextField fromTxt = new JTextField("",20);
        final JTextField toTxt = new JTextField("",20);
        JButton ok = new JButton("Create");
        JButton viewTableLoc = new JButton("List");
        from.setEnabled(false);
        to.setEnabled(false);
        fromTxt.setEnabled(false);
        toTxt.setEnabled(false);
        
        
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
      
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(LEADING)
        			.addComponent(fullRadioButton)
        			.addComponent(specificRadioButton))
                .addGroup(layout.createParallelGroup(LEADING)
                    .addComponent(fromTxt)
                    .addComponent(toTxt))
                .addGroup(layout.createParallelGroup(LEADING)
                    .addComponent(from)
                    .addComponent(to)
                    .addComponent(ok)
                    .addComponent(viewTableLoc)
                    .addComponent(cancel))
            );
            
            layout.linkSize(SwingConstants.HORIZONTAL,from,to,ok,viewTableLoc,cancel);
     
            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                    .addComponent(fullRadioButton)
                    .addComponent(fromTxt)
                    .addComponent(from))
                .addGroup(layout.createParallelGroup(BASELINE)
                	.addComponent(specificRadioButton)	
                	.addComponent(toTxt)	
                    .addComponent(to))
                .addGroup(layout.createParallelGroup(BASELINE)
                	.addComponent(viewTableLoc))
                .addGroup(layout.createParallelGroup(BASELINE)
                	.addComponent(ok))
                .addGroup(layout.createParallelGroup(BASELINE)
                    .addComponent(cancel))
            );
     
            frame.add(panel);
            frame.requestFocus();
            frame.setTitle("Map Options");
            frame.pack();
            frame.setVisible(true);
    		cancel.addActionListener(new ActionListener() {
    			
    			@Override
    			public void actionPerformed(ActionEvent ex) {
    				System.out.println("Cancel Map Procedure");
    				frame.setVisible(false);
    				frame.dispose();
    			}
    		});            
            
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fullRadioButton.isSelected()){
					System.out.println("Full Map");
					int j = 1;
				mapCreator(j,null,null);//create map in HTML format, requires Internet access to view it,while uses Google Maps API
				}
				if(specificRadioButton.isSelected()){
					System.out.println("Specific Time Area");
					String start = fromTxt.getText().trim();
					String end = toTxt.getText().trim();
					//System.out.println(start + " " + end);
					int j = 0;
				mapCreator(j,start,end);//create map in HTML format, requires Internet access to view it,while uses Google Maps API
				}
				frame.setVisible(false);
				frame.dispose();
			}

		});
		
		viewTableLoc.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fullRadioButton.isSelected()){
					//System.out.println("Full Map");
					int j = 1;
					viewTableLocations(j,null,null);
				}
				if(specificRadioButton.isSelected()){
					//System.out.println("Specific Time Area");
					String start = fromTxt.getText().trim();
					String end = toTxt.getText().trim();
					
					//System.out.println(start + " " + end);
					int j = 0;
					viewTableLocations(j,start,end);
				}
				//frame.setVisible(false);
				//frame.dispose();
			}
		});
		specificRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
		        from.setEnabled(true);
		        to.setEnabled(true);
		        fromTxt.setEnabled(true);
		        toTxt.setEnabled(true);
				
			}
		});
		
		fullRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
		        from.setEnabled(false);
		        to.setEnabled(false);
		        fromTxt.setEnabled(false);
		        toTxt.setEnabled(false);
				fromTxt.setText(null);
				toTxt.setText(null);
			}
		});
		
		from.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fromTxt.setText(new DatePicker(frame).setPickedDate());
				
			}
		});
		
		to.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toTxt.setText(new DatePicker(frame).setPickedDate());
				
			}
		});
		
	}
    protected void viewTableLocations(int j, String start, String end) {
    	////----
    	//init - procedure for selecting time area
    	String st = start;
    	String ex = end;
    	Long startD; 
    	Long endD;
    	if(j==0){
    	String tempTime[];
    	String tempTime2[];
    		tempTime=st.split("-");
    		tempTime2= ex.split("-");
    		String startTime,endTime;
    		startTime = tempTime[0]+"/"+tempTime[1]+"/"+tempTime[2]+" 00:00:00 GMT";
    		endTime = tempTime2[0]+"/"+tempTime2[1]+"/"+tempTime2[2]+" 00:00:00 GMT";

    		startD = strDateToUnixTimestamp(startTime);
    		endD = strDateToUnixTimestamp(endTime);
    		endD = endD+999;
    		st = startD.toString();
    		ex = endD.toString();
    	//System.out.println(startD +" "+ endD);
    	}
    	/////-----
    	
		//System.out.println(start + end);
		final JFrame fr = new JFrame("Locations");
		final JPanel panel =  new JPanel();
		final JTable table;
		final JScrollPane scrollPane;
		
		panel.setLayout( new BorderLayout() );
		fr.getContentPane().add(panel);
		
		//values
		// Create columns names
		String columnNames[] = { "id", "time","latitude","longitude","source","event"};
		// Create some data
		String[][] arr = null;
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locations.db");
			Statement stat = conn.createStatement();
			ResultSet rs1;
			if(j==1){
				rs1 = stat.executeQuery("SELECT Count(*) AS total FROM myLocations");
			}else{
				rs1 = stat.executeQuery("SELECT Count(*) AS total FROM myLocations WHERE timestamp BETWEEN '"+st+"' AND '"+ex+"';");	
			}
			int k = rs1.getInt("total");
			arr= new String[k][6];
			int i=0;
			ResultSet rs;
			if(j==1){//all locations
				rs = stat.executeQuery("SELECT * FROM myLocations ORDER BY timestamp;");
			}else{//j=0.. from to
				rs = stat.executeQuery("SELECT * FROM myLocations WHERE timestamp BETWEEN '"+st+"' AND '"+ex+"';");	
			}
			while (rs.next()) {
				String timestamp,latitude,longitude,app,event;
				
				timestamp=rs.getString("timestamp");
				latitude=rs.getString("latitude");
				longitude=rs.getString("longitude");
				app=rs.getString("app");
				event=rs.getString("event");		
				arr[i][0]=Integer.toString(i+1);
				arr[i][1]=timestamp;
				arr[i][2]=latitude;		
				arr[i][3]=longitude;
				arr[i][4]=app;
				arr[i][5]=event;
				i++;
			}

			
			
		}catch(Exception e){
			
		}
		table = new JTable( arr, columnNames );
		scrollPane = new JScrollPane(table);
		panel.add(scrollPane, BorderLayout.CENTER);
		fr.setSize( 500, 600 );
		fr.setVisible(true);
		
	}
	private static long strDateToUnixTimestamp(String dt) {
        DateFormat formatter;
        java.util.Date date = null;
        long unixtime;
        formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            date =  formatter.parse(dt);
        } catch (ParseException ex) {
 
            ex.printStackTrace();
        }
        unixtime = date.getTime();
        return unixtime;
    }
    
public void mapCreator(int j, String start, String end) {		

	//init - procedure for selecting time area
	String st = start;
	String ex = end;
	Long startD; 
	Long endD;
	if(j==0){
	String tempTime[];
	String tempTime2[];
		tempTime=st.split("-");
		tempTime2= ex.split("-");
		String startTime,endTime;
		startTime = tempTime[0]+"/"+tempTime[1]+"/"+tempTime[2]+" 00:00:00 GMT";
		endTime = tempTime2[0]+"/"+tempTime2[1]+"/"+tempTime2[2]+" 00:00:00 GMT";

		startD = strDateToUnixTimestamp(startTime);
		endD = strDateToUnixTimestamp(endTime);
		endD = endD+999;
		st = startD.toString();
		ex = endD.toString();
	//System.out.println(startD +" "+ endD);
	}
	//--General of Method--
	//--getLocations,timeStamp,Events .. etc.After that add to map.
	
	//--connect to db locations.. table:myLocations
	try {
		Connection conLocations = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locations.db");
		Statement myStatement =conLocations.createStatement();
		
		ResultSet myResultSet;
		int k=0;
		if(j==1){//all locations
			myResultSet = myStatement.executeQuery("SELECT * FROM myLocations ORDER BY timestamp;");
		}else{//j=0.. from to
			myResultSet = myStatement.executeQuery("SELECT * FROM myLocations WHERE timestamp BETWEEN '"+st+"' AND '"+ex+"';");	
			//System.out.println(j);	//System.out.println(j);
		}
		while (myResultSet.next()) {//count events
			
			k++;
		}

		//System.out.println("All Positions ->"+k);
	if(k!=0){
		//init
		double[] lat = new double [k];
		double[] lon = new double [k];
		String[] titleEvent = new String [k];
		String[] contentEvent = new String [k];
		ResultSet myResultSet2;
		int p = 0; long timeView = 0;
		if(j==1){//all locations
		myResultSet2 = myStatement.executeQuery("SELECT * FROM myLocations ORDER BY timestamp;");
		}else{//j=0.. from to
			myResultSet2 = myStatement.executeQuery("SELECT * FROM myLocations WHERE timestamp BETWEEN '"+st+"' AND '"+ex+"';");	
			//System.out.println(j);
		}
		while (myResultSet2.next()) {
			//----View timeStamp in Human Format
			timeView = myResultSet2.getLong("timeStamp");
			java.util.Date time=new java.util.Date((long)timeView);
			
			lat[p] = myResultSet2.getDouble("latitude");
			lon[p] = myResultSet2.getDouble("longitude");
			titleEvent[p] = "eventID: "+ (p+1) + "</br>App Used:" + myResultSet2.getString("app");
			contentEvent[p] = "eventID: <b>"+ (p+1) + "</b> | Founded on <b>"+ time 
					+ "</b> | App Used: <b>"+myResultSet2.getString("app") + "</b> | Event Information: <i>" + myResultSet2.getString("event")+"</i>";
			
			p++;
		}
		//number of locations
		int max = k-1;
		
		FileWriter fWriter = null;
		BufferedWriter writer = null;
			fWriter = new FileWriter(projectPath+"map.html");
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
		    writer.write("\nvar marker"+i+" = new google.maps.Marker({position: coordinates["+i+"],map: map,draggable:true,title:\""+titleEvent[i]+"\"});");
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
		   Runtime.getRuntime().exec("google-chrome /"+projectPath+"/map.html");
		   //Runtime.getRuntime().exec("gedit /"+projectPath+"/map.html");
	}else{
		System.out.println("no map, locations are zero" + k);
		JOptionPane.showMessageDialog(null,"No Locations Founded, No map Created");
	}
		} catch (Exception e) {
			System.out.println("Something Went Wrong..");
			e.printStackTrace();
		}	
	}
}
