package com.kithara;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenCase {
	String sCurrentLine; 
	String lastMounted;
	String caseName;
	String pathCase;
	public void loadCase(){
		pathCase = promptForFolder();
		System.out.println(pathCase);
		caseName = getCaseName(pathCase);
		readPathCase(pathCase);
		mountOpenCase();
		buttons();
	}
	
	// mount function
    public void mountOpenCase (){
    	String mountFile;
    	String fileSystem;    	
    	String temp[] = lastMounted.split(":");
    	String[] temp2 = temp[0].split("/");
    	
    	  	
    	mountFile = temp2[temp2.length-1];
    	fileSystem=temp[1];
		
    	String mountPath = "/mnt_"+mountFile+"_"+caseName;
    	System.out.println(mountPath+"gia ton p");
    	
    	
    	
		try{
			Process p= Runtime.getRuntime().exec( "sudo mkdir "+mountPath);
			p.waitFor();
		    }
			catch(IOException | InterruptedException e1){
		    }
				
			  try{
			 
			  System.out.println("sudo mount -t "+fileSystem+" "+temp[0]+" "+mountPath);
			  Process p1= Runtime.getRuntime().exec( "sudo mount -t "+fileSystem+" "+temp[0]+" "+mountPath);
			  p1.waitFor();
			  
			 String log= pathCase.replaceAll("loadFiles.txt","log.txt");
			 
	   	      if(LoadImage.isMounted(mountPath)==true){   
				//JOptionPane.showMessageDialog(null, "mount was held with success");
			    LoadImage.createTreeView(mountPath);
			    Gui.leftPanel.updateUI();
			    FileWriter fw = new FileWriter(log,true);//true is for append-noNeed
		   	    BufferedWriter bufferWritter = new BufferedWriter(fw);
		   	    bufferWritter.write("\nOpen Case Mount point: "+mountPath+"\t File system: "+fileSystem+"\t mounted file"+ mountFile);
		   	    bufferWritter.close();
			    LoadImage.where_log=log;
			    Umount.mountPath=mountPath;
			    CommonData.mountPath= mountPath;
			    
			    
			    CommonData.projectPath = log.replaceAll("log.txt", "");
	   	      }
	   	      else{
				JOptionPane.showMessageDialog(null, "this file it is not an image file or the file system you choose is wrong");
	   	      }
	   	    	      
	   	      Gui.commonEvidences.setEnabled(true);
	   	      Gui.unloadAnImage.setEnabled(true);
	   	      Gui.loadImage.setEnabled(false);
	   	      LoadImage.loadedImage= true;
			  }
			  catch(IOException | InterruptedException e1){
			  }
			  
			  
			
		}
		
    
	
	
	
	public void readPathCase(String pathCase) {
		try {
			FileReader fd = new FileReader(pathCase);
			BufferedReader bufferedReader = new BufferedReader(fd);

			  try {
				while ((sCurrentLine = bufferedReader.readLine()) != null) 
				    {
				        System.out.println(sCurrentLine);
				        lastMounted = sCurrentLine;
				    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.print("Cannot Read CaseFile");
		}
		
	}
	public void buttons(){
    	Gui.openCase.setEnabled(false);
    	Gui.newCase.setEnabled(false);
    	Gui.closeCase.setEnabled(true);
    	Gui.loadImage.setEnabled(true);
	}
	
	public String promptForFolder(){
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fc.setFileFilter(filter);
        if( fc.showOpenDialog(fc ) == JFileChooser.APPROVE_OPTION )
        {
            return fc.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
	
	
	
	public String getCaseName(String arxeioTxt){
		String temp[]= arxeioTxt.split("/");
		int length = temp.length;
		return temp[length-2];
	}
}