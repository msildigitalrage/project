package com.auda;

import java.io.IOException;

import javax.swing.JOptionPane;


public class Umount {
	public static String mountPath;
	
	public void umount (){
		
		 try{
			 	 
			  
			  Process p2= Runtime.getRuntime().exec( "sudo umount "+mountPath);
			  p2.waitFor();
					  Gui.unloadAnImage.setEnabled(false);
					  Gui.loadImage.setEnabled(true);
					  Gui.report.setEnabled(true);
					  TreeView.fileTree.setModel(null);
					  LoadImage.loadedImage=false;
			   	      Gui.commonEvidences.setEnabled(false);
			   	      Gui.hashes.setEnabled(false);
			   	      Gui.routeMap.setEnabled(false);
			   	      Gui.areasLocations.setEnabled(false);
				  try{
					  
					  Process p= Runtime.getRuntime().exec( "sudo rmdir "+mountPath);
					  p.waitFor();
					 	  		 
					  
				  }
				  catch(IOException | InterruptedException e1){
					  
				  } 
			  
		  }
		  catch(IOException | InterruptedException e1){
			  
		  }
		if(LoadImage.isMounted(mountPath)){
			JOptionPane.showMessageDialog(null, "something went wrong");
		}
		else{
			System.out.println(mountPath);
			//JOptionPane.showMessageDialog(null, "the umount held with success");
		}
	}	

}
