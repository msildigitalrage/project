package com.kithara;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Gui extends JFrame {
	/**
	 Graphical User Interface
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Gui() {
		createFrame();
		createMenu();
		
	}
		void createFrame(){
		    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    setBounds(0,0,screenSize.width, screenSize.height);
		    setExtendedState(JFrame.MAXIMIZED_BOTH);
			setTitle("Project");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
			System.out.println("Gui Started");
		}
		
		void createMenu(){
			
			JMenuBar menuBar = new JMenuBar();
			
			JMenu file = new JMenu("File");
			JMenuItem newCase = new JMenuItem("New Case");
			JMenuItem exitClose = new JMenuItem("Exit");
			
		        exitClose.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent event) {
		                System.exit(0);
		            }
		        });
		        
			file.add(newCase);
			file.add(exitClose);
			menuBar.add(file);
			
			JMenu help = new JMenu("Help");
			JMenuItem about = new JMenuItem("about");
			help.add(about);
			menuBar.add(help);
			
			setJMenuBar(menuBar);
			

		}
}
