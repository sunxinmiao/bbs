package com.jeecms.common.lucene;

import org.apache.log4j.Logger;

import java.math.BigDecimal;

import org.apache.lucene.util.NumericUtils;
import org.springframework.util.Assert;

/**
 * 将BigDecimal类型的金额转换成String类型，便于Lucene搜索。
 * 
 * @author liufang
 * 
 */
public class MoneyTools {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MoneyTools.class);

	private static final BigDecimal MULTIPLE = new BigDecimal(1000);

	/**
	 * 将money*1000，转换成long，再转换成string。
	 * 
	 * @param money
	 * @return
	 * @see NumberTools#longToString(long)
	 */
	public static String moneyToString(BigDecimal money) {
		if (logger.isDebugEnabled()) {
			logger.debug("moneyToString(BigDecimal) - start"); //$NON-NLS-1$
		}

		Assert.notNull(money);
		String returnString = NumericUtils.longToPrefixCoded(money.multiply(MULTIPLE).longValue());
		if (logger.isDebugEnabled()) {
			logger.debug("moneyToString(BigDecimal) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 将s转换成long，再转换成BigDecimal，除以1000。
	 * 
	 * @param s
	 * @return
	 */
	public static BigDecimal stringToMoney(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug("stringToMoney(String) - start"); //$NON-NLS-1$
		}

		BigDecimal number = new BigDecimal(NumericUtils.prefixCodedToLong(s));
		BigDecimal returnBigDecimal = number.divide(MULTIPLE);
		if (logger.isDebugEnabled()) {
			logger.debug("stringToMoney(String) - end"); //$NON-NLS-1$
		}
		return returnBigDecimal;
	}
}
