package core;

import org.apache.commons.lang.StringUtils;

public class Util {
	
	public static final String JAVA_HOME = "D:\\tools\\Java\\jdk1.6.0_12";
	public static final String ANDROID_HOME = "D:\\tools\\android-sdk-windows";
	public static final String WORKSPACE = "D:\\temp\\1";

	public static boolean isWin(){
		return StringUtils.startsWithIgnoreCase(System.getProperty("os.name"), "win");
	}
}
