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

        char[] res = new char[src.length * 2]; // 每个byte对应两个字符
        final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >> 4 & 0x0f]; // 先存byte的高4位
            res[j++] = hexDigits[src[i] & 0x0f]; // 再存byte的低4位
        }

        return new String(res);
    }
	
    public static void main(String[] args) {
        File target;    //接收到的文件保存的位置
        FileOutputStream save;  //将接收到的文件写入电脑
        FileInputStream in;     //读取穿送过来的数据文件
        ServerSocket server;    //服务器
        Socket socket;          //套接字

        //处理客户端的请求
        try {
            //接受前文件的准备
            target = new File("E:\\server.jpg");
            save = new FileOutputStream(target);

            server = new ServerSocket(2017);    //服务端口

            //等待客户端的呼叫
            System.out.println("等待客户端的呼叫");
            socket = server.accept();   //阻塞
            in = (FileInputStream)socket.getInputStream();

            //接收数据
            byte[] b = new byte[64];
            int n = in.read(b);
            int start = (int)System.currentTimeMillis();
            while (n != -1) {
//            	String aString = new String(b,"UTF-8");
            	System.out.println(bytes2Hex(b));
                save.write(b, 0, n);    //写入指定地方
                n = in.read(b);
            }
            int end = (int)System.currentTimeMillis();
            System.out.println("接收花费的时间：" + (end-start));
            socket.close();
            server.close();
            in.close();
            save.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}