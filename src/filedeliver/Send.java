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

        char[] res = new char[src.length * 2]; // 每个byte对应两个字符
        final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >> 4 & 0x0f]; // 先存byte的高4位
            res[j++] = hexDigits[src[i] & 0x0f]; // 再存byte的低4位
        }

        return new String(res);
    }

    public static void main(String[] args) {
        File src;       //需要传送的文件
        Socket socket;  //套接字
        FileInputStream open;   //读取文件
        FileOutputStream out;   //传送文件

        try {
            //需要传输的文件
            src = new File("server.jpg");
            open = new FileInputStream(src);

            //连接服务器
            socket = new Socket("127.0.0.1", 2017);
            out = (FileOutputStream)socket.getOutputStream();

            //开始传送
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
            System.out.println( "发送花费的时间：" + (end-start));
            //关闭流
            out.close();
            socket.close();
            open.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}