package des;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EncrypFile {
	public static void openfile(String path) throws IOException{
		try {
			File file = new File(path);
			FileInputStream inputStream = new FileInputStream(file);
			byte bs;
			bs = (byte) inputStream.read();
			System.out.println(bs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		try {
			openfile("timg.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
