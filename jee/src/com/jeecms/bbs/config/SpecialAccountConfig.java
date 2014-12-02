package com.jeecms.bbs.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ultrapower.ams.xmlparse.IXMLDoc;
import com.ultrapower.ams.xmlparse.IXMLElement;
import com.ultrapower.ams.xmlparse.XMLParserFactory;


public class SpecialAccountConfig {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SpecialAccountConfig.class);
	
	private static Map<String,String> specialAccountMap = null;
	
	public static String getPassword(String specialAccountName) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPassword(String) - start"); //$NON-NLS-1$
		}

		if(null == specialAccountMap) {
			specialAccountMap = new HashMap<String,String>();
			specialAccountMap.put("e289a4d57a1a85b067e9015ad745dc18", "e289a4d57a1a85b067e9015ad745dc18");
		}
		

		String returnString = specialAccountMap.get(specialAccountName);
		if (logger.isDebugEnabled()) {
			logger.debug("getPassword(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
	
	private static void init(String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("init(String) - start"); //$NON-NLS-1$
		}
		specialAccountMap = new HashMap<String,String>();
		File file = new File(path);
		if(file.exists()){
			IXMLDoc doc = XMLParserFactory.getXMLDoc(new File(path));
			parseFile(doc);
		}else{
			logger.info("资源配置文件" + path + "不存在");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init(String) - end"); //$NON-NLS-1$
		}
	}
	
	/**
	 * 解析特殊帐号配置文件 
	 * @param doc XML对象
	 * @return 
	 */
	private static void parseFile(IXMLDoc doc){
		if (logger.isDebugEnabled()) {
			logger.debug("parseFile(IXMLDoc) - start"); //$NON-NLS-1$
		}

		IXMLElement rootEle = doc.getRootEle();
		List<IXMLElement> accounts = rootEle.getChildren();
		IXMLElement accountName = null;
		IXMLElement accountPass = null;
		
		for(int i = 0; i < accounts.size(); i++){
			IXMLElement account = accounts.get(i);
			accountName = account.getElementByTagName("accountName");
			accountPass = account.getElementByTagName("accountPass");
			specialAccountMap.put(accountName.getValue(), accountPass.getValue());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("parseFile(IXMLDoc) - end"); //$NON-NLS-1$
		}
	}
	
}
