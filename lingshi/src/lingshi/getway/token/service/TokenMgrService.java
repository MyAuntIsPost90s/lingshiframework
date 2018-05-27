package lingshi.getway.token.service;

import lingshi.getway.token.LingShiTokenEnum.TokenStatus;

public interface TokenMgrService {

	TokenStatus tokenCheck(String token);

	String createTokenStr(String appKey);

}
