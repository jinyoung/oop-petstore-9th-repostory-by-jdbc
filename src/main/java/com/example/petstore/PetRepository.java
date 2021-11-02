package com.example.petstore;

import java.util.List;

public interface PetRepository {    // Repository Pattern Interface
    Pet save(Pet pet) throws Exception;   // CREATE
    Pet delete(Pet pet) throws Exception;   // DELETE
    List<Pet> findAll() throws Exception; // READ
    Pet update(Pet pet) throws Exception; // UPDATE
}
