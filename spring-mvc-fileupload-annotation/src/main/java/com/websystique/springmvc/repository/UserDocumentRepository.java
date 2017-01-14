package com.websystique.springmvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.websystique.springmvc.model.UserDocument;

public interface UserDocumentRepository extends JpaRepository<UserDocument, Integer>{
	UserDocument findById(Integer id);

	List<UserDocument> findAllByUserId(int id);
}
