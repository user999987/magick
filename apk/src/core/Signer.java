package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Signer {
	
	private static final ProcessBuilder pb = new ProcessBuilder();
	static{
		pb.redirectErrorStream(true);
	}
	
	private String javaHome;
	private String androidHome;
	
	public Signer(String workSpace, String javaHome, String androidHome) {
		File workSpaceFile = new File(workSpace);
		if(!workSpaceFile.exists())
			workSpaceFile.mkdir();
		pb.directory(workSpaceFile);
		this.javaHome = javaHome;
		this.androidHome = androidHome;
	}
	
	/**
	 * 创建密钥
	 * @param alias 别名
	 * @param keyalg 签名加密的算法 EX: RSA
	 * @param validity 有效期限(天)
	 * @return
	 * @throws Throwable
	 */
	public List<String> genkey(String alias, String keyalg, int validity) throws Throwable{
		//%JAVA_HOME%\bin\keytool.exe -genkey -alias debug.keystore -keyalg RSA -validity 20000 -keystore debug.keystore
		List<String> command = new ArrayList<String>();
		if(Util.isWin()){
			command.add("cmd");
			command.add("/c");
			command.add("start");
		}
		command.add(javaHome + File.separator + "bin" + File.separator + "keytool");
		command.add("-genkey");
		command.add("-alias");
		command.add(alias);
		command.add("-keyalg");
		command.add(keyalg);
		command.add("-validity");
		command.add(String.valueOf(validity));
		command.add("-keystore");
		command.add(alias);
		pb.command(command);
		Process process = pb.start();
		return IOUtils.readLines(process.getInputStream());
	}
	
	/**
	 * 签名
	 * @param keystore 密钥库位置
	 * @param signedjar 要签名的文件(不包含后缀)
	 * @return
	 * @throws Throwable 
	 */
	public List<String> signer(String keystore, String unsigned) throws Throwable {
		//%JAVA_HOME%\bin\jarsigner -verbose -keystore debug.keystore -signedjar demo_signed.apk demo.apk debug.keystore
		List<String> command = new ArrayList<String>();
		if(Util.isWin()){
			command.add("cmd");
			command.add("/c");
			command.add("start");
		}
		command.add(javaHome + File.separator + "bin" + File.separator + "jarsigner");
		command.add("-verbose");
		command.add("-keystore");
		command.add(keystore);
		command.add("-signedjar");
		command.add(unsigned+ "_signed.apk");
		command.add(unsigned+ ".apk");
		command.add(unsigned+ ".keystore");
		pb.command(command);
		Process process = pb.start();
		return IOUtils.readLines(process.getInputStream());
	}
	
	/**
	 * 对齐
	 * @param signed 签名后的的文件(不包含后缀)
	 * @return
	 * @throws Throwable
	 */
	public List<String> align(String signed) throws Throwable {
		//%ANDROID_HOME%\tools\zipalign -v 4 demo_signed.apk demo_signed_aligned.apk
		List<String> command = new ArrayList<String>();
		if(Util.isWin()){
			command.add("cmd");
			command.add("/c");
			command.add("start");
		}
		command.add(androidHome + File.separator + "tools" + File.separator + "zipalign");
		command.add("-v");
		command.add("4");
		command.add(signed + ".apk");
		command.add(signed + "_aligned.apk");
		pb.command(command);
		Process process = pb.start();
		return IOUtils.readLines(process.getInputStream());
	}
	
	public static void main(String[] args) throws Throwable {
		Signer signer = new Signer(Util.WORKSPACE, Util.JAVA_HOME, Util.ANDROID_HOME);
		signer.genkey("debug.keystore", "RSA", 365);
	}
	
}
