package com.kithara;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CaseClass {
	
	public String where_log = "null";
	JDialog jDialog = new JDialog();
	JPanel descPanel = new JPanel(new GridBagLayout());
    JPanel namePanel = new JPanel(new GridBagLayout());
    JPanel okPanel = new JPanel(new GridBagLayout());
    JLabel descLabel = new JLabel("Description of Case");
    final JTextArea descText = new JTextArea(10, 10);
    JScrollPane scrollPanel = new JScrollPane(descText);
    JLabel labelnameCase = new JLabel("Name");
    final JTextField textNameCase = new JTextField(10);
    JButton buttonOk = new JButton("OK");
    
	public void newCase() {
		jDialog.setModal(true);
		//jDialog.setModalityType(ModalityType.DOCUMENT_MODAL);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		jDialog.setBounds((screenSize.width / 2) - (200/ 2), (screenSize.height / 2) - (200 / 2), 200, 200);
		jDialog.setResizable(false);
		jDialog.setTitle("New Case");
		jDialog.setFocusable(true);
		jDialog.requestFocus();
		
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
		            		jDialog.setVisible(false);
		            		jDialog.dispose();
		            		System.out.println("New Case, Folder Created");
		            		Gui.loadImage.setEnabled(true);
		            	}
		                
		            }
		        });

		        jDialog.add(namePanel, BorderLayout.NORTH);
		        jDialog.add(descPanel, BorderLayout.CENTER);
		        jDialog.add(okPanel, BorderLayout.SOUTH);
		        
		        
		        jDialog.pack();
		        jDialog.setLocationRelativeTo(null);
		        jDialog.setVisible(true);
		
		
	}

	private void createFolderCase(String nameFolder, String descCase ) {
		String path=null;
		  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try{
			 String pwdcmd = "pwd";
			
			 Process p= Runtime.getRuntime().exec(pwdcmd);
			 p.waitFor();
			 reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
			 path=reader.readLine();
		       
		}
		catch(IOException e1){
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File theDir = new File(nameFolder);
			//System.currentTimeMillis()
		  if (!theDir.exists()) {
		    boolean result = theDir.mkdir();  	
		     if(result) {    
		       System.out.println("directory folder named "+nameFolder+" created");  
		       File logFile = new File("/"+nameFolder+"/log.txt");
		       try {
				logFile.createNewFile();
			} catch (IOException e) {}
		       try{
		    	   where_log = path + "/" +nameFolder+"/log.txt";
		    	   FileWriter fw = new FileWriter(path + "/" +nameFolder+"/log.txt",true);//true is for append-noNeed
		    	   BufferedWriter bufferWritter = new BufferedWriter(fw);
		    	   bufferWritter.write("Case Name: "+nameFolder+"\nDescription: " + descCase + "\nCase ID: "+ System.currentTimeMillis());
		    	   bufferWritter.close();
		    	   //System.out.println("Write to Log File Completed");
		    	   
		       }catch(IOException b){
		    	   b.printStackTrace();
		       }
		     }
		  }else{//if exists, choose other name
       		JOptionPane.showMessageDialog(null,"Choose different name for the Case","Case Exists",JOptionPane.OK_OPTION);
       		newCase();
		     }	
	}
	
	public String get_where_log(){
		return where_log;
	}
}