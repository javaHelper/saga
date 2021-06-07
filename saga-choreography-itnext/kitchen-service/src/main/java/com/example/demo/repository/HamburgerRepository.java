package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Hamburger;
import com.example.demo.enums.HamburgerType;

@Repository
public interface HamburgerRepository extends MongoRepository<Hamburger, String> {
	List<Hamburger> findByHamburgerType(HamburgerType hamburgerType);
}