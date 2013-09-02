package lisa;

/**
 * Created with IntelliJ IDEA.
 * User: masyes
 * Date: 13.07.13
 * Time: 0:48
 * To change this template use File | Settings | File Templates.
 * Поскольку с определенной уверенностью можно утверждать, что
 * если угол между векторами a и b составляет n, то для любого
 * вектора c, величина |(c^a)-(c^b)|<=n. На основе этого утверждения
 * можно сильно увеличить производительность.
 *
 *
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class Vector extends HashMap<Integer, Double> { //Имхо, наследовать было рационально, ибо создавать класс с 1 полем и оперировать им - не круто.

	private double norm = 0.0;

	public double getNorm(){
		return norm;
	}


	private static final long serialVersionUID = -301930882514848718L; /* На самом деле, это уже лайфхак,
						но с этой штукой нет проблем, когда чуть измененный класс уже не подниамется из дампа.*/

	protected static Vector toVector(String[] str){
		Vector vector = new Vector();
		ArrayList<String> array = new ArrayList<>();
		array.addAll(Arrays.asList(str)); //еще не определился, как лучше - так, или в конатрукторе сразу привести к такому виду.
		HashSet<String> set = new HashSet<>(array);
		set.remove("");
		for(String i : set){
			vector.put(SQLQuery.getIdWord(i), (double)Collections.frequency(array, i));
		}
		vector.remove(-1);
		vector.normalize();
		return vector;
	}



	public Double at(Integer i){
		Double res = this.get(i);
		if(res != null)
			return res;
		return 0.0;
	}


	protected Integer[] nearest(int count){ // проблема в том, что возаращаемый массив не отсортирован
		Integer[] res = new Integer[count];
		double[] values = new double[count];
		for(int i = 0; i < count; i++){
			res[i] = 0;
			values[i] = 2;
		}
		for(int i = 1; i <= SQLQuery.getCountOfArticles(); i++){
			double curr = angle(SQLQuery.getArticleVector(i));
			int max = findMax(values);
			if(curr < values[max]){
				res[max] = i;
				values[max] = curr;
			}
		}
		return res;
	}

	private static int findMax(double[] array){
		int element = 0;
		double value = array[0];
		for(int i = 0; i < array.length; i++){
			if(array[i]>value){
				element = i;
				value = array[i];
			}
		}
		return element;
	}

	public double angle(Vector a){
		return angle(this, a);
	}

	private static double angle(Vector a, Vector b) { //I'm not sure about this speed. But this is only for tests.
		java.util.HashSet<Integer> set = new java.util.HashSet<Integer>();
		set.addAll(a.keySet());
		set.addAll(b.keySet());
		Double dotProduct = 0.0;
		for(Integer i : set){
			dotProduct += a.at(i)*b.at(i);
		}
		return Math.acos(dotProduct); //so, with /(Math.PI/2) this function return double in [0;1]
	}

	private void normalize(){ // С такой штукой при вычислении углов можно не делить на норму вектора
		norm = 0.0;
		for(Integer i : keySet()){
			norm+= Math.pow(get(i),2);
		}
		norm = Math.sqrt(norm);
		for(Integer i : keySet()){
			put(i, get(i)/norm);
		}
	}

	public double distanse(Vector a, String udc){
		Vector vect = SQLQuery.getUDCVector(udc);
		for(Integer key : SQLQuery.getUDCVector(udc).keySet())
			vect.put(key, vect.get(key)/SQLQuery.getUDCCount(udc));
		return distanse(this, a);
	}


	public static double distanse(Vector a, Vector b){
		double res = 0;
		java.util.HashSet<Integer> set = new java.util.HashSet<>(a.keySet());
		set.addAll(b.keySet());
		for(Integer i : set){
			res += Math.pow(a.at(i)*a.norm - b.at(i)*b.norm, 2);
		}
		return Math.sqrt(res);
	}

	public int crossingSize(Vector vect){
		return crossingSize(this, vect);
	}

	public int crossingSize(Vector vect, int num){
		return crossingSize(this, vect, num);
	}

	private static int crossingSize(Vector a, Vector b){
		HashSet<Integer> set = new HashSet<>(a.keySet());
		set.retainAll(b.keySet());
		return set.size();
	}

	private static int crossingSize(Vector a, Vector b, int num){
		int count = 0;
		for(Integer key : a.keySet()){
			if(b.keySet().contains(key) && a.getNorm()*a.get(key) >= num){
				count++;
			}
		}
		return count;
	}
}