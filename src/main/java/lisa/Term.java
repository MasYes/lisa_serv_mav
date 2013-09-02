package lisa;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: masyes
 * Date: 24.07.13
 * Time: 23:41
 * To change this template use File | Settings | File Templates.
 */
public class Term {

	private static BigInteger[][] snsk;
	private static int height = 1150; //1150
	private static int width = 200; //200
	private static boolean init = false;


	private String word;
	private int units;
	private int frequency;
	private double measure;

	protected void setWord(String str){
		word = str;
	}

	public String getWord(){
		return word;
	}

	protected int getUnits(){
		return units;
	}

	protected int getFrequency(){
		return frequency;
	}

	protected double getMeasure(){
		return measure;
	}

	protected void incrementFrequency(){
		frequency++;
	}

	protected void addToFrequency(int i){
		frequency += i;
	}

	protected void incrementUnits(){
		units++;
	}

	protected Term (String str, int freq){
		word = str;
		units = 1;
		frequency = freq;
		measure = 1;
	}

	protected Term(String str){
		word = str;
		units = 0;
		frequency = 0;
		measure = 1;
	}

	protected Term(){
		word = "";
		units = 0;
		frequency = 0;
		measure = 1;
	}

	protected Term(int freq){
		word = "";
		units = 1;
		frequency = freq;
		measure = 1;
	}

	protected Term (String str, int fr, int un, double ms){
		word = str;
		units =un;
		frequency = fr;
		measure = ms;
	}
}



