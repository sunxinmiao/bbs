package com.jeecms.common.hibernate3;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;

/**
 * HQL语句分页查询
 * 
 * @author liufang
 * 
 */
public class Finder {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Finder.class);

	protected Finder() {
		hqlBuilder = new StringBuilder();
	}

	protected Finder(String hql) {
		hqlBuilder = new StringBuilder(hql);
	}

	public static Finder create() {
		if (logger.isDebugEnabled()) {
			logger.debug("create() - start"); //$NON-NLS-1$
		}

		Finder returnFinder = new Finder();
		if (logger.isDebugEnabled()) {
			logger.debug("create() - end"); //$NON-NLS-1$
		}
		return returnFinder;
	}

	public static Finder create(String hql) {
		if (logger.isDebugEnabled()) {
			logger.debug("create(String) - start"); //$NON-NLS-1$
		}

		Finder returnFinder = new Finder(hql);
		if (logger.isDebugEnabled()) {
			logger.debug("create(String) - end"); //$NON-NLS-1$
		}
		return returnFinder;
	}

	public Finder append(String hql) {
		if (logger.isDebugEnabled()) {
			logger.debug("append(String) - start"); //$NON-NLS-1$
		}

		hqlBuilder.append(hql);

		if (logger.isDebugEnabled()) {
			logger.debug("append(String) - end"); //$NON-NLS-1$
		}
		return this;
	}

	/**
	 * 获得原始hql语句
	 * 
	 * @return
	 */
	public String getOrigHql() {
		if (logger.isDebugEnabled()) {
			logger.debug("getOrigHql() - start"); //$NON-NLS-1$
		}

		String returnString = hqlBuilder.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("getOrigHql() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 获得查询数据库记录数的hql语句。
	 * 
	 * @return
	 */
	public String getRowCountHql() {
		if (logger.isDebugEnabled()) {
			logger.debug("getRowCountHql() - start"); //$NON-NLS-1$
		}

		String hql = hqlBuilder.toString();

		int fromIndex = hql.toLowerCase().indexOf(FROM);
		String projectionHql = hql.substring(0, fromIndex);

		hql = hql.substring(fromIndex);
		String rowCountHql = hql.replace(HQL_FETCH, "");

		int index = rowCountHql.indexOf(ORDER_BY);
		if (index > 0) {
			rowCountHql = rowCountHql.substring(0, index);
		}
		String returnString = wrapProjection(projectionHql) + rowCountHql;
		if (logger.isDebugEnabled()) {
			logger.debug("getRowCountHql() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * 是否使用查询缓存
	 * 
	 * @return
	 */
	public boolean isCacheable() {
		return cacheable;
	}

	/**
	 * 设置是否使用查询缓存
	 * 
	 * @param cacheable
	 * @see Query#setCacheable(boolean)
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	/**
	 * 设置参数
	 * 
	 * @param param
	 * @param value
	 * @return
	 * @see Query#setParameter(String, Object)
	 */
	public Finder setParam(String param, Object value) {
		if (logger.isDebugEnabled()) {
			logger.debug("setParam(String, Object) - start"); //$NON-NLS-1$
		}

		Finder returnFinder = setParam(param, value, null);
		if (logger.isDebugEnabled()) {
			logger.debug("setParam(String, Object) - end"); //$NON-NLS-1$
		}
		return returnFinder;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param param
	 * @param value
	 * @param type
	 * @return
	 * @see Query#setParameter(String, Object, Type)
	 */
	public Finder setParam(String param, Object value, Type type) {
		if (logger.isDebugEnabled()) {
			logger.debug("setParam(String, Object, Type) - start"); //$NON-NLS-1$
		}

		getParams().add(param);
		getValues().add(value);
		getTypes().add(type);

		if (logger.isDebugEnabled()) {
			logger.debug("setParam(String, Object, Type) - end"); //$NON-NLS-1$
		}
		return this;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param paramMap
	 * @return
	 * @see Query#setProperties(Map)
	 */
	public Finder setParams(Map<String, Object> paramMap) {
		if (logger.isDebugEnabled()) {
			logger.debug("setParams(Map<String,Object>) - start"); //$NON-NLS-1$
		}

		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			setParam(entry.getKey(), entry.getValue());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setParams(Map<String,Object>) - end"); //$NON-NLS-1$
		}
		return this;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param name
	 * @param vals
	 * @param type
	 * @return
	 * @see Query#setParameterList(String, Collection, Type))
	 */
	public Finder setParamList(String name, Collection<Object> vals, Type type) {
		if (logger.isDebugEnabled()) {
			logger.debug("setParamList(String, Collection<Object>, Type) - start"); //$NON-NLS-1$
		}

		getParamsList().add(name);
		getValuesList().add(vals);
		getTypesList().add(type);

		if (logger.isDebugEnabled()) {
			logger.debug("setParamList(String, Collection<Object>, Type) - end"); //$NON-NLS-1$
		}
		return this;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param name
	 * @param vals
	 * @return
	 * @see Query#setParameterList(String, Collection)
	 */
	public Finder setParamList(String name, Collection<Object> vals) {
		if (logger.isDebugEnabled()) {
			logger.debug("setParamList(String, Collection<Object>) - start"); //$NON-NLS-1$
		}

		Finder returnFinder = setParamList(name, vals, null);
		if (logger.isDebugEnabled()) {
			logger.debug("setParamList(String, Collection<Object>) - end"); //$NON-NLS-1$
		}
		return returnFinder;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param name
	 * @param vals
	 * @param type
	 * @return
	 * @see Query#setParameterList(String, Object[], Type)
	 */
	public Finder setParamList(String name, Object[] vals, Type type) {
		if (logger.isDebugEnabled()) {
			logger.debug("setParamList(String, Object[], Type) - start"); //$NON-NLS-1$
		}

		getParamsArray().add(name);
		getValuesArray().add(vals);
		getTypesArray().add(type);

		if (logger.isDebugEnabled()) {
			logger.debug("setParamList(String, Object[], Type) - end"); //$NON-NLS-1$
		}
		return this;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param name
	 * @param vals
	 * @return
	 * @see Query#setParameterList(String, Object[])
	 */
	public Finder setParamList(String name, Object[] vals) {
		if (logger.isDebugEnabled()) {
			logger.debug("setParamList(String, Object[]) - start"); //$NON-NLS-1$
		}

		Finder returnFinder = setParamList(name, vals, null);
		if (logger.isDebugEnabled()) {
			logger.debug("setParamList(String, Object[]) - end"); //$NON-NLS-1$
		}
		return returnFinder;
	}

	/**
	 * 将finder中的参数设置到query中。
	 * 
	 * @param query
	 */
	public Query setParamsToQuery(Query query) {
		if (logger.isDebugEnabled()) {
			logger.debug("setParamsToQuery(Query) - start"); //$NON-NLS-1$
		}

		if (params != null) {
			for (int i = 0; i < params.size(); i++) {
				if (types.get(i) == null) {
					query.setParameter(params.get(i), values.get(i));
				} else {
					query.setParameter(params.get(i), values.get(i), types
							.get(i));
				}
			}
		}
		if (paramsList != null) {
			for (int i = 0; i < paramsList.size(); i++) {
				if (typesList.get(i) == null) {
					query
							.setParameterList(paramsList.get(i), valuesList
									.get(i));
				} else {
					query.setParameterList(paramsList.get(i),
							valuesList.get(i), typesList.get(i));
				}
			}
		}
		if (paramsArray != null) {
			for (int i = 0; i < paramsArray.size(); i++) {
				if (typesArray.get(i) == null) {
					query.setParameterList(paramsArray.get(i), valuesArray
							.get(i));
				} else {
					query.setParameterList(paramsArray.get(i), valuesArray
							.get(i), typesArray.get(i));
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setParamsToQuery(Query) - end"); //$NON-NLS-1$
		}
		return query;
	}

	public Query createQuery(Session s) {
		if (logger.isDebugEnabled()) {
			logger.debug("createQuery(Session) - start"); //$NON-NLS-1$
		}

		Query query = setParamsToQuery(s.createQuery(getOrigHql()));
		if (getFirstResult() > 0) {
			query.setFirstResult(getFirstResult());
		}
		if (getMaxResults() > 0) {
			query.setMaxResults(getMaxResults());
		}
		if (isCacheable()) {
			query.setCacheable(true);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("createQuery(Session) - end"); //$NON-NLS-1$
		}
		return query;
	}

	private String wrapProjection(String projection) {
		if (logger.isDebugEnabled()) {
			logger.debug("wrapProjection(String) - start"); //$NON-NLS-1$
		}

		if (projection.indexOf("select") == -1) {
			if (logger.isDebugEnabled()) {
				logger.debug("wrapProjection(String) - end"); //$NON-NLS-1$
			}
			return ROW_COUNT;
		} else {
			String returnString = projection.replace("select", "select count(") + ") ";
			if (logger.isDebugEnabled()) {
				logger.debug("wrapProjection(String) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	}

	private List<String> getParams() {
		if (logger.isDebugEnabled()) {
			logger.debug("getParams() - start"); //$NON-NLS-1$
		}

		if (params == null) {
			params = new ArrayList<String>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getParams() - end"); //$NON-NLS-1$
		}
		return params;
	}

	private List<Object> getValues() {
		if (logger.isDebugEnabled()) {
			logger.debug("getValues() - start"); //$NON-NLS-1$
		}

		if (values == null) {
			values = new ArrayList<Object>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getValues() - end"); //$NON-NLS-1$
		}
		return values;
	}

	private List<Type> getTypes() {
		if (logger.isDebugEnabled()) {
			logger.debug("getTypes() - start"); //$NON-NLS-1$
		}

		if (types == null) {
			types = new ArrayList<Type>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getTypes() - end"); //$NON-NLS-1$
		}
		return types;
	}

	private List<String> getParamsList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getParamsList() - start"); //$NON-NLS-1$
		}

		if (paramsList == null) {
			paramsList = new ArrayList<String>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getParamsList() - end"); //$NON-NLS-1$
		}
		return paramsList;
	}

	private List<Collection<Object>> getValuesList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getValuesList() - start"); //$NON-NLS-1$
		}

		if (valuesList == null) {
			valuesList = new ArrayList<Collection<Object>>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getValuesList() - end"); //$NON-NLS-1$
		}
		return valuesList;
	}

	private List<Type> getTypesList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getTypesList() - start"); //$NON-NLS-1$
		}

		if (typesList == null) {
			typesList = new ArrayList<Type>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getTypesList() - end"); //$NON-NLS-1$
		}
		return typesList;
	}

	private List<String> getParamsArray() {
		if (logger.isDebugEnabled()) {
			logger.debug("getParamsArray() - start"); //$NON-NLS-1$
		}

		if (paramsArray == null) {
			paramsArray = new ArrayList<String>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getParamsArray() - end"); //$NON-NLS-1$
		}
		return paramsArray;
	}

	private List<Object[]> getValuesArray() {
		if (logger.isDebugEnabled()) {
			logger.debug("getValuesArray() - start"); //$NON-NLS-1$
		}

		if (valuesArray == null) {
			valuesArray = new ArrayList<Object[]>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getValuesArray() - end"); //$NON-NLS-1$
		}
		return valuesArray;
	}

	private List<Type> getTypesArray() {
		if (logger.isDebugEnabled()) {
			logger.debug("getTypesArray() - start"); //$NON-NLS-1$
		}

		if (typesArray == null) {
			typesArray = new ArrayList<Type>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getTypesArray() - end"); //$NON-NLS-1$
		}
		return typesArray;
	}

	private StringBuilder hqlBuilder;

	private List<String> params;
	private List<Object> values;
	private List<Type> types;

	private List<String> paramsList;
	private List<Collection<Object>> valuesList;
	private List<Type> typesList;

	private List<String> paramsArray;
	private List<Object[]> valuesArray;
	private List<Type> typesArray;

	private int firstResult = 0;

	private int maxResults = 0;

	private boolean cacheable = false;

	public static final String ROW_COUNT = "select count(*) ";
	public static final String FROM = "from";
	public static final String DISTINCT = "distinct";
	public static final String HQL_FETCH = "fetch";
	public static final String ORDER_BY = "order";

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		Finder find = Finder
				.create("select distinct p FROM BookType join fetch p");
		System.out.println(find.getRowCountHql());
		System.out.println(find.getOrigHql());

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}

}