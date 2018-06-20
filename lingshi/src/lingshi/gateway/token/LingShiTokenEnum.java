package lingshi.gateway.token;

/**
 * Token枚举类
 * 
 * @author caich
 *
 */
public class LingShiTokenEnum {

	/**
	 * Token状态
	 *
	 */
	public enum TokenStatus {
		FAIL(0, "校验失败"), SUCCESS(1, "校验成功"), EXP(2, "过期");

		public int value;
		public String valueZh;

		TokenStatus(int value, String valueZh) {
			this.value = value;
			this.valueZh = valueZh;
		}
	}
}
