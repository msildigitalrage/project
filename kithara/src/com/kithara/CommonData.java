package com.kithara;


import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class CommonData {

	public static String mountPath;
	public static String projectPath;
	public String[][] arr;
	public String[][] arr2;
	public static JScrollPane scroller;
	
	
	
	/*function to copy the contacts from the image to the commonData db
	and display this data to Gui*/
	public void getContacts(){
		
	
	//change mode of the mount file in order the user can access it	
		try{
			  Process p2= Runtime.getRuntime().exec( "sudo chmod -R 777 "+mountPath);
			  p2.waitFor();
			  
		  }
		  catch(IOException | InterruptedException e1){
			  
		  }
		
				
		
		//connect to the db in the image file and copy the data to our database 
		
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
				temp_int=rs.getInt("_id");
				sql= "UPDATE contacts set name = \""+temp+"\" where id="+temp_int+";";
				stat3.executeUpdate(sql);
				conn2.setAutoCommit(false);
			    conn2.commit();
			   
				
			}
			 rs.close();
			    conn.close();
			    conn2.close();
			
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		} 
		
		
		
		//connect to our database and display the data to gui
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement stat = conn.createStatement();
			
			
			
			
			
			
			ResultSet rs1 = stat.executeQuery("SELECT Count(*) AS total FROM contacts");
			int k = rs1.getInt("total");
			
			arr= new String[k][3];
			
			int i=0;
			ResultSet rs = stat.executeQuery("select * from contacts;");
			while (rs.next()) {
				String name,number;
				int id;
				
				id=rs.getInt("id");
				name=rs.getString("name");
				number=rs.getString("number");
				
				arr[i][0]=Integer.toString(id);
				arr[i][1]=name;
				arr[i][2]=number;
					
				i++;
			}

			String[] header = {"id", "name", "number"};
			createTable(arr, header);
			Gui.contacts.add(scroller);    
			Gui.contacts.setVisible(true);
			Gui.contacts.updateUI();
			
			
		}catch(Exception e){
			
		}
	}
	
	
	
	
	
	
	/*function to copy the sms from the image to the commonData db
	and display this data to Gui*/
	
	public void getSms(){
		

		//connect to the db in the image file and copy the data to our database 
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+mountPath+"/data/com.android.providers.telephony/databases/mmssms.db");
			Statement stat = conn.createStatement();
			
			
			Connection conn2 = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement stat2=conn2.createStatement();
			stat2.executeUpdate("drop table if exists sms;");
	        stat2.executeUpdate("create table sms (id,address,person,name,timestamp,date,date_sent,body,service_center);");
	      	        
			ResultSet rs = stat.executeQuery("select * from sms;");
			
			
			
			while(rs.next()){
				
				String address,person,name,body,serviceCenter,sql;
				int id;
				long date_sent,date;
				
				id=rs.getInt("_id");
				address = rs.getString("address");
				person = rs.getString("person");
				name = "null";
				
				date = rs.getLong("date");
				/*long dv = Long.valueOf(date);
				Date df = new java.util.Date(dv);
				String time = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);*/
				java.util.Date time=new java.util.Date((long)date);
				date_sent = rs.getLong("date_sent");
				java.util.Date timeSent=new java.util.Date((long)date_sent);
				body= rs.getString("body");
				serviceCenter = rs.getString("service_center");
				
				
				if(person!=null){
					
					System.out.println("shititititititi");
					ResultSet rs3 = stat2.executeQuery("SELECT name FROM contacts WHERE id="+person);
					String temp = rs3.getString("name");
					rs3.close();
					name = temp;
					
				}
				
							
				sql= "INSERT INTO sms VALUES ("+id+",\'"+address+"\',\'"+person+"\',\'"+name+"\',\'"+date+"\',\'"+time+"\',\'"+timeSent+"\',\'"+body+"\',\'"+serviceCenter+"\');";
				stat2.executeUpdate(sql);
				conn2.setAutoCommit(false);
			    conn2.commit();
			}
			rs.close();
	        conn.close();
	        conn2.close();			
		}
		catch(ClassNotFoundException | SQLException e){
			
		}
		
		//connect to our database and display the data to GUI	
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement stat = conn.createStatement();
				
			
		   ResultSet rs1 = stat.executeQuery("SELECT Count(*) AS total FROM sms");
		   int k = rs1.getInt("total");
		  			
		   arr2= new String[k][8];int i=0;
			
			ResultSet rs = stat.executeQuery("select * from sms;");
			while (rs.next()) {
				String address,person,name,date,date_sent,body,serviceCenter;
				int id;
				
				
				
				
				
				id=rs.getInt("id");
				address = rs.getString("address");
				person = rs.getString("person");
				name = rs.getString("name");
				date = rs.getString("date");
				date_sent = rs.getString("date_sent");
				body= rs.getString("body");
				serviceCenter = rs.getString("service_center");
					
				
				
				
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
			
			String[] header = {"id", "address","Person", "Name","date_received", "date_sent","message", "service_Center"};
			createTable(arr2,header);
			Gui.sms.add(scroller);    
			Gui.sms.setVisible(true);
			Gui.sms.updateUI();
			
		}catch(Exception e){
			
		}
		
	}

	
	
	
	public void getCalls(){
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+mountPath+"/data/com.android.providers.contacts/databases/contacts2.db");
			Statement stat = conn.createStatement();
			
			
			Connection conn2 = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement stat2=conn2.createStatement();
			stat2.executeUpdate("drop table if exists calls;");
	        stat2.executeUpdate("create table calls (id,number,name,timestamp,date,duration,type,countryiso,geocoded_location);");
	      	        
			ResultSet rs = stat.executeQuery("select * from calls;");
			
			while(rs.next()){
				
				String number,type,name,countryIso,geocodedLocation,sql;
				int id,duration,typeTemp;
				long date;
				
				id=rs.getInt("_id");
				number=rs.getString("number");
				name = rs.getString("name");
				date= rs.getLong("date");
				duration= rs.getInt("duration");
				typeTemp=rs.getInt("type");
				countryIso=rs.getString("countryiso");
				geocodedLocation=rs.getString("geocoded_location");
				/*long dv = Long.valueOf(date);
				Date df = new java.util.Date(dv);
				String time = new SimpleDateFormat("MM dd, yyyy hh:mma")
				.format(df);*/
				java.util.Date time2=new java.util.Date((long)date);
				if(typeTemp==1){
					type="incoming";
				}
				else if(typeTemp==2){
					type="outgoing";
				}
				else if(typeTemp==3){
					type="non replied";
				}
				else{
					type="uknown";
				}
				
				sql= "INSERT INTO calls VALUES ("+id+",\'"+number+"\',\'"+name+"\',\'"+date+"\',\'"+time2+"\',\'"+duration+"\',\'"+type+"\',\'"+countryIso+"\',\'"+geocodedLocation+"\');";
				stat2.executeUpdate(sql);
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
						
		   ResultSet rs1 = stat.executeQuery("SELECT Count(*) AS total FROM calls");
		   int k = rs1.getInt("total");
		  			
		   arr2= new String[k][8];int i=0;
			
			ResultSet rs = stat.executeQuery("select * from calls;");
			while (rs.next()) {
				String number,name,date,duration,type,countryiso,geocodedLocation;
				int id;
								
				id=rs.getInt("id");
				number = rs.getString("number");
				name = rs.getString("name");
				date = rs.getString("date");
				duration = rs.getString("duration");
				type = rs.getString("type");
				countryiso= rs.getString("countryiso");
				geocodedLocation = rs.getString("geocoded_location");
								
				
				arr2[i][0]=Integer.toString(id);
				arr2[i][1]= number;
				arr2[i][2]= name;
				arr2[i][3]= date;
				arr2[i][4]= duration;
				arr2[i][5]= type;
				arr2[i][6]= countryiso;
				arr2[i][7]= geocodedLocation;
			
				i++;
				
			}
			
			String[] header = {"id", "number","name", "date","duration", "type","countryiso", "geocoded location"};
			createTable(arr2,header);
			Gui.calls.add(scroller);    
			Gui.calls.setVisible(true);
			Gui.calls.updateUI();
			
		}catch(Exception e){
			
		}
		

	}
	
	


	
	
	public void getBrowserHistory(){
		String line="";
	 try{
		  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		  Process p2= Runtime.getRuntime().exec( "find "+mountPath+"/data/ -type d -name *android.browser*");
		  p2.waitFor();
		  reader=new BufferedReader(new InputStreamReader(p2.getInputStream()));
		  
		  
		  line=reader.readLine();
		  if(line == null || line==" "){
				
		  }
	  }
	  catch(IOException | InterruptedException e1){
		  
	  }
	 
	 
	
	
	
	 try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+line+"/databases/browser2.db");
			Statement stat = conn.createStatement();
			
			
			Connection conn2 = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement stat2=conn2.createStatement();
			stat2.executeUpdate("drop table if exists browserHistory;");
	        stat2.executeUpdate("create table browserHistory (id,title,url,timestamp,date,visits);");
	      	        
			ResultSet rs = stat.executeQuery("select * from history;");
			
			while(rs.next()){
				
				String title,url,visits,sql;
				int id;
				long date;
				
				id=rs.getInt("_id");
				title=rs.getString("title");
				url = rs.getString("url");
				date= rs.getLong("date");
				visits= rs.getString("visits");
				
				/*long dv = Long.valueOf(date);
				Date df = new java.util.Date(dv);
				String time = new SimpleDateFormat("MM dd, yyyy hh:mma")
				.format(df);*/
				java.util.Date time=new java.util.Date((long)date);
				
				
				
				sql= "INSERT INTO browserHistory VALUES ("+id+",\'"+title+"\',\'"+url+"\',\'"+date+"\',\'"+time+"\',\'"+visits+"\');";
				stat2.executeUpdate(sql);
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
						
		   ResultSet rs1 = stat.executeQuery("SELECT Count(*) AS total FROM browserHistory");
		   int k = rs1.getInt("total");
		  			
		   arr2= new String[k][5];int i=0;
			
			ResultSet rs = stat.executeQuery("select * from browserHistory;");
			while (rs.next()) {
				String title,url,date,visits;
				int id;
								
				id=rs.getInt("id");
				title = rs.getString("title");
				url = rs.getString("url");
				date = rs.getString("date");
				visits = rs.getString("visits");
				
								
				
				arr2[i][0]=Integer.toString(id);
				arr2[i][1]= title;
				arr2[i][2]= url;
				arr2[i][3]= date;
				arr2[i][4]= visits;
							
				i++;
				
			}
			
			String[] header = {"id", "title","url", "date","visits"};
			createTable(arr2,header);
			Gui.browserHistory.add(scroller);    
			Gui.browserHistory.setVisible(true);
			Gui.browserHistory.updateUI();
			
		}catch(Exception e){
			
		}
	 
	
	}
	
	
	//function to create a Jtable where the data will be displayed
		public static void createTable(Object[][] obj, String[] header) {
			
			
			JTable table = new JTable(obj, header);
			
					
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumnAdjuster tca = new TableColumnAdjuster(table);
			tca.adjustColumns();

			table.setEnabled(false);
			scroller = new JScrollPane(table);
			Dimension screenSize = Gui.mainFrame.getBounds().getSize();
			scroller.setPreferredSize(new Dimension(screenSize.width, (screenSize.height/4)-20));
			
		}
	
	
}
		