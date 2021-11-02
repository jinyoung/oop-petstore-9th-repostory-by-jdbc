package com.example.petstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.h2.examples.H2FileDatabaseExample;

import org.springframework.stereotype.Component;

@Component
public class DBCart implements PetRepository{



    @Override
    public Pet save(Pet thePet) throws Exception {
        Connection connection = H2FileDatabaseExample.getDBConnection();
        Statement stmt = null;
		StringBuffer petsInDB = new StringBuffer();
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            //stmt.execute("CREATE TABLE PET(name varchar(255))");
            stmt.execute("INSERT INTO PET(name) VALUES('"+ thePet.getClass().getSimpleName()+"')");

            ResultSet rs = stmt.executeQuery("select * from PET");
            while (rs.next()) {
                petsInDB.append("<li> "+rs.getString("name"));
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();

            return thePet;
        }

    }

    @Override
    public Pet delete(Pet pet) throws Exception {
        // pets.remove(pet);

        return pet;
    }

    @Override
    public List<Pet> findAll() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("입양목록: <br>");

        List<Pet> result = new ArrayList<Pet>();

        Connection connection = H2FileDatabaseExample.getDBConnection();
        Statement stmt = null;
        
        try {
            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select * from PET");
            while (rs.next()) {
                
                buffer.append("<li> "+rs.getString("name"));
                Pet thePet = (Pet) Class.forName("com.example.petstore." + rs.getString("name")).newInstance();
                result.add(thePet);
            }
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

//        pets.stream().forEach(pet -> buffer.append("<li>"+pet.toString()));

        return result;
    }

    @Override
    public Pet update(Pet pet) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    

}
