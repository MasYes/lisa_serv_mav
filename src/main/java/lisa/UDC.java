package lisa;

/**
 * Created with IntelliJ IDEA.
 * User: Юлиан
 * Date: 17.08.13
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class UDC {
	private String id;
	private String description;
	private String parent;
	private String children;
//	protected Vector vector; надо ли?



	private UDC(){
		id = "";
		description = "";
		parent = "";
		children = "";
	}

	protected UDC(String id, String descr, String par, String childr){
		this.id = id;
		this.description = descr;
		this.parent = par;
		this.children = childr;
	}




	public static String findCloseUDCTerm(Vector vect){
		HashSet<Code> codes = new HashSet<>();
		String[] main = new String[]{"0", "1", "2", "3", "5", "6", "7", "8", "9"};
		double sum2 = 0;
		for(String i : main){
			sum2 += vect.angle(SQLQuery.getUDCVector(i));
		}
		for(String i : main){
			if(vect.angle(SQLQuery.getUDCVector(i)) < sum2/9.0){
				String max = "";
				double value;
				UDC udc = SQLQuery.getUDC(i);
				LinkedHashSet<Integer> set = new LinkedHashSet<>();
				set.add(SQLQuery.getUDCTerms(i).crossingSize(vect, 2));
				while(!udc.children.equals("")){
					value = -1;
					for(String str : udc.children.split(";")){
						Vector terms = SQLQuery.getUDCTerms(str);
						double curr = 0;
						if(terms.size() > 0 && SQLQuery.getUDCCount(str) > 2){
							curr = 1.0*Math.log(vect.crossingSize(terms, 2)) / Math.log(terms.size());
						}
						if(curr >= value){
							if(curr > value || vect.angle(terms) > vect.angle(SQLQuery.getUDCTerms(max))){
								max = str;
								value = curr;
							}
						}
					}
					set.add(vect.crossingSize(SQLQuery.getUDCTerms(max), 2));
					udc = SQLQuery.getUDC(max);
				}
				codes.add(new Code(max, computeProbability(set)));
			}
		}
		String res = "";
		double aw_an = 0;
		double aw_pr = 0;
		for(Code c : codes){
			aw_an += vect.angle(SQLQuery.getUDCVector(c.udc));
			aw_pr += c.likelihood;
		}
		aw_an = aw_an/codes.size();
		aw_pr = aw_pr/9;
		for(Code c : codes)
			if(c.likelihood >=aw_pr && vect.angle(SQLQuery.getUDCVector(c.udc)) < aw_an)
				res += c.udc + "  " + SQLQuery.getUDC(c.udc).description + "<br>";

		return res;
	}

	protected static String findCloseUDC(Vector vect){
		return findCloseUDCTerm(vect);
	}

	private static double computeProbability(LinkedHashSet<Integer> set){
		double res = 0;
		int last = 0;
		for(Integer i : set){
			res += i;
		}
		return res/set.size();
	}


	private static class Code{
		String udc;
		double likelihood;
		protected Code(String str, double prob){
			udc = str;
			likelihood = prob;
		}
	}


}