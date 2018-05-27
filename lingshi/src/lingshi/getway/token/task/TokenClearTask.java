package lingshi.getway.token.task;

import java.util.TimerTask;

import org.apache.log4j.Logger;

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
		Logger.getRootLogger().info("BeginClearToken:" + DateUtil.format(DateUtil.YYYYMMDDHHMMSSSpt));
		TokenPoolMap tokenPool=TokenPoolMap.get();
		tokenPool.clearExp();
		Logger.getRootLogger().info("EndClearToken:" + DateUtil.format(DateUtil.YYYYMMDDHHMMSSSpt));
	}

}
