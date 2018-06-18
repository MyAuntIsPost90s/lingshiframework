package lingshi.getway.token.task;

import java.util.TimerTask;

import lingshi.getway.token.service.impl.TokenPoolMap;
import lingshi.utilities.DateUtil;

/**
 * 清除过期的token
 * 
 * @author caich
 *
 */
public class TokenClearTask extends TimerTask {

	@Override
	public void run() {
		System.out.println("BeginClearToken:" + DateUtil.format(DateUtil.YYYYMMDDHHMMSSSpt));
		TokenPoolMap tokenPool = TokenPoolMap.get();
		tokenPool.clearExp();
		System.out.println("EndClearToken:" + DateUtil.format(DateUtil.YYYYMMDDHHMMSSSpt));
	}

}
