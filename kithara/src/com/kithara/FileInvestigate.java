package com.kithara;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
	
	public class FileInvestigate {
		String extension;
		public static JTextArea textViewer = new JTextArea();
		public static JLabel picLabel = new JLabel();
		
		//counters for Text View
		public static int counterLine= 0;
		public static int sumLines = 0;
		public static JLabel linesLabel = new JLabel();

		public FileInvestigate(String file){
			checkExtension(file);
			//textViewer.setEditable(false);
			linesLabel.setFont(Gui.lblTop);
		}
	
		private void checkExtension(String file) {
			emptyAreaViewer();
			File f = new File(file);
			if(f.isDirectory()){
				 //System.out.println("Directory, do nothing");
			}else{
				//System.out.println("This is a File");
				extension = file.substring(file.lastIndexOf(".") + 1, file.length());
				//System.out.println(extension);
				
				switch (extension) {
				//Database Files
				case "db":
					System.out.println("db");				
					break;
					//Text files
				case "txt":
					System.out.println("txt");
					readFile(file);
					break;
				case "dat":
					System.out.println("dat");	
					readFile(file);
					break;
				case "list":
					System.out.println("list");	
					readFile(file);
					break;	
				case "xml":
					System.out.println("xml");	
					readFile(file);
					break;	
				case ".layout_version":
					System.out.println("layout_version");	
					readFile(file);
					break;	
					//Images	
				case "jpeg":
					System.out.println("jpeg");	
					viewImage(file);
					break;
				case "jpg":
					System.out.println("jpeg");		
					viewImage(file);
					break;
				case "png":
					System.out.println("png");	
					viewImage(file);
					break;
				case "bin":
					System.out.println("bin");	
					//do nothing
					break;
				case "wallpaper":
					System.out.println("wallpaper");	
					//do nothing
					break;
					//Unknown
				default:
					System.out.println("Unknown Type - Try to Open It");
					readFile(file);
					break;
				}
			}
			
		}
		
		private void viewImage(String file) {
			try {
				BufferedImage ImageFile = ImageIO.read(new File(file));
				picLabel = new JLabel(new ImageIcon(ImageFile));
				Gui.centerPanel.add(picLabel);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		public static void readFile(String file){
			
			BufferedReader in = null;
			String line = null;
			
			//----------------calc lines-----------------------
			try {
				in = new BufferedReader(new FileReader(file));
				line = in.readLine();
				while(line != null){
					counterLine++;
				    line = in.readLine();
				    //System.out.println("Line: " + counterLine);
				}
			}catch (IOException e) {
				e.printStackTrace();		
			}
			sumLines = counterLine;
			counterLine = 0;
			//System.out.println("Sum Lines of File: " + sumLines);
			linesLabel.setText("Lines of File: "+sumLines);
			

			//if big file, do not open it
			if(sumLines > 4000){ //limit
				System.out.println("Too Large File, pls open it from your File System");
				linesLabel.setText("Large File, try another program to view Contents");
				
				
			}else{
			//View Contents of File
				try {
					in = new BufferedReader(new FileReader(file));
					line = in.readLine();
					while(line != null){
						counterLine++;
					    textViewer.append(line + "\n");
					    line = in.readLine();
					}
				}catch (IOException e) {
					e.printStackTrace();		
				}
				
			}
			Gui.centerPanel.add(textViewer);

			
		}
	

		private void emptyAreaViewer(){
			Gui.centerPanel.remove(textViewer);
			Gui.centerPanel.remove(picLabel);
			sumLines = 0;
			counterLine = 0;
			textViewer.setText(null);
			picLabel.setText(null);
			linesLabel.setText(null);
			
		}
		    
		
	}
