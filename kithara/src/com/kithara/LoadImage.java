package com.kithara;

 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.swing.*;

import static javax.swing.GroupLayout.Alignment.*;
 
public class LoadImage  {
	public String where_log;
	
    public LoadImage() {
    	
    	
    	final JDialog dialog = new JDialog();
    	JPanel panel = new JPanel();
    	
    	
    	
        JLabel label = new JLabel("Path mounted:");;
        final JTextField path = new JTextField();
        final ButtonGroup fileSystem = new ButtonGroup();
        JRadioButton vfatBox = new JRadioButton("ext3");
        JRadioButton ext4Box = new JRadioButton("ext4");
        JRadioButton ntfsBox = new JRadioButton("ntfs");
        JRadioButton ext3Box = new JRadioButton("vfat");
        fileSystem.add(ext3Box);
        fileSystem.add(ext4Box);
        fileSystem.add(ntfsBox);
        fileSystem.add(vfatBox);
        
       
        
        
        
        
        JButton mountButton = new JButton("Mount");
        
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
    			 mount(path , dialog, fileSystem);
    			 
    	         
    			
    		}
    	});
        
 
        
        panel.add(label);
        panel.add(path);
        panel.add(vfatBox);
        panel.add(ext3Box);
        panel.add(ext4Box);
        panel.add(ntfsBox);
        panel.add(mountButton);
        panel.add(ok);
        panel.setVisible(true);
        
        vfatBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ext4Box.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ntfsBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ext3Box.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
 
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
 
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addComponent(label)
            .addGroup(layout.createParallelGroup(LEADING)
                .addComponent(path)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(vfatBox)
                        .addComponent(ntfsBox))
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(ext4Box)
                        .addComponent(ext3Box))))
            .addGroup(layout.createParallelGroup(LEADING)
                .addComponent(mountButton)
                .addComponent(ok))
        );
        
        layout.linkSize(SwingConstants.HORIZONTAL, mountButton, ok);
 
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(BASELINE)
                .addComponent(label)
                .addComponent(path)
                .addComponent(mountButton))
            .addGroup(layout.createParallelGroup(LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(vfatBox)
                        .addComponent(ext4Box))
                    .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(ntfsBox)
                        .addComponent(ext3Box)))
                .addComponent(ok))
        );
 
        dialog.add(panel);
        dialog.setVisible(true);
        dialog.setTitle("Find");
        dialog.pack();
       // dialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
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
    public void mount (JTextField path, JDialog dialog, ButtonGroup fileSystem){
    	
    	String mountFile;
    	String filesystem1 = getSelectedButtonText(fileSystem);
    	
    	System.out.println(filesystem1);
    	String mount_path = "/mnt"; //+System.currentTimeMillis();
    	System.out.println(mount_path);
		mountFile=path.getText();
		
		
		if(mountFile.equals("") || filesystem1.equals("")){
			JOptionPane.showMessageDialog(null, "you have to fill  mounted file and choose file system");
		}
		else{
			try{
				  Process p1= Runtime.getRuntime().exec( "sudo mkdir "+mount_path);
				  p1.waitFor();
				  }
				  catch(IOException | InterruptedException e1){
				  }
			  try{
			  Process p1= Runtime.getRuntime().exec( "sudo mount -t "+filesystem1+" "+mountFile+" "+mount_path);
			  p1.waitFor();
			  System.out.println("kasiarakos"+where_log);
			  FileWriter fw = new FileWriter(where_log,true);//true is for append-noNeed
	   	      BufferedWriter bufferWritter = new BufferedWriter(fw);
	   	      bufferWritter.write("\nMount point: "+mount_path+"\tFile system: "+filesystem1+"\t mounted file"+ mountFile);
	   	      bufferWritter.close();
			  }
			  catch(IOException | InterruptedException e1){
			  }
			if(isMounted(mount_path)==true){   
		  JOptionPane.showMessageDialog(null, "mount was held with success");
		  
		 
		  // container.setVisible(false);
     	 // container.dispose();
			}
			else{
				JOptionPane.showMessageDialog(null, "this file it is not an image file or the file system you choose is wrong");
			}
		}
		
    }
    
    
    public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
        
    }

    
  public void set_where_log(String where_log_s){
    	
	 where_log = where_log_s;
    }
  
  
 
}