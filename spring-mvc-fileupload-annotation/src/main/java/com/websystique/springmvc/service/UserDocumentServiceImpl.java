package com.websystique.springmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.websystique.springmvc.model.UserDocument;
import com.websystique.springmvc.repository.UserDocumentRepository;

@Service("userDocumentService")
@Transactional
public class UserDocumentServiceImpl implements UserDocumentService{

	@Autowired
	private UserDocumentRepository documentRepository;

	public UserDocument findById(int id) {
		return documentRepository.findById(id);
	}

	public List<UserDocument> findAll() {
		return documentRepository.findAll();
	}

	public List<UserDocument> findAllByUserId(int userId) {
		return documentRepository.findAllByUserId(userId);
	}
	
	public void saveDocument(UserDocument document){
		documentRepository.save(document);
	}

	public void deleteById(int id){
		UserDocument userDocument = documentRepository.findById(id);
		if(userDocument != null){
			documentRepository.delete(userDocument);
		}
	}
}
