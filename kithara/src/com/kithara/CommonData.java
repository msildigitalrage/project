package com.kithara;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.PopupMenu;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class CommonData {

	public static String mountPath;
	public static String projectPath;
	public String[] arr;
	public String[][] arr2;
	
	public void getContacts(){
		
		
		
		List<String> myList = new ArrayList<String>();
	         
		
		
		try{
			  Process p2= Runtime.getRuntime().exec( "sudo chmod -R 777 "+mountPath);
			  p2.waitFor();
			  
		  }
		  catch(IOException | InterruptedException e1){
			  
		  }
		
				
		
		
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+mountPath+"/data/com.android.providers.contacts/databases/contacts2.db");
			Statement stat = conn.createStatement();
			
			
			Connection conn2 = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement stat2=conn2.createStatement();
			stat2.executeUpdate("drop table if exists contacts;");
	        stat2.executeUpdate("create table contacts (id, number,name);");
	        PreparedStatement prep2 = conn2.prepareStatement("insert into contacts values (?, ?, ?);");
			int flag=-1;
	        int flag2=-2;
			ResultSet rs = stat.executeQuery("select * from phone_lookup;");
			while (rs.next()) {
				
				
				
				
				
				 flag=rs.getInt("raw_contact_id");
		        	if(flag2!=flag){
		        		prep2.setInt(1, rs.getInt("raw_contact_id"));
		            	prep2.setString(2, rs.getString("normalized_number"));
		            	prep2.setString(3, "");
		            	prep2.addBatch();
		        	//	System.out.println("name = " + rs.getInt("raw_contact_id"));
		              //  System.out.println("job = " + rs.getString("normalized_number"));
		                flag2=rs.getInt("raw_contact_id");
		        	}
		        	conn2.setAutoCommit(false);
		            prep2.executeBatch();
		            conn2.setAutoCommit(true);
			 
			}
			rs.close();
	        conn.close();
			
	        conn = DriverManager.getConnection("jdbc:sqlite:"+mountPath+"/data/com.android.providers.contacts/databases/contacts2.db");
			stat = conn.createStatement();
			Statement stat3=conn2.createStatement();
			rs = stat.executeQuery("select * from raw_contacts;");
			String temp="";
			int temp_int=-1;
			
		      String sql ;
		      
			while (rs.next()) {
				
				temp=rs.getString("display_name");
			//	System.out.println(temp+"sssss\n");
				temp_int=rs.getInt("_id");
			//	System.out.println(temp_int);
				sql= "UPDATE contacts set name = \""+temp+"\" where id="+temp_int+";";
				stat3.executeUpdate(sql);
				conn2.setAutoCommit(false);
			    conn2.commit();
			   
				
			}
			 rs.close();
			    conn.close();
			    conn2.close();
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
		
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement stat = conn.createStatement();
			
			ResultSet rs = stat.executeQuery("select * from contacts;");
			while (rs.next()) {
				String name,number;
				int id;
				
				id=rs.getInt("id");
				name=rs.getString("name");
				number=rs.getString("number");
				myList.add(id+" "+name+" "+number);
					
			}

			arr = myList.toArray(new String[myList.size()]);
			
		}catch(Exception e){
			
		}
		
		
		
		arr = myList.toArray(new String[myList.size()]);
		JList<String> listbox = new JList<String>( arr );
		Gui.contacts.add(listbox);
		
		
		//System.out.println("contacts");
		Gui.contacts.setVisible(true);
		Gui.contacts.updateUI();
		
	}
	
	
	
	public void getSms(){
		
		List<String> myList = new ArrayList<String>();
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+mountPath+"/data/com.android.providers.telephony/databases/mmssms.db");
			Statement stat = conn.createStatement();
			
			
			Connection conn2 = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement stat2=conn2.createStatement();
			stat2.executeUpdate("drop table if exists sms;");
	        stat2.executeUpdate("create table sms (id,address,person,name,date,date_sent,body,service_center);");
	       // PreparedStatement prep3 = conn2.prepareStatement("insert into sms values (?, ?, ?, ?, ?, ?, ?, ?, ?);");
	        
			ResultSet rs = stat.executeQuery("select * from sms;");
			
			while(rs.next()){
				
				String address,person,name,date,date_sent,body,serviceCenter,sql;
				int id;
				
				id=rs.getInt("_id");
				address = rs.getString("address");
				person = rs.getString("person");
				name = "null";
				date = rs.getString("date");
				date_sent = rs.getString("date_sent");
				body= rs.getString("body");
				serviceCenter = rs.getString("service_center");
				
							
				sql= "INSERT INTO sms VALUES ("+id+",\'"+address+"\',\'"+person+"\',\'"+name+"\',\'"+date+"\',\'"+date_sent+"\',\'"+body+"\',\'"+serviceCenter+"\');";
				stat2.executeUpdate(sql);
				//System.out.print(sql);
				conn2.setAutoCommit(false);
			    conn2.commit();
			}
			rs.close();
	        conn.close();
	        conn2.close();
	        
			
			
		}
		catch(ClassNotFoundException | SQLException e){
			
		}
		
		
		
		
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement stat = conn.createStatement();
			
			
			
		   ResultSet rs1 = stat.executeQuery("SELECT Count(*) AS total FROM sms");
		   int k = rs1.getInt("total");
		   System.out.println(k+"    gamw thn poutana mou");
			
		   arr2= new String[k][8];int i=0;
			
			ResultSet rs = stat.executeQuery("select * from sms;");
			while (rs.next()) {
				String address,person,name,date,date_sent,body,serviceCenter,sql;
				int id;
				
				id=rs.getInt("id");
				address = rs.getString("address");
				person = rs.getString("person");
				name = rs.getString("name");
				date = rs.getString("date");
				date_sent = rs.getString("date_sent");
				body= rs.getString("body");
				serviceCenter = rs.getString("service_center");
				System.out.println(address+" jasuaeraskas");
				//myList.add(id+" "+address+" "+person+" "+name+" "+date+" "+date_sent+" "+body+" "+serviceCenter);
				
				
				arr2[i][0]=Integer.toString(id);
				arr2[i][1]= address;
				arr2[i][2]= person;
				arr2[i][3]= name;
				arr2[i][4]= date;
				arr2[i][5]= date_sent;
				arr2[i][6]= body;
				arr2[i][7]= serviceCenter;
				
				
				i++;
				
			}
			
			String[] header = {"Animal", "Family","Animal", "Family","Animal", "Family","Animal", "Family"};

			test(arr2,header);
			//arr2 = myList.toArray(new String[myList.size()]);
			//JList<String> listbox = new JList<String>( arr2 );
			//Gui.sms.add(listbox);
			
			
			//System.out.println("contacts");
			
		}catch(Exception e){
			
		}
		
		
	
		
		
		
	}



	public void test(Object[][] obj, String[] header) {
		
		// constructor of JTable with a fix number of objects
		JTable table = new JTable(obj, header);
		
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnAdjuster tca = new TableColumnAdjuster(table);
		tca.adjustColumns();

		table.setEnabled(false);
		JScrollPane scroller = new JScrollPane(table);
		Dimension screenSize = Gui.mainFrame.getBounds().getSize();
		scroller.setPreferredSize(new Dimension(screenSize.width, (screenSize.height/4)-20));
		Gui.sms.add(scroller);    // adding panel to frame
		// and display it
		Gui.sms.setVisible(true);
		Gui.sms.updateUI();
		
	}























	private void creayeDatble() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
		
