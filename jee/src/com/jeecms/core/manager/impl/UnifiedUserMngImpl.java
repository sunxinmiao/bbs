package com.jeecms.core.manager.impl;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.security.BadCredentialsException;
import com.jeecms.common.security.UsernameNotFoundException;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.core.dao.UnifiedUserDao;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.UnifiedUserMng;

@Service
@Transactional
public class UnifiedUserMngImpl implements UnifiedUserMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UnifiedUserMngImpl.class);

	public UnifiedUser passwordForgotten(Integer userId, EmailSender email,
			MessageTemplate tpl) {
		if (logger.isDebugEnabled()) {
			logger.debug("passwordForgotten(Integer, EmailSender, MessageTemplate) - start"); //$NON-NLS-1$
		}

		UnifiedUser user = findById(userId);
		String uuid = StringUtils.remove(UUID.randomUUID().toString(), '-');
		user.setResetKey(uuid);
		String resetPwd = RandomStringUtils.randomNumeric(10);
		user.setResetPwd(resetPwd);
		senderEmail(user.getId(), user.getUsername(), user.getEmail(), user
				.getResetKey(), user.getResetPwd(), email, tpl);

		if (logger.isDebugEnabled()) {
			logger.debug("passwordForgotten(Integer, EmailSender, MessageTemplate) - end"); //$NON-NLS-1$
		}
		return user;
	}

	private void senderEmail(final Integer uid, final String username,
			final String to, final String resetKey, final String resetPwd,
			final EmailSender email, final MessageTemplate tpl) {
		if (logger.isDebugEnabled()) {
			logger.debug("senderEmail(Integer, String, String, String, String, EmailSender, MessageTemplate) - start"); //$NON-NLS-1$
		}

		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(email.getHost());
		sender.setUsername(email.getUsername());
		sender.setPassword(email.getPassword());
		sender.send(new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws MessagingException, UnsupportedEncodingException {
				if (logger.isDebugEnabled()) {
					logger.debug("$MimeMessagePreparator.prepare(MimeMessage) - start"); //$NON-NLS-1$
				}

				MimeMessageHelper msg = new MimeMessageHelper(mimeMessage,
						false, email.getEncoding());
				msg.setSubject(tpl.getForgotPasswordSubject());
				msg.setTo(to);
				msg.setFrom(email.getUsername(), email.getPersonal());
				String text = tpl.getForgotPasswordText();
				text = StringUtils.replace(text, "${uid}", String.valueOf(uid));
				text = StringUtils.replace(text, "${username}", username);
				text = StringUtils.replace(text, "${resetKey}", resetKey);
				text = StringUtils.replace(text, "${resetPwd}", resetPwd);
				msg.setText(text);

				if (logger.isDebugEnabled()) {
					logger.debug("$MimeMessagePreparator.prepare(MimeMessage) - end"); //$NON-NLS-1$
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("senderEmail(Integer, String, String, String, String, EmailSender, MessageTemplate) - end"); //$NON-NLS-1$
		}
	}
	
	private void senderEmail(final String username, final String to,
			final String activationCode, final EmailSender email,
			final MessageTemplate tpl) {
		if (logger.isDebugEnabled()) {
			logger.debug("senderEmail(String, String, String, EmailSender, MessageTemplate) - start"); //$NON-NLS-1$
		}

		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(email.getHost());
		sender.setUsername(email.getUsername());
		sender.setPassword(email.getPassword());
		sender.send(new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws MessagingException, UnsupportedEncodingException {
				if (logger.isDebugEnabled()) {
					logger.debug("$MimeMessagePreparator.prepare(MimeMessage) - start"); //$NON-NLS-1$
				}

				MimeMessageHelper msg = new MimeMessageHelper(mimeMessage,
						false, email.getEncoding());
				msg.setSubject(tpl.getRegisterSubject());
				msg.setTo(to);
				msg.setFrom(email.getUsername(), email.getPersonal());
				String text = tpl.getRegisterText();
				text = StringUtils.replace(text, "${username}", username);
				text = StringUtils.replace(text, "${activationCode}",
						activationCode);
				msg.setText(text);

				if (logger.isDebugEnabled()) {
					logger.debug("$MimeMessagePreparator.prepare(MimeMessage) - end"); //$NON-NLS-1$
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("senderEmail(String, String, String, EmailSender, MessageTemplate) - end"); //$NON-NLS-1$
		}
	}

	public UnifiedUser resetPassword(Integer userId) {
		if (logger.isDebugEnabled()) {
			logger.debug("resetPassword(Integer) - start"); //$NON-NLS-1$
		}

		UnifiedUser user = findById(userId);
		user.setPassword(pwdEncoder.encodePassword(user.getResetPwd()));
		user.setResetKey(null);
		user.setResetPwd(null);

		if (logger.isDebugEnabled()) {
			logger.debug("resetPassword(Integer) - end"); //$NON-NLS-1$
		}
		return user;
	}

	public UnifiedUser login(String username, String password, String ip)
			throws UsernameNotFoundException, BadCredentialsException {
		if (logger.isDebugEnabled()) {
			logger.debug("login(String, String, String) - start"); //$NON-NLS-1$
		}

		UnifiedUser user = getByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("username not found: "
					+ username);
		}
		if (!pwdEncoder.isPasswordValid(user.getPassword(), password)) {
			throw new BadCredentialsException("password invalid");
		}
		if (!user.getActivation()) {
			throw new BadCredentialsException("account not activated");
		}
		updateLoginInfo(user.getId(), ip);

		if (logger.isDebugEnabled()) {
			logger.debug("login(String, String, String) - end"); //$NON-NLS-1$
		}
		return user;
	}

	public void updateLoginInfo(Integer userId, String ip) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateLoginInfo(Integer, String) - start"); //$NON-NLS-1$
		}

		Date now = new Timestamp(System.currentTimeMillis());
		UnifiedUser user = findById(userId);

		user.setLoginCount(user.getLoginCount() + 1);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);

		if (logger.isDebugEnabled()) {
			logger.debug("updateLoginInfo(Integer, String) - end"); //$NON-NLS-1$
		}
	}

	public boolean usernameExist(String username) {
		if (logger.isDebugEnabled()) {
			logger.debug("usernameExist(String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = getByUsername(username) != null;
		if (logger.isDebugEnabled()) {
			logger.debug("usernameExist(String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public boolean emailExist(String email) {
		if (logger.isDebugEnabled()) {
			logger.debug("emailExist(String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = dao.countByEmail(email) > 0;
		if (logger.isDebugEnabled()) {
			logger.debug("emailExist(String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public UnifiedUser getByUsername(String username) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByUsername(String) - start"); //$NON-NLS-1$
		}

		UnifiedUser returnUnifiedUser = dao.getByUsername(username);
		if (logger.isDebugEnabled()) {
			logger.debug("getByUsername(String) - end"); //$NON-NLS-1$
		}
		return returnUnifiedUser;
	}

	public List<UnifiedUser> getByEmail(String email) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByEmail(String) - start"); //$NON-NLS-1$
		}

		List<UnifiedUser> returnList = dao.getByEmail(email);
		if (logger.isDebugEnabled()) {
			logger.debug("getByEmail(String) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public UnifiedUser findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		UnifiedUser entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public UnifiedUser save(String username, String email, String password,
			String ip) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(String, String, String, String) - start"); //$NON-NLS-1$
		}

		UnifiedUser returnUnifiedUser = save(username, email, password, ip, true, null, null);
		if (logger.isDebugEnabled()) {
			logger.debug("save(String, String, String, String) - end"); //$NON-NLS-1$
		}
		return returnUnifiedUser;
	}
	public UnifiedUser save(String username, String email, String password,
			String ip, Boolean activation, EmailSender sender,
			MessageTemplate msgTpl) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(String, String, String, String, Boolean, EmailSender, MessageTemplate) - start"); //$NON-NLS-1$
		}

		Date now = new Timestamp(System.currentTimeMillis());
		UnifiedUser user = new UnifiedUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(pwdEncoder.encodePassword(password));
		user.setRegisterIp(ip);
		user.setRegisterTime(now);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);
		user.setLoginCount(0);
		user.setActivation(activation);
		dao.save(user);
		if (!activation) {
			String uuid = StringUtils.remove(UUID.randomUUID().toString(), '-');
			user.setActivationCode(uuid);
			senderEmail(username, email, uuid, sender, msgTpl);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("save(String, String, String, String, Boolean, EmailSender, MessageTemplate) - end"); //$NON-NLS-1$
		}
		return user;
	}

	/**
	 * @see UnifiedUserMng#update(Integer, String, String)
	 */
	public UnifiedUser update(Integer id, String password, String email) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(Integer, String, String) - start"); //$NON-NLS-1$
		}

		UnifiedUser user = findById(id);
		if (!StringUtils.isBlank(email)) {
			user.setEmail(email);
		} else {
			user.setEmail(null);
		}
		if (!StringUtils.isBlank(password)) {
			user.setPassword(pwdEncoder.encodePassword(password));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("update(Integer, String, String) - end"); //$NON-NLS-1$
		}
		return user;
	}

	public boolean isPasswordValid(Integer id, String password) {
		if (logger.isDebugEnabled()) {
			logger.debug("isPasswordValid(Integer, String) - start"); //$NON-NLS-1$
		}

		UnifiedUser user = findById(id);
		boolean returnboolean = pwdEncoder.isPasswordValid(user.getPassword(), password);
		if (logger.isDebugEnabled()) {
			logger.debug("isPasswordValid(Integer, String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public UnifiedUser deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		UnifiedUser bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public UnifiedUser[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		UnifiedUser[] beans = new UnifiedUser[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	public UnifiedUser active(String username, String activationCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("active(String, String) - start"); //$NON-NLS-1$
		}

		UnifiedUser bean = getByUsername(username);
		bean.setActivation(true);
		bean.setActivationCode(null);

		if (logger.isDebugEnabled()) {
			logger.debug("active(String, String) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public UnifiedUser activeLogin(UnifiedUser user, String ip) {
		if (logger.isDebugEnabled()) {
			logger.debug("activeLogin(UnifiedUser, String) - start"); //$NON-NLS-1$
		}

		updateLoginSuccess(user.getId(), ip);

		if (logger.isDebugEnabled()) {
			logger.debug("activeLogin(UnifiedUser, String) - end"); //$NON-NLS-1$
		}
		return user;
	}
	public void updateLoginSuccess(Integer userId, String ip) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateLoginSuccess(Integer, String) - start"); //$NON-NLS-1$
		}

		UnifiedUser user = findById(userId);
		Date now = new Timestamp(System.currentTimeMillis());
		user.setLoginCount(user.getLoginCount() + 1);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);

		if (logger.isDebugEnabled()) {
			logger.debug("updateLoginSuccess(Integer, String) - end"); //$NON-NLS-1$
		}
	}

	private PwdEncoder pwdEncoder;
	private UnifiedUserDao dao;

	@Autowired
	public void setPwdEncoder(PwdEncoder pwdEncoder) {
		this.pwdEncoder = pwdEncoder;
	}

	@Autowired
	public void setDao(UnifiedUserDao dao) {
		this.dao = dao;
	}
}