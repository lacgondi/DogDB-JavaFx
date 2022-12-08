package hu.petrik.dogsdbfx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DogDB {
    Connection con;
    public static String DB_DRIVER = "mysql";
    public static String DB_HOST = "localhost";
    public static String DB_PORT = "3306";
    public static String DB_DBNAME = "java";
    public static String DB_USER="root";
    public static String DB_PASS = "";

    public DogDB() throws SQLException {
        String url = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_DBNAME);
        con = DriverManager.getConnection(url, DB_USER, DB_PASS);
    }

    public boolean createDog(Dog dog) throws SQLException {
        String sql = "INSERT INTO dogs(name, age, breed) VALUES (?,?,?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,dog.getName());
        stmt.setInt(2,dog.getAge());
        stmt.setString(3,dog.getBreed());
        return stmt.executeUpdate() > 0;
    }

    public List<Dog> readDogs(){

    }

    public void updateDog(){

    }

    public void deleteDog(){

    }
}
