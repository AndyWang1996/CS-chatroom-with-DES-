package des;

import java.io.IOException;
import java.util.*;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Des {
	public static class DES {
	    // 初次置换IP表
	    private static int[] IP_Table = { 
	    		58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14,
	            6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45,
	            37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };
	    
	    // 逆置换IP-1表
	    private static int[] IPR_Table = { 
	    		40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62,
	            30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42,
	            10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };
	    
	    // E位选择表(扩展置换表)
	    private static int[] E_Table = { 
	    		32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17,
	            18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };
	   
	    // P换位表(单纯换位表)
	    private static int[] P_Table = { 
	    		16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9,
	            19, 13, 30, 6, 22, 11, 4, 25 };
	   
	    // PC1选位表(密钥生成置换表1)
	    private static int[] PC1_Table = { 
	    		57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11,
	            3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13,
	            5, 28, 20, 12, 4 };
	   
	    // PC2选位表(密钥生成置换表2)
	    private static int[] PC2_Table = { 
	    		14, 17, 11, 24, 1,  5,  3,  28, 15, 6,  21, 10, 23, 19, 12, 4,  26, 8,  16, 7,  27, 20, 13, 2,
	            41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };
	   
	    // 左移位数表
	    private static int[] LOOP_Table = { 
	    		1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
	    
	    // S盒
	    private static int[][] S_Box = {
	            // S1
	            { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3,
	                    8, 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10,
	                    0, 6, 13 },
	            // S2
	            { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11,
	                    5, 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0,
	                    5, 14, 9 },
	            // S3
	            { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15,
	                    1, 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11,
	                    5, 2, 12 },
	            // S4
	            { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14,
	                    9, 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12,
	                    7, 2, 14 },
	            // S5
	            { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8,
	                    6, 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9,
	                    10, 4, 5, 3 },
	            // S6
	            { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3,
	                    8, 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6,
	                    0, 8, 13 },
	            // S7
	            { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8,
	                    6, 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14,
	                    2, 3, 12 },
	            // S8
	            { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9,
	                    2, 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3,
	                    5, 6, 11 } };

	    /**
	     * @Descroption:生成16轮密钥组k1-k16
	     * @param String
	     * @return StringBuffer*/
	    public static StringBuffer[] getSubKey(String key) {
	        StringBuffer[] subKey = new StringBuffer[16]; // 最终输出的子密钥组

	        //1、将原密钥转换成二进制数组（只由0,1构成）
	        StringBuffer keyBinary = new StringBuffer(); 
	        for (int i = 0; i < 8; ++i) {//这里的循环条件将其拆分为8位一组
	            StringBuffer mSubKeyTemp = new StringBuffer(Integer.toBinaryString(key.charAt(i)));
	            while (mSubKeyTemp.length() < 8) {//default 不足位用0补齐
	                mSubKeyTemp.insert(0, 0);
	            }
	            keyBinary.append(mSubKeyTemp);//初始密钥key0
	        }

	        //2、通过PC1置换密钥
	        StringBuffer substituteKey = new StringBuffer(); // 存储PC1置换后的密钥
	        for (int i = 0; i < 56; ++i) {
	            substituteKey.append(keyBinary.charAt(PC1_Table[i] - 1));
	        }

	        //3、将密钥压缩为56位并拆分成两部分
	        //这里由于C0D0的置换表是以原密钥的64位计算的，也可以不经过PC1直接置换来实现
	        StringBuffer C0 = new StringBuffer(); // 存储密钥左块
	        StringBuffer D0 = new StringBuffer(); // 存储密钥右块
	        C0.append(substituteKey.substring(0, 28));
	        D0.append(substituteKey.substring(28));

	        //4、循环16轮生成子密钥
	        for (int i = 0; i < 16; ++i) {
	            // 根据LOOP_Table循环左移
	            for (int j = 0; j < LOOP_Table[i]; ++j) {
	                char mTemp;
	                //左移变化的永远是最左边的（第一位）数据
	                //因此左移的基本思路是把第一位去掉，放到最后一位去
	                mTemp = C0.charAt(0);
	                C0.deleteCharAt(0);
	                C0.append(mTemp);
	                mTemp = D0.charAt(0);
	                D0.deleteCharAt(0);
	                D0.append(mTemp);
	            }

	            // 把左右两块合并
	            StringBuffer C0D0 = new StringBuffer(C0.toString() + D0.toString());

	            // 根据PC2压缩C0D0，得到48位子密钥
	            StringBuffer C0D0Temp = new StringBuffer();
	            for (int j = 0; j < 48; ++j) {
	                C0D0Temp.append(C0D0.charAt(PC2_Table[j] - 1));
	            }

	            // 把此轮产生的子密钥存储到数组中
	            subKey[i] = C0D0Temp;
	        }
	        return subKey;
	    }
	    
	    /**
	     * @Descroption:将String用Base64编码
	     * @param String
	     * @return byte[]*/
	    public static String jdkBase64String(byte[] secretKey) {
	        Encoder encoder = Base64.getEncoder();
	        return encoder.encodeToString(secretKey);
	    }
	    
	    /**
	     * @Descroption:将String用Base64解码
	     * @param String
	     * @return byte[]*/
	    public static byte[] jdkBase64Decoder(String str) throws IOException {
	        Decoder decoder = Base64.getDecoder();
	        return decoder.decode(str);
	    }
	    
	    /**
	     * @Descroption:将String转byte
	     * @param byte[]
	     * @return String*/
	    public static byte[] StringtoByte(String string){
	    	int l = string.length()/2;
	    	byte[] ret = new byte[l];
	    	for (int i = 0; i < l; i++){
	    		ret[i] = (byte)Integer.valueOf(string.substring(i*2, i*2+2),16).byteValue();
	    	}
			return ret;
	    }
	    
	    /**
	     * @Description:加密过程 包含置换 密钥异或 S盒 P盒操作
	     * @param String text 待加密的文本
	     * @param String key 	密钥
	     * @param String type  加密还是解密（模式）
	     * @return String*/
	    public static String encrypt(String plaintext, String key, String type) {
	        StringBuffer ciphertext = new StringBuffer(); // 存储密文

	        //1、把明文转换成二进制字符串
	        StringBuffer plaintextBinary = new StringBuffer(); // 存储明文二进制
	        for (int i = 0; i < 8; ++i) {
	            StringBuffer mSubPlaintextTemp = new StringBuffer(Integer.toBinaryString(plaintext.charAt(i)));
	            while (mSubPlaintextTemp.length() < 8) {
	                mSubPlaintextTemp.insert(0, 0);
	            }
	            plaintextBinary.append(mSubPlaintextTemp);
	        }

	        ///2、 通过IP置换明文（初次置换）
	        StringBuffer substitutePlaintext = new StringBuffer(); // 存储置换后的明文
	        for (int i = 0; i < 64; ++i) {
	            substitutePlaintext.append(plaintextBinary.charAt(IP_Table[i] - 1));
	        }

	        //3、拆分明文
	        StringBuffer L = new StringBuffer(substitutePlaintext.substring(0, 32));
	        StringBuffer R = new StringBuffer(substitutePlaintext.substring(32));

	        //4、获取子密钥组 如果是解密模式，则将密钥顺序倒置再进行运算
	        StringBuffer[] subKey = getSubKey(key);
	        if (type.equals("decrypt")) {
	            StringBuffer[] mTemp = getSubKey(key);
	            for (int i = 0; i < 16; ++i) {
	                subKey[i] = mTemp[15 - i];
	            }
	        }

	        //5、十六轮迭代
	        for (int i = 0; i < 16; ++i) {
	            StringBuffer mLTemp = new StringBuffer(L); // 存储原来的左边

	            // L&R 置换
	            L.replace(0, 32, R.toString());

	            // 按E位选择表扩展右边
	            StringBuffer mRTemp = new StringBuffer(); // 存储扩展后的右边
	            for (int j = 0; j < 48; ++j) {
	                mRTemp.append(R.charAt(E_Table[j] - 1));
	            }

	            // 扩展后的右边和子密钥异或
	            for (int j = 0; j < 48; ++j) {
	                if (mRTemp.charAt(j) == subKey[i].charAt(j)) {
	                    mRTemp.replace(j, j + 1, "0");
	                } else {
	                    mRTemp.replace(j, j + 1, "1");
	                }
	            }

	            // 进行S盒压缩
	            // 取六位“1”“0”中第一位和最后一位作为行（0,1,2,3）
	            // 取六位中中间四位作为列（0-15对应S盒中4*16=64位数字）
	            // 获得一个小于十六的整数，转换成二进制（小于等于四位）
	            // 将不足四位的二进制数字前项补齐0，输出四位压缩后的密文
	            R.setLength(0);
	            for (int j = 0; j < 8; ++j) {
	                String mSNumber = mRTemp.substring(j * 6, (j + 1) * 6);
	                int row = Integer.parseInt(Character.toString(mSNumber.charAt(0)) + mSNumber.charAt(5), 2);//1+6
	                int column = Integer.parseInt(mSNumber.substring(1, 5), 2);//2345
	                int number = S_Box[j][row * 16 + column];//将行和列换算成具体的位置
	                StringBuffer numberString = new StringBuffer(Integer.toBinaryString(number));
	                while (numberString.length() < 4) {
	                    numberString.insert(0, 0);//补0
	                }
	                R.append(numberString);
	            }

	            // 将压缩后的R通过P_Table置换
	            StringBuffer mRTemp1 = new StringBuffer(); // 存储置换后的R
	            for (int j = 0; j < 32; ++j) {
	                mRTemp1.append(R.charAt(P_Table[j] - 1));
	            }
	            R.replace(0, 32, mRTemp1.toString());

	            // 将置换后的R与最开始的L异或
	            for (int j = 0; j < 32; ++j) {
	                if (R.charAt(j) == mLTemp.charAt(j)) {
	                    R.replace(j, j + 1, "0");
	                } else {
	                    R.replace(j, j + 1, "1");
	                }
	            }
	        }

	        //6 、合并LR
	        StringBuffer LR = new StringBuffer(R.toString() + L.toString());

	        //7 、根据IPR_Table置换LR
	        StringBuffer mLRTemp = new StringBuffer(); // 存储置换后的LR
	        for (int i = 0; i < 64; ++i) {
	            mLRTemp.append(LR.charAt(IPR_Table[i] - 1));
	        }

	        //8、输出密文
	        for (int i = 0; i < 8; ++i) {
	            String mCharTemp = mLRTemp.substring(i * 8, (i + 1) * 8);
	            ciphertext.append((char) Integer.parseInt(mCharTemp, 2));
	        }
	        
	        return ciphertext.toString();
	    }
	    
	    /**
	     * @Description:加密过程 包含置换 密钥异或 S盒 P盒操作
	     * @param Stringbuffer text 待加密的文本
	     * @param String key 	密钥
	     * @param String type  加密还是解密（模式）
	     * @return String*/
	    public static String encryptINanylength(String plaintext, String key, String type) {
	        StringBuffer ciphertext = new StringBuffer(); // 存储密文
	        StringBuffer plaintextBinary = new StringBuffer(plaintext);
//	        //1、把明文转换成二进制字符串
//	        StringBuffer plaintextBinary = new StringBuffer(); // 存储明文二进制
//	        for (int i = 0; i < 8; ++i) {
//	            StringBuffer mSubPlaintextTemp = new StringBuffer(Integer.toBinaryString(plaintext.charAt(i)));
//	            while (mSubPlaintextTemp.length() < 8) {
//	                mSubPlaintextTemp.insert(0, 0);
//	            }
//	            plaintextBinary.append(mSubPlaintextTemp);
//	        }

	        ///2、 通过IP置换明文（初次置换）
	        StringBuffer substitutePlaintext = new StringBuffer(); // 存储置换后的明文
	        for (int i = 0; i < 64; ++i) {
	            substitutePlaintext.append(plaintextBinary.charAt(IP_Table[i] - 1));
	        }

	        //3、拆分明文
	        StringBuffer L = new StringBuffer(substitutePlaintext.substring(0, 32));
	        StringBuffer R = new StringBuffer(substitutePlaintext.substring(32));

	        //4、获取子密钥组 如果是解密模式，则将密钥顺序倒置再进行运算
	        StringBuffer[] subKey = getSubKey(key);
	        if (type.equals("decrypt")) {
	            StringBuffer[] mTemp = getSubKey(key);
	            for (int i = 0; i < 16; ++i) {
	                subKey[i] = mTemp[15 - i];
	            }
	        }

	        //5、十六轮迭代
	        for (int i = 0; i < 16; ++i) {
	            StringBuffer mLTemp = new StringBuffer(L); // 存储原来的左边

	            // L&R 置换
	            L.replace(0, 32, R.toString());

	            // 按E位选择表扩展右边
	            StringBuffer mRTemp = new StringBuffer(); // 存储扩展后的右边
	            for (int j = 0; j < 48; ++j) {
	                mRTemp.append(R.charAt(E_Table[j] - 1));
	            }

	            // 扩展后的右边和子密钥异或
	            for (int j = 0; j < 48; ++j) {
	                if (mRTemp.charAt(j) == subKey[i].charAt(j)) {
	                    mRTemp.replace(j, j + 1, "0");
	                } else {
	                    mRTemp.replace(j, j + 1, "1");
	                }
	            }

	            // 进行S盒压缩
	            // 取六位“1”“0”中第一位和最后一位作为行（0,1,2,3）
	            // 取六位中中间四位作为列（0-15对应S盒中4*16=64位数字）
	            // 获得一个小于十六的整数，转换成二进制（小于等于四位）
	            // 将不足四位的二进制数字前项补齐0，输出四位压缩后的密文
	            R.setLength(0);
	            for (int j = 0; j < 8; ++j) {
	                String mSNumber = mRTemp.substring(j * 6, (j + 1) * 6);
	                int row = Integer.parseInt(Character.toString(mSNumber.charAt(0)) + mSNumber.charAt(5), 2);//1+6
	                int column = Integer.parseInt(mSNumber.substring(1, 5), 2);//2345
	                int number = S_Box[j][row * 16 + column];//将行和列换算成具体的位置
	                StringBuffer numberString = new StringBuffer(Integer.toBinaryString(number));
	                while (numberString.length() < 4) {
	                    numberString.insert(0, 0);//补0
	                }
	                R.append(numberString);
	            }

	            // 将压缩后的R通过P_Table置换
	            StringBuffer mRTemp1 = new StringBuffer(); // 存储置换后的R
	            for (int j = 0; j < 32; ++j) {
	                mRTemp1.append(R.charAt(P_Table[j] - 1));
	            }
	            R.replace(0, 32, mRTemp1.toString());

	            // 将置换后的R与最开始的L异或
	            for (int j = 0; j < 32; ++j) {
	                if (R.charAt(j) == mLTemp.charAt(j)) {
	                    R.replace(j, j + 1, "0");
	                } else {
	                    R.replace(j, j + 1, "1");
	                }
	            }
	        }

	        //6 、合并LR
	        StringBuffer LR = new StringBuffer(R.toString() + L.toString());

	        //7 、根据IPR_Table置换LR
	        StringBuffer mLRTemp = new StringBuffer(); // 存储置换后的LR
	        for (int i = 0; i < 64; ++i) {
	            mLRTemp.append(LR.charAt(IPR_Table[i] - 1));
	        }

	        //8、输出密文
	        for (int i = 0; i < 8; ++i) {
	            String mCharTemp = mLRTemp.substring(i * 8, (i + 1) * 8);
	            ciphertext.append((char) Integer.parseInt(mCharTemp, 2));
	        }
	        
	        return ciphertext.toString();
	    }
	
	    /**
	     * @param plaintext 待处理字符串
	     * @param key 密钥
	     * @param type 模式
	     * @return 成品字符串
	     */
	    public static String encryptoperation(String plaintext, String key, String type){
	    	List<String> textinbinary = new ArrayList<String>();
	        //1、把明文转换成二进制字符串
	    	StringBuffer tempcount64 = new StringBuffer();//每64位向list中存储一次
	        for (int i = 0; i < plaintext.length(); ++i) {
	            StringBuffer mSubPlaintextTemp = new StringBuffer(Integer.toBinaryString(plaintext.charAt(i)));
	            while (mSubPlaintextTemp.length() < 8) {
	                mSubPlaintextTemp.insert(0, 0);
	            }
	            tempcount64.append(mSubPlaintextTemp);
	            if (tempcount64.length() == 64) {
//	            	System.out.println("0");
					textinbinary.add(tempcount64.toString());//执行存储
//					System.out.println(tempcount64.length());
//					System.out.println(textinbinary.get(0));
					tempcount64.setLength(0);//清空tempbuffer
				}
	            if (i+1 == plaintext.length()) {//讨论即将退出循环时，tempbuffer不足64位的情况
//	            	System.out.println("1");
					if (tempcount64.length()%64 != 0) {
//						System.out.println("2");
						while (tempcount64.length() != 64){
//							System.out.println("3");
							tempcount64.insert(0, 0);//用0补齐
						}
						textinbinary.add(tempcount64.toString());//执行存储
//						System.out.println(tempcount64.length());
//						System.out.println(textinbinary.get(1));
						tempcount64.setLength(0);
					}
				}
	        }
	        System.out.println(textinbinary);
	        
	        
	        //字符串转换完成，开始加(解)密
	        String finalresult = "";
	        for (int i = 0; i < textinbinary.size(); i++) {
//	        	System.out.println(textinbinary);
				finalresult = finalresult + "" + encryptINanylength(textinbinary.get(i), key, type);
			}
	        return finalresult;
	    }
	}
	
}
