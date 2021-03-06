package com.watermelon.manager.security.dao;

import java.util.List;

import org.easy.eao.annotations.Paging;
import org.easy.eao.annotations.Paging.Number;
import org.easy.eao.annotations.Paging.Size;
import org.easy.eao.annotations.Query;
import org.easy.eao.spring.Eao;
import org.easy.eao.support.Page;

import com.watermelon.manager.common.manager.common.dao.BaseDao;
import com.watermelon.manager.security.entity.User;

/**
 * 用户 DAO 接口
 * 
 * @author KKON
 */
@Eao
public interface UserDao extends BaseDao<User, String> {



	/**
	 * 以分页形式获取全部用户
	 * 
	 * @param page 页码
	 * @param size 页容量
	 * @return
	 */
	@Paging(query = @Query(query = "from User u order by u.createTime"), 
			count = @Query(query = "select count(u.name) from User u"))
	Page<User> page(@Number int page, @Size int size);
	
	/**
	 * 以分页形式获取全部用户
	 * @param company 公司序号
	 * @param page 页码
	 * @param size 页容量
	 * @return
	 */
	@Paging(query = @Query(query = "from User u where u.company=? order by u.createTime"), 
			count = @Query(query = "select count(u.name) from User u where u.company=? "))
	Page<User> page(String company, @Number int page, @Size int size);
	
	/**
	 * 获取全部用户
	 * 
	 * @return
	 */
	@Query(query = "from User u order by u.createTime")
	List<User> listAll();
	
	/**
	 * 以分页形式获取全部用户
	 * 
	 * @param page 页码
	 * @param size 页容量
	 * @return
	 */
	@Paging(query = @Query(query = "from User u where u.company = ? order by u.createTime "), 
			count = @Query(query = "select count(u.name) from User u where u.company = ? "))
	Page<User> pageCompanyUser(String company, @Number int page, @Size int size);
	
	/**
	 * 获取当前公司的全部用户
	 * 
	 * @return
	 */
	@Query(query = "from User u where u.company = ? order by u.createTime")
	List<User> listGompanyUser(String company);

}
