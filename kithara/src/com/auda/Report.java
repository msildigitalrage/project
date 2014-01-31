package com.auda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Report {

	public static String mountPath;
	public static String  projectPath;
	BufferedWriter out;
	String caseName,caseInfo,caseId;
	public void makeReport(){
		System.out.println("Starting Making Report of Case");
		try{
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(projectPath+"report.csv"),"UTF-8"));
			out.append("Report");
			out.append(",");
			FileReader readerCaseInfo = new FileReader(projectPath+"log.txt");
			String line; int numLines = 0; BufferedReader br = null;
			br = new BufferedReader(readerCaseInfo);
			while ((line = br.readLine()) != null && numLines <3){
				//line = br.readLine();
				if(numLines==0){
					caseName = line;
					out.append(caseName);
					out.append(",");
				}
				if(numLines==1){
					caseInfo = line;
					out.append(caseInfo);
					out.append(",");
				}
				if(numLines==2){
					caseId = line;
					out.append(caseId);
					out.append('\n');
				}
				numLines++;
			}
			br.close();
			exportContacts();
			exportCalls();
			exportSms();
			exportHistory();
			exportViber();
			exportWhatsUp();
			exporttwitter();
			exportSkype();
			exportLocations();
		    out.close();
		    
		}catch(Exception prob){
			System.out.println("Problem making Full Report");
		}
	}
	private void exportSkype() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement myStatement =connection.createStatement();
			ResultSet myResultSet;
			myResultSet = myStatement.executeQuery("SELECT * FROM skype;");
			out.append('\n');
			out.append("Skype History Messages");
			out.append(",");
			out.append('\n');
			out.append("id");
			out.append(",");
			out.append("author");
			out.append(",");
			out.append("interlocutor");
			out.append(",");
			out.append("timestamp");
			out.append(",");
			out.append("date");
			out.append(",");
			out.append("message");
			out.append(",");
			out.append('\n');
			String tmpMessage = null;
			int gm = 0;
			while (myResultSet.next()) {
				gm++;
				out.append(Integer.toString(gm));
				out.append(",");
				out.append(myResultSet.getString("author"));
				out.append(",");
				out.append(myResultSet.getString("interlocutor"));
				out.append(",");
				out.append(myResultSet.getString("timestamp"));
				out.append(",");
				out.append(myResultSet.getString("date"));
				out.append(",");
				if(myResultSet.getString("message").contains("\n")
						|| myResultSet.getString("message").contains(",")){
					tmpMessage = myResultSet.getString("message").replaceAll("\n", "");
					out.append(tmpMessage);
				}else{
					out.append(myResultSet.getString("message"));
				}
				out.append(",");
				out.append('\n');
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
	}
	private void exporttwitter() {
		
		try {

			Connection conIDS = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locations.db");
			Statement myStatement =conIDS.createStatement();
			ResultSet rs = myStatement.executeQuery("select * from tmpTwitter;");
			out.append('\n');
			out.append("Twitter IDS were Used from Device");
			out.append('\n');
			out.append("id");
			out.append(",");
			out.append("Twitter ID");
			out.append(",");
			out.append("\n");
			int gh = 0;
			while(rs.next()){
				gh++;
				out.append(Integer.toString(gh));
				out.append(",");
				out.append(rs.getString("id"));
				out.append(",");
				out.append("\n");
			}
			rs.close();
			conIDS.close();
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement myStatement2 =con.createStatement();
			ResultSet myResultSet2;
			myResultSet2 = myStatement2.executeQuery("SELECT * FROM twitter;");
			out.append('\n');
			out.append("Twitter Data");
			out.append(",");
			out.append('\n');
			out.append("id");
			out.append(",");
			out.append("User ID");
			out.append(",");
			out.append("latitude");
			out.append(",");
			out.append("longitude");
			out.append(",");
			out.append("created");
			out.append(",");
			out.append("content");
			out.append(",");
			out.append('\n');
			String tmpMessage = null;
			int cc = 0; 
			while (myResultSet2.next()) {
				cc++;
				out.append(Integer.toString(cc));
				out.append(",");
				out.append(myResultSet2.getString("author_id"));
				out.append(",");
				out.append(myResultSet2.getString("latitude"));
				out.append(",");
				out.append(myResultSet2.getString("longitude"));
				out.append(",");
				out.append(myResultSet2.getString("created"));
				out.append(",");
				if(myResultSet2.getString("content").contains("\n")
						|| myResultSet2.getString("content").contains(",")){
					tmpMessage = myResultSet2.getString("content").replaceAll("\n", "");
					out.append(tmpMessage);
				}else{
					out.append(myResultSet2.getString("content"));
				}
				out.append(",");
				out.append('\n');
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
		
	}
	private void exportWhatsUp() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement myStatement =connection.createStatement();
			ResultSet myResultSet;
			myResultSet = myStatement.executeQuery("SELECT * FROM whatsUp;");
			out.append('\n');
			out.append("WhatsUp History");
			out.append(",");
			out.append('\n');
			out.append("id");
			out.append(",");
			out.append("timestamp");
			out.append(",");
			out.append("latitude");
			out.append(",");
			out.append("longitude");
			out.append(",");
			out.append("contactWith");
			out.append(",");
			out.append("whoSend");
			out.append(",");
			out.append("data");
			out.append(",");
			out.append('\n');
			while (myResultSet.next()) {
				out.append(myResultSet.getString("id"));
				out.append(",");
				out.append(myResultSet.getString("timestamp"));
				out.append(",");
				out.append(myResultSet.getString("latitude"));
				out.append(",");
				out.append(myResultSet.getString("longitude"));
				out.append(",");
				out.append(myResultSet.getString("contactWith"));
				out.append(",");
				out.append(myResultSet.getString("whoSend"));
				out.append(",");
				out.append(myResultSet.getString("data"));
				out.append(",");
				out.append('\n');
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
		
	}
	private void exportViber() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement myStatement =connection.createStatement();
			ResultSet myResultSet;
			myResultSet = myStatement.executeQuery("SELECT * FROM viber;");
			out.append('\n');
			out.append("Viber History");
			out.append(",");
			out.append('\n');
			out.append("id");
			out.append(",");
			out.append("timestamp");
			out.append(",");
			out.append("latitude");
			out.append(",");
			out.append("longitude");
			out.append(",");
			out.append("interlocutor");
			out.append(",");
			out.append("message");
			out.append(",");
			out.append("type");
			out.append(",");
			out.append("extra_uri");
			out.append(",");
			out.append('\n');
			while (myResultSet.next()) {
				out.append(myResultSet.getString("id"));
				out.append(",");
				out.append(myResultSet.getString("timestamp"));
				out.append(",");
				out.append(myResultSet.getString("latitude"));
				out.append(",");
				out.append(myResultSet.getString("longitude"));
				out.append(",");
				out.append(myResultSet.getString("interlocutor"));
				out.append(",");
				out.append(myResultSet.getString("message"));
				out.append(",");
				out.append(myResultSet.getString("type"));
				out.append(",");
				out.append(myResultSet.getString("extra_uri"));
				out.append(",");
				out.append('\n');
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
		
	}
	private void exportHistory() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement myStatement =connection.createStatement();
			ResultSet myResultSet;
			myResultSet = myStatement.executeQuery("SELECT * FROM browserHistory;");
			out.append('\n');
			out.append("Browser History");
			out.append(",");
			out.append('\n');
			out.append("id");
			out.append(",");
			out.append("title");
			out.append(",");
			out.append("visits");
			out.append(",");
			out.append("timestamp");
			out.append(",");
			out.append("date");
			out.append(",");
			out.append("url");
			out.append(",");
			out.append('\n');
			while (myResultSet.next()) {
				out.append(myResultSet.getString("id"));
				out.append(",");
				out.append(myResultSet.getString("title"));
				out.append(",");
				out.append(myResultSet.getString("visits"));
				out.append(",");
				out.append(myResultSet.getString("timestamp"));
				out.append(",");
				out.append(myResultSet.getString("date"));
				out.append(",");
				out.append(myResultSet.getString("url"));
				out.append(",");
				out.append('\n');
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
		
		
	}
	private void exportCalls() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement myStatement =connection.createStatement();
			ResultSet myResultSet;
			myResultSet = myStatement.executeQuery("SELECT * FROM calls;");
			out.append('\n');
			out.append("Calls");
			out.append(",");
			out.append('\n');
			out.append("id");
			out.append(",");
			out.append("name");
			out.append(",");
			out.append("number");
			out.append(",");
			out.append("timestamp");
			out.append(",");
			out.append("date");
			out.append(",");
			out.append("duration");
			out.append(",");
			out.append("type");
			out.append(",");
			out.append("Country ISO");
			out.append(",");
			out.append("Geocoded Location");
			out.append(",");
			out.append('\n');
			while (myResultSet.next()) {
				out.append(myResultSet.getString("id"));
				out.append(",");
				out.append(myResultSet.getString("name"));
				out.append(",");
				out.append(myResultSet.getString("number"));
				out.append(",");
				out.append(myResultSet.getString("timestamp"));
				out.append(",");
				out.append(myResultSet.getString("date"));
				out.append(",");
				out.append(myResultSet.getString("duration"));
				out.append(",");
				out.append(myResultSet.getString("type"));
				out.append(",");
				out.append(myResultSet.getString("countryiso"));
				out.append(",");
				out.append(myResultSet.getString("geocoded_location"));
				out.append(",");
				out.append('\n');
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
		
	}
	private void exportSms() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement myStatement =connection.createStatement();
			ResultSet myResultSet;
			myResultSet = myStatement.executeQuery("SELECT * FROM sms;");
			out.append('\n');
			out.append("SMS");
			out.append(",");
			out.append('\n');
			out.append("id");
			out.append(",");
			out.append("Address Receiver");
			out.append(",");
			out.append("Name Receiver");
			out.append(",");
			out.append("Timestamp");
			out.append(",");
			out.append("Date");
			out.append(",");
			out.append("Message");
			out.append(",");
			out.append('\n');
			String tmpBody = null;
			while (myResultSet.next()) {
				out.append(myResultSet.getString("id"));
				out.append(",");
				out.append(myResultSet.getString("address"));
				out.append(",");
				out.append(myResultSet.getString("name"));
				out.append(",");
				out.append(myResultSet.getString("timestamp"));
				out.append(",");
				out.append(myResultSet.getString("date"));
				out.append(",");
				if(myResultSet.getString("body").contains("\n")
						|| myResultSet.getString("body").contains(",")){
					tmpBody = myResultSet.getString("body").replaceAll("\n", "");
					//System.out.println(tmpBody);
					out.append(tmpBody);
				}else{
					out.append(myResultSet.getString("body"));
				}
				
				out.append(",");
				out.append('\n');
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
		
	}
	
	private void exportContacts() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement myStatement =connection.createStatement();
			ResultSet myResultSet;
			myResultSet = myStatement.executeQuery("SELECT * FROM contacts;");
			out.append('\n');
			out.append("Contacts");
			out.append(",");
			out.append('\n');
			out.append("id");
			out.append(",");
			out.append("name");
			out.append(",");
			out.append("number");
			out.append(",");
			out.append('\n');
			while (myResultSet.next()) {
				out.append(myResultSet.getString("id"));
				out.append(",");
				out.append(myResultSet.getString("name"));
				out.append(",");
				out.append(myResultSet.getString("number"));
				out.append(",");
				out.append('\n');
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
		
	}

	private void exportLocations() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locations.db");
			Statement myStatement =connection.createStatement();
			ResultSet myResultSet;
			myResultSet = myStatement.executeQuery("SELECT * FROM myLocations;");
			out.append('\n');
			out.append("Locations");
			out.append(",");
			out.append('\n');
			out.append("id");
			out.append(",");
			out.append("TimeStamp");
			out.append(",");
			out.append("Latitude");
			out.append(",");
			out.append("Longitude");
			out.append(",");
			out.append("Application Used");
			out.append(",");
			out.append("Event Occured");
			out.append(",");
			out.append('\n');
			while (myResultSet.next()) {
				out.append(myResultSet.getString("id"));
				out.append(",");
				out.append(myResultSet.getString("timestamp"));
				out.append(",");
				out.append(myResultSet.getString("latitude"));
				out.append(",");
				out.append(myResultSet.getString("longitude"));
				out.append(",");
				out.append(myResultSet.getString("app"));
				out.append(",");
				out.append(myResultSet.getString("event"));
				out.append(",");
				out.append('\n');
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
		
	}
	
	
}
