package com.auda;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.*;



import static javax.swing.GroupLayout.Alignment.*;
 
public class Timeline {
	
	public static String mountPath;
	public static String  projectPath;
	public String[][] arr2;
	public static JScrollPane scroller;
	public static JFrame f = new JFrame();
	public static JDialog dialog = new JDialog();
	public static JPanel timelineTable = new JPanel();
	public static String applications;	
	public static String[] viber,calls,sms,androidBrowser = new String [5];
	public static int numberOfApps;
	
	
    public Timeline() throws ClassNotFoundException, SQLException {
    	
        
        JLabel label = new JLabel("give the starting Date:");
        JLabel search = new JLabel("give a search term");
        final ButtonGroup typeOfTimeline = new ButtonGroup();
        JRadioButton monthButton = new JRadioButton("month s timeline");
        JRadioButton dayButton = new JRadioButton("day s timeline");
        monthButton.setSelected(true);
        typeOfTimeline.add(dayButton);
        typeOfTimeline.add(monthButton);
        final JTextField textField = new JTextField();
        final JTextField searchTerm = new JTextField();
        final JCheckBox callsCheckbox = new JCheckBox("Calls",true);
        final JCheckBox smsBox = new JCheckBox("Sms");
        final JCheckBox browserBox = new JCheckBox("Browser history");
        final JCheckBox skypeBox = new JCheckBox("Skype");
        final JCheckBox ViberBox = new JCheckBox("Viber");
        final JCheckBox whatsUpBox = new JCheckBox("Whuts up");
        final JButton dateButton = new JButton("Date start");
        final JButton timelinebutton = new JButton("Create timeline");
        
       
               		
       dateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                   textField.setText(new DatePicker(f).setPickedDate());
            }
    });
 
       
       timelinebutton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			timelineTable.removeAll();
			String dateTime = textField.getText();
			boolean watsUp = whatsUpBox.isSelected();
			boolean skype = skypeBox.isSelected();
			boolean calls = callsCheckbox.isSelected();
			boolean sms= smsBox.isSelected();
			boolean browser = browserBox.isSelected();
			boolean viber = ViberBox.isSelected();
			String likeStatement = searchTerm.getText();
			String application = selectionOfApps(calls, sms, browser, likeStatement, viber, skype, watsUp);
			String typeOfT=LoadImage.getSelectedButtonText(typeOfTimeline);
			timelineCreator(dateTime,typeOfT,application);
			
		}
	});
        
       
        callsCheckbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        smsBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        browserBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        skypeBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ViberBox.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        whatsUpBox.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        
        GroupLayout layout = new GroupLayout(f.getContentPane());
        f.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
 
        layout.setHorizontalGroup(layout.createSequentialGroup()
        	.addGroup(layout.createParallelGroup(LEADING)
                .addComponent(label)
                .addComponent(dayButton)
                .addComponent(monthButton)
                .addComponent(search))
            .addGroup(layout.createParallelGroup(LEADING)
                .addComponent(textField)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(callsCheckbox)
                        .addComponent(browserBox)
                        .addComponent(ViberBox))
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(smsBox)
                        .addComponent(whatsUpBox)
                        .addComponent(skypeBox)))
                  .addComponent(searchTerm))
            .addGroup(layout.createParallelGroup(LEADING)
                .addComponent(dateButton)
                .addComponent(timelinebutton))
        );
        
        layout.linkSize(SwingConstants.HORIZONTAL, dateButton, timelinebutton);
 
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(BASELINE)
                .addComponent(label)
                .addComponent(textField)
                .addComponent(dateButton))
            .addGroup(layout.createParallelGroup(LEADING)
            	.addGroup(layout.createSequentialGroup()
                     .addGroup(layout.createParallelGroup(BASELINE)
                         .addComponent(dayButton))
                      .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(monthButton)))
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(callsCheckbox)
                        .addComponent(smsBox))
                    .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(browserBox)
                        .addComponent(skypeBox))
                    .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(ViberBox)
                        .addComponent(whatsUpBox)))
                .addGroup(layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(BASELINE)
                			.addComponent(timelinebutton))))
                 .addGroup(layout.createParallelGroup(BASELINE)
                	.addComponent(search)
                	.addComponent(searchTerm)
                		 )
        );
        
        
        
        String line="";
      	 try{
      		  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      		  Process p2= Runtime.getRuntime().exec( "find "+projectPath+" -name tempDb.db");
      		  p2.waitFor();
      		  reader=new BufferedReader(new InputStreamReader(p2.getInputStream()));
      		  
      		  
      		  line=reader.readLine();
      		  if(line == null || line==" "){
      			Gui.detailsFileLbl.setText("please wait.. (may take some time)");
      			
      			SwingUtilities.invokeLater(new Runnable()  {
   	              public void run() {	
   	            	  try {
   						prepareTimeline();
   					} catch (ClassNotFoundException | SQLException e) {
   						// TODO Auto-generated catch block
   						e.printStackTrace();
   					}
   	            	  Gui.detailsFileLbl.setText(null);
   	            f.setTitle("Find");
   	        	f.pack();
   	        	f.setVisible(true);
   	              }
   	            });
      			
      		  }
      		  else{
      			f.setTitle("Find");
   	        	f.pack();
   	        	f.setVisible(true);
      		  }
      	  }
      	  catch(IOException | InterruptedException e1){
      		  
      	  }  
        
    }
    
    
    
    
    public void prepareTimeline() throws SQLException, ClassNotFoundException{	
    	
    	Class.forName("org.sqlite.JDBC");
    	Connection connTemp = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/tempDb.db");
		Statement stat2 = connTemp.createStatement();
		stat2.executeUpdate("drop table if exists timeline;");
        stat2.executeUpdate("create table timeline (timestamp ,application , numberName ,date,message,assciMessage ,duration);");
        
    	

    		try{
    			Class.forName("org.sqlite.JDBC");
    			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/commonData.db");
    			Statement stat = conn.createStatement();
    		    		    			
    			
    		   ResultSet rs = stat.executeQuery("select * from calls;");
   			   while (rs.next()) {
   				   				
   				String sql;
   				long timestampId= rs.getLong("timestamp");
   				String application = "calls";
   				String number = rs.getString("number");
   				String name = rs.getString("name");
   				String nameNumber = number+"/"+name;
   				String date = rs.getString("date");
   				String duration = rs.getString("duration");
   				String message = "null";
   				BigInteger asciMess = asci(message);

   				//System.out.println(application+ " " + message);
				sql= "INSERT INTO timeline VALUES ("+timestampId+",\'"+application+"\',\'"+nameNumber+"\',\'"+date+"\',\'"+message+"\',\'"+asciMess+"\',\'"+duration+"\');";
				
				stat2.executeUpdate(sql);
				connTemp.setAutoCommit(false);
			    connTemp.commit();
   			    
   			    } 
   			rs.close();
   			conn.close();
			stat.close();
    		}catch(Exception e){
    			
    		}
    		
    	
    		try{
    			Class.forName("org.sqlite.JDBC");
    			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/commonData.db");
    			Statement stat = conn.createStatement();
    						    			
    			
    		   ResultSet rs = stat.executeQuery("select * from sms;");
   			   while (rs.next()) {
   				   				
   				String sql;
   				// number/name ,date,duration
   				long timestampId= rs.getLong("timestamp");
   				String application = "sms";
   				String number = rs.getString("address");
   				String name = rs.getString("name");
   				String nameNumber = number+"/"+name;
   				String date = rs.getString("date");
   				String message= rs.getString("body");
   				if (message!=null){
	        		message = message.replaceAll("'", "''");
	        	}
   				String duration = "0";
   				BigInteger asciMess = asci(message);
   				System.out.println(application+ " " + message);
   				
				sql= "INSERT INTO timeline VALUES ("+timestampId+",\'"+application+"\',\'"+nameNumber+"\',\'"+date+"\',\'"+message+"\',\'"+asciMess+"\',\'"+duration+"\');";
				
				stat2.executeUpdate(sql);
				connTemp.setAutoCommit(false);
			    connTemp.commit();
   			    
   			    } 
   			rs.close();
   			conn.close();
			stat.close();
    		}catch(Exception e){
    			
    		}
    	
    	
    	
    	
    	
    	
    		try{
    			Class.forName("org.sqlite.JDBC");
    			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/commonData.db");
    			Statement stat = conn.createStatement();
    		   ResultSet rs = stat.executeQuery("select * from browserHistory;");
   			   while (rs.next()) {
   				   				
   				String sql;
   				long timestampId= rs.getLong("timestamp");
   				String application = "android browser";
   				String number = rs.getString("title");
   				String name ="";
   				String nameNumber = number+"/"+name;
   				String date = rs.getString("date");
   				String message= rs.getString("url");
   				if (message!=null){
	        		message = message.replaceAll("'", "''");
	        	}
   				String duration = "0";
   				BigInteger asciMess = asci(message);
   				System.out.println(application+ " " + message);
   				
				sql= "INSERT INTO timeline VALUES ("+timestampId+",\'"+application+"\',\'"+nameNumber+"\',\'"+date+"\',\'"+message+"\',\'"+asciMess+"\',\'"+duration+"\');";
				
				stat2.executeUpdate(sql);
				connTemp.setAutoCommit(false);
			    connTemp.commit();
   			    
   			    } 
   			rs.close();
   			conn.close();
			stat.close();
    		}catch(Exception e){
    			
    		}  	
    		
    		
    		
    		
    		try{
    			Class.forName("org.sqlite.JDBC");
    			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/commonData.db");
    			Statement stat = conn.createStatement();
    		   ResultSet rs = stat.executeQuery("select * from viber;");
   			   while (rs.next()) {
   				   				
   				String sql;
   				long timestampId= rs.getLong("timestamp");
   				String application = "viber";
   				String number = rs.getString("interlocutor");
   				String name ="";
   				String nameNumber = number+"/"+name;
   				String date;
				java.util.Date time=new java.util.Date(timestampId);
				date=time.toString();
   				String message= rs.getString("message");
   				
   				if (message.isEmpty()){
   					message=" ";
   				}
   				if (message.contains("'")){
	        		message = message.replaceAll("'", "''");
   					
   				}
   				System.out.println(application+ " " + message);
   				String duration = "0";
   				BigInteger asciMess = asci(message);

   				
				sql= "INSERT INTO timeline VALUES ("+timestampId+",\'"+application+"\',\'"+nameNumber+"\',\'"+date+"\',\'"+message+"\',\'"+asciMess+"\',\'"+duration+"\');";
				
				stat2.executeUpdate(sql);
				connTemp.setAutoCommit(false);
			    connTemp.commit();
   			    
   			    } 
   			rs.close();
   			conn.close();
			stat.close();
    		}catch(Exception e){
    			
    		}  
    		
    		
    		
     		try{
    			Class.forName("org.sqlite.JDBC");
    			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/commonData.db");
    			Statement stat = conn.createStatement();
    						    			
    			
    		   ResultSet rs = stat.executeQuery("select * from skype;");
   			   while (rs.next()) {
   				   				
   				String sql;
   				// number/name ,date,duration
   				long timestampId= rs.getLong("timestamp");
   				String application = "skype";
   				String number = rs.getString("interlocutor");
   				String name = rs.getString("author");
   				String nameNumber = number+"/"+name;
   				String date = rs.getString("date");
   				String message= rs.getString("message");
   				if (message!=null){
	        		message = message.replaceAll("'", "''");
	        	}
   				String duration = "0";
   				BigInteger asciMess = asci(message);
   				System.out.println(application+ " " + message);
   				
				sql= "INSERT INTO timeline VALUES ("+timestampId+",\'"+application+"\',\'"+nameNumber+"\',\'"+date+"\',\'"+message+"\',\'"+asciMess+"\',\'"+duration+"\');";
				
				stat2.executeUpdate(sql);
				connTemp.setAutoCommit(false);
			    connTemp.commit();
   			    
   			    } 
   			rs.close();
   			conn.close();
			stat.close();
    		}catch(Exception e){
    			
    		}
	
     		
     		
    		try{
    			Class.forName("org.sqlite.JDBC");
    			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/commonData.db");
    			Statement stat = conn.createStatement();
    						    			
    			
    		   ResultSet rs = stat.executeQuery("select * from whatsUp;");
   			   while (rs.next()) {
   				   				
   				String sql;
   				// number/name ,date,duration
   				long timestampId= rs.getLong("timeStamp");
   				//String timestamp2 = 
   				String application = "whatsUp";
   				String number = rs.getString("contactWith");
   				String name = "null";
   				String nameNumber = number+"/"+name;
   				
   				java.util.Date time=new java.util.Date((long)timestampId);
   				String date = time.toString();
   				String message= rs.getString("data");
   				if (message!=null){
	        		message = message.replaceAll("'", "''");
	        	}
   				String duration = "0";
   				BigInteger asciMess = asci(message);
   				//System.out.println(application+ " " + message);
   				
				sql= "INSERT INTO timeline VALUES ("+timestampId+",\'"+application+"\',\'"+nameNumber+"\',\'"+date+"\',\'"+message+"\',\'"+asciMess+"\',\'"+duration+"\');";
				
				stat2.executeUpdate(sql);
				connTemp.setAutoCommit(false);
			    connTemp.commit();
   			    
   			    } 
   			rs.close();
   			conn.close();
			stat2.close();
    		}catch(Exception e){
    			System.out.println("no whats'up");
    		}
    		
    		
    }
    
    public static void createTable(Object[][] obj, String[] header) {
		
		
		JTable table = new JTable(obj, header);
		
				
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnAdjuster tca = new TableColumnAdjuster(table);
		tca.adjustColumns();

		table.setEnabled(false);
		scroller = new JScrollPane(table);
		
		Dimension screenSize = Gui.mainFrame.getBounds().getSize();
		scroller.setPreferredSize(new Dimension((screenSize.width/2)+100, (screenSize.height/2)));

		
	}

    
public void timelineCreator(String dateTime, String typeOfT, String application) {		
	
	Dimension screenSize = Gui.mainFrame.getBounds().getSize();
	dialog.setPreferredSize(new Dimension((screenSize.width/2)+150, (screenSize.height/2)+50));
    dialog.setResizable(true);
    dialog.setTitle("timeline");
    dialog.dispose();
    dialog.add(timelineTable);
    timelineTable.setPreferredSize(new Dimension(100,100));
    dialog.pack();
   
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		
		try {
			
			Class.forName("org.sqlite.JDBC");
	    	Connection connTime = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/tempDb.db");
			Statement stat2 = connTime.createStatement();
			
			// create the duration from the datepicker
			 String tempTime[];
				int year_t,nextmonth;
				int month;
				tempTime=dateTime.split("-");
				
				if(tempTime[1]=="12"){
					nextmonth=1;
					year_t = Integer.parseInt(tempTime[2])+1;
				}
				else{
					nextmonth=Integer.parseInt(tempTime[1]);
					nextmonth++;
					year_t=Integer.parseInt(tempTime[2]);
				}
				
				String startTime = tempTime[0]+"/"+tempTime[1]+"/"+year_t+" 00:00:00 GMT";
				Long startD;
				Long end;
				month=Integer.parseInt(tempTime[1]);
				month--;
				
				int endmonth=Integer.parseInt(tempTime[1]);
				String endTime=null;
				if(typeOfT=="month s timeline"){
				
			    endTime = tempTime[0]+"/"+nextmonth+"/"+year_t+" 23:59:59 GMT";
				startD = strDateToUnixTimestamp(startTime);
				
				end= strDateToUnixTimestamp(endTime);
				end = end+999;
			
				}
				else{
					
					endTime = tempTime[0]+"/"+tempTime[1]+"/"+year_t+" 23:59:59 GMT";
					startD = strDateToUnixTimestamp(startTime);
				    end= strDateToUnixTimestamp(endTime);
				    endmonth--;
					end = end+999;
				}
				
				

			//insert the values in the Databse of the timeline	
				
			String sql = "SELECT count(*) AS total FROM timeline WHERE timestamp<"+end+" AND timestamp>"+startD;
			sql=sql+application;
			String sql2="SELECT * FROM timeline WHERE timestamp<"+end+" AND timestamp>"+startD;
			sql2= sql2+application;
			//System.out.println(sql2);
			String headerTable[] = applications.split("/");
			String appNameMax[][]=new String[headerTable.length-1][6];
			for (int i = 1; i < headerTable.length; i++) {
				int k=1;
			
			
			String sql3 = "SELECT numberName AS kas FROM timeline WHERE application = '"+headerTable[i]+"' AND timestamp<"+end+" AND timestamp>"+startD+" GROUP BY numberName ORDER BY COUNT(*) DESC LIMIT 5";
			ResultSet rss = stat2.executeQuery(sql3+" ;");
			appNameMax[i-1][0]=headerTable[i];
				while(rss.next()){
					
					appNameMax[i-1][k]= rss.getString("kas");
					
					k++;
				}
			}
			
			
			ResultSet rs1 = stat2.executeQuery(sql+" ;");
			int k = rs1.getInt("total");
			int i=0;
			
			arr2= new String[k][5];
			
			ResultSet rs = stat2.executeQuery(sql2+" ;");
			
		    fWriter = new FileWriter(projectPath+"/timeline.html");
		    writer = new BufferedWriter(fWriter);
 
		    writer.write("<!DOCTYPE html> \n <head> \n <title>timeline</title> \n <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\"> \n <meta charset=\"utf-8\">  \n \n"); 		  
		    writer.write("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization',\n");
		    writer.write("      'version':'1','packages':['timeline']}]}\"></script>\n");
		    writer.write("<script type=\"text/javascript\">\n");
		    writer.write("\n");
		    writer.write("google.setOnLoadCallback(drawChart);\n");
		    writer.write("function drawChart() {\n");
		    writer.write("  var container = document.getElementById('kasiarakos');\n");
		    writer.write("  var chart = new google.visualization.Timeline(container);\n");		    
		    writer.write("  var dataTable = new google.visualization.DataTable();\n");
		    writer.write("\n");
		    writer.write("  dataTable.addColumn({ type: 'string', id: 'Term' });\n");
		    writer.write("  dataTable.addColumn({ type: 'string', id: 'Name' });\n");
		    writer.write("  dataTable.addColumn({ type: 'date', id: 'Start' });\n");
		    writer.write("  dataTable.addColumn({ type: 'date', id: 'End' });\n\n");
		    writer.write("  dataTable.addRows([\n");
		    writer.write("    [ 'start', '', new Date("+year_t+", "+month+", "+tempTime[0]+", 0, 0, 0 ), new Date("+year_t+", "+endmonth+", "+tempTime[0]+", 23, 59, 59) ],\n");


		    Long date,duration,dateEnd;
		    String start_t = null,end_t = null,programm;
		    int dateStart[],dateEnds[];
		    while(rs.next()){	
		    	
		    	programm = rs.getString("application");
		    	date = rs.getLong("timestamp");
		    	duration=rs.getLong("duration");
		    	
		    	arr2[i][0]=rs.getString("date");
		    	arr2[i][1]=programm;
		    	arr2[i][2]=rs.getString("message");
		    	arr2[i][3]=rs.getString("numberName");
		    	arr2[i][4]= duration.toString();
		    	
		    	
		    	
		     if(typeOfT=="month s timeline"){
		    	
		        dateEnd=date;
		     }
		     else{
		    	 duration = duration *1000;
			     dateEnd=date+duration;
		     }
	
		    	start_t = simpleDateConverter(date.toString());
		    	end_t = simpleDateConverter(dateEnd.toString());
		    	
		    	dateStart= returnDate(start_t);
		    	dateEnds= returnDate(end_t);
		    	
		    		    	
			    if(i<k-1){
				   writer.write("    [ '"+programm+"', '', new Date("+dateStart[0]+", "+dateStart[1]+", "+dateStart[2]+", "+dateStart[3]+", "+dateStart[4]+", "+dateStart[5]+"), new Date("+dateEnds[0]+", "+dateEnds[1]+", "+dateEnds[2]+", "+dateEnds[3]+", "+dateEnds[4]+", "+dateEnds[5]+") ],\n");
			    }else{
					   writer.write("    [ '"+programm+"', '', new Date("+dateStart[0]+", "+dateStart[1]+", "+dateStart[2]+", "+dateStart[3]+", "+dateStart[4]+", "+dateStart[5]+"), new Date("+dateEnds[0]+", "+dateEnds[1]+", "+dateEnds[2]+", "+dateEnds[3]+", "+dateEnds[4]+", "+dateEnds[5]+") ]\n");
	
			    }
				   i++;
				   
			    }
		    
		    
		    writer.write("]);\n");
		    writer.write("\n");
		    writer.write("  chart.draw(dataTable);\n");
		    writer.write("}\n");
		    writer.write("</script>\n");
		    writer.write("\n");	    
		    
		    
		    writer.write("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization',");
	    	writer.write("       'version':'1','packages':['timeline']}]}\"></script>");
	    	writer.write("<script type=\"text/javascript\">");
	    	writer.write("\ngoogle.setOnLoadCallback(drawChart);");
	    	writer.write("function drawChart() {");
	    	writer.write("  var container = document.getElementById('example4.2');");
	    	writer.write("  var chart = new google.visualization.Timeline(container);");
	    	writer.write("  var dataTable = new google.visualization.DataTable();\n");
	    	writer.write("  dataTable.addColumn({ type: 'string', id: 'Role' });");
	    	writer.write("  dataTable.addColumn({ type: 'string', id: 'Name' });");
	    	writer.write("  dataTable.addColumn({ type: 'date', id: 'Start' });");
	    	writer.write("  dataTable.addColumn({ type: 'date', id: 'End' });");
	    	writer.write("  dataTable.addRows([");
		    writer.write("    [ 'start', '', new Date("+year_t+", "+month+", "+tempTime[0]+", 0, 0, 0 ), new Date("+year_t+", "+endmonth+", "+tempTime[0]+", 23, 59, 59) ],\n");

	        rs = stat2.executeQuery(sql2+" ;");
	        while(rs.next()){
	        	programm = rs.getString("application");
		    	date = rs.getLong("timestamp");
		    	duration=rs.getLong("duration");
		    	
		    	 if(typeOfT=="month s timeline"){
				    	
				        dateEnd=date;
				     }
				 else{
				    	 duration = duration *1000;
					     dateEnd=date+duration;
				     }
			
				 start_t = simpleDateConverter(date.toString());
				 end_t = simpleDateConverter(dateEnd.toString());
				    	
				 dateStart= returnDate(start_t);
				 dateEnds= returnDate(end_t);
		    	
	        	writer.write("    [ 'Correlation', '"+programm+"', new Date("+dateStart[0]+", "+dateStart[1]+", "+dateStart[2]+", "+dateStart[3]+", "+dateStart[4]+", "+dateStart[5]+"), new Date("+dateEnds[0]+", "+dateEnds[1]+", "+dateEnds[2]+", "+dateEnds[3]+", "+dateEnds[4]+", "+dateEnds[5]+") ],\n");

	        	
	        }
	    	writer.write("]);\n");
	    	writer.write("  var options = {");
	    	writer.write("     timeline: { groupByRowLabel: true }");
	    	writer.write("  };");
	    	writer.write("  chart.draw(dataTable, options);");
	    	writer.write("}\n</script>");		    
		    
		    writer.write("</head> \n <body>");
		    
		    String heighOfdiv ="140";
		    if(numberOfApps==2){
		       heighOfdiv ="180";
		    }
		    if(numberOfApps==3){
			       heighOfdiv ="220";
			    }
		    if(numberOfApps==4){
			       heighOfdiv ="260";
			    }
		    if(numberOfApps==5){
			       heighOfdiv ="300";
			    }
		    if(numberOfApps>5){
			       heighOfdiv ="340";
			    }
		    
		    writer.write("<div id=\"kasiarakos\" style=\"width: 2000px; height: "+heighOfdiv+"px;\"></div>");
		    writer.write("<p>Date &nbsp &nbsp From "+startTime+"    &nbsp &nbsp &nbsp Until "+endTime+"</p>");
		    writer.write("</br></br>");
		    writer.write("<div id=\"example4.2\" style=\"width: 2000px; height: 180px;\"></div>");
		    writer.write("</br></br>");
		    writer.write("<p><table border=\"1\">");
		    	
		    
		   
		    for (int j = 1; j < headerTable.length; j++) {
		    	writer.write("<tr>");
				for (int j2 = 0; j2 < 5; j2++) {
					writer.write("<td>");
					writer.write(appNameMax[j-1][j2]+"   ");
					
					writer.write("</td>");
			
				}
				writer.write("</tr>");
			}
		    writer.write("</table></p>");
		    
		    writer.write("</body>   \n </html> ");
		    writer.close(); //make sure you close the writer object 
		   Runtime.getRuntime().exec("google-chrome "+projectPath+"timeline.html");
		 

		    String[] header = {"date", "app","message", "number","duration"};
		    createTable(arr2,header);
	    	timelineTable.add(scroller);
			timelineTable.updateUI();
			dialog.setVisible(true);
		   
		} catch ( ClassNotFoundException | SQLException | IOException e) {
			System.out.println("Something Went Wrong..");
			e.printStackTrace();
		}	
	}

    
	
	private static String simpleDateConverter(String date){
		String k=date;
    	Long timeSent = Long.parseLong(k);
    	java.util.Date myDate=new java.util.Date((long)timeSent);
    	
    	String timeDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(myDate);
    	
    	return timeDate;
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
    
    public String selectionOfApps(boolean calls, boolean sms, boolean bhist, String likeStatement, boolean viber, boolean skype, boolean watsUp){
    	
    	applications = "/";
    	numberOfApps=0;
    	
		   String appl=" AND ("; 
		   if(calls){
			   if(appl==" AND (")
			   appl=appl+" application='calls' ";
			   else{
			   appl=appl+" OR application='calls'" ;  
			   }
			   applications = applications+"calls/";
			   numberOfApps++;
		   }
		   
		   if(sms){
			   if(appl==" AND (")
			   appl=appl+" application='sms' ";
			   else{
			   appl=appl+" OR application='sms'" ;  
			   }
			   applications = applications+"sms/";
			   numberOfApps++;
		   }
		   
		   if(viber){
			   if(appl==" AND (")
			   appl=appl+" application='viber' ";
			   else{
			   appl=appl+" OR application='viber'" ;  
			   }
			   applications = applications+"viber/";
			   numberOfApps++;
		   }
		   if(bhist){
			   if(appl==" AND (")
			   appl=appl+" application='android browser' ";
			   else{
			   appl=appl+" OR application='android browser'" ;  
			   }
			   applications = applications+"androidBrowser/";
			   numberOfApps++;
		   }
		   
		   if(skype){
			   if(appl==" AND (")
			   appl=appl+" application='skype' ";
			   else{
			   appl=appl+" OR application='skype'" ;  
			   }
			   applications = applications+"skype/";
			   numberOfApps++;
		   }
		   
		   if(watsUp){
			   if(appl==" AND (")
			   appl=appl+" application='whatsUp' ";
			   else{
			   appl=appl+" OR application='whatsUp'" ;  
			   }
			   applications = applications+"whatsUp/";
			   numberOfApps++;
		   }
		   
		   appl=appl+" ) ";
		   
		   if(!likeStatement.isEmpty()){
			   
			   BigInteger statment= asci(likeStatement)	;		 
			   appl=appl+"AND ( assciMessage LIKE '%"+statment+"%' OR numberName LIKE '%"+likeStatement+"%')";

		   }
		   
		   
		   appl=appl+" ORDER BY timestamp ";
		    
    	return appl;
    	
    }
    
    public int[] returnDate(String dt){
    	
    	 
    	int date[] = new int[6];
    	String parts[]=dt.split(" ");
        String dateY[]=parts[0].split("-");
    	String dateH[]=parts[1].split(":");
     	
    	date[0]=Integer.parseInt(dateY[2]);
    	date[1]=Integer.parseInt(dateY[1]);
    	date[2]=Integer.parseInt(dateY[0]);
    	
    	date[1]--;
    	
    	date[3]=Integer.parseInt(dateH[0]);
        date[4]=Integer.parseInt(dateH[1]);
    	date[5]=Integer.parseInt(dateH[2]);
    	
    	
		return date;
    	
    }
    
    
    public BigInteger asci (String str){
		StringBuilder sb = new StringBuilder();
		for (char c : str.toCharArray())
		    sb.append((int)c);

		BigInteger mInt = new BigInteger(sb.toString());
		
		return mInt;
	}
    
    
    
   
}