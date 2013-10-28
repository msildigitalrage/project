package com.kithara;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenCase {
	String sCurrentLine; 
	String lastMounted;
	public void loadCase(){
		String pathCase = promptForFolder();
		System.out.println(pathCase);
		readPathCase(pathCase);
		buttons();
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
}
