package lisa;

/**
 * Created with IntelliJ IDEA.
 * User: masyes
 * Date: 14.07.13
 * Time: 3:07
 * To change this template use File | Settings | File Templates.
 * в некоторых функциях можно ретернить не из самого результсет, а сначала сохранить результат в стринги,
 * а потом вызвать rs.close(), что несколько снижает затраты оперативной (вроде).
 * Засунуть все коннекты в "трай витх ресорс"
 *
 * CREATE TABLE lisa.articles(id int primary key auto_increment, author text, title text, vector long binary, UDC text, Template text, link text,
 mark int, language text, info text, publication text);
 *
 */

import java.io.*;
import java.sql.*;



public class SQLQuery {
	private static Connection conn;
	private static String user = "masyes";
	private static String password = "jYbtGf27";
	private static String url = "localhost";
	private static String port = "3306";
	private static String DB = "lisa";
	private static boolean connected = false;

	private static void connect() throws SQLException {

			conn = DriverManager.getConnection(
					"jdbc:mysql://" + url + ":" + port + "/" + DB,
					user, password);
			if (conn == null) {
				System.out.println("Нет соединения с БД!");
			}
			else connected = true;
	}

	protected static void disconnect() throws SQLException {
		//На самом деле я не хотел делать эту функцию, но
		//если иногда не закрывать соединение, то об]ём используемой
		//оперативки приводит к аутофмемори
		conn.close();
		connected = false;
	}

	private SQLQuery(){}


	private static Object deserialize(byte[] array) {
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(array);
			ObjectInputStream objInputStream = new ObjectInputStream(is);
			return objInputStream.readObject(); //Если класс каст эксепшн, то, скорее всего, класс был изменен, а посему восстановить его нет возможности.
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	protected static String getEnd(String word){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM words WHERE word=\'" + word + "\'");
			rs.next();
			return rs.getString("ends");
		} catch (SQLException e){
			//Common.createLog(e); и это забивает тоже ><
			return null;
		}
	}

	protected static String getEnd(int id){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ends WHERE id=" + id + ";");
			rs.next();
			return rs.getString("ends");
		} catch (SQLException e){
			//Common.createLog(e); Не загоняем в соммон крейт лог, поскольку они засоряют весь лог файл ><
			return "";
		}
	}

	public static Term getWordData(int i){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM lisa.dict WHERE id=" + i);
			rs.next();
			return  new Term(rs.getString("word"), rs.getInt("freq"), rs.getInt("units"), rs.getDouble("meas"));
		} catch (SQLException e){
			Common.createLog(e);
			return null;
		}
	}

	public static Vector getArticleVector(int id){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT vector FROM lisa.articles WHERE id=" + id);
			rs.next();
			return (Vector) deserialize(stringToArray(rs.getString("vector")));
		} catch (SQLException e){
			Common.createLog(e);
			return null;
		}
	}

	public static String getArticleTitle(int id){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT title FROM lisa.articles WHERE id=" + id);
			rs.next();
			return rs.getString("title");
		} catch (SQLException e){
			Common.createLog(e);
			return null;
		}
	}

	public static String getArticleInfo(int id){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT author, title, link FROM lisa.articles WHERE id=" + id);
			rs.next();
			return rs.getString("author") + ":<br><b>" + rs.getString("title") +
					"</b><br>" + rs.getString("link") + "<br><br>";
		} catch (SQLException e){
			Common.createLog(e);
			return null;
		}
	}

	public static int getIdWord(String word){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM lisa.dict WHERE word=\"" + word + "\"");

			rs.next();
			return  rs.getInt("id");
		} catch (SQLException e){
			return -1;
		}
	}


	protected static int getCountOfArticles(){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM lisa.articles");
			rs.next();
			return rs.getInt("MAX(id)");
		} catch (SQLException e){
			Common.createLog(e);
			return -1;
		}
	}


	private static byte[] stringToArray(String str){
		str = str.substring(1, str.length() - 1);
		byte[] res = new byte[str.length()/2];
		for(int i = 0; i < str.length(); i = i + 2){
			res[i/2] = (byte) (Integer.parseInt("" + str.charAt(i) + str.charAt(i + 1), 16) & 0xFF);
		}
		return res;
	}

	public static Vector getUDCVector(String code){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT vector FROM lisa.udc WHERE id=\'" + code + "\';");
			rs.next();
			return (Vector) deserialize(stringToArray(rs.getString("vector")));
		} catch (Exception e){
			return new Vector();
		}
	}

	public static Vector getUDCTerms(String code){
		try{
			if(connected)
				disconnect();
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT terms FROM lisa.udc WHERE id=\'" + code + "\';");
			rs.next();
			return (Vector) deserialize(stringToArray(rs.getString("terms")));
		} catch (Exception e){
			return new Vector();
		}
	}

	public static int getUDCCount(String code){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT count FROM lisa.udc WHERE id=\'" + code + "\';");
			rs.next();
			return rs.getInt("count");
		} catch (Exception e){
			return 0;
		}
	}

	public static UDC getUDC(String code){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM udc WHERE id=\'" + code + "\'");
			rs.next();
			return new UDC(rs.getString("id"), rs.getString("description"),
					rs.getString("parent"),rs.getString("children"));
		} catch (SQLException e){
			return null;
		}
	}

	protected static String getGroupArticles(int id){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT articles FROM lisa.groups WHERE id=\'" + id + "\';");
			rs.next();
			return rs.getString("articles");
		} catch (Exception e){
			return "";
		}
	}

	protected static Vector getGroupVector(int id){
		try{
			if(!connected)
				connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT vector FROM lisa.groups WHERE id=\'" + id + "\';");
			rs.next();
			return (Vector) deserialize(stringToArray(rs.getString("vector")));
		} catch (Exception e){
			return new Vector();
		}
	}
}