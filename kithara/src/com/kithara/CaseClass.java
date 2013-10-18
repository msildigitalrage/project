package com.kithara;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CaseClass {

	public void NewCase() {
		System.out.println("New Case");
		  final JFrame jFrame = new JFrame("New Case");
	        jFrame.setResizable(false);
	        
	        JPanel descPanel = new JPanel(new GridBagLayout());
	        JPanel namePanel = new JPanel(new GridBagLayout());
	        JPanel okPanel = new JPanel(new GridBagLayout());
	        JLabel descLabel = new JLabel("Description of Case");
	        final JTextArea descText = new JTextArea(10, 10);
	        JScrollPane scrollPanel = new JScrollPane(descText);
	        JLabel labelnameCase = new JLabel("Name");
	        final JTextField textNameCase = new JTextField(10);
	        JButton buttonOk = new JButton("OK");

		        GridBagConstraints gc = new GridBagConstraints();
		        gc.fill = GridBagConstraints.HORIZONTAL;
		        gc.weightx = 1;
		        gc.gridx = 0;
		        gc.gridy = 0;
		        gc.ipadx = 10;
		        namePanel.add(labelnameCase, gc);
	
		        gc.gridx = 1;
		        gc.gridy = 0;
		        namePanel.add(textNameCase, gc);
	        
		        GridBagConstraints gc1 = new GridBagConstraints();
	
		        gc1.fill = GridBagConstraints.HORIZONTAL;
		        gc1.weightx = 1;
		        gc1.gridx = 0;
		        gc1.gridy = 0;
		        descPanel.add(descLabel, gc1);
	
		        gc1.gridx = 0;
		        gc1.gridy = 1;
		        descPanel.add(scrollPanel, gc1);
		        
		        GridBagConstraints gc2 = new GridBagConstraints();
		        
		        gc2.fill = GridBagConstraints.HORIZONTAL;
		        gc2.gridx = 1;
		        gc2.gridy = 1;
		        okPanel.add(buttonOk, gc2);

			        buttonOk.addActionListener(new ActionListener() {
			            @Override
			            public void actionPerformed(ActionEvent ev) {
			            	
			            	if(textNameCase.getText().isEmpty() || descText.getText().isEmpty()){
			            		JOptionPane.showMessageDialog(null,"Please, fill all the fields..","Try Again",JOptionPane.OK_OPTION);
			            		System.out.println("Didn't Fill Name or Description");
			            	}else{
			            		String nameFolder = textNameCase.getText().toString();
			            		String descCase = descText.getText().toString();
			            		createFolderCase(nameFolder,descCase);
			            		jFrame.setVisible(false);
			            		jFrame.dispose();
			            		System.out.println("New Case, Folder Created");
			            	}
			                
			            }
			        });

	        jFrame.add(namePanel, BorderLayout.NORTH);
	        jFrame.add(descPanel, BorderLayout.CENTER);
	        jFrame.add(okPanel, BorderLayout.SOUTH);

	        //pack frame (size JFrame to match preferred sizes of added components and set visible
	        jFrame.pack();
	        jFrame.setLocationRelativeTo(null);
	        jFrame.setVisible(true);
		    

	}

	private void createFolderCase(String nameFolder, String descCase ) {
		File theDir = new File(nameFolder);
			//System.currentTimeMillis()
		  if (!theDir.exists()) {
		    boolean result = theDir.mkdir();  	
		     if(result) {    
		       System.out.println("directory folder named "+nameFolder+"created");  
		       File logFile = new File("/"+nameFolder+"/log.txt");
		       try {
				logFile.createNewFile();
			} catch (IOException e) {}
		       try{
		    	   FileWriter fw = new FileWriter("/home/iceman/git/kithara/kithara/"+nameFolder+"/log.txt",true);//true is for append-noNeed
		    	   BufferedWriter bufferWritter = new BufferedWriter(fw);
		    	   bufferWritter.write("Case Name: "+nameFolder+"\nDescription: " + descCase + "\nCase ID: "+ System.currentTimeMillis());
		    	   bufferWritter.close();
		    	   System.out.println("Write to Log File Completed");
		       }catch(IOException b){
		    	   b.printStackTrace();
		       }
		     }
		  }else{//if exists, choose other name
       		JOptionPane.showMessageDialog(null,"Choose different name for the Case","Case Exists",JOptionPane.OK_OPTION);
       		NewCase();
		     }	
	}

}
