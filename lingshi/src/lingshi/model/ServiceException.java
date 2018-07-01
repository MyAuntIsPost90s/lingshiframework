package lingshi.model;

public class ServiceException extends Throwable {

	private static final long serialVersionUID = -3405750937683912354L;

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(Exception e) {
		super(e);
	}

	public ServiceException(String msg, Exception e) {
		super(msg, e);
	}

}
