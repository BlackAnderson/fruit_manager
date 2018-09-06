package com.watermelon.manager.common.manager.common.dao;

import java.io.Serializable;
import org.easy.eao.annotations.Merge;
import org.easy.eao.annotations.Persist;
import org.easy.eao.annotations.Remove;
import org.easy.eao.annotations.Retrieve;

public interface BaseDao<T, PK extends Serializable> {
	@Persist
	void create(T... arg0);

	@Remove
	void remove(T... arg0);

	@Merge
	void update(T... arg0);

	@Retrieve
	T get(PK arg0);

	@Retrieve
	T[] get(PK... arg0);

	@Retrieve(lazy = true)
	T load(PK arg0);

	@Retrieve(lazy = true)
	T[] load(PK... arg0);
}