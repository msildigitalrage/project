package com.kithara;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
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
    buffer.append(" >> " + file.getPath() + "\n");
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