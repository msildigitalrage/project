package com.kithara;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	public String[][] arr2;
	public static JScrollPane scroller;
	public static JFrame f = new JFrame();
	public static JDialog dialog = new JDialog();
	public static JPanel timelineTable = new JPanel();
    public Timeline() {
        
        JLabel label = new JLabel("give the starting Date:");;
        final JTextField textField = new JTextField();
        final JCheckBox callsCheckbox = new JCheckBox("calls",true);
        final JCheckBox smsBox = new JCheckBox("sms");
        final JCheckBox browserBox = new JCheckBox("browser history");
        final JCheckBox backCheckBox = new JCheckBox("Skype");
        JCheckBox ViberBox = new JCheckBox("Viber");
        JCheckBox FacebookBox = new JCheckBox("Facebook");
        JButton dateButton = new JButton("Date start");
        JButton timelinebutton = new JButton("Cancel");
        
        
               		
       dateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                   textField.setText(new DatePicker(f).setPickedDate());
            }
    });
       timelinebutton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			boolean calls = callsCheckbox.isSelected();
			String dateTime = textField.getText();
			boolean sms= smsBox.isSelected();
			boolean browser = browserBox.isSelected();
			try {
				createtimeline(calls,sms,browser,dateTime);
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	});
       
       
 
        // remove redundant default border of check boxes - they would hinder
        // correct spacing and aligning (maybe not needed on some look and feels)
        callsCheckbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        smsBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        browserBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        backCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
 
        GroupLayout layout = new GroupLayout(f.getContentPane());
        f.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
 
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addComponent(label)
            .addGroup(layout.createParallelGroup(LEADING)
                .addComponent(textField)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(callsCheckbox)
                        .addComponent(browserBox)
                        .addComponent(ViberBox))
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(smsBox)
                        .addComponent(FacebookBox)
                        .addComponent(backCheckBox))))
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
                        .addComponent(callsCheckbox)
                        .addComponent(smsBox))
                    .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(browserBox)
                        .addComponent(backCheckBox))
                    .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(ViberBox)
                        .addComponent(FacebookBox)))
                .addComponent(timelinebutton))
        );
 
        f.setTitle("Find");
     //   f.setDefaultCloseOperation(timelineTable.removeAll());
        f.pack();
        f.setVisible(true);
        
    }
    
    
    
    
    public void createtimeline(boolean callscheckbox, boolean sms, boolean browser, String dateTime ) throws SQLException, ClassNotFoundException{
    	
    	
    	
    	//timelineTable.removeAll();
    	Class.forName("org.sqlite.JDBC");
    	Connection connTemp = DriverManager.getConnection("jdbc:sqlite:/home/kasiarakos/workspace/Kithara_jet/Kasiarakos/tempDb.db");
		Statement stat2 = connTemp.createStatement();
		stat2.executeUpdate("drop table if exists timeline;");
        stat2.executeUpdate("create table timeline (timestamp ,application , numberName ,date,message,duration);");
        
        
        String tempTime[];
		int year,month;
		tempTime=dateTime.split("-");
		
		if(tempTime[1]=="12"){
			month=1;
			year = Integer.parseInt(tempTime[2])+1;
		}
		else{
			month=Integer.parseInt(tempTime[1]);
			month++;
			year=Integer.parseInt(tempTime[2]);
		}
		
		
		
		String startTime = tempTime[0]+"/"+tempTime[1]+"/"+year+" 00:00:00 GMT";
		String endTime = tempTime[0]+"/"+month+"/"+year+" 23:59:59 GMT";
		Long start = strDateToUnixTimestamp(startTime);
		Long end= strDateToUnixTimestamp(endTime);
		end = end+999;
        
        dialog.setBounds(200, 200, 1000, 1000);
        dialog.setResizable(true);
        dialog.setTitle("shit");
        dialog.dispose();
        dialog.add(timelineTable);
        timelineTable.setPreferredSize(new Dimension(1000,1000));
        dialog.pack();
       
        
    	if(callscheckbox){

    		try{
    			Class.forName("org.sqlite.JDBC");
    			Connection conn = DriverManager.getConnection("jdbc:sqlite:/home/kasiarakos/workspace/Kithara_jet/Kasiarakos/commonData.db");
    			Statement stat = conn.createStatement();
    						    			
    			
    		   ResultSet rs = stat.executeQuery("select * from calls WHERE timestamp<'"+end+"' AND timestamp>'"+start+"';");
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
    		
    	}
    	
    	if(sms){
    		
    		try{
    			Class.forName("org.sqlite.JDBC");
    			Connection conn = DriverManager.getConnection("jdbc:sqlite:/home/kasiarakos/workspace/Kithara_jet/Kasiarakos/commonData.db");
    			Statement stat = conn.createStatement();
    						    			
    			
    		   ResultSet rs = stat.executeQuery("select * from sms WHERE timestamp<'"+end+"' AND timestamp>'"+start+"';");
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
    	
    	}
    	
    	
    	
if(browser){
    		
    		try{
    			Class.forName("org.sqlite.JDBC");
    			Connection conn = DriverManager.getConnection("jdbc:sqlite:/home/kasiarakos/workspace/Kithara_jet/Kasiarakos/commonData.db");
    			Statement stat = conn.createStatement();
    						    			
    			
    		   ResultSet rs = stat.executeQuery("select * from browserHistory WHERE timestamp<'"+end+"' AND timestamp>'"+start+"';");
   			   while (rs.next()) {
   				   				
   				String sql;
   				// number/name ,date,duration
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
    	/*
    	timelineTable.add(scroller);
		timelineTable.updateUI();
		dialog.setVisible(true);
    	*/
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
    
   
}