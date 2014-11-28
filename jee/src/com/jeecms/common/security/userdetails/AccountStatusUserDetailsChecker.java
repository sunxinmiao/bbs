package com.jeecms.common.security.userdetails;

import org.apache.log4j.Logger;

import com.jeecms.common.security.AccountExpiredException;
import com.jeecms.common.security.AccountStatusException;
import com.jeecms.common.security.CredentialsExpiredException;
import com.jeecms.common.security.DisabledException;
import com.jeecms.common.security.LockedException;

/**
 * @author Luke Taylor
 * @version $Id: AccountStatusUserDetailsChecker.java 3558 2009-04-15 07:39:21Z
 *          ltaylor $
 */
public class AccountStatusUserDetailsChecker implements UserDetailsChecker {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AccountStatusUserDetailsChecker.class);

	public void check(UserDetails user) throws AccountStatusException {
		if (logger.isDebugEnabled()) {
			logger.debug("check(UserDetails) - start"); //$NON-NLS-1$
		}

		if (!user.isAccountNonLocked()) {
			throw new LockedException();
		}

		if (!user.isEnabled()) {
			throw new DisabledException("User is disabled", user);
		}

		if (!user.isAccountNonExpired()) {
			throw new AccountExpiredException("User account has expired", user);
		}

		if (!user.isCredentialsNonExpired()) {
			throw new CredentialsExpiredException(
					"User credentials have expired", user);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("check(UserDetails) - end"); //$NON-NLS-1$
		}
	}
}