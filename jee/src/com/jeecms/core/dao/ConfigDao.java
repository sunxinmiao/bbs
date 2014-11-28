package com.jeecms.core.dao;

import org.apache.log4j.Logger;

import java.util.List;

import com.jeecms.core.entity.Config;

public interface ConfigDao {
	public List<Config> getList();

	public Config findById(String id);

	public Config save(Config bean);

	public Config deleteById(String id);
}