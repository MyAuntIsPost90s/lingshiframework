package lingshi.model;

import java.io.Serializable;

public class SOAResponseValue<T> implements Serializable {

	private static final long serialVersionUID = -672112789529120705L;

	private T value;
	private ServiceException exception;
	private Boolean responseError;
	private Boolean responseRight;

	public SOAResponseValue() {
		this.responseError = false;
		this.responseRight = true;
		this.exception = null;
		this.value = null;
	}

	public ServiceException getException() {
		return exception;
	}

	public void setException(ServiceException exception) {
		this.responseError = true;
		this.responseRight = false;
		this.exception = exception;
	}

	public void setException(String msg) {
		setException(new ServiceException(msg));
	}

	public void setException(Exception e) {
		setException(new ServiceException(e));
	}

	public void setException(String msg, Exception e) {
		setException(new ServiceException(msg, e));
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public Boolean getResponseError() {
		return responseError;
	}

	public Boolean getResponseRight() {
		return responseRight;
	}

}
