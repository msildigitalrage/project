package com.kithara;

public class HashCalculate {
	public static String head = null;//rootOfMountedFiles.. example mnt_userdata.dd_John
	public static String caseName = null;
	public void calcMD5(){
		System.out.println(">"+head);
		System.out.println(">"+caseName);
		Process hashProcess;
		try {
			System.out.println("start");
			hashProcess= Runtime.getRuntime().exec("sudo find /mnt_userdata.dd_makos/data/ -type f -print0 | xargs -0 md5sum >> /home/iceman/Desktop/roberta.md5");
			hashProcess.waitFor();
			System.out.println("sudo find /mnt_userdata.dd_makos/data/ -type f -print0 | xargs -0 md5sum >> /home/iceman/Desktop/roberta.md5");
			System.out.println("stop");
			
		} catch (Exception e) {
			System.out.println("to poulo reeee");
		}

	}
}
