import lisa.*;
import java.util.HashSet;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: masyes
 * Date: 13.07.13
 * Time: 1:05
 * To change this template use File | Settings | File Templates.
 * \\p{Punct}\n]
 *
 * В целом, как я и говорил, вся проблема с парсером... Углы очень большие.
 * Да, между похожими они меньше, но всё равно... И с леммером надо разобраться.
 * Но так, если в целом, то всё даже работает....
 */
public class Main {
	public static void main(String[] args){
		args = new String[]{"A:\\example4.pdf"};
		String res;
		Common.createLog(args[0] + "   обработка статьи\n");
		long time = System.currentTimeMillis();
		try{
			Article art = new Article(args[0]);
		//	System.out.println(System.currentTimeMillis() - time);
			res = "Ключевые слова для данной статьи:<br><b>";
			for(String i : art.keywords()){
				if(i==null) break;
					res+=i+"<br>";
			}
		//	System.out.println(System.currentTimeMillis() - time);
			res+="</b>УДК:<br>" + art.findUDC();
		//	System.out.println(System.currentTimeMillis() - time);
			res += "</b><br><br><br>Похожие статьи:<br>";
			for(Integer i : art.findClose()){
				if(i==null) break;
				res+=SQLQuery.getArticleInfo(i)+"<br>";
			}
		//	System.out.println(System.currentTimeMillis() - time);
			Common.createLog(args[0] + "   обработано");
		} catch (UnsupportedFormatException e){
			res = "К сожалению, данный формат файлов не поддерживается.";
			Common.createLog(args[0] + "   проблемы формата");
		} catch (LargeFileException e){
			res = "Ваш файл слишком большой. Попробуйте выбрать файл поменьше.";
			Common.createLog(args[0] + "   огромен");
		} catch (Exception e){
			Common.createLog(e);
			res = "Во время обработки статьи произошла непредвиденная ошибка. Приносим свои извинения.";
		}
		res+="<br><br>Время обработки: " + (System.currentTimeMillis() - time)/1000.0;
		System.out.println(res);
	}
}

//Менять!
//Article art = new Article(args[0]);
///home/mystem/mystem30 -w -e utf-8 -l -n /home/mystem/example.txt /home/mystem/example_out.txt
//Файл логов