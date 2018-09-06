package com.watermelon.manager.security.service;

import java.util.List;
import java.util.Set;

import org.easy.eao.support.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.watermelon.manager.security.dao.UserDao;
import com.watermelon.manager.security.entity.Authority;
import com.watermelon.manager.security.entity.Group;
import com.watermelon.manager.security.entity.User;

@Service
public class LoginServiceImpl implements LoginService{
	@Autowired
	private UserDao userDao;
	
	@Override
	public User getUser(String userName) {
		return userDao.get(userName);
	}

	@Override
	public List<User> listUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createUser(String name, String trueName, String password, String company, Set<Integer> groups) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserExtra(String name, Set<Integer> operatorIds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updUserPassword(String userName, String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updUserEnabled(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delUser(String userName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Authority> listAuthorityByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> listGroupByCreater(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Authority> pageAuth(int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authority getAuth(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createAuth(String name, String displayName, String description, int number) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delAuth(Authority auth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updAuth(Authority auth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Group> listAllGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> listGroup(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group getGroup(int groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createGroup(String groupName, String description, String creater, Set<String> groupAuth,
			Set<String> groupUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updGroup(int groupId, String groupName, String description, Set<String> groupAuth,
			Set<String> groupUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delGroup(int groupId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserAuth(String name, String trueName, Set<Integer> groups) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Authority> listAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

}
