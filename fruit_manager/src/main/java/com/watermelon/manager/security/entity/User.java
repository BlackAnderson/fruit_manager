package com.watermelon.manager.security.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 登录用户
 * 
 * @author CHU
 * 
 * 2018年6月12日
 */
@Entity
@Table(name = "user")
public class User implements Serializable{
	
	private static final long serialVersionUID = -4846956706619420424L;
	
	/** 用户名 */
	@Id
	private String name;
	/** 密码 */
	private String password;
	/** 真名 */
	private String trueName;
	/** 创建时间 */
	private Date createTime;
	private boolean enabled;
	
	public User() {}
	public User(String name, String password, String trueName, Date createTime, boolean enabled) {
		super();
		this.name = name;
		this.password = password;
		this.trueName = trueName;
		this.createTime = createTime;
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name.equals(other.name) && password.equals(other.password))
			return true;
		return false;
	}
	
}
