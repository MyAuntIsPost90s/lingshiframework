package lingshi.gateway.token.service;

import lingshi.gateway.token.LingShiTokenEnum.TokenStatus;

public interface TokenMgrService {

	TokenStatus tokenCheck(String token);

	String createTokenStr(String appKey);

}
