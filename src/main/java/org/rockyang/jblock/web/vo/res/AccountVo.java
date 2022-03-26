package org.rockyang.jblock.web.vo.res;

import org.rockyang.jblock.chain.Account;

/**
 * account VO
 * @author yangjian
 * @since 18-7-14
 */
public class AccountVo extends Account {

	@Override
	public String toString() {
		return "AccountVo{" +
				"address='" + address + '\'' +
				", priKey='" + priKey + '\'' +
				'}';
	}
}
