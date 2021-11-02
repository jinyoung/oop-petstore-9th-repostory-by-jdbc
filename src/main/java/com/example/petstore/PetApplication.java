package com.example.petstore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import com.h2.examples.H2FileDatabaseExample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController("pets")
@EnableAspectJAutoProxy
public class PetApplication {

	static HashMap<String, Pet> pets = new HashMap<String, Pet>();

	public static void main(String[] args) {
		pets.put(Dog.class.getSimpleName().toLowerCase(), new Dog());
		pets.put(Cat.class.getSimpleName().toLowerCase(), new Cat());

		SpringApplication.run(PetApplication.class, args);
	}


	@RequestMapping(method = RequestMethod.GET, path="/")
	public String listPets(){
		final StringBuffer result = new StringBuffer();

		result.append("<h1> PET STORE </h1>");

		pets.values().forEach(pet -> {result.append("<li>"+pet);});

		result.append("<p> 총 페이지뷰:"+ HomeAdvice.getPageView());
		return result.toString();
	}

	@RequestMapping(method = RequestMethod.GET, path="{petId}")
	public String showPet(@PathVariable(value = "petId") String petId){
		StringBuffer result = new StringBuffer();
		Pet thePet = pets.get(petId);

		if(thePet==null) return null;

		result.append("<h1>"+petId+"</h1>");

		result.append("<br>에너지: " + thePet.getEnergy());

		result.append("<li> <a href='"+petId+"/feed'>먹이주기</a>");
		result.append("<li> <a href='"+petId+"/sleep'>재우기</a>");
		result.append("<li> <a href='"+petId+"/cart'>입양하기</a>");

		if(thePet instanceof Groomable)
			result.append("<li> <a href='"+petId+"/groom'>그루밍하기</a>");

		return result.toString();
	}

	@Autowired
	PetRepository cart;


	@RequestMapping(method = RequestMethod.GET, path="{petId}/cart")
	public String addToCart(@PathVariable(value = "petId") String petId) throws Exception{
		//StringBuffer result = new StringBuffer();
		Pet thePet = pets.get(petId);

		//// GOOD ////
		cart.save(thePet);  // Separation of Concerns. Dependency Inversion Principle. //Ubiqutous Language.

		/////  BAD ///////

		// why? - following code is technically specific, infra-related code. must be moved to Repository implementation and hidden.

		// Connection connection = H2FileDatabaseExample.getDBConnection();
        // Statement stmt = null;
		// StringBuffer petsInDB = new StringBuffer();
        // try {
        //     connection.setAutoCommit(false);
        //     stmt = connection.createStatement();
        //     //stmt.execute("CREATE TABLE PET(name varchar(255))");
        //     stmt.execute("INSERT INTO PET(name) VALUES('"+ thePet.getClass().getSimpleName()+"')");

        //     ResultSet rs = stmt.executeQuery("select * from PET");
        //     while (rs.next()) {
        //         petsInDB.append("<li> "+rs.getString("name"));
        //     }
        //     stmt.close();
        //     connection.commit();
        // } catch (SQLException e) {
        //     System.out.println("Exception Message " + e.getLocalizedMessage());
        // } catch (Exception e) {
        //     e.printStackTrace();
        // } finally {
        //     connection.close();
        // }

		List<Pet> result = cart.findAll();
		StringBuffer buffer = new StringBuffer();
		result.forEach(pet -> {
			buffer.append("<li>"+ pet.getClass().getSimpleName());
		});


		return "성공적으로 입양했습니다<br>" + buffer.toString();
	}

	@RequestMapping(method = RequestMethod.GET, path="{petId}/feed")
	public String feed(@PathVariable(value = "petId") String petId){
		StringBuffer result = new StringBuffer();
		Pet thePet = pets.get(petId);

		thePet.eat();

		return "맛있는 거 먹였습니다.";
	}


	@RequestMapping(method = RequestMethod.GET, path="{petId}/groom")
	public String groom(@PathVariable(value = "petId") String petId){
		Pet thePet = pets.get(petId);

		if(thePet instanceof Groomable){
			return ((Groomable)thePet).grooming();
		}


		return "그루밍이 불가능한 Pet 입니다";
	}
}
