package lingshi.gateway.token.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Set;
import java.util.Timer;

import lingshi.gateway.token.model.TokenBase;
import lingshi.gateway.token.model.UserToken;
import lingshi.gateway.token.service.TokenPoolBase;
import lingshi.gateway.token.task.TokenClearTask;
import lingshi.model.LingShiConfig;
import lingshi.utilities.DateUtil;

public class TokenPoolMap extends TokenPoolBase {

	protected static Map<String, TokenBase> pool = new HashMap<String, TokenBase>();
	protected static Lock lock = new ReentrantLock();

	private static TokenPoolMap tokenPool = null;

	private TokenPoolMap() {
	}

	public static TokenPoolMap get() {
		if (tokenPool == null) {
			try {
				lock.lock();
				tokenPool = new TokenPoolMap();
				new Timer().schedule(new TokenClearTask(), 0, LingShiConfig.getInstance().getTokenExp());
			} finally {
				lock.unlock();
			}
		}
		return tokenPool;
	}

	@Override
	public TokenBase get(String token) {
		return pool.get(token);
	}

	@Override
	public void add(TokenBase baseToken) {
		// add之前判断重复
		Map<String, TokenBase> map = pool;
		UserToken userToken = (UserToken) baseToken;
		Set<Entry<String, TokenBase>> entries = map.entrySet();
		for (Entry<String, TokenBase> entry : entries) {
			UserToken item = (UserToken) entry.getValue();
			if (item.getData().equals(userToken.getData())) {
				delete(item.getToken());
			}
		}
		if (pool.containsKey(baseToken.getToken())) {
			update(baseToken);
		} else {
			baseToken.setExp(DateUtil.addMilliSecond(LingShiConfig.getInstance().getTokenExp()));
			pool.put(baseToken.getToken(), baseToken);
		}
	}

	@Override
	public void update(TokenBase baseToken) {
		delete(baseToken.getToken());
		baseToken.setExp(DateUtil.addMilliSecond(LingShiConfig.getInstance().getTokenExp()));
		add(baseToken);
	}

	@Override
	public void delete(String token) {
		try {
			lock.lock();
			pool.remove(token);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void clearExp() {
		List<String> removes = new ArrayList<String>();
		Date date = new Date();
		try {
			lock.lock();
			Set<Entry<String, TokenBase>> entries = pool.entrySet();
			for (Entry<String, TokenBase> entry : entries) {
				if (date.after(entry.getValue().getExp())) {
					removes.add(entry.getKey());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		for (String string : removes) {
			delete(string);
		}
	}

	public Map<String, TokenBase> getPool() {
		return pool;
	}

}
