package des;

public class DES_object {
	
	private String key;
	private String data;
	private String mode;
	
	public DES_object(String k, String d, String m){
		this.key = k;
		this.data = d;
		this.mode = m;
	}
	
	public String getkey() {
		return key;
	}
	
	public String getdata() {
		return data;
	}
	
	public String getmode() {
		return mode;
	}
}
