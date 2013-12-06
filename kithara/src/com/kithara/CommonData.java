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
	public static String tableID[];
	
	public void getPermissions(){
		
		//change mode of the mount file in order the user can access it	
			try{
				  Process p2= Runtime.getRuntime().exec( "sudo chmod -R 777 "+mountPath);
				  p2.waitFor();
			  }
			  catch(IOException | InterruptedException e1){
				  
			  }
	}
	
	
	/*function to copy the contacts from the image to the commonData db
	and display this data to Gui*/
	public void getContacts(){
		
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
				java.util.Date time=new java.util.Date((long)date);
				date_sent = rs.getLong("date_sent");
				java.util.Date timeSent=new java.util.Date((long)date_sent);
				body= rs.getString("body");
				if (body!=null){
	        		body = body.replaceAll("'", "''");
	        	}
				serviceCenter = rs.getString("service_center");
				
				
				if(person!=null){
					ResultSet rs3 = stat2.executeQuery("SELECT name FROM contacts WHERE id="+person);
					String temp = rs3.getString("name");
					rs3.close();
					name = temp;
					if (name!=null){
		        		name = name.replaceAll("'", "''");
		        	}
					
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
				if (name!=null){
	        		name =name.replaceAll("'", "''");
	        	}
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
	
	//getViber
		public void getViber(){
			
			try{
				//connect to imageViberDB
				Class.forName("org.sqlite.JDBC");//use openSource SQLite.JDBC
				Connection conn = DriverManager.getConnection("jdbc:sqlite:"+mountPath+"/data/com.viber.voip/databases/viber_messages");
				Statement stat = conn.createStatement();
				//create LocalViberTable
				Connection conn2 = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
				Statement stat2=conn2.createStatement();
				stat2.executeUpdate("drop table if exists viber;");
				stat2.executeUpdate("create table viber (id,timestamp,latitude,longitude,interlocutor,message,type,extra_uri);");				//read
		        ResultSet rs = stat.executeQuery("select * from messages;");
		        
		        String sql;
	        	int id; 
	        	String timeStamp; 
	        	String latitude,longitude;
	        	String interlocutor,message;
	        	int type;
	        	String extra_uri;
	        	
		        conn2.setAutoCommit(false);
		        while(rs.next()){

		        	id = rs.getInt("_id");
		        	
		        	timeStamp = rs.getString("date");
		        	latitude = rs.getString("location_lat");
		        	longitude = rs.getString("location_lng");
		        	interlocutor = rs.getString("address");
		        	message = rs.getString("body");
		        	if (message!=null){
		        		message = message.replaceAll("'", "''");
		        	}
		        	type = rs.getInt("type");
		        	extra_uri = rs.getString("extra_uri");
		
					sql= "INSERT INTO viber VALUES ("+id+",'"+timeStamp+"','"+latitude+"','"+longitude+"','"+interlocutor+"','"+message+"','"+type+"','"+extra_uri+"');";
					stat2 .executeUpdate(sql);
		        	
		        	conn2.commit();
		        
		        }
		        rs.close();
		        conn.close();
		        conn2.close();
				
			}catch(Exception e){
				System.out.println("getViber failed");
			}
			
			//Connect to local db , display data to Gui
			try{
				Class.forName("org.sqlite.JDBC");
				Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
				Statement stat = conn.createStatement();
		        ResultSet rs1 = stat.executeQuery("SELECT Count(*) AS total FROM viber");
				int k = rs1.getInt("total");
				arr2= new String[k][8];
				int i=0;
				ResultSet rs = stat.executeQuery("select * from viber;");
					while (rs.next()) {//put them in the table
						arr2[i][0] = rs.getString("id");
						long timeView = rs.getLong("timeStamp");
						java.util.Date time2=new java.util.Date((long)timeView);
						arr2[i][1] = time2.toString();
						arr2[i][2] = rs.getString("latitude");
						arr2[i][3] = rs.getString("longitude");
						arr2[i][4] = rs.getString("interlocutor");
						arr2[i][5] = rs.getString("message");
						arr2[i][6] = rs.getString("type");
						arr2[i][7] = rs.getString("extra_uri");
						
						i++;
					}
					
					String[] header = {"id", "timestamp","latitude", "longitude","interlocutor", "message","type", "extra_uri"};
					createTable(arr2,header);
					
					Gui.viberPanel.add(scroller);    
					Gui.viberPanel.setVisible(true);
					Gui.viberPanel.updateUI();
					
			}catch(Exception ec){
				System.out.println("error!");
			}
			
			
		}
		
		//getWhatsUp
		public void getWhatsUp(){
			
			try{
				//connect to imageWhatsDB
				Class.forName("org.sqlite.JDBC");//use openSource SQLite.JDBC
				Connection conn = DriverManager.getConnection("jdbc:sqlite:"+mountPath+"/data/com.whatsup/databases/msgstore.db");
				Statement stat = conn.createStatement();
				//create LocalWhatsUpTable
				Connection conn2 = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
				Statement stat2=conn2.createStatement();
				stat2.executeUpdate("drop table if exists whatsUp;");
				stat2.executeUpdate("create table whatsUp (id,contactWith,whoSend,data,timeStamp,latitude,longitude);");

		        ResultSet rs = stat.executeQuery("select * from messages;");
		        
		        String sql;
	        	int id; 
	        	String contactWith,whoSend,data,timeStamp,latitude,longitude;

		        //int i=1;
		        conn2.setAutoCommit(false);
		        while(rs.next()){//for each record you read
		        	id = rs.getInt("_id");
		        	if(rs.getString("key_remote_jid").contains("@")){
		        		contactWith = rs.getString("key_remote_jid").replaceAll("@.*$", "");
		        	}else{
		        		contactWith = rs.getString("key_remote_jid");	
		        	}
		        	if(rs.getString("key_from_me").contains("0")){
		        		whoSend = "Received";
		        	}else{
		        		whoSend = "Sent";
		        	}
		        	data = rs.getString("data");
			        	if (data!=null){
			        		data = data.replaceAll("'", "''");
			        	}
		        	timeStamp = rs.getString("timestamp");
		        	latitude = rs.getString("latitude");
		        	longitude = rs.getString("longitude");
		        	
		        	//write it to localWhatsUpTable
					sql= "INSERT INTO whatsUp VALUES ("+id+",'"+contactWith+"','"+whoSend+"','"+data+"','"+timeStamp+"','"+latitude+"','"+longitude+"');";
					stat2 .executeUpdate(sql);
		        	
		        	conn2.commit();
		        
		        }
		        rs.close();
		        conn.close();
		        conn2.close();
				
			}catch(Exception e){
				System.out.println("getWhatsUp failed");
			}
			
			//Connect to local db , display data to Gui
			try{
				Class.forName("org.sqlite.JDBC");
				Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
				Statement stat = conn.createStatement();
		        ResultSet rs1 = stat.executeQuery("SELECT Count(*) AS total FROM whatsUp");
				int k = rs1.getInt("total");
				arr2= new String[k][7];
				int i=0;
				ResultSet rs = stat.executeQuery("select * from whatsUp;");
					while (rs.next()) {//put them in the table
						arr2[i][0] = rs.getString("id");				
						arr2[i][1] = rs.getString("contactWith");
						arr2[i][2] = rs.getString("whoSend");
						arr2[i][3] = rs.getString("data");
							long timeView = rs.getLong("timeStamp");
							java.util.Date time2=new java.util.Date((long)timeView);
						arr2[i][4] = time2.toString();
						arr2[i][5] = rs.getString("latitude");
						arr2[i][6] = rs.getString("longitude");	
						i++;
					}
					
					String[] header = {"id", "contactWith","Send / Receive", "Message","Time", "latitude","longitude"};
					createTable(arr2,header);
					Gui.whatsUpPanel.add(scroller);    
					Gui.whatsUpPanel.setVisible(true);
					Gui.whatsUpPanel.updateUI();
					
			}catch(Exception ec){
				System.out.println("errorWhatsUp!");
			}
			
			
		}
		
		//twitter
		public void getTwitter(){
			/*
			 for each userID that has used the device there is a database "userID.db" with statuses-table
			 * 
			 */
			
			int ui = 0; String line; int j = 0;					

			//find how many userID.db exists
			try{
				Process myPr= Runtime.getRuntime().exec( "find "+mountPath+"/data/com.twitter.android/databases/"+ " -name" +" ??*-?.db");
				BufferedReader in = new BufferedReader(new InputStreamReader(myPr.getInputStream()));
			    	while ((line = in.readLine()) != null) {
			    		ui++;		
			    	}
				myPr.waitFor();
			}catch (Exception e) {
				System.out.println("Error find UserId db's");
			}
			//save idDB's to table
			tableID = new String[ui];
			try{
				 
				Process myPr= Runtime.getRuntime().exec( "find "+mountPath+"/data/com.twitter.android/databases/"+ " -name" +" ??*-?.db");
				BufferedReader in = new BufferedReader(new InputStreamReader(myPr.getInputStream()));
			    	while ((line = in.readLine()) != null) { 
			    		tableID[j]= line;
			    		System.out.println(tableID[j]); //pathsofIDtables save in tableID
			    		j++;
			    	}
				myPr.waitFor();
			
				for (int p = 0; p < tableID.length; p++) {
					
				
				//connect to TwitterDB
				Class.forName("org.sqlite.JDBC");//use openSource SQLite.JDBC
				Connection conn = DriverManager.getConnection("jdbc:sqlite:"+tableID[p]);
				Statement stat = conn.createStatement();
				//create globalTable
				Connection conn2 = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
				Statement stat2=conn2.createStatement();
				
				if(p == 0){
				stat2.executeUpdate("drop table if exists twitter;");
				stat2.executeUpdate("create table twitter (id,author_id,content,created,latitude,longitude);");
				}
		        ResultSet rs = stat.executeQuery("select * from statuses;");
		        //System.out.println("ok man");
		        String sql;
		        // _id,     author_id,content,created,latitude,longitude
	        	int id;String whoSend,data,timeStamp,latitude,longitude;

		        //int i=1;
		        conn2.setAutoCommit(false);
		        while(rs.next()){//for each record you read
		        	id = rs.getInt("_id");
		        	whoSend = rs.getString("author_id");
		        	data = rs.getString("content");
			        	if (data!=null){
			        		data = data.replaceAll("'", "''");
			        	}
		        	timeStamp = rs.getString("created");
		        	latitude = rs.getString("latitude");
		        	longitude = rs.getString("longitude");
		        	//write it to localTwitterTable
					sql= "INSERT INTO twitter VALUES ("+id+",'"+whoSend+"','"+data+"','"+timeStamp+"','"+latitude+"','"+longitude+"');";
					stat2 .executeUpdate(sql);        	
		        	conn2.commit();		        
		        }
		        rs.close();
		        conn.close();
		        conn2.close();
				}
				
			}catch(Exception e){
				System.out.println("Twitter failed");
			}
			
			//Connect to local db , display dataTwitter to Gui
			try{
				Class.forName("org.sqlite.JDBC");
				Connection conn = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
				Statement stat = conn.createStatement();
		        ResultSet rs1 = stat.executeQuery("SELECT Count(*) AS total FROM twitter");
				int k = rs1.getInt("total");
				arr2= new String[k][7];
				int i=0;
				//--get from tableID the exactlyID of deviceUser
					String tmpUID[] = null;
					String part1;
					for (int l = 0; l < tableID.length; l++) {
						   tmpUID= tableID[l].split("/"); 
						   part1 = tmpUID[tmpUID.length-1];
						   tmpUID= part1.split("-");
						   tableID[l]=tmpUID[0];
						   System.out.println(tableID[l]);
					}
				//--	

				//--	
				ResultSet rs = stat.executeQuery("select * from twitter;");
					while (rs.next()) {//put them in the table
						arr2[i][0] = String.valueOf(i+1);
						/*procedure to echo WhoSend message,
						 so if msg send from device, echo that
						 */
						boolean b = false;
						for (int q = 0; q < tableID.length; q++) {
							if(rs.getString("author_id").contains(tableID[q])){	
								b = true;
								}
						}
						if(b==true){
							arr2[i][1] = "SentFromDevice with id "+rs.getString("author_id");
						}
						else{
							arr2[i][1] = rs.getString("author_id");
						}
						arr2[i][2] = rs.getString("content");
							long timeView = rs.getLong("created");
							java.util.Date time2=new java.util.Date((long)timeView);
						arr2[i][3] = time2.toString();
						arr2[i][4] = rs.getString("latitude");
						arr2[i][5] = rs.getString("longitude");	
						i++;
					}
					
					String[] header = {"id", "Send By","Content", "Created","Latitude","Longitude"};
					createTable(arr2,header);
					Gui.twitterPanel.add(scroller);    
					Gui.twitterPanel.setVisible(true);
					Gui.twitterPanel.updateUI();
					
			        rs.close();
			        rs1.close();
			        conn.close();
			   
			}catch(Exception ec){
				System.out.println("errorTwitter!");
			}
			
			try {//tmpTwitter ---- holds userIds that send message from the device
				Connection conn2 = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locations.db");			
				Statement statTmp = conn2.createStatement();
				statTmp.executeUpdate("drop table if exists tmpTwitter;");
				statTmp.executeUpdate("create table tmpTwitter (id);");
				String sql;
				for (int l = 0; l < tableID.length; l++) {
					sql = "INSERT INTO tmpTwitter VALUES ('"+tableID[l]+"');";
					statTmp .executeUpdate(sql); 
					conn2.setAutoCommit(false);
		        	conn2.commit();	
				}
				conn2.close();
			} catch (Exception e) {
				System.out.println("erroTMP!");
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