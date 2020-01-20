package client;

public class User {
	private String name;
	private String ip;
	private String history;
	private String key;
	public User(String name,String ip,String history) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.ip=ip;
		this.history = history;
		this.key = null;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getHistory(){
		return history;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
	
	public void cleanKey(){
		this.key = null;
	}
	
	public void addhistory(String command){
		this.history = this.history + command + "\n";
	}
}
