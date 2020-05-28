import org.w3c.dom.ls.LSInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Main {

    private static Connection conn = null;
    private static PreparedStatement statement = null;
    private static boolean end = false;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DB", "root", "...");
            while(!end) run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            safeClose(statement);
            safeClose(conn);
        }
    }

    private static void safeClose(AutoCloseable toClose) {
        if(toClose==null) return;
        try {
            toClose.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void run() {
        System.out.println("If you want to add only an author type 1 else 2 or end to end: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String read = null;
        try {
            read = reader.readLine();
            while(!read.equals("1") && !read.equals("2")) {
                if(read.equals("end")) {
                    end = true;
                    return;
                }
                System.out.println("1 or 2: ");
                read = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(read.equals("1")) askForAuthor(reader);
        else askForBoth(reader);
    }

    private static void askForBoth(BufferedReader reader) {
        System.out.println("Enter author: ");
        try {
            String author = reader.readLine();
            System.out.println("Enter book: ");
            String book = reader.readLine();
            String sql = "insert into books values (?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, book);
            statement.setString(2, author);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void askForAuthor(BufferedReader reader) {
        System.out.println("Enter author: ");
        try {
            String author = reader.readLine();
            String sql = "insert into books(author) values (?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, author);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
