package lisa;

/**
 * Created with IntelliJ IDEA.
 * User: Юлиан
 * Date: 17.08.13
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.util.Scanner;

public class Lemmer {

	private static String mystem = "/home/mystem/mystem30";
	private static String path_in = "/home/mystem/example.txt";
	private static String path_out = "/home/mystem/example_out.txt";
/*
	private static String mystem = "A:\\mystem.exe";
	private static String path_in = "A:\\example.txt";
	private static String path_out = "A:\\example_out.txt";
*/
	public static String[] lemmer (String str){ // Возможно, не лучшее решение, но лучшего я еще не придумал
		File file = new File(path_in);
		try(FileWriter wr = new FileWriter(file)){
			wr.write(str);
			String command = mystem + " -w -e utf-8 -l -n " + path_in + " " + path_out;
			wr.close();
			java.lang.Runtime.getRuntime().exec(command).waitFor();
			Scanner in = new Scanner(new File(path_out));
			String res = "";
			String curr;
			while(in.hasNext()){
				curr = in.nextLine();
				if(!curr.contains("??")){
					if(curr.contains("|"))
						curr = curr.substring(0, curr.indexOf("|"));
					res += curr + ";";
				}
				else
					res += curr.substring(0, curr.length() - 2) + ";";
			}
			return res.split(";");
		} catch (Exception e){
			e.printStackTrace();
			return new String[0];
		}
	}
}
