package com.kithara;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Locations {
public void mapCreator() {		
		
		//ypothetic positions
		double[] lat = new double []{50.375457,51.511214,53.479324};
		double[] lon = new double []{-4.142656,-0.119824,-2.248485};
		String[] titleEvent = new String []{"Event A","Event B","Event C"};
		String[] contentEvent = new String [] {"Information about the Event A","Information about the Event B","Information about the Event C"};
			
		//number of locations
		int max = 2;
		
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try {
			fWriter = new FileWriter("map.html");
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
		    writer.write("\nvar marker"+i+" = new google.maps.Marker({position: coordinates["+i+"],map: map,title:\""+titleEvent[i]+"\"});");
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
		   Runtime.getRuntime().exec("google-chrome /home/iceman/Project/MapCreator/map.html");
		   Runtime.getRuntime().exec("gedit /home/iceman/Project/MapCreator/map.html");
		} catch (IOException e) {
			System.out.println("Something Went Wrong..");
			e.printStackTrace();
		}	
	}
}
