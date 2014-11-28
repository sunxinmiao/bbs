package com.jeecms.bbs.web;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.Constants.RES_PATH;
import static com.jeecms.bbs.Constants.TPLDIR_COMMON;
import static com.jeecms.bbs.Constants.TPLDIR_STYLE_LIST;
import static com.jeecms.bbs.Constants.TPLDIR_TAG;
import static com.jeecms.bbs.Constants.TPL_STYLE_PAGE_CHANNEL;
import static com.jeecms.bbs.Constants.TPL_STYLE_PAGE_TOPIC;
import static com.jeecms.bbs.Constants.TPL_SUFFIX;
import static com.jeecms.common.web.Constants.MESSAGE;
import static com.jeecms.common.web.Constants.UTF8;
import static com.jeecms.common.web.ProcessTimeFilter.START_TIME;
import static com.jeecms.common.web.freemarker.DirectiveUtils.PARAM_TPL_SUB;
import static com.jeecms.core.action.front.LoginAct.PROCESS_URL;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.context.MessageSource;

import com.jeecms.core.entity.CmsSite;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.web.front.URLHelper;
import com.jeecms.core.web.front.URLHelper.PageInfo;

import freemarker.core.Environment;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

/**
 * 前台工具类
 * 
 * @author liufang
 * 
 */
public class FrontUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FrontUtils.class);

	/**
	 * 页面没有找到
	 */
	public static final String PAGE_NOT_FOUND = "tpl.pageNotFound";
	/**
	 * 操作成功页面
	 */
	public static final String SUCCESS_PAGE = "tpl.successPage";
	/**
	 * 操作失败页面
	 */
	public static final String ERROR_PAGE = "tpl.errorPage";
	/**
	 * 信息提示页面
	 */
	public static final String MESSAGE_PAGE = "tpl.messagePage";
	/**
	 * 系统资源路径
	 */
	public static final String RES_SYS = "resSys";
	/**
	 * 模板资源路径
	 */
	public static final String RES_TPL = "res";
	/**
	 * 模板资源表达式
	 */
	public static final String RES_EXP = "${res}";
	/**
	 * 部署路径
	 */
	public static final String BASE = "base";
	/**
	 * 站点
	 */
	public static final String SITE = "site";
	/**
	 * 用户
	 */
	public static final String USER = "user";
	/**
	 * 页码
	 */
	public static final String PAGE_NO = "pageNo";
	/**
	 * 总条数
	 */
	public static final String COUNT = "count";
	/**
	 * 起始条数
	 */
	public static final String FIRST = "first";

	/**
	 * 页面完整地址
	 */
	public static final String LOCATION = "location";
	/**
	 * 页面翻页地址
	 */
	public static final String HREF = "href";
	/**
	 * href前半部（相对于分页）
	 */
	public static final String HREF_FORMER = "hrefFormer";
	/**
	 * href后半部（相对于分页）
	 */
	public static final String HREF_LATTER = "hrefLatter";

	/**
	 * 传入参数，列表样式。
	 */
	public static final String PARAM_STYLE_LIST = "styleList";
	/**
	 * 传入参数，系统预定义翻页。
	 */
	public static final String PARAM_SYS_PAGE = "sysPage";
	
	public static final String PARAM_SYS_TOPIC_PAGE = "sysTopicPage";
	/**
	 * 传入参数，用户自定义翻页。
	 */
	public static final String PARAM_USER_PAGE = "userPage";

	/**
	 * 返回页面
	 */
	public static final String RETURN_URL = "returnUrl";

	/**
	 * 国际化参数
	 */
	public static final String ARGS = "args";

	/**
	 * 获得模板路径。将对模板文件名称进行本地化处理。
	 * 
	 * @param request
	 * @param solution
	 *            方案路径
	 * @param dir
	 *            模板目录。不本地化处理。
	 * @param name
	 *            模板名称。本地化处理。
	 * @return
	 */
	public static String getTplPath(HttpServletRequest request,
			String solution, String dir, String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getTplPath(HttpServletRequest, String, String, String) - start"); //$NON-NLS-1$
		}

		String returnString = solution + "/" + dir + "/" + MessageResolver.getMessage(request, name) + TPL_SUFFIX;
		if (logger.isDebugEnabled()) {
			logger.debug("getTplPath(HttpServletRequest, String, String, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 获得模板路径。将对模板文件名称进行本地化处理。
	 * 
	 * @param messageSource
	 * @param lang
	 *            本地化语言
	 * @param solution
	 *            方案路径
	 * @param dir
	 *            模板目录。不本地化处理。
	 * @param name
	 *            模板名称。本地化处理。
	 * @return
	 */
	public static String getTplPath(MessageSource messageSource, String lang,
			String solution, String dir, String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getTplPath(MessageSource, String, String, String, String) - start"); //$NON-NLS-1$
		}

		LocaleEditor localeEditor = new LocaleEditor();
		localeEditor.setAsText(lang);
		Locale locale = (Locale) localeEditor.getValue();
		String returnString = solution + "/" + dir + "/" + messageSource.getMessage(name, null, locale) + TPL_SUFFIX;
		if (logger.isDebugEnabled()) {
			logger.debug("getTplPath(MessageSource, String, String, String, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 获得模板路径。不对模板文件进行本地化处理。
	 * 
	 * @param solution
	 *            方案路径
	 * @param dir
	 *            模板目录。不本地化处理。
	 * @param name
	 *            模板名称。不本地化处理。
	 * @return
	 */
	public static String getTplPath(String solution, String dir, String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getTplPath(String, String, String) - start"); //$NON-NLS-1$
		}

		String returnString = solution + "/" + dir + "/" + name + TPL_SUFFIX;
		if (logger.isDebugEnabled()) {
			logger.debug("getTplPath(String, String, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 页面没有找到
	 * 
	 * @param request
	 * @param response
	 * @return 返回“页面没有找到”的模板
	 */
	public static String pageNotFound(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> model) {
		if (logger.isDebugEnabled()) {
			logger.debug("pageNotFound(HttpServletRequest, HttpServletResponse, Map<String,Object>) - start"); //$NON-NLS-1$
		}

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		CmsSite site = CmsUtils.getSite(request);
		frontData(request, model, site);
		String returnString = getTplPath(request, site.getSolutionPath(), TPLDIR_COMMON, PAGE_NOT_FOUND);
		if (logger.isDebugEnabled()) {
			logger.debug("pageNotFound(HttpServletRequest, HttpServletResponse, Map<String,Object>) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 成功提示页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	public static String showSuccess(HttpServletRequest request,
			Map<String, Object> model, String nextUrl) {
		if (logger.isDebugEnabled()) {
			logger.debug("showSuccess(HttpServletRequest, Map<String,Object>, String) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		frontData(request, model, site);
		if (!StringUtils.isBlank(nextUrl)) {
			model.put("nextUrl", nextUrl);
		}
		String returnString = getTplPath(request, site.getSolutionPath(), TPLDIR_COMMON, SUCCESS_PAGE);
		if (logger.isDebugEnabled()) {
			logger.debug("showSuccess(HttpServletRequest, Map<String,Object>, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 错误提示页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public static String showError(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> model,
			WebErrors errors) {
		if (logger.isDebugEnabled()) {
			logger.debug("showError(HttpServletRequest, HttpServletResponse, Map<String,Object>, WebErrors) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		frontData(request, model, site);
		errors.toModel(model);
		String returnString = getTplPath(request, site.getSolutionPath(), TPLDIR_COMMON, ERROR_PAGE);
		if (logger.isDebugEnabled()) {
			logger.debug("showError(HttpServletRequest, HttpServletResponse, Map<String,Object>, WebErrors) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 信息提示页面
	 * 
	 * @param request
	 * @param model
	 * @param message
	 *            进行国际化处理
	 * @return
	 */
	public static String showMessage(HttpServletRequest request,
			Map<String, Object> model, String message, String... args) {
		if (logger.isDebugEnabled()) {
			logger.debug("showMessage(HttpServletRequest, Map<String,Object>, String, String) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		frontData(request, model, site);
		model.put(MESSAGE, message);
		if (args != null) {
			model.put(ARGS, args);
		}
		String returnString = getTplPath(request, site.getSolutionPath(), TPLDIR_COMMON, MESSAGE_PAGE);
		if (logger.isDebugEnabled()) {
			logger.debug("showMessage(HttpServletRequest, Map<String,Object>, String, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 显示登录页面
	 * 
	 * @param request
	 * @param model
	 * @param site
	 * @param message
	 * @return
	 */
	public static String showLogin(HttpServletRequest request,
			Map<String, Object> model, CmsSite site, String message) {
		if (logger.isDebugEnabled()) {
			logger.debug("showLogin(HttpServletRequest, Map<String,Object>, CmsSite, String) - start"); //$NON-NLS-1$
		}

		if (!StringUtils.isBlank(message)) {
			model.put(MESSAGE, message);
		}
		StringBuilder buff = new StringBuilder("redirect:");
		buff.append(site.getLoginUrl()).append("?");
		buff.append(RETURN_URL).append("=");
		buff.append(RequestUtils.getLocation(request));
		if (!StringUtils.isBlank(site.getProcessUrl())) {
			buff.append("&").append(PROCESS_URL).append(site.getProcessUrl());
		}
		String returnString = buff.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("showLogin(HttpServletRequest, Map<String,Object>, CmsSite, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 显示登录页面
	 * 
	 * @param request
	 * @param model
	 * @param site
	 * @return
	 */
	public static String showLogin(HttpServletRequest request,
			Map<String, Object> model, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("showLogin(HttpServletRequest, Map<String,Object>, CmsSite) - start"); //$NON-NLS-1$
		}

		String returnString = showLogin(request, model, site, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("showLogin(HttpServletRequest, Map<String,Object>, CmsSite) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 为前台模板设置公用数据
	 * 
	 * @param request
	 * @param model
	 */
	public static void frontData(HttpServletRequest request,
			Map<String, Object> map, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("frontData(HttpServletRequest, Map<String,Object>, CmsSite) - start"); //$NON-NLS-1$
		}

		BbsUser user = CmsUtils.getUser(request);
		String location = RequestUtils.getLocation(request);
		Long startTime = (Long) request.getAttribute(START_TIME);
		frontData(map, site, user, location, null, startTime);

		if (logger.isDebugEnabled()) {
			logger.debug("frontData(HttpServletRequest, Map<String,Object>, CmsSite) - end"); //$NON-NLS-1$
		}
	}

	public static void frontData(Map<String, Object> map, CmsSite site,
			BbsUser user, String location, String base, Long startTime) {
		if (logger.isDebugEnabled()) {
			logger.debug("frontData(Map<String,Object>, CmsSite, BbsUser, String, String, Long) - start"); //$NON-NLS-1$
		}

		if (startTime != null) {
			map.put(START_TIME, startTime);
		}
		if (base != null) {
			map.put(BASE, base);
		}
		if (user != null) {
			map.put(USER, user);
		}
		map.put(SITE, site);
		String ctxPath = site.getContextPath();
		if ((ctxPath + "").equals("null")) {
			ctxPath = "";
		}
		map.put(RES_SYS, ctxPath + RES_PATH);

		String res = ctxPath + RES_PATH + "/" + site.getPath() + "/"
				+ site.getTplSolution();
		// res路径需要去除第一个字符'/'
		map.put(RES_TPL, res.substring(1));
		map.put(LOCATION, location);

		if (logger.isDebugEnabled()) {
			logger.debug("frontData(Map<String,Object>, CmsSite, BbsUser, String, String, Long) - end"); //$NON-NLS-1$
		}
	}

	public static void putLocation(Map<String, Object> map, String location) {
		if (logger.isDebugEnabled()) {
			logger.debug("putLocation(Map<String,Object>, String) - start"); //$NON-NLS-1$
		}

		map.put(LOCATION, location);

		if (logger.isDebugEnabled()) {
			logger.debug("putLocation(Map<String,Object>, String) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 为前台模板设置分页相关数据
	 * 
	 * @param request
	 * @param map
	 */
	public static void frontPageData(HttpServletRequest request,
			Map<String, Object> map) {
		if (logger.isDebugEnabled()) {
			logger.debug("frontPageData(HttpServletRequest, Map<String,Object>) - start"); //$NON-NLS-1$
		}

		int pageNo = URLHelper.getPageNo(request);
		PageInfo info = URLHelper.getPageInfo(request);
		String href = info.getHref();
		String hrefFormer = info.getHrefFormer();
		String hrefLatter = info.getHrefLatter();
		frontPageData(pageNo, href, hrefFormer, hrefLatter, map);

		if (logger.isDebugEnabled()) {
			logger.debug("frontPageData(HttpServletRequest, Map<String,Object>) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 为前台模板设置分页相关数据
	 * 
	 * @param pageNo
	 * @param href
	 * @param urlFormer
	 * @param urlLatter
	 * @param map
	 */
	public static void frontPageData(int pageNo, String href,
			String hrefFormer, String hrefLatter, Map<String, Object> map) {
		if (logger.isDebugEnabled()) {
			logger.debug("frontPageData(int, String, String, String, Map<String,Object>) - start"); //$NON-NLS-1$
		}

		map.put(PAGE_NO, pageNo);
		map.put(HREF, href);
		map.put(HREF_FORMER, hrefFormer);
		map.put(HREF_LATTER, hrefLatter);

		if (logger.isDebugEnabled()) {
			logger.debug("frontPageData(int, String, String, String, Map<String,Object>) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 标签中获得站点
	 * 
	 * @param env
	 * @return
	 * @throws TemplateModelException
	 */
	public static CmsSite getSite(Environment env)
			throws TemplateModelException {
		if (logger.isDebugEnabled()) {
			logger.debug("getSite(Environment) - start"); //$NON-NLS-1$
		}

		TemplateModel model = env.getGlobalVariable(SITE);
		if (model instanceof AdapterTemplateModel) {
			CmsSite returnCmsSite = (CmsSite) ((AdapterTemplateModel) model).getAdaptedObject(CmsSite.class);
			if (logger.isDebugEnabled()) {
				logger.debug("getSite(Environment) - end"); //$NON-NLS-1$
			}
			return returnCmsSite;
		} else {
			throw new TemplateModelException("'" + SITE
					+ "' not found in DataModel");
		}
	}
	/**
	 * 标签中获得用户
	 * 
	 * @param env
	 * @return
	 * @throws TemplateModelException
	 */
	public static BbsUser getUser(Environment env)
			throws TemplateModelException {
		if (logger.isDebugEnabled()) {
			logger.debug("getUser(Environment) - start"); //$NON-NLS-1$
		}

		TemplateModel model = env.getGlobalVariable(USER);
		if (model instanceof AdapterTemplateModel) {
			BbsUser returnBbsUser = (BbsUser) ((AdapterTemplateModel) model).getAdaptedObject(CmsSite.class);
			if (logger.isDebugEnabled()) {
				logger.debug("getUser(Environment) - end"); //$NON-NLS-1$
			}
			return returnBbsUser;
		} else {
			throw new TemplateModelException("'" + USER
					+ "' not found in DataModel");
		}
	}
	
	/**
	 * 标签中获得页码
	 * 
	 * @param env
	 * @return
	 * @throws TemplateException
	 */
	public static int getPageNo(Environment env) throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getPageNo(Environment) - start"); //$NON-NLS-1$
		}

		TemplateModel pageNo = env.getGlobalVariable(PAGE_NO);
		if (pageNo instanceof TemplateNumberModel) {
			int returnint = ((TemplateNumberModel) pageNo).getAsNumber().intValue();
			if (logger.isDebugEnabled()) {
				logger.debug("getPageNo(Environment) - end"); //$NON-NLS-1$
			}
			return returnint;
		} else {
			throw new TemplateModelException("'" + PAGE_NO
					+ "' not found in DataModel.");
		}
	}

	public static int getFirst(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getFirst(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer first = DirectiveUtils.getInt(FIRST, params);
		if (first == null || first <= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("getFirst(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return 0;
		} else {
			int returnint = first - 1;
			if (logger.isDebugEnabled()) {
				logger.debug("getFirst(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return returnint;
		}
	}

	/**
	 * 标签参数中获得条数。
	 * 
	 * @param params
	 * @return 如果不存在，或者小于等于0，或者大于2000则返回2000；否则返回条数。
	 * @throws TemplateException
	 */
	public static int getCount(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getCount(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer count = DirectiveUtils.getInt(COUNT, params);
		if (count == null || count <= 0 || count >= 2000) {
			if (logger.isDebugEnabled()) {
				logger.debug("getCount(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return 2000;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getCount(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return count;
		}
	}

	public static void includePagination(CmsSite site,
			Map<String, TemplateModel> params, Environment env)
			throws TemplateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("includePagination(CmsSite, Map<String,TemplateModel>, Environment) - start"); //$NON-NLS-1$
		}

		String sysPage = DirectiveUtils.getString(PARAM_SYS_PAGE, params);
		String sysTopicPage = DirectiveUtils.getString(PARAM_SYS_TOPIC_PAGE, params);
		String userPage = DirectiveUtils.getString(PARAM_USER_PAGE, params);
		if (!StringUtils.isBlank(sysPage)) {
			String tpl = TPL_STYLE_PAGE_CHANNEL + sysPage + TPL_SUFFIX;
			env.include(tpl, UTF8, true);
		}else if (!StringUtils.isBlank(sysTopicPage)) {
			String tpl = TPL_STYLE_PAGE_TOPIC + sysTopicPage + TPL_SUFFIX;
			env.include(tpl, UTF8, true);
		} else if (!StringUtils.isBlank(userPage)) {
			String tpl = getTplPath(site.getSolutionPath(), TPLDIR_STYLE_LIST,
					userPage);
			env.include(tpl, UTF8, true);
		} else {
			// 没有包含分页
		}

		if (logger.isDebugEnabled()) {
			logger.debug("includePagination(CmsSite, Map<String,TemplateModel>, Environment) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 标签中包含页面
	 * 
	 * @param tplName
	 * @param site
	 * @param params
	 * @param env
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void includeTpl(String tplName, CmsSite site,
			Map<String, TemplateModel> params, Environment env)
			throws IOException, TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("includeTpl(String, CmsSite, Map<String,TemplateModel>, Environment) - start"); //$NON-NLS-1$
		}

		String subTpl = DirectiveUtils.getString(PARAM_TPL_SUB, params);
		String tpl;
		if (StringUtils.isBlank(subTpl)) {
			tpl = getTplPath(site.getSolutionPath(), TPLDIR_TAG, tplName);
		} else {
			tpl = getTplPath(site.getSolutionPath(), TPLDIR_TAG, tplName + "_"
					+ subTpl);
		}
		env.include(tpl, UTF8, true);

		if (logger.isDebugEnabled()) {
			logger.debug("includeTpl(String, CmsSite, Map<String,TemplateModel>, Environment) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 标签中包含用户预定义列表样式模板
	 * 
	 * @param listStyle
	 * @param site
	 * @param env
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void includeTpl(String listStyle, CmsSite site,
			Environment env) throws IOException, TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("includeTpl(String, CmsSite, Environment) - start"); //$NON-NLS-1$
		}

		String tpl = getTplPath(site.getSolutionPath(), TPLDIR_STYLE_LIST,
				listStyle);
		env.include(tpl, UTF8, true);

		if (logger.isDebugEnabled()) {
			logger.debug("includeTpl(String, CmsSite, Environment) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 敏感词替换
	 * 
	 * @param s
	 * @return
	 */
	public static String replaceSensitivity(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug("replaceSensitivity(String) - start"); //$NON-NLS-1$
		}

		StringBuilder sb = new StringBuilder();
		if (StringUtils.isBlank(s)) {
			if (logger.isDebugEnabled()) {
				logger.debug("replaceSensitivity(String) - end"); //$NON-NLS-1$
			}
			return s;
		}
		List<CmsSensitivity> sensitivityList = CmsThreadVariable.getSensitivityList();
		if (sensitivityList == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("replaceSensitivity(String) - end"); //$NON-NLS-1$
			}
			return s;
		}
		String[] searchArr = new String[sensitivityList.size()];
		String[] replacementArr = new String[sensitivityList.size()];
		int i = 0;
		for (CmsSensitivity sen : sensitivityList) {
			searchArr[i] = sen.getSearch();
			replacementArr[i] = sen.getReplacement();
			i++;
		}
		sb.append(StringUtils.replaceEach(s, searchArr,
				replacementArr));
		String returnString = sb.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("replaceSensitivity(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
}
