package com.watermelon.manager.security.service;

import java.util.List;
import java.util.Set;

import org.easy.eao.support.Page;

import com.watermelon.manager.security.entity.Authority;
import com.watermelon.manager.security.entity.Group;
import com.watermelon.manager.security.entity.User;

public interface LoginService {

	User getUser(String userName);

	List<User> listUser();

	void createUser(String name, String trueName, String password, String company, Set<Integer> groups);

	void updateUserExtra(String name, Set<Integer> operatorIds);

	/**
	 * 
	 * @param user
	 *            password 传明文
	 */
	boolean updUserPassword(String userName, String oldPassword, String newPassword);

	void updUserEnabled(User user);

	void delUser(String userName);

	List<Authority> listAuthorityByUserName(String userName);

	List<Group> listGroupByCreater(String userName);

	/**
	 * 分页权限
	 */
	Page<Authority> pageAuth(int page, int size);

	Authority getAuth(String name);

	void createAuth(String name, String displayName, String description, int number);

	void delAuth(Authority auth);

	void updAuth(Authority auth);

	/**
	 * 组列表
	 */
	List<Group> listAllGroup();

	List<Group> listGroup(User user);

	Group getGroup(int groupId);

	void createGroup(String groupName, String description, String creater, Set<String> groupAuth,
			Set<String> groupUser);

	void updGroup(int groupId, String groupName, String description, Set<String> groupAuth, Set<String> groupUser);

	void delGroup(int groupId);

	void updateUserAuth(String name, String trueName, Set<Integer> groups);

	/**
	 * 获取全部权限许可的列表
	 * 
	 * @return
	 */
	List<Authority> listAuthorities();
}
