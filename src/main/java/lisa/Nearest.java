package lisa;

/**
 * Created with IntelliJ IDEA.
 * User: Юлиан
 * Date: 24.08.13
 * Time: 18:32
 * To change this template use File | Settings | File Templates.
 */
public class Nearest {
	private static int count = 200;
	private static double closeness = 1.0;
	protected static Integer[] findClose(Vector vect){
		Integer[] res = new Integer[100];
		int min = 0;
		double value = 1.6;
		double curr;
		for(int i = 0; i < count; i++){
			curr = vect.angle(SQLQuery.getGroupVector(i));
			if(curr < value){
				min = i;
				value = curr;
			}
		}
		int count = 0;
		for(String i : SQLQuery.getGroupArticles(min).split(";")){
			if(!i.equals(""))
				if(vect.angle(SQLQuery.getArticleVector(Integer.parseInt(i))) < closeness){
					res[count++] = Integer.parseInt(i);
				}
		}
		return res;
	}
}
