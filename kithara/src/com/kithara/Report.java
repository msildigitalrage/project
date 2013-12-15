package com.kithara;

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
			exportLocations();
		    out.close();
		    
		}catch(Exception prob){
			System.out.println("Problem making Full Report");
		}
	}

	private void exportContacts() {
		try {
			Connection conLocations = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"commonData.db");
			Statement myStatement =conLocations.createStatement();
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
			int p = 0;
			while (myResultSet.next()) {
				out.append(myResultSet.getString("id"));
				out.append(",");
				out.append(myResultSet.getString("name"));
				out.append(",");
				out.append(myResultSet.getString("number"));
				out.append(",");
				out.append('\n');
				p++;
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
		
	}

	private void exportLocations() {
		try {
			Connection conLocations = DriverManager.getConnection("jdbc:sqlite:"+projectPath+"locations.db");
			Statement myStatement =conLocations.createStatement();
			ResultSet myResultSet;
			myResultSet = myStatement.executeQuery("SELECT * FROM myLocations;");
			int k = 0;
			while (myResultSet.next()) {//count events		
				k++;
			}
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
			int p = 0;
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
				p++;
			}
		} catch (Exception erloc) {
			// TODO Auto-generated catch block
			erloc.printStackTrace();
		}
		
	}
	
	
}
