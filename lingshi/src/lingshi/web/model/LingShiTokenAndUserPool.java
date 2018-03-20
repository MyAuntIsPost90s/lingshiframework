package lingshi.web.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import lingshi.utilities.DESHelper;
import lingshi.utilities.EncryptHelper;

public class LingShiTokenAndUserPool {
	private static LingShiTokenAndUserPool lingShiTokenAndUserPool = null;
	private static Map<String, LingShiToken> tokenandusers = null;
	private static Lock lock = new ReentrantLock();
	private static Thread clearthread = null;
	private static Runnable clearRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				// 每隔2小时分钟清除过期token
				while (true) {
					Thread.sleep(1000 * 60 * 60 * 2);
					if (tokenandusers == null) {
						return;
					}

					lock.lock();
					Date date = new Date();
					Logger.getRootLogger().info("BeginClearToken:" + date);
					for (Entry<String, LingShiToken> entry : tokenandusers.entrySet()) {
						String key = entry.getKey();
						if (tokenandusers.get(key).getExp().getTime() < date.getTime()) {
							tokenandusers.remove(key);
						}
					}
					Logger.getRootLogger().info("EndClearToken:" + new Date());
					lock.unlock();
				}
			} catch (Exception e) {
				// 打log
				Logger.getRootLogger().error(e);
			} finally {
				lock.unlock();
			}
		}
	};

	private LingShiTokenAndUserPool() {
	}

	public static LingShiTokenAndUserPool getLingShiTokenAndUserPool() {
		if (lingShiTokenAndUserPool == null) {
			lock.lock();
			if (lingShiTokenAndUserPool == null) {
				lingShiTokenAndUserPool = new LingShiTokenAndUserPool();

				try {
					// 启动线程
					clearthread = new Thread(clearRunnable);
					clearthread.start();
					Logger.getRootLogger().info("ClearTokenThread Start:" + new Date());
				} catch (Exception e) {
					// 写日志
					Logger.getRootLogger().error(e);
				}
			}
			lock.unlock();
		}

		return lingShiTokenAndUserPool;
	}

	public Map<String, LingShiToken> getTokenPool() {
		if (tokenandusers == null) {
			lock.lock();
			if (tokenandusers == null) {
				tokenandusers = new HashMap<>();
			}
			lock.unlock();
		}
		return tokenandusers;
	}

	/**
	 * 添加用户到token池中
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public LingShiToken addTokenUser(Object user, String appKey) {
		Map<String, LingShiToken> map = getTokenPool();

		lock.lock();
		try {
			Set<Entry<String, LingShiToken>> entries = map.entrySet();
			for (Entry<String, LingShiToken> entry : entries) {
				if (entry.getValue().getData().equals(user)) {
					map.remove(entry.getKey());
					break;
				}
			}

			// 不存在时签发token
			LingShiToken token = new LingShiToken();
			String id = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
			String key = EncryptHelper.EncoderByMd5(DESHelper.encode(appKey, id)).toUpperCase(); // 先通过appkey生成秘钥，再通过MD5加密
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.add(Calendar.HOUR, 2);
			token.setExp(calendar.getTime());
			token.setId(id);
			token.setToken(key);
			token.setData(user);

			tokenandusers.put(token.getToken(), token);
			return token;
		} catch (Exception e) {
			Logger.getRootLogger().error(e);
		} finally {
			lock.unlock();
		}
		return null;
	}

	/**
	 * 更新token
	 * 
	 * @param user
	 * @return
	 */
	public LingShiToken updateTokenUser(Object user) {
		Map<String, LingShiToken> map = getTokenPool();

		lock.lock();
		try {
			Set<Entry<String, LingShiToken>> entries = map.entrySet();
			for (Entry<String, LingShiToken> entry : entries) {
				if (entry.getValue().getData().equals(user)) {
					LingShiToken token = entry.getValue();
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(new Date());
					calendar.add(Calendar.HOUR, 2);
					token.setExp(calendar.getTime());
					token.setData(user);
					return token;
				}
			}
		} catch (Exception e) {
			Logger.getRootLogger().error(e);
		} finally {
			lock.unlock();
		}
		return null;
	}

	/**
	 * 获取Token
	 * 
	 * @param token
	 * @return
	 */
	public LingShiToken getToken(String token) {
		Map<String, LingShiToken> map = getTokenPool();
		LingShiToken result = null;

		if (map.containsKey(token)) {
			result = map.get(token);
		}
		return result;
	}

	/**
	 * 移除token
	 * 
	 * @param user
	 */
	public void removeTokenUser(String token) {
		Map<String, LingShiToken> map = getTokenPool();
		if (map.containsKey(token)) {
			lock.lock();
			if (map.containsKey(token)) {
				map.remove(token);
			}
			lock.unlock();
		}
	}

	/**
	 * 校验Token
	 * 
	 * @param appkey
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public int checkToken(String appkey, String token) {
		Map<String, LingShiToken> map = getTokenPool();

		try {
			Date date = new Date();
			if (map.containsKey(token)) {
				if (map.get(token).getExp().getTime() < date.getTime()) {
					return LingShiTokenCode.EXP;
				}
				String key = EncryptHelper.EncoderByMd5(DESHelper.encode(appkey, map.get(token).getId())).toUpperCase();
				if (!token.equals(key)) { // 校验失败
					return LingShiTokenCode.FAIL;
				}
				return LingShiTokenCode.SUCCESS; // 校验成功
			}
		} catch (Exception e) {
			Logger.getRootLogger().error(e);
		}

		// 校验失败
		return LingShiTokenCode.FAIL;
	}
}
