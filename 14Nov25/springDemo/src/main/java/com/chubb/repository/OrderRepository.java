package com.chubb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chubb.request.Order1;




@Repository

public interface OrderRepository extends JpaRepository<Order1, Integer>  //help us save data in db
//public interface OrderRepository extends CrudRepository<Order1, Integer>  //help us save data in db
{   //CrudRepository has a lot of methods for db interactions

}
