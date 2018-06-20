package lingshi.gateway.token.model;

public class UserToken extends TokenBase {
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
