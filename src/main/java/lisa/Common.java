package lisa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: masyes
 * Date: 16.07.13
 * Time: 1:44
 * To change this template use File | Settings | File Templates.
 */
public class Common {
	private Common(){}
	protected final static int COUNT_THREADS = 1;

	public static void createLog(Exception e){
		System.out.println("LOG");
		String log = "";
		for(StackTraceElement elem : e.getStackTrace()){
			log += elem.toString() + "\n";
		}
		createLog(e.toString() + log);
	}

	public synchronized static void createLog(String str){
		try(FileWriter logs = new FileWriter("/home/java/log.txt", true)){
			//log.txt
			// /home/java/log.txt
			logs.write("[" + new Date() + "]\n" + str + "\n\n");
		} catch (IOException e){
			System.out.println("Всё очень плохо...");
		}
	}
}
