package com.auda;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
 
public class TreeView {
	public static JTree fileTree;
	private FileSystemModel fileSystemModel;

  public TreeView(String directory) {
    fileSystemModel = new FileSystemModel(new File(directory));
    fileTree = new JTree(fileSystemModel);
    fileTree.setEditable(false);//was true
    fileTree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent event) {
        File file = (File) fileTree.getLastSelectedPathComponent();
        	Gui.detailsFileLbl.setText(getFileDetails(file));
        	FileInvestigate fileInvestigate = new FileInvestigate(getFilePath(file));
        	
      }
    });
    Gui.leftPanel.add(fileTree);
    Gui.leftPanel.updateUI();
    Gui.mainFrame.validate();
    Gui.mainFrame.repaint();
  }
  
  public String getFileDetails(File file) {
    if (file == null)
      return "";
    StringBuffer buffer = new StringBuffer();
    buffer.append("Mount on: " + file.getPath());
	String lineMD5;//MD5 of file
	Process p1;//use this for MD5sum
	try{
		if(file.length()!=4096){
		p1= Runtime.getRuntime().exec( "md5sum "+file.getPath());
		p1.waitFor();
	       BufferedReader in = new BufferedReader(
	               new InputStreamReader(p1.getInputStream()) );
	       while ((lineMD5 = in.readLine()) != null) {//just hold only the hash value
	    	   if(lineMD5.contains(" ")){
	    		   lineMD5= lineMD5.substring(0, lineMD5.indexOf(" ")); 
	    		}
	         //System.out.println(lineMD5);
	buffer.append(" | Md5: " + lineMD5);
	       }
	       in.close();
		}else{
			//Do not Calculate HASH of a folder or it's contents
		}
	  }
	  	catch(IOException | InterruptedException e1){
	  }
    return buffer.toString();
  }
  
  public String getFilePath(File file) {
	    if (file == null)
	      return "";
	    StringBuffer buffer = new StringBuffer();
	    buffer.append(file.getPath());
	    return buffer.toString();
	  }

}
 
class FileSystemModel implements TreeModel {
  private File root;
 
  private Vector listeners = new Vector();
 
  public FileSystemModel(File rootDirectory) {
    root = rootDirectory;
  }
 
  public Object getRoot() {
    return root;
  }
 
  public Object getChild(Object parent, int index) {
    File directory = (File) parent;
    String[] children = directory.list();
    return new TreeFile(directory, children[index]);
  }
 
  public int getChildCount(Object parent) {
    File file = (File) parent;
    if (file.isDirectory()) {
      String[] fileList = file.list();
      if (fileList != null)
        return file.list().length;
    }
    return 0;
  }
 
  public boolean isLeaf(Object node) {
    File file = (File) node;
    return file.isFile();
  }
 
  public int getIndexOfChild(Object parent, Object child) {
    File directory = (File) parent;
    File file = (File) child;
    String[] children = directory.list();
    for (int i = 0; i < children.length; i++) {
      if (file.getName().equals(children[i])) {
        return i;
      }
    }
    return -1;
 
  }
 
  public void valueForPathChanged(TreePath path, Object value) {
    File oldFile = (File) path.getLastPathComponent();
    String fileParentPath = oldFile.getParent();
    String newFileName = (String) value;
    File targetFile = new File(fileParentPath, newFileName);
    oldFile.renameTo(targetFile);
    File parent = new File(fileParentPath);
    int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
    Object[] changedChildren = { targetFile };
    fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices, changedChildren);
 
  }
 
  private void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children) {
    TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
    Iterator iterator = listeners.iterator();
    TreeModelListener listener = null;
    while (iterator.hasNext()) {
      listener = (TreeModelListener) iterator.next();
      listener.treeNodesChanged(event);
    }
  }
 
  public void addTreeModelListener(TreeModelListener listener) {
    listeners.add(listener);
  }
 
  public void removeTreeModelListener(TreeModelListener listener) {
    listeners.remove(listener);
  }
 
  private class TreeFile extends File {
    public TreeFile(File parent, String child) {
      super(parent, child);
    }
 
    public String toString() {
      return getName();
    }
  }
}