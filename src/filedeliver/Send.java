package filedeliver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class Send {
	
	public static String bytes2Hex(byte[] src) {
        if (src == null || src.length <= 0) {   
            return null;   
        } 

        char[] res = new char[src.length * 2]; // ÿ��byte��Ӧ�����ַ�
        final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >> 4 & 0x0f]; // �ȴ�byte�ĸ�4λ
            res[j++] = hexDigits[src[i] & 0x0f]; // �ٴ�byte�ĵ�4λ
        }

        return new String(res);
    }

    public static void main(String[] args) {
        File src;       //��Ҫ���͵��ļ�
        Socket socket;  //�׽���
        FileInputStream open;   //��ȡ�ļ�
        FileOutputStream out;   //�����ļ�

        try {
            //��Ҫ������ļ�
            src = new File("server.jpg");
            open = new FileInputStream(src);

            //���ӷ�����
            socket = new Socket("127.0.0.1", 2017);
            out = (FileOutputStream)socket.getOutputStream();

            //��ʼ����
            byte[] b = new byte[64];
            int n = open.read(b);
            int start = (int)System.currentTimeMillis();
            System.out.println(n);
            while (n != -1) {
                out.write(b, 0, n);
//                System.out.println(aString);
                n = open.read(b);
            }
            int end = (int)System.currentTimeMillis();
            System.out.println( "���ͻ��ѵ�ʱ�䣺" + (end-start));
            //�ر���
            out.close();
            socket.close();
            open.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}