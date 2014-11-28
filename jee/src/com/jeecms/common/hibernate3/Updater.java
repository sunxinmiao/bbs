package com.jeecms.common.hibernate3;

import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * 更新对象类
 * 
 * 提供三种更新模式：MAX, MIN, MIDDLE
 * <ul>
 * <li>MIDDLE：默认模式。除了null外，都更新。exclude和include例外。</li>
 * <li>MAX：最大化更新模式。所有字段都更新（包括null）。exclude例外。</li>
 * <li>MIN：最小化更新模式。所有字段都不更新。include例外。</li>
 * </ul>
 * 
 * @author liufang
 * 
 */
public class Updater<T> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Updater.class);

	/**
	 * 构造器
	 * 
	 * @param bean
	 *            待更新对象
	 */
	public Updater(T bean) {
		this.bean = bean;
	}

	/**
	 * 构造器
	 * 
	 * @param bean
	 *            待更新对象
	 * @param mode
	 *            更新模式
	 * @return
	 */
	public Updater(T bean, UpdateMode mode) {
		this.bean = bean;
		this.mode = mode;
	}

	/**
	 * 设置更新模式
	 * 
	 * @param mode
	 * @return
	 */
	public Updater<T> setUpdateMode(UpdateMode mode) {
		if (logger.isDebugEnabled()) {
			logger.debug("setUpdateMode(UpdateMode) - start"); //$NON-NLS-1$
		}

		this.mode = mode;

		if (logger.isDebugEnabled()) {
			logger.debug("setUpdateMode(UpdateMode) - end"); //$NON-NLS-1$
		}
		return this;
	}

	/**
	 * 必须更新的字段
	 * 
	 * @param property
	 * @return
	 */
	public Updater<T> include(String property) {
		if (logger.isDebugEnabled()) {
			logger.debug("include(String) - start"); //$NON-NLS-1$
		}

		includeProperties.add(property);

		if (logger.isDebugEnabled()) {
			logger.debug("include(String) - end"); //$NON-NLS-1$
		}
		return this;
	}

	/**
	 * 不更新的字段
	 * 
	 * @param property
	 * @return
	 */
	public Updater<T> exclude(String property) {
		if (logger.isDebugEnabled()) {
			logger.debug("exclude(String) - start"); //$NON-NLS-1$
		}

		excludeProperties.add(property);

		if (logger.isDebugEnabled()) {
			logger.debug("exclude(String) - end"); //$NON-NLS-1$
		}
		return this;
	}

	/**
	 * 某一字段是否更新
	 * 
	 * @param name
	 *            字段名
	 * @param value
	 *            字段值。用于检查是否为NULL
	 * @return
	 */
	public boolean isUpdate(String name, Object value) {
		if (logger.isDebugEnabled()) {
			logger.debug("isUpdate(String, Object) - start"); //$NON-NLS-1$
		}

		if (this.mode == UpdateMode.MAX) {
			boolean returnboolean = !excludeProperties.contains(name);
			if (logger.isDebugEnabled()) {
				logger.debug("isUpdate(String, Object) - end"); //$NON-NLS-1$
			}
			return returnboolean;
		} else if (this.mode == UpdateMode.MIN) {
			boolean returnboolean = includeProperties.contains(name);
			if (logger.isDebugEnabled()) {
				logger.debug("isUpdate(String, Object) - end"); //$NON-NLS-1$
			}
			return returnboolean;
		} else if (this.mode == UpdateMode.MIDDLE) {
			if (value != null) {
				boolean returnboolean = !excludeProperties.contains(name);
				if (logger.isDebugEnabled()) {
					logger.debug("isUpdate(String, Object) - end"); //$NON-NLS-1$
				}
				return returnboolean;
			} else {
				boolean returnboolean = includeProperties.contains(name);
				if (logger.isDebugEnabled()) {
					logger.debug("isUpdate(String, Object) - end"); //$NON-NLS-1$
				}
				return returnboolean;
			}
		} else {
			// never reach
		}

		if (logger.isDebugEnabled()) {
			logger.debug("isUpdate(String, Object) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private T bean;

	private Set<String> includeProperties = new HashSet<String>();

	private Set<String> excludeProperties = new HashSet<String>();

	private UpdateMode mode = UpdateMode.MIDDLE;

	// private static final Logger log = LoggerFactory.getLogger(Updater.class);

	public static enum UpdateMode {
		MAX, MIN, MIDDLE
	}

	public T getBean() {
		return bean;
	}

	public Set<String> getExcludeProperties() {
		return excludeProperties;
	}

	public Set<String> getIncludeProperties() {
		return includeProperties;
	}
}
