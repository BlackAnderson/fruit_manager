package com.watermelon.manager.security.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * 权限许可
 * 
 * @author KKON
 */
@Entity
@Table(name = "sec_authorities")
public class Authority implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5687227073895191832L;
	@Id
	@NotEmpty(message = "关键字不能为空")
	@Size(min = 4, max = 40, message = "关键字长度必须在4-40之间")
	@Pattern(regexp = "[A-Z_0-9]*", message = "关键字只允许大写字母、下划线和数字")
	private String name;
	@NotEmpty(message = "显示名不能为空")
	@Size(min = 2, max = 30, message = "显示名长度必须在2-30之间")
	private String displayName;
	
	/**
	 * 排序号
	 * 排序规则(权限分三个级别显示)
	 * 在左则菜单中大菜单一级，小菜单二级，菜单栏之外三级
	 * 每一级占两位数,每级最小值1，例如一级菜单 1 00 00, 二级菜单1 01 00, 三级菜单1 01 01
	 * 如果一级菜单与二级菜单重叠则取一级菜单，
	 * 如果二级菜单与三级菜单重叠则取三级菜单，
	 */
	private int number = 0;
	
	private String description;

	// Getter and Setter ...

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GrantedAuthority toGrantedAuthority() {
		if (name == null)
			return null;
		return new SimpleGrantedAuthority(name);
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
		Authority other = (Authority) obj;
		if (name.equals(other.name))
			return true;
		return false;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * 是否是正确定的排序号
	 * @param number
	 * @return
	 */
	public static boolean isRightNumber(int number){
		if(number > 999999)
			return false;
		if(number < 10000)
			return false;
		
		int first = number / 10000;
		int tmp = number - first * 10000;
		
		int second = tmp / 100;
		tmp = tmp - second * 100;
		
		int third = tmp;
		
		System.out.println(number + ":" + first + "_" + second + "_" + third);
		
		//是否每一级都大于10
		//如果是一级菜单
		if(number % 10000 == 0)
			return first >= 1;
		
		//如果是二级菜单
		if(number % 100 == 0){
			return first >= 1 && second >= 1;
		}
		
		
		//如果是三级菜单
		return first >= 1 && second >= 1 && third >= 1;
	}
}
