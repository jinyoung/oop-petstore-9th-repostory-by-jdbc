package com.example.petstore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

//@Component
public class Cart implements PetRepository{

    List<Pet> pets = new ArrayList<Pet>();

    public Cart(){
    }

    @Override
    public Pet save(Pet pet) throws Exception {
        pets.add(pet);

        return pet;
    }

    @Override
    public Pet delete(Pet pet) throws Exception {
        pets.remove(pet);

        return pet;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("입양목록: <br>");
        pets.stream().forEach(pet -> buffer.append("<li>"+pet.toString()));

        return buffer.toString();
    }

    @Override
    public List<Pet> findAll() throws Exception {
        // TODO Auto-generated method stub
        return pets;
    }

    @Override
    public Pet update(Pet pet) throws Exception {


        // TODO Auto-generated method stub
        return null;
    }

    

}
