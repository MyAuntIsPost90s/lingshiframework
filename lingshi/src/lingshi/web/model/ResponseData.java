package lingshi.web.model;

public class ResponseData {
	private int code;	//返回码
	private String msg;	//返回信息
	private String msgcode;	//信息码
	private Object data;	//数据
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMsgcode() {
		return msgcode;
	}
	public void setMsgcode(String msgcode) {
		this.msgcode = msgcode;
	}
	
	public void success(){
		this.code=ResponseCode.SUCCESS;
		this.msg=null;
		this.data=null;
	}
	public void success(String msg){
		this.code=ResponseCode.SUCCESS;
		this.msg=msg;
		this.data=null;
	}
	public void success(Object obj){
		this.code=ResponseCode.SUCCESS;
		this.msg=null;
		this.data=obj;
	}
	public void success(String msg,Object obj){
		this.code=ResponseCode.SUCCESS;
		this.msg=msg;
		this.data=obj;
	}
	public void success(String msg,Object obj,String msgcode){
		this.code=ResponseCode.SUCCESS;
		this.msg=msg;
		this.data=obj;
		this.msgcode=msgcode;
	}
	
	public void fail(){
		this.code=ResponseCode.FAIL;
		this.msg=null;
		this.data=null;
	}
	public void fail(String msg){
		this.code=ResponseCode.FAIL;
		this.msg=msg;
		this.data=null;
	}
	public void fail(Object data){
		this.code=ResponseCode.FAIL;
		this.msg=null;
		this.data=data;
	}
	public void fail(String msg,Object data){
		this.code=ResponseCode.FAIL;
		this.msg=msg;
		this.data=data;
	}
	public void fail(String msg,Object data,String msgcode){
		this.code=ResponseCode.FAIL;
		this.msg=msg;
		this.data=data;
		this.msgcode=msgcode;
	}
}
