package com.watermelon.manager.security.vo;

import java.util.Set;

public class UserAuthForm {
	
	private String name;
	private String trueName;
	private Set<Integer> groups;
	
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
	public Set<Integer> getGroups() {
		return groups;
	}
	public void setGroups(Set<Integer> groups) {
		this.groups = groups;
	}
	
	
}
