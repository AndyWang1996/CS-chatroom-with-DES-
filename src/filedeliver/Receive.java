package filedeliver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class Receive {
	
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
        File target;    //���յ����ļ������λ��
        FileOutputStream save;  //�����յ����ļ�д�����
        FileInputStream in;     //��ȡ���͹����������ļ�
        ServerSocket server;    //������
        Socket socket;          //�׽���

        //����ͻ��˵�����
        try {
            //����ǰ�ļ���׼��
            target = new File("E:\\server.jpg");
            save = new FileOutputStream(target);

            server = new ServerSocket(2017);    //����˿�

            //�ȴ��ͻ��˵ĺ���
            System.out.println("�ȴ��ͻ��˵ĺ���");
            socket = server.accept();   //����
            in = (FileInputStream)socket.getInputStream();

            //��������
            byte[] b = new byte[64];
            int n = in.read(b);
            int start = (int)System.currentTimeMillis();
            while (n != -1) {
//            	String aString = new String(b,"UTF-8");
            	System.out.println(bytes2Hex(b));
                save.write(b, 0, n);    //д��ָ���ط�
                n = in.read(b);
            }
            int end = (int)System.currentTimeMillis();
            System.out.println("���ջ��ѵ�ʱ�䣺" + (end-start));
            socket.close();
            server.close();
            in.close();
            save.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}