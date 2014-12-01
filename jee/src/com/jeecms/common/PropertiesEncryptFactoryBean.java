package com.jeecms.common;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

import com.jeecms.common.util.StrUtil;
import com.ultrapower.algorithm.Algorithm;
import com.ultrapower.algorithm.impl.AMSBase3DES;
import com.ultrapower.encrypt.encryptkey.KeyGenerator;
import com.ultrapower.encrypt.encryptkey.impl.AMSEncryptKey;
import com.ultrapower.exception.EncryptException;

/**
 * 对数据库用户名密码加解密
 * @author sxm
 *
 */
public class PropertiesEncryptFactoryBean implements FactoryBean {

	private Properties properties;

	public Object getObject() throws Exception {
		return getProperties();
	}

	public Class getObjectType() {
		return java.util.Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties inProperties) {
		this.properties = inProperties;
		String originalUsername = properties.getProperty("user");
		String originalPassword = properties.getProperty("password");
		if (originalUsername != null) {
			String newUsername = decrypt(originalUsername);
			properties.put("user", newUsername);
		}
		if (originalPassword != null) {
			String newPassword = decrypt(originalPassword);
			properties.put("password", newPassword);
		}
	}

	 /**
     * 加密算法
     * @param s
     * @return
     */
    public static String encrypt(String s) {
        if(StrUtil.isNullStr(s)) {
            return s;
        }
        try {
            // 获得生成加密key的实现类
            KeyGenerator keyGenerator = new AMSEncryptKey();
            // 获得加密算法实现类
            Algorithm algorithm = new AMSBase3DES();
        
            // 获得加密种子
            String seed = keyGenerator.getEncryptSeed("");
            // 获得加密盐
            String salt = keyGenerator.getEncryptSalt("");
            
            // 加密
            return algorithm.encrypt(s, seed, salt);
        } catch (EncryptException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 解密算法
     * @param s
     * @return
     */
    public static String decrypt(String s) {
        if(StrUtil.isNullStr(s)) {
            return s;
        }
        try {
         // 获得生成加密key的实现类
            KeyGenerator keyGenerator = new AMSEncryptKey();
            // 获得加密算法实现类
            Algorithm algorithm = new AMSBase3DES();
        
            // 获得解密时候使用的种子
            String decryptSeed = keyGenerator.revertSeed(s, "");
            // 获得解密时候使用的盐
            String decryptSalt = keyGenerator.revertSalt(s, "");
            // 解密
            return algorithm.decrypt(s, decryptSeed, decryptSalt);
        } catch (EncryptException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
