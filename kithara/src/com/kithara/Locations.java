package com.kithara;

import java.io.IOException;

public class Locations {
	public static String mountPath;
	public void getLocations(){
		try{
			  Process p2= Runtime.getRuntime().exec( "sudo chmod -R 777 "+mountPath);
			  p2.waitFor();
			  
		  }
		  catch(IOException | InterruptedException e1){}
	}
}
