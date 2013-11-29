package com.kithara;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class HashCalculate {
	public static String head = null;//rootOfMountedFiles.. example mnt_userdata.dd_John
	public static String caseName = null;//name of Folder Case
	public void calcMD5(){
		System.out.println(">"+head);
		System.out.println(">"+caseName);
		Process hashProcess;
		try {
			System.out.println("start");
			  FileWriter fw = new FileWriter(caseName+"/hashScript",false);
	   	      BufferedWriter bufferWritter = new BufferedWriter(fw);
	   	      bufferWritter.write("#!/bin/bash");
	   	      bufferWritter.write("\nsudo find "+head+" -type f -print0 | xargs -0 md5sum >> "+caseName+"/Hashes.md5");
	   	      bufferWritter.close();
			hashProcess= Runtime.getRuntime().exec("sh "+caseName+"/hashScript");
			hashProcess.waitFor();
			System.out.println("finished");	
		} catch (Exception e) {
			System.out.println("Hash Procedure Problem..");
		}

	}
}