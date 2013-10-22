package com.kithara;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class Gui {

	public static JFrame mainFrame = new JFrame();
	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem newCase = new JMenuItem("New Case");
	JMenuItem openCase = new JMenuItem("Open Case");
	public static JMenuItem loadImage = new JMenuItem("Load Image");
	final JMenuItem exitClose = new JMenuItem("Exit");
	public static Panel leftPanel = new Panel();

    
    //JScrollBar vbar=new JScrollBar(JScrollBar.HORIZONTAL, 30, 40, 0, 500);
    
	public static Panel centerPanel = new Panel();
	Panel botPanel = new Panel();
	
	
	public Gui() {
		createFrame();
		createMenu();
		createPanels();
		createTreeView();
		
	}
		void createFrame(){
			
		    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    mainFrame.setBounds(0,0,screenSize.width, screenSize.height);
		    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		    mainFrame.setTitle("Project");
		    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//System.out.println("Gui Started");
		    
		}
		
		void createMenu(){
			//loadImage.setEnabled(false);
	        newCase.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	CaseClass caseClass = new CaseClass();
	            	caseClass.newCase();
	            }
	        });
	        
	        openCase.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	//open reeeeeeeeeeee
	            }
	        });
	        
	        loadImage.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	LoadImage loadImage = new LoadImage();
	            	//loadImage.setVisible(true);
	            }
	        });
		        exitClose.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent event) {
		                System.exit(0);
		            }
		        });
		             
			file.add(newCase);
			file.add(openCase);
			file.add(loadImage);
			file.add(exitClose);
			menuBar.add(file);
			
			JMenu help = new JMenu("Help");
			JMenuItem about = new JMenuItem("about");
			help.add(about);
			menuBar.add(help);
			mainFrame.setJMenuBar(menuBar);
			

		}
		
		void createPanels(){
			mainFrame.setLayout(new BorderLayout());  
			mainFrame.add(leftPanel,BorderLayout.WEST);
			mainFrame.add(centerPanel,BorderLayout.CENTER);
			mainFrame.add(botPanel,BorderLayout.SOUTH);
			Dimension screenSize = mainFrame.getBounds().getSize();
			//leftPanel.add(vbar);
			leftPanel.setBackground(Color.WHITE);
			leftPanel.setPreferredSize(new Dimension((int) (screenSize.width*0.25),(int) (screenSize.height*0.75)));
			centerPanel.setBackground(Color.lightGray);
			centerPanel.setPreferredSize(new Dimension((int) (screenSize.width*0.75),(int) (screenSize.height*0.75)));
			botPanel.setBackground(Color.gray);
			botPanel.setPreferredSize(new Dimension(screenSize.width,(int) (screenSize.height*0.25)));
		}
		
		void createTreeView(){
			new TreeView("/home/");//Give the Path of LoadesImage
		}
}