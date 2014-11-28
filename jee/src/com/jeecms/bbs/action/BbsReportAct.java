package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsReport;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsReportExtMng;
import com.jeecms.bbs.manager.BbsReportMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.page.SimplePage;
import com.jeecms.common.web.CookieUtils;

@Controller
public class BbsReportAct {
	private static final Logger logger = Logger.getLogger(BbsReportAct.class);

	@RequestMapping("/report/v_list.do")
	public String list(Boolean status, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(Boolean, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		if(status==null){
			status=false;
		}
		Pagination pagination = reportMng.getPage(status, SimplePage
				.cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("status", status);

		if (logger.isDebugEnabled()) {
			logger.debug("list(Boolean, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "report/list";
	}

	@RequestMapping("/report/v_more.do")
	public String more(Integer reportId, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("more(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		Pagination pagination = reportExtMng.getPage(reportId, SimplePage
				.cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("reportId", reportId);

		if (logger.isDebugEnabled()) {
			logger.debug("more(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "report/more";
	}

	@RequestMapping("/report/o_process.do")
	public String process(Integer[] ids,Integer[] points,String[] results, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("process(Integer[], Integer[], String[], HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsReport report;
		BbsUser reportUser;
		BbsUser user=CmsUtils.getUser(request);
		Calendar cal=Calendar.getInstance();
		for(int i=0;i<ids.length;i++){
			report=reportMng.findById(ids[i]);
			reportUser=report.getReportExt().getReportUser();
			userMng.updatePoint(reportUser.getId(), points[i], null, null,0, -1);
			report.setProcessResult(results[i]);
			report.setProcessTime(cal.getTime());
			report.setProcessUser(user);
			report.setStatus(true);
			reportMng.update(report);
		}
		String returnString = list(false, 1, request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("process(Integer[], Integer[], String[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/report/o_delete.do")
	public String delete(Boolean status, Integer[] ids, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Boolean, Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsReport[] beans = reportMng.deleteByIds(ids);
		for (BbsReport bean : beans) {
			logger.info("delete BbsReport id="+ bean.getId());
		}
		String returnString = list(status, pageNo, request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Boolean, Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@Autowired
	private BbsReportExtMng reportExtMng;
	@Autowired
	private BbsReportMng reportMng;
	@Autowired
	private BbsUserMng userMng;
}