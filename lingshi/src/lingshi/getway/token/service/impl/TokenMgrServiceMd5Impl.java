package lingshi.getway.token.service.impl;

import java.util.Date;
import java.util.UUID;

import lingshi.getway.token.LingShiTokenEnum.TokenStatus;
import lingshi.getway.token.model.TokenBase;
import lingshi.getway.token.service.TokenMgrService;
import lingshi.getway.token.service.TokenPoolBase;
import lingshi.utilities.DESUtil;
import lingshi.utilities.EncryptUtil;

public class TokenMgrServiceMd5Impl implements TokenMgrService {

	@Override
	public TokenStatus tokenCheck(String token) {
		TokenPoolBase tokenPool = TokenPoolMap.get();
		TokenBase tokenBase = tokenPool.get(token);
		if (tokenBase == null) {
			return TokenStatus.FAIL;
		}
		if (tokenBase.getExp().before(new Date())) {
			return TokenStatus.EXP;
		}
		return TokenStatus.SUCCESS;
	}

	@Override
	public String createTokenStr(String appKey) {
		String token = null;
		try {
			String id = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
			token = EncryptUtil.EncoderByMd5(DESUtil.encode(appKey, id)).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

}
