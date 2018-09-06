package com.watermelon.manager.security.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 用户组
 * 
 * @author KKON
 */
@Entity
@Table(name = "sec_group")
public class Group implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8429947508126479610L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//组id
	private String name;//组名字
	private String description;//描述
	private String creater;//组创建者
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sec_group_allow_authority", joinColumns = @JoinColumn(name = "group_id"),
			inverseJoinColumns = @JoinColumn(name = "authority"))
	private Set<Authority> allow = new HashSet<Authority>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sec_group_users", joinColumns = @JoinColumn(name = "group_id"),
			inverseJoinColumns = @JoinColumn(name = "user"))
	private Set<User> users = new HashSet<User>();

	/**
	 * 添加用户
	 * 
	 * @see Set#add(Object)
	 * @param user
	 * @return
	 */
	public boolean addUser(User user) {
		if (user != null) {
			return getUsers().add(user);
		}
		return false;
	}

	/**
	 * 移除用户
	 * 
	 * @see Set#remove(Object)
	 * @param user
	 * @return
	 */
	public boolean removeUser(User user) {
		if (user != null) {
			return getUsers().remove(user);
		}
		return false;
	}

	/**
	 * 添加许可
	 * 
	 * @see Set#add(Object)
	 * @param authority
	 * @return
	 */
	public boolean addAllow(Authority authority) {
		if (authority != null) {
			return getAllow().add(authority);
		}
		return false;
	}

	/**
	 * 移除许可
	 * 
	 * @see Set#add(Object)
	 * @param authority
	 * @return
	 */
	public boolean removeAllow(Authority authority) {
		if (authority != null) {
			return getAllow().remove(authority);
		}
		return false;
	}

	// Getter and Setter ...

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Authority> getAllow() {
		return allow;
	}

	public void setAllow(Set<Authority> allow) {
		this.allow = allow;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}
	
	

}
