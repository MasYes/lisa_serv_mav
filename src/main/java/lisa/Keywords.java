/**
 * Created with IntelliJ IDEA.
 * User: masyes
 * Date: 12.07.13
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 * Распоточить по возможности.
 *
 * Внимание! Расчет мер нужно производить ПОСЛЕ добавления коллекции статей, ибо
 * используется количество статей коллекции.
 * Проверить ключевые слова, ибо результаты меня не впечатляют 0о Мб просто маленькая коллекция.
 * PS Пока сделано обязательным условием чтобы быть ключевым - чтобы freq/units>2 && units < countOfArticles*0.1;
 * В общем, философия метода такова, что все найденные слова несут какой-то "смысл". В этом плане
 * он верен. Второй шаг - найти "ключевые" слова из всех, что несут какой-то "смысл".
 * А еще надо попинать леммер, ибо бесит ><
 *
 * А вообще, в будущем надо использовать этот метод не для поиска ключевых, а для выделения "значимых слов", что заметно
 * уменьшит словарь => все будет работать быстрее)
 *
 *term.frequency/term.units>2 && term.units < count*0.1 && term.frequency < height && term.units < width
 */
package lisa;

public class Keywords{ //implements Runnable {

	private Keywords(){}

	protected static String[] getKeywords(Vector vect) {
		String res = "";
		double sum = 1;
		double max = 0;
		for(Integer i : vect.keySet()){
			if(vect.get(i)*vect.getNorm() > max)
				max = vect.get(i)*vect.getNorm()*1.0;
			sum += vect.get(i)*vect.getNorm();
		}
		sum = sum / (vect.keySet().size()*1.0);
		for(Integer i : vect.keySet())
			if(vect.get(i)*vect.getNorm() > sum+(max-sum)*0.2 && SQLQuery.getWordData(i).getFrequency() < 13000)
				res += SQLQuery.getWordData(i).getWord() + ";";
		return res.split(";");
	}
}
