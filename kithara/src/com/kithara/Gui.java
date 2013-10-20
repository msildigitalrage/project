package com.kithara;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Gui {
	/**
	 Graphical User Interface
	 * 
	 */
	JFrame mainFrame = new JFrame();
	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem newCase = new JMenuItem("New Case");
	public static JMenuItem loadImage = new JMenuItem("Load Image");
	final JMenuItem exitClose = new JMenuItem("Exit");
	
	public Gui() {
		createFrame();
		createMenu();
		
	}
		void createFrame(){
			
		    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    mainFrame.setBounds(0,0,screenSize.width, screenSize.height);
		    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		    mainFrame.setTitle("Project");
		    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			System.out.println("Gui Started");
		}
		
		void createMenu(){
			loadImage.setEnabled(false);
	        newCase.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	CaseClass caseClass = new CaseClass();
	            	caseClass.NewCase();
	            }
	        });
	        
	        loadImage.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	
	            }
	        });
		        exitClose.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent event) {
		                System.exit(0);
		            }
		        });
		        

		        
			file.add(newCase);
			file.add(loadImage);
			file.add(exitClose);
			menuBar.add(file);
			
			JMenu help = new JMenu("Help");
			JMenuItem about = new JMenuItem("about");
			help.add(about);
			menuBar.add(help);
			
			mainFrame.setJMenuBar(menuBar);
			

		}

}
