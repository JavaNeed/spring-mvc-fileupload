package com.websystique.springmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.websystique.springmvc.model.User;
import com.websystique.springmvc.repository.UserRepository;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	public User findById(int id) {
		return userRepository.findById(id);
	}

	public User findBySSO(String ssoId) {
		User user = userRepository.findBySsoId(ssoId);
		return user;
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	/*
	 * Since the method is running with Transaction, No need to call hibernate
	 * update explicitly. Just fetch the entity from db and update it with
	 * proper values within transaction. It will be updated in db once
	 * transaction ends.
	 */
	public void updateUser(User user) {
		User entity = userRepository.findById(user.getId());
		if (entity != null) {
			entity.setSsoId(user.getSsoId());
			entity.setFirstName(user.getFirstName());
			entity.setLastName(user.getLastName());
			entity.setEmail(user.getEmail());
			entity.setUserDocuments(user.getUserDocuments());
		}
	}

	public void deleteUserBySSO(String ssoId) {
		User user = userRepository.findBySsoId(ssoId);
		if (user != null) {
			userRepository.delete(user);
		}
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public boolean isUserSSOUnique(Integer id, String sso) {
		User user = findBySSO(sso);
		return (user == null || ((id != null) && (user.getId() == id)));
	}
}
