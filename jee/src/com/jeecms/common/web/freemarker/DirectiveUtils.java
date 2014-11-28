package com.jeecms.common.web.freemarker;

import org.apache.log4j.Logger;

import static org.springframework.web.servlet.view.AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.support.RequestContext;

import com.jeecms.common.web.springmvc.DateTypeEditor;

import freemarker.core.Environment;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

/**
 * Freemarker标签工具类
 * 
 * @author liufang
 * 
 */
public abstract class DirectiveUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DirectiveUtils.class);

	/**
	 * 输出参数：对象数据
	 */
	public static final String OUT_BEAN = "tag_bean";
	/**
	 * 输出参数：列表数据
	 */
	public static final String OUT_LIST = "tag_list";
	/**
	 * 输出参数：分页数据
	 */
	public static final String OUT_PAGINATION = "tag_pagination";
	/**
	 * 参数：是否调用模板。
	 */
	public static final String PARAM_TPL = "tpl";
	/**
	 * 参数：次级模板名称
	 */
	public static final String PARAM_TPL_SUB = "tplSub";

	/**
	 * 将params的值复制到variable中
	 * 
	 * @param env
	 * @param params
	 * @return 原Variable中的值
	 * @throws TemplateException
	 */
	public static Map<String, TemplateModel> addParamsToVariable(
			Environment env, Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("addParamsToVariable(Environment, Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Map<String, TemplateModel> origMap = new HashMap<String, TemplateModel>();
		if (params.size() <= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("addParamsToVariable(Environment, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return origMap;
		}
		Set<Map.Entry<String, TemplateModel>> entrySet = params.entrySet();
		String key;
		TemplateModel value;
		for (Map.Entry<String, TemplateModel> entry : entrySet) {
			key = entry.getKey();
			value = env.getVariable(key);
			if (value != null) {
				origMap.put(key, value);
			}
			env.setVariable(key, entry.getValue());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("addParamsToVariable(Environment, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return origMap;
	}

	/**
	 * 将variable中的params值移除
	 * 
	 * @param env
	 * @param params
	 * @param origMap
	 * @throws TemplateException
	 */
	public static void removeParamsFromVariable(Environment env,
			Map<String, TemplateModel> params,
			Map<String, TemplateModel> origMap) throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("removeParamsFromVariable(Environment, Map<String,TemplateModel>, Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		if (params.size() <= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("removeParamsFromVariable(Environment, Map<String,TemplateModel>, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return;
		}
		for (String key : params.keySet()) {
			env.setVariable(key, origMap.get(key));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("removeParamsFromVariable(Environment, Map<String,TemplateModel>, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 获得RequestContext
	 * 
	 * ViewResolver中的exposeSpringMacroHelpers必须为true
	 * 
	 * @param env
	 * @return
	 * @throws TemplateException
	 */
	public static RequestContext getContext(Environment env)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getContext(Environment) - start"); //$NON-NLS-1$
		}

		TemplateModel ctx = env
				.getGlobalVariable(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE);
		if (ctx instanceof AdapterTemplateModel) {
			RequestContext returnRequestContext = (RequestContext) ((AdapterTemplateModel) ctx).getAdaptedObject(RequestContext.class);
			if (logger.isDebugEnabled()) {
				logger.debug("getContext(Environment) - end"); //$NON-NLS-1$
			}
			return returnRequestContext;
		} else {
			throw new TemplateModelException("RequestContext '"
					+ SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE
					+ "' not found in DataModel.");
		}
	}

	public static String getString(String name,
			Map<String, TemplateModel> params) throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getString(String, Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		TemplateModel model = params.get(name);
		if (model == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getString(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String returnString = ((TemplateScalarModel) model).getAsString();
			if (logger.isDebugEnabled()) {
				logger.debug("getString(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return returnString;
		} else if ((model instanceof TemplateNumberModel)) {
			String returnString = ((TemplateNumberModel) model).getAsNumber().toString();
			if (logger.isDebugEnabled()) {
				logger.debug("getString(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			throw new MustStringException(name);
		}
	}

	public static Long getLong(String name, Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getLong(String, Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		TemplateModel model = params.get(name);
		if (model == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getLong(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				if (logger.isDebugEnabled()) {
					logger.debug("getLong(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
				}
				return null;
			}
			try {
				Long returnLong = Long.parseLong(s);
				if (logger.isDebugEnabled()) {
					logger.debug("getLong(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
				}
				return returnLong;
			} catch (NumberFormatException e) {
				logger.error("getLong(String, Map<String,TemplateModel>)", e); //$NON-NLS-1$

				throw new MustNumberException(name);
			}
		} else if (model instanceof TemplateNumberModel) {
			Long returnLong = ((TemplateNumberModel) model).getAsNumber().longValue();
			if (logger.isDebugEnabled()) {
				logger.debug("getLong(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return returnLong;
		} else {
			throw new MustNumberException(name);
		}
	}

	public static Integer getInt(String name, Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getInt(String, Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		TemplateModel model = params.get(name);
		if (model == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getInt(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				if (logger.isDebugEnabled()) {
					logger.debug("getInt(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
				}
				return null;
			}
			try {
				Integer returnInteger = Integer.parseInt(s);
				if (logger.isDebugEnabled()) {
					logger.debug("getInt(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
				}
				return returnInteger;
			} catch (NumberFormatException e) {
				logger.error("getInt(String, Map<String,TemplateModel>)", e); //$NON-NLS-1$

				throw new MustNumberException(name);
			}
		} else if (model instanceof TemplateNumberModel) {
			Integer returnInteger = ((TemplateNumberModel) model).getAsNumber().intValue();
			if (logger.isDebugEnabled()) {
				logger.debug("getInt(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return returnInteger;
		} else {
			throw new MustNumberException(name);
		}
	}

	public static Short getShort(String name, Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getShort(String, Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		TemplateModel model = params.get(name);
		if (model == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getShort(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				if (logger.isDebugEnabled()) {
					logger.debug("getShort(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
				}
				return null;
			}
			try {
				Short returnShort = Short.parseShort(s);
				if (logger.isDebugEnabled()) {
					logger.debug("getShort(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
				}
				return returnShort;
			} catch (NumberFormatException e) {
				logger.error("getShort(String, Map<String,TemplateModel>)", e); //$NON-NLS-1$

				throw new MustNumberException(name);
			}
		} else if (model instanceof TemplateNumberModel) {
			Short returnShort = ((TemplateNumberModel) model).getAsNumber().shortValue();
			if (logger.isDebugEnabled()) {
				logger.debug("getShort(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return returnShort;
		} else {
			throw new MustNumberException(name);
		}
	}

	public static Integer[] getIntArray(String name,
			Map<String, TemplateModel> params) throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getIntArray(String, Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		String str = DirectiveUtils.getString(name, params);
		if (StringUtils.isBlank(str)) {
			if (logger.isDebugEnabled()) {
				logger.debug("getIntArray(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		String[] arr = StringUtils.split(str, ',');
		Integer[] ids = new Integer[arr.length];
		int i = 0;
		try {
			for (String s : arr) {
				ids[i++] = Integer.valueOf(s);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("getIntArray(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return ids;
		} catch (NumberFormatException e) {
			logger.error("getIntArray(String, Map<String,TemplateModel>)", e); //$NON-NLS-1$

			throw new MustSplitNumberException(name, e);
		}
	}

	public static Boolean getBool(String name, Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getBool(String, Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		TemplateModel model = params.get(name);
		if (model == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getBool(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		if (model instanceof TemplateBooleanModel) {
			Boolean returnBoolean = ((TemplateBooleanModel) model).getAsBoolean();
			if (logger.isDebugEnabled()) {
				logger.debug("getBool(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return returnBoolean;
		} else if (model instanceof TemplateNumberModel) {
			Boolean returnBoolean = !(((TemplateNumberModel) model).getAsNumber().intValue() == 0);
			if (logger.isDebugEnabled()) {
				logger.debug("getBool(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return returnBoolean;
		} else if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			// 空串应该返回null还是true呢？
			if (!StringUtils.isBlank(s)) {
				Boolean returnBoolean = !(s.equals("0") || s.equalsIgnoreCase("false") || s.equalsIgnoreCase("f"));
				if (logger.isDebugEnabled()) {
					logger.debug("getBool(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
				}
				return returnBoolean;
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("getBool(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
				}
				return null;
			}
		} else {
			throw new MustBooleanException(name);
		}
	}

	public static Date getDate(String name, Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getDate(String, Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		TemplateModel model = params.get(name);
		if (model == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getDate(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		if (model instanceof TemplateDateModel) {
			Date returnDate = ((TemplateDateModel) model).getAsDate();
			if (logger.isDebugEnabled()) {
				logger.debug("getDate(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return returnDate;
		} else if (model instanceof TemplateScalarModel) {
			DateTypeEditor editor = new DateTypeEditor();
			editor.setAsText(((TemplateScalarModel) model).getAsString());
			Date returnDate = (Date) editor.getValue();
			if (logger.isDebugEnabled()) {
				logger.debug("getDate(String, Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return returnDate;
		} else {
			throw new MustDateException(name);
		}
	}

	/**
	 * 模板调用类型
	 * 
	 * @author liufang
	 */
	public enum InvokeType {
		body, custom, sysDefined, userDefined
	};

	/**
	 * 是否调用模板
	 * 
	 * 0：不调用，使用标签的body；1：调用自定义模板；2：调用系统预定义模板；3：调用用户预定义模板。默认：0。
	 * 
	 * @param params
	 * @return
	 * @throws TemplateException
	 */
	public static InvokeType getInvokeType(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getInvokeType(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		String tpl = getString(PARAM_TPL, params);
		if ("3".equals(tpl)) {
			if (logger.isDebugEnabled()) {
				logger.debug("getInvokeType(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return InvokeType.userDefined;
		} else if ("2".equals(tpl)) {
			if (logger.isDebugEnabled()) {
				logger.debug("getInvokeType(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return InvokeType.sysDefined;
		} else if ("1".equals(tpl)) {
			if (logger.isDebugEnabled()) {
				logger.debug("getInvokeType(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return InvokeType.custom;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getInvokeType(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return InvokeType.body;
		}
	}
}
