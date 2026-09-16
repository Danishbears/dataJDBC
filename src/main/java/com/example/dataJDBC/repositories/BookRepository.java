package com.example.dataJDBC.repositories;


import com.example.dataJDBC.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book,String >,
        PagingAndSortingRepository<Book,String>
{

}
