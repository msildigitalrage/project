package com.kithara;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class LoadImage  {

    public LoadImage() {
        initUI();
    }
    

    public void initUI() {
    
       final JDialog dialog = new JDialog();
       JPanel panel = new JPanel();
       panel.setBounds(40, 40, 500, 500);
       dialog.add(panel);
       JButton mountButton = new JButton("Mount");
       
       final JTextArea path = new JTextArea("",2,10);
       path.setVisible(true);
       
       mountButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent event) {
        
			String sas=  promptForFolder();      	 
        	path.setText(sas);
               
          }
       });
       
       JButton ok = new JButton("ok");
       ok.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			 mount(path, dialog);
			
		}
	});
       
      

       panel.add(mountButton);
       panel.add(path);
       panel.add(ok,2);
 
       dialog.setModal(true);
       dialog.setTitle("mount a Device");
       dialog.setSize(800, 800);
       dialog.setLocationRelativeTo(null);
       dialog.setVisible(true);
    }

   //  see if something is mounted 
   public boolean isMounted(String mount_path) {
	   
	   int flag =0;
	   try{
			  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			  Process p2= Runtime.getRuntime().exec( "sudo ls "+mount_path);
			  p2.waitFor();
			  reader=new BufferedReader(new InputStreamReader(p2.getInputStream()));
			  String line="";
			  
			  line=reader.readLine();
			  System.out.println(line);
			  if(line == null || line==" "){
					flag = 0;
			  }
			  else{
				  flag =1;
				  
			  }
		  }
		  catch(IOException | InterruptedException e1){
			  
		  }
	   if(flag == 0 ){
		   return false;
	   }else
	   { return true;
	   }
	   }
   
    
   
   
    
    public String promptForFolder(  )
    {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode( JFileChooser.FILES_ONLY);

        if( fc.showOpenDialog(fc ) == JFileChooser.APPROVE_OPTION )
        {
            return fc.getSelectedFile().getAbsolutePath();
        }

        return null;
    }
    
    
    
    //umount function
    public void umount(String mnt){
    	
    }
    
    
    
    
    
    
    
    // mount function
    public void mount ( JTextArea path, JDialog dialog){
    	String mountFile;
    	String mount_path = "/mnt"+System.currentTimeMillis();
    	System.out.println(mount_path);
		mountFile=path.getText();
		if(mountFile.equals("")){
			JOptionPane.showMessageDialog(null, "you have to fill the mount field");
		}
		else{
			try{
				  Process p1= Runtime.getRuntime().exec( "sudo mkdir "+mount_path);
				  p1.waitFor();
				  }
				  catch(IOException | InterruptedException e1){
				  }
			  try{
			  Process p1= Runtime.getRuntime().exec( "sudo mount -t ext4 "+mountFile+" "+mount_path);
			  p1.waitFor();
			  }
			  catch(IOException | InterruptedException e1){
			  }
			if(isMounted(mount_path)==true){   
		  JOptionPane.showMessageDialog(null, "mount was held with success");
	      dialog.setVisible(false);
     	  dialog.dispose();
			}
			else{
				JOptionPane.showMessageDialog(null, "this file it is not an image file");
			}
		}
		
    }
    
    
    
    
}