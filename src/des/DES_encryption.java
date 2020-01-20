package des;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Templates;

import org.omg.PortableServer.ServantActivator;

public class DES_encryption {
	
	public static int[] P = {16,7,20,21,29,12,28,17,1,15,23,26,5,18,31,10,2,8,24,14,32,27,3,9,19,13,30,6,22,11,4,25};
	
	public static int[] S1 = {14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7, 
			0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8, 
			4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0, 
			15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13};
	
	public static int[] S2 = {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10, 
			3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5, 
			0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15, 
			13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9};
	
	public static int[] S3 = {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8, 
			13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1, 
			13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7, 
			1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12};
	
	public static int[] S4 = {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15, 
			13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9, 
			10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4, 
			3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14};
	
	public static int[] S5 = {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9, 
			14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6, 
			4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14, 
			11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3};
	
	public static int[] S6 = {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11, 
			10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8, 
			9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6, 
			4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13};
	
	public static int[] S7 = {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1, 
			13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6, 
			1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2, 
			6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12};
	
	public static int[] S8 = {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7, 
			1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2, 
			7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8, 
			2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11};
	
	public static int[] expand = {32,1,2,3,4,5, 
			4,5,6,7,8,9, 
			8,9,10,11,12,13, 
			12,13,14,15,16,17, 
			16,17,18,19,20,21, 
			20,21,22,23,24,25, 
			24,25,26,27,28,29, 
			28,29,30,31,32,1};
	
	public static int[] start_replacement = 
							  {58,50,42,34,26,18,10,2,
							   60,52,44,36,28,20,12,4,
							   62,54,46,38,30,22,14,6,
							   64,56,48,40,32,24,16,8,
							   57,49,41,33,25,17, 9,1,
							   59,51,43,35,27,19,11,3,
							   61,53,45,37,29,21,13,5,
							   63,55,47,39,31,23,15,7,};
	
	public static int[] PC1 = {57,49,41,33,25,17,9,1,  
							   58,50,42,34,26,18,10,2,  
							   59,51,43,35,27,19,11,3,  
							   60,52,44,36,63,55,47,39,  
							   31,23,15,7,62,54,46,38,  
							   30,22,14,6,61,53,45,37,  
							   29,21,13,5,28,20,12,4};
	
	public static int[] PC2 = {14,17,11,24,1,5,  
							   3,28,15,6,21,10,  
							   23,19,12,4,26,8,  
							   16,7,27,20,13,2,  
							   41,52,31,37,47,55,  
							   30,40,51,45,33,48,  
							   44,49,39,56,34,53,  
							   46,42,50,36,29,32};
	
	public static int[] C = 
							  {57,49,41,33,25,17,9 ,
							   1 ,58,50,42,34,26,18,
							   10,2 ,59,51,43,35,27, 
							   19,11, 3,60,52,44,36};
	
	public static int[] D = 
		  					  {63,55,47,39,31,23,15, 
		  					   7 ,62,54,46,38,30,22,
		  					   14,6 ,61,53,45,37,29,
		  					   21,13,5 ,28,20,12,4 };
	
	public static int[] Move = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
	
	public static String encryption(DES_object pack){
		String result = "";
		List<String> data = new ArrayList<String>();
		List<String> key = new ArrayList<String>();
		List<String> cphier = new ArrayList<String>();
		data = Textoperation(pack.getdata());
	    key = GetsubKeyGroup(TranstoBinary(pack.getkey()));
	    if (pack.getmode() == "2") {
			List<String> temp = new ArrayList<String>();
			for (int i = 0; i<16; i++){
				temp.add(i, key.get(15-i));
			}
			key = temp;
		}
	    cphier = Encryption(key, data);
	    for (int i = 0; i<cphier.size();i++){
	    	result = result + "" + cphier.get(i);
	    }
		
		return result;
	}
	
	public static List<String> Encryption(List<String> keyGroup, List<String> textGroup){
		
		for (int i = 0; i<textGroup.size(); i++){
			String LString = textGroup.get(i).substring(0,32);
			String RString = textGroup.get(i).substring(32);
			
			for (int k = 0;k<16;k++){
				RString = ExpandReplacement(RString);
				StringBuffer rBuffer = new StringBuffer(RString);
				//Òì»òÔËËã
				for (int j = 0; j<rBuffer.length(); j++){
					if (rBuffer.charAt(j) == keyGroup.get(k).charAt(j)) {
						rBuffer.replace(j, j+1, "0");
					}else{
						rBuffer.replace(j, j+1, "1");
					}
				}
				RString = rBuffer.toString();
				RString = SboxOperation(RString);
//				System.out.println(RString.length());
				RString = PboxOperation(RString);
				//Òì»òÔËËã
				StringBuffer L = new StringBuffer(LString);
				StringBuffer R = new StringBuffer(RString);
				for (int j = 0; j<L.length(); j++){
					if (R.charAt(j) == L.charAt(j)) {
						R.replace(j, j+1, "0");
					}else{
						R.replace(j, j+1, "1");
					}
				}
				RString = R.toString();		
				LString = L.toString();
			}
			
			String ciphertext = LString + "" + RString;
			textGroup.remove(i);
			textGroup.add(i,ciphertext);
			
		}
		
//		Encryptedtext = textGroup.
		return textGroup;
	}
	
	//PºÐÖÃ»»
	public static String PboxOperation(String R0){
		String temp = "";
//		System.out.println(R0.length());
		for (int i = 0; i < P.length;i++){
//			System.out.println(i);
			temp = temp + "" + R0.charAt(P[i]-1);
		}
		R0 = temp;
		return R0;
	}
	
	//SºÐÖÃ»»
	public static String SboxOperation(String R0){
		String s1 = R0.substring(0,6);
		String s2 = R0.substring(6,12);
		String s3 = R0.substring(12,18);
		String s4 = R0.substring(18,24);
		String s5 = R0.substring(24,30);
		String s6 = R0.substring(30,36);
		String s7 = R0.substring(36,42);
		String s8 = R0.substring(42);
		
		R0 = "";
//		System.out.println(R0.length());
		
		String s1_l = s1.charAt(0) + "" + s1.charAt(5);
		String s1_r = s1.substring(1,5);
//		System.out.println(Integer.parseInt(s1_l,2));
//		System.out.println(Integer.parseInt(s1_r,2));
		int s1_index = Integer.parseInt(s1_l,2) * 16 + Integer.parseInt(s1_r,2);
//		System.out.println(s1_index);
		s1_index = S1[s1_index];
		String solution1 = Integer.toBinaryString(s1_index);
		solution1 = add0(solution1);
		R0 = R0 + "" + solution1;
//		System.out.println(R0.length());
		
		String s2_l = s2.charAt(0) + "" + s2.charAt(5);
		String s2_r = s2.substring(1,5);
//		System.out.println(s2);
//		System.out.println(Integer.parseInt(s2_l,2));
//		System.out.println(Integer.parseInt(s2_r,2));
		int s2_index = Integer.parseInt(s2_l,2) * 16 + Integer.parseInt(s2_r,2);
//		System.out.println(s2_index);
		s2_index = S2[s2_index];
		String solution2 = Integer.toBinaryString(s2_index);
		solution2 = add0(solution2);
		R0 = R0 + "" + solution2;
//		System.out.println(R0.length());
		
		String s3_l = s3.charAt(0) + "" + s3.charAt(5);
		String s3_r = s3.substring(1,5);
//		System.out.println(Integer.parseInt(s3_l,2));
//		System.out.println(Integer.parseInt(s3_r,2));
		int s3_index = Integer.parseInt(s3_l,2) * 16 + Integer.parseInt(s3_r,2);
//		System.out.println(s3_index);
		s3_index = S3[s3_index];
		String solution3 = Integer.toBinaryString(s3_index);
		solution3 = add0(solution3);
		R0 = R0 + "" + solution3;
//		System.out.println(R0.length());
		
		String s4_l = s4.charAt(0) + "" + s4.charAt(5);
		String s4_r = s4.substring(1,5);
//		System.out.println(Integer.parseInt(s4_l,2));
//		System.out.println(Integer.parseInt(s4_r,2));
		int s4_index = Integer.parseInt(s4_l,2) * 16 + Integer.parseInt(s4_r,2);
//		System.out.println(s4_index);
		s4_index = S4[s4_index];
		String solution4 = Integer.toBinaryString(s4_index);
		solution4 = add0(solution4);
		R0 = R0 + "" + solution4;
//		System.out.println(R0.length());
		
		String s5_l = s5.charAt(0) + "" + s5.charAt(5);
		String s5_r = s5.substring(1,5);
//		System.out.println(Integer.parseInt(s5_l,2));
//		System.out.println(Integer.parseInt(s5_r,2));
		int s5_index = Integer.parseInt(s5_l,2) * 16 + Integer.parseInt(s5_r,2);
//		System.out.println(s5_index);
		s5_index = S5[s5_index];
		String solution5 = Integer.toBinaryString(s5_index);
		solution5 = add0(solution5);
		R0 = R0 + "" + solution5;
//		System.out.println(R0.length());
		
		String s6_l = s6.charAt(0) + "" + s6.charAt(5);
		String s6_r = s6.substring(1,5);
//		System.out.println(Integer.parseInt(s6_l,2));
//		System.out.println(Integer.parseInt(s6_r,2));
		int s6_index = Integer.parseInt(s6_l,2) * 16 + Integer.parseInt(s6_r,2);
//		System.out.println(s6_index);
		s1_index = S1[s6_index];
		String solution6 = Integer.toBinaryString(s6_index);
		solution6 = add0(solution6);
		R0 = R0 + "" + solution6;
//		System.out.println(R0.length());
		
		String s7_l = s7.charAt(0) + "" + s7.charAt(5);
		String s7_r = s7.substring(1,5);
//		System.out.println(Integer.parseInt(s7_l,2));
//		System.out.println(Integer.parseInt(s7_r,2));
		int s7_index = Integer.parseInt(s7_l,2) * 16 + Integer.parseInt(s7_r,2);
//		System.out.println(s7_index);
		s7_index = S7[s7_index];
		String solution7 = Integer.toBinaryString(s7_index);
		solution7 = add0(solution7);
		R0 = R0 + "" + solution7;
//		System.out.println(R0.length());
		
		String s8_l = s8.charAt(0) + "" + s8.charAt(5);
		String s8_r = s8.substring(1,5);
//		System.out.println(Integer.parseInt(s8_l,2));
//		System.out.println(Integer.parseInt(s8_r,2));
		int s8_index = Integer.parseInt(s8_l,2) * 16 + Integer.parseInt(s8_r,2);
//		System.out.println(s8_index);
		s8_index = S8[s8_index];
		String solution8 = Integer.toBinaryString(s8_index);
		solution8 = add0(solution8);
		R0 = R0 + "" + solution8;
//		System.out.println(R0.length());
		
		return R0;
	}
	
	//À©Õ¹ÖÃ»»
	public static String ExpandReplacement(String R0){
		String temp = "";
		for (int i = 0; i < expand.length; i++){
			temp = temp + "" + R0.charAt(expand[i] - 1);
		}
		R0 = temp;
		return R0;
	}
	
	//Éú³É°üº¬Ê®Áù¸ö×ÓÃÜÔ¿µÄ×ÓÃÜÔ¿×é
	public static List<String> GetsubKeyGroup(String key0){
		List<String> subKeygroup = new ArrayList<String>();
		for (int i = 0; i < 16; i++){
//			System.out.println(i);
			String tempkey = "";
			for (int j = 0; j < C.length; j++){
//				System.out.println((j+Move[i])%C.length);
				tempkey = tempkey + "" + key0.charAt(C[(j+Move[i])%C.length]);
			}
			for (int j = 0; j < D.length; j++){
				tempkey = tempkey + "" + key0.charAt(D[(j+Move[i])%D.length]);
			}
			String key = "";
			for (int j = 0; j < PC2.length; j++){
				key = key + "" + tempkey.charAt(PC2[j]-1);
			}
			subKeygroup.add(i, key);
		}
		return subKeygroup;
	}
	
	//¼ì²é×Ö·û´®µÄ³¤¶È£¬²»×ã64µÄÕûÊý±¶ÓÃ0²¹Æë
	public static String check_length(String text) {
		if (text.length() < 64) {
			int less = 64 - text.length();
			for (int i = 0; i < less; i++){
				text = text + "0";
			}
		}else if (text.length() > 64 && text.length() % 64 != 0) {
			int less = 64 - (text.length()%64);
			for (int i = 0; i < less; i++){
				text = text + "0";
			}
		}
		return text;
	}
	
	//½«ÃÜÔ¿Ñ¹ËõÎª56Î»
	public static String Keyoperate(String key){
		key = TranstoBinary(key);
		String temp = "";
		for (int i = 0; i<PC1.length;i++){
			temp = temp + "" + key.charAt(PC1[i]);
		}
		key = temp;
		return key;
	}
	
	//½«×Ö·û´®´¦Àí³É64Î»×Ö·û´®×é
	public static List<String> Textoperation(String text){
		
		text = TranstoBinary(text);
		text = check_length(text);
		
		List<String> result = new ArrayList<String>();
		
		if (text.length()%64 != 0) {
			text = check_length(text);
		}
		
		//²ð·Ö×Ö·û´®£¬ÁùÊ®ËÄÎ»Ò»×é
		//divide the String to 64s
		int listsize = text.length()/64;
		for (int i = 0; i < listsize; i++){
			String Temp = text.substring(0, 64);
			result.add(i, Temp);
			text = text.substring(63);
		}
		
		//ÖÃ»»²Ù×÷
		//replacement
//		List<String> result = new ArrayList<String>();
//		System.out.println(result);
		result = Replacement(result);
		
		
		
		
		return result;
	}
	
	//×Ö·û´®µÄ³õÊ¼IPÖÃ»»
	public static List<String> Replacement(List<String> list){
		List<String> object = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++){
			String temp = list.get(i);
//			System.out.println(temp.length());
			String output = "";
			for (int j = 0; j < temp.length();j++){
//				System.out.println(j);
				output = output + "" + temp.charAt(start_replacement[j]-1);
			}
			object.add(i, output);
		}
		
		return object;
	}
	
	//
	public static byte[] get7Bit(String strContent) {
		   // ½á¹û
		   byte[] arrResult = null;
		   try {
		      // ±àÂë·½Ê½
		      byte[] arrs = strContent.getBytes("ASCII");
		      System.out.println(new String(arrs));

		      arrResult = new byte[arrs.length - (arrs.length / 8)];
		      int intRight = 0;
		      int intLeft = 7;
		      int intIndex = 0;
		      for (int i = 1; i <= arrs.length; i++, intRight++, intLeft--) {
		         if (i % 8 == 0) {
		            intRight = -1;
		            intLeft = 8;
		            continue;
		         }
		         byte newItem = 0;
		         if (i == arrs.length) {
		            newItem = (byte) (arrs[i - 1] >> intRight);
		         } else {
		            newItem = (byte) ((arrs[i - 1] >> intRight) | (arrs[i] << intLeft));
		         }

		         arrResult[intIndex] = newItem;
		         intIndex++;

		      }
		   } catch (UnsupportedEncodingException e) {
		      e.printStackTrace();
		   }
		   return arrResult;
		}
	
	//¶þ½øÖÆ->×Ö·û
	public static String TranstoText(String text){
		StringBuffer textOrigin = new StringBuffer();
		for (int i = 0; i < (text.length()/8); ++i) {
            String mCharTemp = text.substring(i * 8, (i + 1) * 8);
            textOrigin.append((char) Integer.parseInt(mCharTemp, 2));
        }
		text = textOrigin.toString();
		return text;
	}
	
	//×Ö·û->¶þ½øÖÆ
	public static String TranstoBinary(String text){
		StringBuffer textBinary = new StringBuffer();
        for (int i = 0; i < text.length(); ++i) {
            StringBuffer mSubKeyTemp = new StringBuffer(Integer.toBinaryString(text.charAt(i)));
            while (mSubKeyTemp.length() < 8) {
                mSubKeyTemp.insert(0, 0);
            }
            textBinary.append(mSubKeyTemp);
        }
        text = textBinary.toString();
		return text;
	}
	
	//²¹ÆëÎ»Êý£¨SºÐ£©
	public static String add0(String b){
		if (b.length() == 4) {
			return b;	
		}else if (b.length() == 3){
			return "0" + b;
		}else if (b.length() == 2){
			return "00" + b;
		}else if (b.length() == 1){
			return "000" + b;
		}else {
			return "0000";
		}
	}
	
	
	public static void main(String args[]) {
		String testkey = "afsdeeff";
		DES_object aDes_object = new DES_object("aaaaaaaa", "abcdefgh", "1");
		String aString = encryption(aDes_object);
//		System.out.println(aString.length());
		String output = TranstoText(aString);
		System.out.println(output);
		DES_object bDes_object = new DES_object("aaaaaaaa", "???QR?", "2");
		String bString = encryption(bDes_object);
////		System.out.println(bString.length());
		String outputb = TranstoText(bString);
		System.out.println(outputb);
//		List<String> list = new ArrayList<String>();
//		list = GetsubKeyGroup(TranstoBinary(testkey));
//		System.out.println(list);
//		String aString = "1";
//		String bString = "2";
//		String cString = "3";
//		String dString = "4";
//		String eString = "5";
//		String fString = "6";
//		String gString = "7";
//		list.add(0, aString);
//		list.add(1, bString);
//		list.add(2, cString);
//		list.add(3, dString);
//		list.add(4, eString);
//		list.add(5, fString);
//		System.out.println(list);
//		list.remove(2);
//		list.add(2,gString);
//		System.out.println(list.toString());
//		String s1 = "110011";
//		StringBuffer aBuffer = new StringBuffer(s1);
//		String s2 = aBuffer.toString();
//		System.out.println(s2);
//		String s1_l = s1.charAt(0) + "" + s1.charAt(5);
//		String s1_r = s1.substring(1,5);
//		System.out.println(Integer.parseInt(s1_l,2));
//		System.out.println(Integer.parseInt(s1_r,2));
//		int s1_index = Integer.parseInt(s1_l,2) * 16 + Integer.parseInt(s1_r,2);
//		System.out.println(s1_index);
//		s1_index = S8[s1_index];
//		String solution = Integer.toBinaryString(s1_index);
//		System.out.println(solution);
//		int a = Integer.parseInt(teString,2);
//		System.out.println(a);
//		System.out.println(Keyoperate(teString).length());
//		List<String> aList = GetsubKeyGroup(TranstoBinary(teString));
//		System.out.println(aList);
//		System.out.println(encryption(teString));
//		System.out.println(TranstoBinary(teString));
//		System.out.println(TranstoText(TranstoBinary(teString)));
		
        
//        System.out.println(keyBinary.length());

		
//		teString = check_length(teString);
//		System.out.println(teString.length());
//		System.out.println(teString);
		
//		int i[] = teString.toCharArray();
		
//		List<String> test = new ArrayList<String>();
//		test = encryption(teString);
//		System.out.println(test.size());
//		for (int i = 0; i<test.size();i++){
//			System.out.println(test.get(i));
//		}
		

	}
}
