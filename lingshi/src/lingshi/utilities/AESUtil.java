package lingshi.utilities;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESUtil {
	/*
	 * 加密 1.构造密钥生成器 2.根据ecnodeRules规则初始化密钥生成器 3.产生密钥 4.创建和初始化密码器 5.内容加密 6.返回字符串
	 */
	public static String AESEncode(String encodeRules, String content) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(encodeRules.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
		byte[] byteContent = content.getBytes("utf-8");
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(byteContent);
		// 10.将加密后的数据转换为字符串
		// 这里用Base64Encoder中会找不到包
		// 解决办法：
		// 在项目的Build path中先移除JRE System Library，再添加库JRE System
		// Library，重新编译后就一切正常了。
		String AES_encode = new String(new BASE64Encoder().encode(result));
		// 11.将字符串返回
		return AES_encode;
	}

	/*
	 * 解密 解密过程： 1.同加密1-4步 2.将加密后的字符串反纺成byte[]数组 3.将加密内容解密
	 */
	public static String AESDncode(String encodeRules, String content) throws Exception {
		// 1.构造密钥生成器，指定为AES算法,不区分大小写
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		// 2.根据ecnodeRules规则初始化密钥生成器
		// 生成一个128位的随机源,根据传入的字节数组
		keygen.init(128, new SecureRandom(encodeRules.getBytes()));
		// 3.产生原始对称密钥
		SecretKey original_key = keygen.generateKey();
		// 4.获得原始对称密钥的字节数组
		byte[] raw = original_key.getEncoded();
		// 5.根据字节数组生成AES密钥
		SecretKey key = new SecretKeySpec(raw, "AES");
		// 6.根据指定算法AES自成密码器
		Cipher cipher = Cipher.getInstance("AES");
		// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
		cipher.init(Cipher.DECRYPT_MODE, key);
		// 8.将加密并编码后的内容解码成字节数组
		byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
		/*
		 * 解密
		 */
		byte[] byte_decode = cipher.doFinal(byte_content);
		String AES_decode = new String(byte_decode, "utf-8");
		return AES_decode;
	}
}
