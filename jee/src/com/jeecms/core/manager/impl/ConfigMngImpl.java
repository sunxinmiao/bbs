package com.jeecms.core.manager.impl;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.core.dao.ConfigDao;
import com.jeecms.core.entity.Config;
import com.jeecms.core.entity.Config.ConfigEmailSender;
import com.jeecms.core.entity.Config.ConfigLogin;
import com.jeecms.core.entity.Config.ConfigMessageTemplate;
import com.jeecms.core.manager.ConfigMng;

@Service
@Transactional
public class ConfigMngImpl implements ConfigMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ConfigMngImpl.class);

	@Transactional(readOnly = true)
	public Map<String, String> getMap() {
		if (logger.isDebugEnabled()) {
			logger.debug("getMap() - start"); //$NON-NLS-1$
		}

		List<Config> list = dao.getList();
		Map<String, String> map = new HashMap<String, String>(list.size());
		for (Config config : list) {
			map.put(config.getId(), config.getValue());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getMap() - end"); //$NON-NLS-1$
		}
		return map;
	}

	@Transactional(readOnly = true)
	public String getValue(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("getValue(String) - start"); //$NON-NLS-1$
		}

		Config entity = dao.findById(id);
		if (entity != null) {
			String returnString = entity.getValue();
			if (logger.isDebugEnabled()) {
				logger.debug("getValue(String) - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getValue(String) - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	@Transactional(readOnly = true)
	public ConfigLogin getConfigLogin() {
		if (logger.isDebugEnabled()) {
			logger.debug("getConfigLogin() - start"); //$NON-NLS-1$
		}

		ConfigLogin returnConfigLogin = ConfigLogin.create(getMap());
		if (logger.isDebugEnabled()) {
			logger.debug("getConfigLogin() - end"); //$NON-NLS-1$
		}
		return returnConfigLogin;
	}

	@Transactional(readOnly = true)
	public EmailSender getEmailSender() {
		if (logger.isDebugEnabled()) {
			logger.debug("getEmailSender() - start"); //$NON-NLS-1$
		}

		EmailSender returnEmailSender = ConfigEmailSender.create(getMap());
		if (logger.isDebugEnabled()) {
			logger.debug("getEmailSender() - end"); //$NON-NLS-1$
		}
		return returnEmailSender;
	}

	@Transactional(readOnly = true)
	public MessageTemplate getForgotPasswordMessageTemplate() {
		if (logger.isDebugEnabled()) {
			logger.debug("getForgotPasswordMessageTemplate() - start"); //$NON-NLS-1$
		}

		MessageTemplate returnMessageTemplate = ConfigMessageTemplate.createForgotPasswordMessageTemplate(getMap());
		if (logger.isDebugEnabled()) {
			logger.debug("getForgotPasswordMessageTemplate() - end"); //$NON-NLS-1$
		}
		return returnMessageTemplate;
	}
	
	@Transactional(readOnly = true)
	public MessageTemplate getRegisterMessageTemplate() {
		if (logger.isDebugEnabled()) {
			logger.debug("getRegisterMessageTemplate() - start"); //$NON-NLS-1$
		}

		MessageTemplate returnMessageTemplate = ConfigMessageTemplate.createRegisterMessageTemplate(getMap());
		if (logger.isDebugEnabled()) {
			logger.debug("getRegisterMessageTemplate() - end"); //$NON-NLS-1$
		}
		return returnMessageTemplate;
	}

	public void updateOrSave(Map<String, String> map) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateOrSave(Map<String,String>) - start"); //$NON-NLS-1$
		}

		if (map != null && map.size() > 0) {
			for (Entry<String, String> entry : map.entrySet()) {
				updateOrSave(entry.getKey(), entry.getValue());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("updateOrSave(Map<String,String>) - end"); //$NON-NLS-1$
		}
	}

	public Config updateOrSave(String key, String value) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateOrSave(String, String) - start"); //$NON-NLS-1$
		}

		Config config = dao.findById(key);
		if (config != null) {
			config.setValue(value);
		} else {
			config = new Config(key);
			config.setValue(value);
			dao.save(config);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("updateOrSave(String, String) - end"); //$NON-NLS-1$
		}
		return config;
	}

	public Config deleteById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - start"); //$NON-NLS-1$
		}

		Config bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Config[] deleteByIds(String[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(String[]) - start"); //$NON-NLS-1$
		}

		Config[] beans = new Config[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(String[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private ConfigDao dao;

	@Autowired
	public void setDao(ConfigDao dao) {
		this.dao = dao;
	}
}