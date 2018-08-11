package tvtrader.utils;

public enum Encoding {
	SHA1("HmacSHA1"),
	SHA224("HmacSHA224"),
	SHA256("HmacSHA256"),
	SHA384("HmacSHA384"),
	SHA512("HmacSHA512");
	
	String hash;
	
	Encoding(String hash) {
		this.hash = hash;
	}
	
	public String getEncoding() {
		return hash;
	}
}
