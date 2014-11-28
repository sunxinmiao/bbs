package com.jeecms.common.developer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * 模块生成器
 * 
 * <p>
 * 用于生成JEE模块。
 * <p>
 * 包括JAVA类：action,dao,dao.impl,manager,manager.impl；
 * 配置文件：action配置,spring配置；ftl页面：list.html,add.html,edit.html。
 * 数据验证文件：Act-Com_save
 * -validation.xml,Act-Com_edit-validation.xml,Act-Com_update-validation.xml
 * <p>
 * 可设置的参数有：模块实体类名、java类包地址、配置文件地址、ftl页面地址。
 * 
 * @author liufang
 * 
 */
public class ModuleGenerator {
	private static final Logger logger = Logger.getLogger(ModuleGenerator.class);
	public static final String SPT = File.separator;

	public static final String ENCODING = "UTF-8";

	private Properties prop = new Properties();
	private String packName;
	private String fileName;

	private File daoImplFile;
	private File daoFile;
	private File managerFile;
	private File managerImplFile;
	private File actionFile;
	private File pageListFile;
	private File pageEditFile;
	private File pageAddFile;

	private File daoImplTpl;
	private File daoTpl;
	private File managerTpl;
	private File managerImplTpl;
	private File actionTpl;
	private File pageListTpl;
	private File pageEditTpl;
	private File pageAddTpl;

	public ModuleGenerator(String packName, String fileName) {
		this.packName = packName;
		this.fileName = fileName;
	}

	@SuppressWarnings("unchecked")
	private void loadProperties() {
		if (logger.isDebugEnabled()) {
			logger.debug("loadProperties() - start"); //$NON-NLS-1$
		}

		try {
			logger.debug("packName=" + packName);
			logger.debug("fileName=" + fileName);
			FileInputStream fileInput = new FileInputStream(getFilePath(
					packName, fileName));
			prop.load(fileInput);
			String entityUp = prop.getProperty("Entity");
			logger.debug("entityUp:" + entityUp);
			if (entityUp == null || entityUp.trim().equals("")) {
				logger.warn("Entity not specified, exit!");
				return;
			}
			String entityLow = entityUp.substring(0, 1).toLowerCase()
					+ entityUp.substring(1);
			logger.debug("entityLow:" + entityLow);
			prop.put("entity", entityLow);
			if (logger.isDebugEnabled()) {
				Set ps = prop.keySet();
				for (Object o : ps) {
					logger.debug(o + "=" + prop.get(o));
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("loadProperties()", e); //$NON-NLS-1$

			e.printStackTrace();
		} catch (IOException e) {
			logger.error("loadProperties()", e); //$NON-NLS-1$

			e.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("loadProperties() - end"); //$NON-NLS-1$
		}
	}

	private void prepareFile() {
		if (logger.isDebugEnabled()) {
			logger.debug("prepareFile() - start"); //$NON-NLS-1$
		}

		String daoImplFilePath = getFilePath(prop.getProperty("dao_impl_p"),
				prop.getProperty("Entity") + "DaoImpl.java");
		daoImplFile = new File(daoImplFilePath);
		logger.debug("daoImplFile:" + daoImplFile.getAbsolutePath());

		String daoFilePath = getFilePath(prop.getProperty("dao_p"), prop
				.getProperty("Entity")
				+ "Dao.java");
		daoFile = new File(daoFilePath);
		logger.debug("daoFile:" + daoFile.getAbsolutePath());

		String managerFilePath = getFilePath(prop.getProperty("manager_p"),
				prop.getProperty("Entity") + "Mng.java");
		managerFile = new File(managerFilePath);
		logger.debug("managerFile:" + managerFile.getAbsolutePath());

		String managerImplFilePath = getFilePath(prop
				.getProperty("manager_impl_p"), prop.getProperty("Entity")
				+ "MngImpl.java");
		managerImplFile = new File(managerImplFilePath);
		logger.debug("managerImplFile:" + managerImplFile.getAbsolutePath());
		String actionFilePath = getFilePath(prop.getProperty("action_p"), prop
				.getProperty("Entity")
				+ "Act.java");
		actionFile = new File(actionFilePath);
		logger.debug("actionFile:" + actionFile.getAbsolutePath());

		String pagePath = "WebContent/WEB-INF/"
				+ prop.getProperty("config_sys") + "/"
				+ prop.getProperty("config_entity") + "/";
		pageListFile = new File(pagePath + "list.html");
		logger.debug("pageListFile:" + pageListFile.getAbsolutePath());
		pageEditFile = new File(pagePath + "edit.html");
		logger.debug("pageEditFile:" + pageEditFile.getAbsolutePath());
		pageAddFile = new File(pagePath + "add.html");
		logger.debug("pageAddFile:" + pageAddFile.getAbsolutePath());
	}

	private void prepareTemplate() {
		if (logger.isDebugEnabled()) {
			logger.debug("prepareTemplate() - start"); //$NON-NLS-1$
		}

		String tplPack = prop.getProperty("template_dir");
		logger.debug("tplPack:" + tplPack);
		daoImplTpl = new File(getFilePath(tplPack, "dao_impl.txt"));
		daoTpl = new File(getFilePath(tplPack, "dao.txt"));
		managerImplTpl = new File(getFilePath(tplPack, "manager_impl.txt"));
		managerTpl = new File(getFilePath(tplPack, "manager.txt"));
		actionTpl = new File(getFilePath(tplPack, "action.txt"));
		pageListTpl = new File(getFilePath(tplPack, "page_list.txt"));
		pageAddTpl = new File(getFilePath(tplPack, "page_add.txt"));
		pageEditTpl = new File(getFilePath(tplPack, "page_edit.txt"));

		if (logger.isDebugEnabled()) {
			logger.debug("prepareTemplate() - end"); //$NON-NLS-1$
		}
	}

	private static void stringToFile(File file, String s) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("stringToFile(File, String) - start"); //$NON-NLS-1$
		}

		FileUtils.writeStringToFile(file, s, ENCODING);

		if (logger.isDebugEnabled()) {
			logger.debug("stringToFile(File, String) - end"); //$NON-NLS-1$
		}
	}

	private void writeFile() {
		if (logger.isDebugEnabled()) {
			logger.debug("writeFile() - start"); //$NON-NLS-1$
		}

		try {
			if ("true".equals(prop.getProperty("is_dao"))) {
				stringToFile(daoImplFile, readTpl(daoImplTpl));
				stringToFile(daoFile, readTpl(daoTpl));
			}
			if ("true".equals(prop.getProperty("is_manager"))) {
				stringToFile(managerImplFile, readTpl(managerImplTpl));
				stringToFile(managerFile, readTpl(managerTpl));
			}
			if ("true".equals(prop.getProperty("is_action"))) {
				stringToFile(actionFile, readTpl(actionTpl));
			}
			if ("true".equals(prop.getProperty("is_page"))) {
				stringToFile(pageListFile, readTpl(pageListTpl));
				stringToFile(pageAddFile, readTpl(pageAddTpl));
				stringToFile(pageEditFile, readTpl(pageEditTpl));
			}
		} catch (IOException e) {
			logger.warn("write file faild! " + e.getMessage());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("writeFile() - end"); //$NON-NLS-1$
		}
	}

	private String readTpl(File tpl) {
		if (logger.isDebugEnabled()) {
			logger.debug("readTpl(File) - start"); //$NON-NLS-1$
		}

		String content = null;
		try {
			content = FileUtils.readFileToString(tpl, ENCODING);
			Set<Object> ps = prop.keySet();
			for (Object o : ps) {
				String key = (String) o;
				String value = prop.getProperty(key);
				content = content.replaceAll("\\#\\{" + key + "\\}", value);
			}
		} catch (IOException e) {
			logger.warn("read file faild. " + e.getMessage());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("readTpl(File) - end"); //$NON-NLS-1$
		}
		return content;

	}

	private String getFilePath(String packageName, String name) {
		logger.debug("replace:" + packageName);
		String path = packageName.replaceAll("\\.", "/");
		logger.debug("after relpace:" + path);
		return "src/" + path + "/" + name;
	}

	public void generate() {
		if (logger.isDebugEnabled()) {
			logger.debug("generate() - start"); //$NON-NLS-1$
		}

		loadProperties();
		prepareFile();
		prepareTemplate();
		writeFile();

		if (logger.isDebugEnabled()) {
			logger.debug("generate() - end"); //$NON-NLS-1$
		}
	}

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		String packName = "com.jeecms.common.developer.template";
		String fileName = "template.properties";
		new ModuleGenerator(packName, fileName).generate();

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
}
