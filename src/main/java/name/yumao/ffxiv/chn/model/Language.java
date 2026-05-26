package name.yumao.ffxiv.chn.model;

public enum Language {
	CHS("Simplified Chinese", "CHS", "chs", "5"),
	CHT("Traditional Chinese", "CHT", "cht", "5"),
	CSV("CSV", "CSV", "csv", "6"),
	JA("Japanese", "JA", "ja", "0"),
	EN("English", "EN", "en-gb", "1"),
	DE("German", "DE", "de", "2"),
	FR("French", "FR", "fr", "3");
	
	private String name;
	private String lang;
	private String code;
	private String lcode;
	
	Language(String name, String lang, String code, String lcode) {
		this.name = name;
		this.lang = lang;
		this.code = code;
		this.lcode = lcode;
	}
	
	public static String toLang(String name) {
		for (Language langs : values()) {
			if (langs.name.equals(name))
				return langs.lang; 
		} 
		return "JA";
	}
	
	public static String toCode(String lang) {
		for (Language langs : values()) {
			if (langs.lang.equals(lang))
				return langs.code; 
		} 
		return "ja";
	}
	
	public static String toLCode(String lang) {
		for (Language langs : values()) {
			if (langs.lang.equals(lang))
				return langs.lcode; 
		} 
		return "0";
	}
	
	public static String getNameByLang(String lang) {
		for (Language langs : values()) {
			if (langs.lang.equals(lang))
				return langs.name; 
		} 
		return "CSV";
	}
	
	public static void main(String[] args) {
		System.out.println(toLang("正體中文"));
	}
}
