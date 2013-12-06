package com.kithara;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.*;

import org.omg.CORBA.portable.ApplicationException;


import static javax.swing.GroupLayout.Alignment.*;
 
public class Timeline {
	
	public static String mountPath;
	public static String  projectPath;
	public String[][] arr2;
	public static JScrollPane scroller;
	public static JFrame f = new JFrame();
	public static JDialog dialog = new JDialog();
	public static JPanel timelineTable = new JPanel();
    public Timeline() {
        
        JLabel label = new JLabel("give the starting Date:");
        final ButtonGroup typeOfTimeline = new ButtonGroup();
        JRadioButton monthButton = new JRadioButton("month s timeline");
        JRadioButton dayButton = new JRadioButton("day s timeline");
        monthButton.setSelected(true);
        typeOfTimeline.add(dayButton);
        typeOfTimeline.add(monthButton);
        final JTextField textField = new JTextField();
        final JCheckBox callsCheckbox = new JCheckBox("calls",true);
        final JCheckBox smsBox = new JCheckBox("sms");
        final JCheckBox browserBox = new JCheckBox("browser history");
        final JCheckBox skypeBox = new JCheckBox("Skype");
        JCheckBox ViberBox = new JCheckBox("Viber");
        JCheckBox facebookBox = new JCheckBox("Facebook");
        JButton dateButton = new JButton("Date start");
        JButton timelinebutton = new JButton("Create timeline");
        JButton preparebutton = new JButton("Prep timeline");
        
               		
       dateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                   textField.setText(new DatePicker(f).setPickedDate());
            }
    });
       
       preparebutton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				prepareTimeline();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	});
       
       timelinebutton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String dateTime = textField.getText();
			boolean calls = callsCheckbox.isSelected();
			boolean sms= smsBox.isSelected();
			boolean browser = browserBox.isSelected();
			String application = selectionOfApps(calls, sms, browser);
			String typeOfT=LoadImage.getSelectedButtonText(typeOfTimeline);
			timelineCreator(dateTime,typeOfT,application);
			
		}
	});
        
        callsCheckbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        smsBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        browserBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        skypeBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ViberBox.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        facebookBox.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        
        GroupLayout layout = new GroupLayout(f.getContentPane());
        f.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
 
        layout.setHorizontalGroup(layout.createSequentialGroup()
        	.addGroup(layout.createParallelGroup(LEADING)
                .addComponent(label)
                .addComponent(dayButton)
                .addComponent(monthButton))
            .addGroup(layout.createParallelGroup(LEADING)
                .addComponent(textField)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(callsCheckbox)
                        .addComponent(browserBox)
                        .addComponent(ViberBox))
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(smsBox)
                        .addComponent(facebookBox)
                        .addComponent(skypeBox))))
            .addGroup(layout.createParallelGroup(LEADING)
                .addComponent(dateButton)
                .addComponent(preparebutton)
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
                        .addComponent(facebookBox)))
                .addGroup(layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(BASELINE)
                			.addComponent(preparebutton))
                	.addGroup(layout.createParallelGroup(BASELINE)
                			.addComponent(timelinebutton))))
        );
 
        f.setTitle("Find");
      //f.setDefaultCloseOperation(timelineTable.removeAll());
        f.pack();
        f.setVisible(true);
        
    }
    
    
    
    
    public void prepareTimeline() throws SQLException, ClassNotFoundException{	
    	//timelineTable.removeAll();

    	Class.forName("org.sqlite.JDBC");
    	Connection connTemp = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/tempDb.db");
		Statement stat2 = connTemp.createStatement();
		stat2.executeUpdate("drop table if exists timeline;");
        stat2.executeUpdate("create table timeline (timestamp ,application , numberName ,date,message,duration);");
        
    	

    		try{
    			Class.forName("org.sqlite.JDBC");
    			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/commonData.db");
    			Statement stat = conn.createStatement();
    		    System.out.println(projectPath+"kasiarakos");		    			
    			
    		   ResultSet rs = stat.executeQuery("select * from calls;");
   			   while (rs.next()) {
   				   				
   				String sql;
   				// number/name ,date,duration
   				long timestampId= rs.getLong("timestamp");
   				String application = "calls";
   				String number = rs.getString("number");
   				String name = rs.getString("name");
   				String nameNumber = number+"/"+name;
   				String date = rs.getString("date");
   				String duration = rs.getString("duration");
   				String message = "null";
   				
				sql= "INSERT INTO timeline VALUES ("+timestampId+",\'"+application+"\',\'"+nameNumber+"\',\'"+date+"\',\'"+message+"\',\'"+duration+"\');";
				
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
   				String duration = "0";
   				
				sql= "INSERT INTO timeline VALUES ("+timestampId+",\'"+application+"\',\'"+nameNumber+"\',\'"+date+"\',\'"+message+"\',\'"+duration+"\');";
				
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
   				String duration = "0";
   				
				sql= "INSERT INTO timeline VALUES ("+timestampId+",\'"+application+"\',\'"+nameNumber+"\',\'"+date+"\',\'"+message+"\',\'"+duration+"\');";
				
				stat2.executeUpdate(sql);
				connTemp.setAutoCommit(false);
			    connTemp.commit();
   			    
   			    } 
   			rs.close();
   			conn.close();
			stat.close();
    		}catch(Exception e){
    			
    		}  	
    }
    
    public static void createTable(Object[][] obj, String[] header) {
		
		
		JTable table = new JTable(obj, header);
		
				
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnAdjuster tca = new TableColumnAdjuster(table);
		tca.adjustColumns();

		table.setEnabled(false);
		scroller = new JScrollPane(table);
		
		scroller.setPreferredSize(new Dimension(1000,1000));
		
	}

    
public void timelineCreator(String dateTime, String typeOfT, String application) {		
		
	
	dialog.setBounds(200, 200, 1000, 1000);
    dialog.setResizable(true);
    dialog.setTitle("shit");
    dialog.dispose();
    dialog.add(timelineTable);
    timelineTable.setPreferredSize(new Dimension(1000,1000));
    dialog.pack();
   
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		
		
		
		
		try {
			
			Class.forName("org.sqlite.JDBC");
	    	Connection connTime = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"/tempDb.db");
			Statement stat2 = connTime.createStatement();
			
			
			 String tempTime[];
				int year_t,nextmonth;
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
				
				if(typeOfT=="month s timeline"){
				String endTime = tempTime[0]+"/"+nextmonth+"/"+year_t+" 23:59:59 GMT";
				startD = strDateToUnixTimestamp(startTime);
				
				end= strDateToUnixTimestamp(endTime);
				end = end+999;
				System.out.println("Mariaaaaa");
				}
				else{
					System.out.println("elsaaaaaassasa");
					String endTime = tempTime[0]+"/"+tempTime[1]+"/"+year_t+" 23:59:59 GMT";
					startD = strDateToUnixTimestamp(startTime);
				    end= strDateToUnixTimestamp(endTime);
					end = end+999;
				}
				
				

			String sql = "SELECT count(*) AS total FROM timeline WHERE timestamp<"+end+" AND timestamp>"+startD;
			sql=sql+application;
			String sql2="SELECT * FROM timeline WHERE timestamp<"+end+" AND timestamp>"+startD;
			sql2= sql2+application;
			
			
			ResultSet rs1 = stat2.executeQuery(sql+" ;");
			int k = rs1.getInt("total");
			int i=0;
			
			
			ResultSet rs = stat2.executeQuery(sql2+" ;");
			
		    fWriter = new FileWriter(projectPath+"/timeline.html");
		    writer = new BufferedWriter(fWriter);
		   		  
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
		    

		    while(rs.next()){
		    	
		    	String programm = rs.getString("application");
		    	String message = rs.getString("message");
		    	String date = rs.getString("timestamp");
				//java.util.Date timeSent=new java.util.Date((long)date_sent);
		    	
		    	String start_t = simpleDateConverter(date);
		    	String parts[]=start_t.split(" ");
		    	String dateY[]=parts[0].split("-");
		    	String dateH[]=parts[1].split(":");
		   
		    	
		    	int year=Integer.parseInt(dateY[2]);
		    	int month=Integer.parseInt(dateY[1]);
		    	int day=Integer.parseInt(dateY[0]);
		    	
		    	month--;
		    	
		    	int hour=Integer.parseInt(dateH[0]);
		    	int minutes=Integer.parseInt(dateH[1]);
		    	int seconds=Integer.parseInt(dateH[2]);
		    	
		    	//String end =simpleDateConverter(date);
		    	
			    	
			    if(i<k-1){
				   writer.write("    [ '"+programm+"', '', new Date("+year+", "+month+", "+day+", "+hour+", "+minutes+", "+seconds+"), new Date("+year+", "+month+", "+day+", "+hour+", "+minutes+", "+seconds+") ],\n");
			    }else{
				   writer.write("    [ '"+programm+"', '', new Date("+year+", "+month+", "+day+", "+hour+", "+minutes+", "+seconds+"), new Date("+year+", "+month+", "+day+", "+hour+", "+minutes+", "+seconds+") ]\n");
	
			    }
				   i++;
				   
			    }
		    
		    
		    writer.write("]);\n");
		   // writer.write("    [ '2', 'John Adams',        new Date(1797, 2, 3),  new Date(1801, 2, 3) ],\n");
		    //writer.write("    [ '3', 'Thomas Jefferson',  new Date(1801, 2, 3),  new Date(1809, 2, 3) ]]);\n");
		    writer.write("\n");
		    writer.write("  chart.draw(dataTable);\n");
		    writer.write("}\n");
		    writer.write("</script>\n");
		    writer.write("\n");	    
		    writer.write("<div id=\"kasiarakos\" style=\"width: 2000px; height: 400px;\"></div>");
		    
		    writer.close(); //make sure you close the writer object 
		   Runtime.getRuntime().exec("google-chrome "+projectPath+"timeline.html");
		 
		   
		   
		   /*
	    	timelineTable.add(scroller);
			timelineTable.updateUI();
			dialog.setVisible(true);
	    	*/
		   
		   
		   
		   
		   // Runtime.getRuntime().exec("gedit /kasiarakos/Workspace/sample/timeline.html");
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
    
    public String selectionOfApps(Boolean calls, Boolean sms, Boolean bhist){
    	
		   String appl=" AND ("; 
		   if(calls){
			   if(appl==" AND (")
			   appl=appl+" application='calls' ";
			   else{
			   appl=appl+" OR application='calls'" ;  
			   }
		   }
		   
		   if(sms){
			   if(appl==" AND (")
			   appl=appl+" application='sms' ";
			   else{
			   appl=appl+" OR application='sms'" ;  
			   }
		   }
		   if(bhist){
			   if(appl==" AND (")
			   appl=appl+" application='android browser' ";
			   else{
			   appl=appl+" OR application='android browser'" ;  
			   }
		   }
		   appl=appl+" )";
		    
    	return appl;
    	
    }
   
}