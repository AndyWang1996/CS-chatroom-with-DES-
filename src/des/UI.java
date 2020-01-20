package des;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UI {
	public static JFrame frame;
	public static JTextField txt_message;
	public static JTextField txt_key;
	public static JTextField txt_result;
	public static JLabel a;
	public static JLabel b;
	public static JLabel c;
	public static JButton Encrypt;
	public static JButton DEncrypt;
	
	public UI(){
		frame = new JFrame("DES");
		frame.setLocation(550, 300);
		frame.setSize(520, 400);
		frame.setVisible(true);
		
		txt_message = new JTextField("");
		txt_message.setVisible(true);
		frame.add(txt_message);
		txt_message.setSize(400, 50);
		txt_message.setLocation(100, 0);
		
		txt_key = new JTextField("");
		txt_key.setVisible(true);
		frame.add(txt_key);
		txt_key.setSize(400, 50);
		txt_key.setLocation(100,60);
		
		txt_result = new JTextField();
		txt_result.setVisible(true);
		frame.add(txt_result);
		txt_result.setSize(400, 50);
		txt_result.setLocation(100,120);
		
		Encrypt = new JButton("加密");
		DEncrypt = new JButton("解密");
		Encrypt.setVisible(true);
		DEncrypt.setVisible(true);
		frame.add(Encrypt);
		Encrypt.setSize(100, 50);
		Encrypt.setLocation(80, 240);
		frame.add(DEncrypt);
		DEncrypt.setSize(100, 50);
		DEncrypt.setLocation(340, 240);
		
		a = new JLabel();
		a.setText("明文：");
		b = new JLabel();
		b.setText("密钥：");
		c = new JLabel();
		c.setText("输出：");
		frame.add(a);
		frame.add(b);
		frame.add(c);
		a.setSize(50,50);
		b.setSize(50,50);
		c.setSize(50,50);
		a.setLocation(10, 0);
		b.setLocation(10, 60);
		c.setLocation(10, 120);
		a.setVisible(true);
		b.setVisible(true);
		c.setVisible(true);
		
		Encrypt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String text = txt_message.getText();
				String key = txt_key.getText();
				String mode = "encrypt";
				String result = Des.DES.encryptoperation(text, key, mode);
				txt_result.setText(result);
				
			}
		});
		
		DEncrypt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String text = txt_message.getText();
				String key = txt_key.getText();
				String mode = "decrypt";
				String result = Des.DES.encryptoperation(text, key, mode);
				txt_result.setText(result);
				
			}
		});
	}
	
	public static void main(String args[]){
		new UI();
	}
}
