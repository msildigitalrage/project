package com.kithara;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Gui {
	
    public String case_name;
	public String where_log;
	public String file_name=null;
	public static JFrame mainFrame = new JFrame();
	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("File");
	public static JMenuItem newCase = new JMenuItem("New Case");
	public static JMenuItem openCase = new JMenuItem("Open Case");
	public static JMenuItem closeCase = new JMenuItem("Close Case");
	public static JMenuItem loadImage = new JMenuItem("Add Image (.dd)");
	public static JMenuItem unloadAnImage = new JMenuItem("Remove Image");
	final JMenuItem exitClose = new JMenuItem("Exit");
	public static JTabbedPane botPanel = new JTabbedPane();
	public static JMenu evidences = new JMenu("Evidences");
	public static JPanel leftPanel = new JPanel();
	public static JPanel centerPanel = new JPanel();
	public static JPanel topPanel = new JPanel();
	public static JPanel contacts = new JPanel();
	public static JPanel sms = new JPanel();
	public static JTextArea logText= new JTextArea();
	public static JMenuItem commonEvidences = new JMenuItem("Common Evidences");
	//pagination for files 
	public static JPanel paginationPanel = new JPanel();
	public static JPanel tmpPanel = new JPanel();
	//public static JLabel paginationLbl = new JLabel();
	//public static JButton nextPage = new JButton(">>");
	//public static JButton previousPage = new JButton("<<");
	public static int offset = 0;
   	public static JScrollPane scrollPaneLeft = new JScrollPane(leftPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    
   	public static JScrollPane scrollPaneCenter = new JScrollPane(centerPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
   /*	public static JScrollPane scrollPaneSouth = new JScrollPane(botPanel,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    */
   	
    public static JLabel detailsFileLbl = new JLabel();
	public static Font lblTop = new Font("Courier", Font.BOLD,10);
	public Gui() {
		createFrame();
		createPanels();
		createMenu();
		botPanel.addTab("contacts", contacts);
		botPanel.addTab("sms",sms);
		
	}
		void createFrame(){
		    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    mainFrame.setBounds(0,0,screenSize.width, screenSize.height);
		    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		    mainFrame.setTitle("Project");
		    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	    
		}
		
		void createMenu(){
			loadImage.setEnabled(false);
			unloadAnImage.setEnabled(false);
			closeCase.setEnabled(false);
	        newCase.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	CaseClass caseClass = new CaseClass();
	            	caseClass.newCase();
	            	where_log=caseClass.get_where_log();//kas
	            	case_name=caseClass.get_case_name();
	            }
	        });
	        
	        openCase.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	OpenCase openCase = new OpenCase();
	            	openCase.loadCase();
	            }
	        });
	        
	        closeCase.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	//Close the Case

	            	if(LoadImage.loadedImage==true){
		            	Umount mntClose = new Umount();
		            	mntClose.umount();
	            	}
	            	loadImage.setEnabled(false);
	            	closeCase.setEnabled(false);
	            	newCase.setEnabled(true);
	            	openCase.setEnabled(true);
	            	
	            }
	        });
	        
	        loadImage.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	LoadImage loadImage = new LoadImage();
	            	loadImage.set_where_log(where_log);
	            	loadImage.set_case_name(case_name);
	            	file_name=loadImage.get_file_name();
	            }
	        });
	        
	        unloadAnImage.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	Umount mnt = new Umount();
	            	mnt.umount();
	            }
	        });
		        exitClose.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent event) {
		                System.exit(0);
		            }
		        });
		        
		             
			file.add(newCase);
			file.add(openCase);
			file.add(closeCase);
			file.addSeparator();
			file.add(loadImage);
			file.add(unloadAnImage);
			file.addSeparator();
			file.add(exitClose);
			menuBar.add(file);
			
			JMenu help = new JMenu("Help");
			JMenuItem about = new JMenuItem("about");
			help.add(about);
			menuBar.add(help);
			
			//common Evidences
			commonEvidences.setEnabled(false);
			evidences.add(commonEvidences);
			menuBar.add(evidences);
			
			
			commonEvidences.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {		
	            	final CommonData cd = new CommonData();
	            	SwingUtilities.invokeLater(new Runnable() {
	  	              public void run() {
	  	            	cd.getContacts();
	  	            	cd.getSms();
	  	              }
	  	            });
	            	
	            	
	            }
	        });
	        
			
			mainFrame.setJMenuBar(menuBar);
			
		}
		
		void createPanels(){
			Dimension screenSize = mainFrame.getBounds().getSize();
			//borders - colors
			mainFrame.setLayout(new BorderLayout());  		
			leftPanel.setBorder(BorderFactory.createLineBorder(Color.red));
			centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			botPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
			botPanel.setBackground(Color.WHITE);
			leftPanel.setBackground(Color.WHITE);
			centerPanel.setBackground(Color.WHITE);
		
			// ----------------------sizes panel----------------------
			scrollPaneLeft.setPreferredSize(new Dimension(screenSize.width*2/8, screenSize.height*3/4));
	        scrollPaneCenter.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height*3/4));
	        topPanel.setPreferredSize(new Dimension(screenSize.width, 40));

			botPanel.setPreferredSize(new Dimension(screenSize.width, (screenSize.height/4 +20)));
			
			// -------------Add top Module Information File Clicked---------------------------
			topPanel.setLayout(new BorderLayout());
			topPanel.setBackground(Color.WHITE);
			topPanel.setBorder(BorderFactory.createLineBorder(Color.red));	
			topPanel.add(detailsFileLbl,BorderLayout.WEST);
			detailsFileLbl.setFont(lblTop);
			detailsFileLbl.setForeground(Color.BLACK);
			
			//Add pagination Buttons
			paginationPanel.setBackground(Color.WHITE);
			/*
				previousPage.setForeground(Color.ORANGE);
				nextPage.setForeground(Color.ORANGE);
				previousPage.setBackground(Color.BLACK);
				nextPage.setBackground(Color.BLACK);
				paginationPanel.add(previousPage);
				paginationPanel.add(paginationLbl);
				paginationPanel.add(nextPage);		
				*/
			paginationPanel.add(FileInvestigate.linesLabel);
			topPanel.add(paginationPanel,BorderLayout.EAST);
			
	        // ------------Add Panels to mainFrame-------------------------
	        mainFrame.setLayout(new BorderLayout());
	        //mainFrame.getContentPane().add(topPanel,BorderLayout.NORTH);
	        mainFrame.getContentPane().add(scrollPaneLeft,BorderLayout.WEST);
	        mainFrame.getContentPane().add(scrollPaneCenter,BorderLayout.CENTER);
	        mainFrame.getContentPane().add(topPanel,BorderLayout.NORTH);
	        mainFrame.getContentPane().add(botPanel,BorderLayout.SOUTH);
	        mainFrame.pack();			
		}
		
		 
		
}