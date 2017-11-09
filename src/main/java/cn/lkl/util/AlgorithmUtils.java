package cn.lkl.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class AlgorithmUtils {
	
	/**
	 * @描述：对称加密/解密
	 * @开发人员：likaihao
	 * @开发时间：2015年10月10日 上午10:57:09
	 * @param algorithmName 算法名称
	 * @param isEncrypt 是否是加密
	 * @param content 数据
	 * @param password 密钥
	 * @param keySize 密钥长度,采用密钥的前n位长度进行加密,一般对称加密都提供多种密钥长度供选择,长度越长,安全性越高,如3des提供两种密钥长度:112和168
	 * @return
	 */
	public static byte[] symmetricCipher(String algorithmName, boolean isEncrypt, byte[] content, byte[] password,int keySize){
		try {
			int mode = isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
			
			// 获得 随机数生成器,可以通过密钥生成指定长度的字节数组.
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password);

			// 获得 密钥生成器
			KeyGenerator kgen = KeyGenerator.getInstance(algorithmName);
			kgen.init(keySize, secureRandom);

			// 获得密钥
			SecretKey secretKey = kgen.generateKey();

			// 获得 密码生成器
			Cipher cipher = Cipher.getInstance(kgen.getAlgorithm());
			cipher.init(mode, secretKey);

			// 加密/解密
			return cipher.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 使用AES进行加密
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] encryptAES(byte[] content, byte[] password) {
		int keySize = 128;//128, 192 or 256
		return symmetricCipher("AES",true,content,password,keySize);
	}

	/**
	 * 使用AES进行解密
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] decryptAES(byte[] content, byte[] password) {
		int keySize = 128;//128, 192 or 256
		return symmetricCipher("AES",false,content,password,keySize);
	}
	
	/**
	 * 使用DES进行加密
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] encryptDES2(byte[] content, byte[] password) {
		int keySize = 56;//只有56
		return symmetricCipher("DES",true,content,password,keySize);
	}

	/**
	 * 使用DES进行解密
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] decryptDES2(byte[] content, byte[] password) {
		int keySize = 56;//只有56
		return symmetricCipher("DES",false,content,password,keySize);
	}
	
	/**
	 * 使用3DES进行加密
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] encrypt3DES(byte[] content, byte[] password) {
		int keySize = 112;//112 , 168
		return symmetricCipher("DESede",true,content,password,keySize);
	}

	/**
	 * 使用3DES进行解密
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] decrypt3DES(byte[] content, byte[] password) {
		int keySize = 112;//112 , 168
		return symmetricCipher("DESede",false,content,password,keySize);
	}
	
	/**
     * 使用DES进行加密(使用SecretKeyFactory获取秘钥)
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
	public static byte[] encryptDES(byte[] content, byte[] password){
        try {
			// 生成一个可信任的随机数源
			//SecureRandom sr = new SecureRandom();
 
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(password);
 
			// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
 
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
 
			// 用密钥初始化Cipher对象
			//cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
			cipher.init(Cipher.ENCRYPT_MODE, securekey);
 
			return cipher.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
     
    /**
     * 使用DES进行解密(使用SecretKeyFactory获取秘钥)
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static byte[] decryptDES(byte[] data, byte[] key){
        try {
			// 生成一个可信任的随机数源
			//SecureRandom sr = new SecureRandom();
 
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);
 
			// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
 
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
 
			// 用密钥初始化Cipher对象
			//cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
			cipher.init(Cipher.DECRYPT_MODE, securekey);
 
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	

	/**
	 * 获得RSA公钥或私钥对象
	 * 
	 * @param keystr
	 * @return
	 */
	private static Key getKey(String keystr) {
		try {
			Key key = null;
			byte[] keyBytes = decodeBase64(keystr);
			// 获得密钥工厂类(KeyFactory)
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			if (keystr.length() == 216) { // 获得公钥,模长为1024则公钥长度为216
				// 获得X509EncodedKeySpec对象
				X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
						keyBytes);
				// 将X509EncodedKeySpec对象转换为 公钥
				key = keyFactory.generatePublic(x509KeySpec);
			} else if (keystr.length() == 848) { // 获得私钥,模长为1024则公钥长度为848
				// 获得PKCS8EncodedKeySpec对象
				PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
						keyBytes);
				// 将PKCS8EncodedKeySpec对象转换为 私钥
				key = keyFactory.generatePrivate(pkcs8KeySpec);
			}
			return key;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * RSA加密或解密
	 * 
	 * @param datastr
	 * @param isPublicKey
	 * @param isEncrypt
	 * @return
	 */
	private static byte[] cipher(byte[] data, String keystr,
			boolean isEncrypt) {
		try {
			int mode = -1; // 加密类型
			int blackSize = -1; // 数据分块大小
			if (isEncrypt) {
				mode = Cipher.ENCRYPT_MODE;
				//dataByte = datastr.getBytes("utf-8");
				//dataByte = parseHexStr2Byte(datastr);
				blackSize = 1024 / 8 - 11;
			} else {
				mode = Cipher.DECRYPT_MODE;
				//dataByte = decodeBase64(datastr);
				blackSize = 1024 / 8;
			}
			Key key = getKey(keystr);// 生成公钥或私钥

			// 获得 密码生成器
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(mode, key);

			// 分段加密/解密
			// (要加密的数据最大长度为 模长/8-11 ,模长在生成公钥和私钥时指定, 这里为1024, 所以数据长度不能超过117)
			// (要解密的数据最大长度为 模长/8 ,模长在生成公钥和私钥时指定, 这里为1024, 所以数据长度不能超过128)
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int count = data.length / blackSize;
			for (int i = 0; i < count; i++) {
				out.write(cipher.doFinal(data, blackSize * i, blackSize));
			}
			int yu = data.length % blackSize;
			if (yu != 0) {
				out.write(cipher.doFinal(data, blackSize * count, yu));
			}
			data = out.toByteArray();
			out.close();

//			if (isEncrypt) {
//				datastr = encodeBase64(dataByte);
//			} else {
//				datastr = new String(dataByte, "utf-8");
//			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 生成RSA私钥和公钥
	 * 
	 * @return
	 */
	public static Map<String, String> createPublicAndPrivateKey(){
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(1024);// RSA模长,太大影响加密效率,太小不安全.要加密的数据最大长度为 模长/8-11
										// ,如果数据过长,要分段加密
			KeyPair keyPair = keyPairGen.generateKeyPair();
			Map<String, String> map = new HashMap<String, String>();
			map.put("publicKey",
					encodeBase64(keyPair.getPublic().getEncoded()));
			map.put("privateKey",
					encodeBase64(keyPair.getPrivate().getEncoded()));
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用公钥加密 RSA
	 * 
	 * @param datastr
	 * @param publicKey
	 * @return
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey){
		return cipher(data, publicKey, true);
	}

	/**
	 * 使用私钥加密 RSA
	 * 
	 * @param datastr
	 * @param privateKey
	 * @return
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey){
		return cipher(data, privateKey, true);
	}

	/**
	 * 使用公钥解密 RSA
	 * 
	 * @param datastr
	 * @param publicKey
	 * @return
	 */
	public static byte[] decryptByPublicKey(byte[] data, String publicKey){
		return cipher(data, publicKey, false);
	}

	/**
	 * 使用私钥解密 RSA
	 * 
	 * @param datastr
	 * @param privateKey
	 * @return
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String privateKey){
		return cipher(data, privateKey, false);
	}

	/**
	 * 获得数字签名 RSA (先md5或sha1摘要,再对摘要RSA加密)
	 * @param datastr 要进行签名的数据
	 * @param privateKey 私钥
	 * @return
	 */
	public static String sign(byte[] dataByte, String privateKey) {
		try {
			Key key = getKey(privateKey);

			Signature signature = Signature.getInstance("SHA1WithRSA");
			signature.initSign((PrivateKey) key);// 初始化
			signature.update(dataByte);// 更新要前面的数据
			return encodeBase64(signature.sign());// 签名
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * 验证数字签名 RSA
	 * 
	 * @param datastr
	 * @param signstr
	 * @param publicKey
	 * @return
	 */
	public static boolean verifySign(byte[] dataByte, String signstr,
			String publicKey) {
		try {
			byte[] signByte = decodeBase64(signstr);
			Key key = getKey(publicKey);

			Signature signature = Signature.getInstance("SHA1WithRSA");
			signature.initVerify((PublicKey) key);// 初始化
			signature.update(dataByte);// 更新要前面的数据
			return signature.verify(signByte);// 验签
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获得哈希算法的值
	 * @param data
	 * @param hashName 算法名称
	 * @return
	 */
	public static byte[] getHashValue(byte[] data,String algorithmName) {
		try {
			// 获得算法
			MessageDigest digest = MessageDigest.getInstance(algorithmName);
			// 加密数据--返回的数组为十进制结果
			return digest.digest(data);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获得MD5值
	 * @param str
	 * @return
	 */
	public static String getMD5Value(String data) {
		try {
			return byteToHexStr(getMD5Value(data.getBytes("utf-8")));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	
	/**
	 * 获得MD5值
	 * @param str
	 * @return
	 */
	public static byte[] getMD5Value(byte[] data) {
		return getHashValue(data,"md5");
	}
	
	/**
	 * 获得SHA1值
	 * @param str
	 * @return
	 */
	public static String getSHA1Value(String data) {
		try {
			return byteToHexStr(getSHA1Value(data.getBytes("utf-8")));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * 获得sha1值
	 * @param data
	 * @return
	 */
	public static byte[] getSHA1Value(byte[] data) {
		return getHashValue(data,"SHA-1");
	}
	
	/**
	 * 获得sha256值
	 * @param data
	 * @return
	 */
	public static byte[] getSHA256Value(byte[] data) {
		return getHashValue(data,"SHA-256");
	}
	
	private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D',
		'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
		'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
		'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
		'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
		'4', '5', '6', '7', '8', '9', '+', '/' };
	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
			60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
			-1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
			-1, -1 };
	
	/**
	 * base64编码
	 * @param data
	 * @return
	 */
	public static String encodeBase64(byte[] data) {
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		int b1, b2, b3;
		while (i < len) {
			b1 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4)
						| ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(base64EncodeChars[b1 >>> 2]);
			sb.append(base64EncodeChars[((b1 & 0x03) << 4)
					| ((b2 & 0xf0) >>> 4)]);
			sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
					| ((b3 & 0xc0) >>> 6)]);
			sb.append(base64EncodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}
	
	/**
	 * base64解码
	 * @param str
	 * @return
	 */
	public static byte[] decodeBase64(String str){
		try {
			StringBuffer sb = new StringBuffer();
			byte[] data = str.getBytes("US-ASCII");
			int len = data.length;
			int i = 0;
			int b1, b2, b3, b4;
			while (i < len) {
				do {
					b1 = base64DecodeChars[data[i++]];
				} while (i < len && b1 == -1);
				if (b1 == -1)
					break;
	
				do {
					b2 = base64DecodeChars[data[i++]];
				} while (i < len && b2 == -1);
				if (b2 == -1)
					break;
				sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
	
				do {
					b3 = data[i++];
					if (b3 == 61)
						return sb.toString().getBytes("iso8859-1");
					b3 = base64DecodeChars[b3];
				} while (i < len && b3 == -1);
				if (b3 == -1)
					break;
				sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
	
				do {
					b4 = data[i++];
					if (b4 == 61)
						return sb.toString().getBytes("iso8859-1");
					b4 = base64DecodeChars[b4];
				} while (i < len && b4 == -1);
				if (b4 == -1)
					break;
				sb.append((char) (((b3 & 0x03) << 6) | b4));
			}
			return sb.toString().getBytes("iso8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 将字节数组转换为16进制字符串形式(字节-->表意字节字符串,16进制每个字节显示为固定2位字符,可将字节数组显示为字符串)
	 * @param bytes
	 * @return
	 */
	public static String byteToHexStr(byte[] bytes) {
		if (bytes == null){
			return null;
		}
		StringBuffer output = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			int current = bytes[i] & 0xff;
			if (current < 16){
				output.append("0");
			}
			output.append(Integer.toString(current, 16));
		}
		return output.toString();
	}
	
	/**将16进制字符串转换为字节数组(表意字节字符串-->字节数组,16进制固定2个字符转换为一个字节)
     * @param hexStr
     * @return 经字节数组编码可得到原始字符串
     */
    public static byte[] hexStrToByte(String hexStr) {
    	if (hexStr.length() < 1){
    		return null;
    	}
    	byte[] result = new byte[hexStr.length()/2];
    	for (int i = 0;i< hexStr.length()/2; i++) {
    		int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
    		int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
    		result[i] = (byte) (high * 16 + low);
    	}
    	return result;
    }
    
    
    
	
}
