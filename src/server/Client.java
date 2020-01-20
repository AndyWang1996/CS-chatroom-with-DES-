package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.User;
import des.Des.DES;

public class Client{

	private JFrame frame;
	@SuppressWarnings("rawtypes")
	private JList userList;
	private JTextArea textArea;
	private JTextField textField;
	private JTextField txt_port;
	private JTextField txt_hostIp;
	private JTextField txt_name;
	private JButton btn_start;
	private JButton btn_stop;
	private JButton btn_send;
	private JPanel northPanel1;
	private JPanel northPanel2;
	private JPanel northPanelm;
	private JPanel southPanel1;
	private JScrollPane rightScroll;
	private JScrollPane leftScroll;
	private JSplitPane centerSplit;
	@SuppressWarnings("rawtypes")
	private DefaultListModel listModel;
	private boolean isConnected = false;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private MessageThread messageThread;// thread for receiving
	private Map<String, User> onLineUsers = new HashMap<String, User>();// all online user
	
	private Icon image = null;
	
	String[] commands = {"#BROADCAST+","#PRIVATE-+","#STOP","#LIST","#DESCONNECT"};//command list

	// main
	public static void main(String[] args) {
		new Client();
	}

	// Send method
	public void send(){
		if (!isConnected) {//firstly check the connection static
			JOptionPane.showMessageDialog(frame, "Disconnected!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String message = textField.getText().trim();
		if (message == null || message.equals("")) {//secondly check the input blank
			JOptionPane.showMessageDialog(frame, "Blank message unabled.", "error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (message.equals("#STOP")) {//thirdly we check weather there is a command or normal message
			closeConnection();        //this if for Stop the connection
		}
		if (message.equals("#LIST")) {//thirdly we check weather there is a command or normal message
			sendMessage(message);
			textArea.append("Online users: \n");//this if for Stop the connection
			
		}
		if (message.contains("#PRIVATE-")) {//the make the client chat to someone else privately
			try {
				@SuppressWarnings("unused")
				String text = message.substring(message.indexOf("+"));//this try catch ensure the command form is correct 
			} catch (Exception e) {                                   //or it will not send to the server
				// TODO: handle exception
				textArea.append("Invalid command\n");
				textField.setText(null);
				return;
			}
			String targetuser = message.substring(message.indexOf("-")+1, message.indexOf("+"));
			String context = message.substring(message.indexOf("+")+1);
			if (onLineUsers.get(targetuser)!=null) {
				if (onLineUsers.get(targetuser).getKey() != null) {
					context = DES.encryptoperation(context, onLineUsers.get(targetuser).getKey(), "encrypt");
					message = "#PRIVATE-" + targetuser + "+" + context;
					sendMessage(message);
				}else{
					String require = "DESCONNECT#" + targetuser;
					sendMessage(require);
					textArea.append("NO secrect key remain. KEY requirement has been sent. plz try again.");
				}
			}else{
				textArea.append("NO SUCH USER.");
			}
			textArea.append(frame.getTitle() + ":" + DES.encryptoperation(context, onLineUsers.get(targetuser).getKey(), "decrypt") + " \n");
		}
		if(message.contains("#BROADCAST+")){
			try {
//				System.out.println("MARK1");
				@SuppressWarnings("unused")
				String text = message.substring(message.indexOf("+")+1);//this try catch ensure the command form is correct 
			} catch (Exception e) {                                   //or it will not send to the server
				// TODO: handle exception
				textArea.append("Invalid command\n");
				textField.setText(null);
//				System.out.println("MARK2");
				return;
			}
			sendMessage(frame.getTitle() + "#" + " BROADCAST:" + "#" + message);
		}
		if (message.contains("DESCONNECT#")) {
			try {
//				System.out.println("MARK1");
				@SuppressWarnings("unused")
				String text = message.substring(message.indexOf("#")+1);//this try catch ensure the command form is correct 
			} catch (Exception e) {                                   //or it will not send to the server
				// TODO: handle exception
				textArea.append("Invalid command\n");
				textField.setText(null);
//				System.out.println("MARK2");
				return;
			}
			sendMessage(message);
		}
		textField.setText(null);
	}

	// constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Client() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setForeground(Color.blue);
		textField = new JTextField();
		txt_port = new JTextField("0001");
		txt_hostIp = new JTextField(getip());
		txt_name = new JTextField("user");
		btn_start = new JButton("Connect");
		btn_stop = new JButton("Disconnect");
		btn_send = new JButton("Send");
		listModel = new DefaultListModel();
		userList = new JList(listModel);

		northPanel1 = new JPanel();
		northPanel1.setLayout(new GridLayout(4, 2));
		northPanel1.add(new JLabel("Port"));
		northPanel1.add(txt_port);
		northPanel1.add(new JLabel("Server IP"));
		northPanel1.add(txt_hostIp);
		northPanel1.add(new JLabel("Name"));
		northPanel1.add(txt_name);
		northPanel1.add(btn_start);
		northPanel1.add(btn_stop);
		northPanel1.setBorder(new TitledBorder("Link Info"));
		
		northPanel2 = new JPanel();
//		northPanel2.setBorder(new TitledBorder("other"));
		try {
//			image = new ImageIcon("timg.jpg");
			JLabel label = new JLabel();
			image = new ImageIcon("timg.jpg");
			label.setIcon(image);
//			label.setBounds(400, 0, 80, 80);
			label.setVisible(true);
			northPanel2.add(label);
//			image = ImageIO.read(new File("timg.jpg"));
//			ImageObserver observer = null;
//			northPanel2.checkImage(image, 100, 100, observer);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//Something
		
		northPanelm = new JPanel();
		northPanelm.setLayout(new GridLayout(1, 2));
		northPanelm.add(northPanel1);
		northPanelm.add(northPanel2);

		rightScroll = new JScrollPane(textArea);
		rightScroll.setBorder(new TitledBorder("Message area"));
		leftScroll = new JScrollPane(userList);
		leftScroll.setBorder(new TitledBorder("Online user"));
		
		@SuppressWarnings({ })
		JList command = new JList(commands);
		command.setBorder(new TitledBorder("command"));
		command.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				//this part of code add the click event for the command list
				Object index = ((JList)e.getSource()).getSelectedValue();
				if (index == "#STOP") {
					textField.setText("");
					textField.setText("#STOP");
				}
				if (index == "#BROADCAST+") {
					textField.setText("#BROADCAST+");
				}
				if (index == "#PRIVATE-+") {
					textField.setText("#PRIVATE-+");
				}
				if (index == "#LIST") {
					textField.setText("#LIST");
				}
				if (index == "#DESCONNECT") {
					textField.setText("DESCONNECT#");
				}
			}
		});

		southPanel1 = new JPanel(new BorderLayout());
		southPanel1.add(command,"West");
		southPanel1.add(textField, "Center");
		southPanel1.add(btn_send, "East");//set the comment list
		southPanel1.setBorder(new TitledBorder("input"));

		centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rightScroll,
				leftScroll);
		centerSplit.setDividerLocation(400);

		frame = new JFrame("Client");
		frame.setLayout(new BorderLayout());
		frame.add(northPanelm, "North");
		frame.add(centerSplit, "Center");
		frame.add(southPanel1, "South");
		frame.setSize(500, 600);
		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setLocation((screen_width - frame.getWidth()) / 2,
				(screen_height - frame.getHeight()) / 2);
		frame.setVisible(true);
		frame.setResizable(false);

		// Event when press enter
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send();
			}
		});

		// Event when click send
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});

		// Event when click connect
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int port;
				if (isConnected) {
					JOptionPane.showMessageDialog(frame, "Server already linked",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					try {
						port = Integer.parseInt(txt_port.getText().trim());
					} catch (NumberFormatException e2) {
						throw new Exception("Invalid port number");
					}
					String hostIp = txt_hostIp.getText().trim();
					String name = txt_name.getText().trim();
					if (name.equals("") || hostIp.equals("")) {
						throw new Exception("Invalid IP");
					}
					boolean flag = connectServer(port, hostIp, name);
					if (flag == false) {
						throw new Exception("Connection failed");
					}
					if (flag == true) {
						frame.setTitle(name);
						JOptionPane.showMessageDialog(frame, "Successfully connected");
					}
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(frame, exc.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Event when click disconnect
		btn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isConnected) {
					JOptionPane.showMessageDialog(frame, "Already Disconnected",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					boolean flag = closeConnection();// turn off the connection
					if (flag == false) {
						throw new Exception("Unknown Error");
					}
					JOptionPane.showMessageDialog(frame, "Disconnected");
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(frame, exc.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// event when close the window
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (isConnected) {
					closeConnection();// turn off the connection
				}
				System.exit(0);// exit
			}
		});
	}

	/**
	 * 连接服务器
	 * 
	 * @param port
	 * @param hostIp
	 * @param name
	 */
	public boolean connectServer(int port, String hostIp, String name) {
		// 连接服务器
		try {
			socket = new Socket(hostIp, port);// get port and IP to connect
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			// send the basic user info(name+IP)
			sendMessage(name + "#" + socket.getLocalAddress().toString());
			// open the message receiving thread
			messageThread = new MessageThread(reader, textArea);
			messageThread.start();
			isConnected = true;// connected
			return true;
		} catch (Exception e) {
			textArea.append("Connection to port： " + port + "    IP： " + hostIp
					+ "  failed" + "\r\n");
			isConnected = false;// disconnected
			return false;
		}
	}

	/**
	 * send message
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		writer.println(message);//send
		writer.flush();         //suggest
	}

	/**
	 * client turn off the connection
	 */
	@SuppressWarnings("deprecation")
	public synchronized boolean closeConnection() {
		try {
			sendMessage("CLOSE");// send the fixed command to turn down the connection
			messageThread.stop();// stop the message receiving thread
			// 释放资源
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;
			textArea.append("Disconnected!");
			frame.setTitle("Client");
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			isConnected = true;
			return false;
		}
	}

	// keep active of the thread
	class MessageThread extends Thread {
		private BufferedReader reader;
		private JTextArea textArea;

		// receiving the message
		public MessageThread(BufferedReader reader, JTextArea textArea) {
			this.reader = reader;
			this.textArea = textArea;
		}

		// turn down the link by server 
		public synchronized void closeCon() throws Exception {
			                                                    // empty the user list
			listModel.removeAllElements();
			                                                    // close the connection and release the resource
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;                                // adjust the link static
		}

		@SuppressWarnings("unchecked")
		public void run() {
			String message = "";
			while (true) {
				try {
					message = reader.readLine();
					StringTokenizer stringTokenizer = new StringTokenizer(
							message, "/#");
					String command = stringTokenizer.nextToken();// fixed command
					if (command.equals("CLOSE"))                 // server is closed
					{
						textArea.append("Server is closed!\r\n");
						closeCon();                              // close the connection by server
						return;                                  // end the thread
					} else if (command.equals("ADD")) {          // refresh the list when someone online
						String username = "";
						String userIp = "";
						if ((username = stringTokenizer.nextToken()) != null
								&& (userIp = stringTokenizer.nextToken()) != null) {
							User user = new User(username, userIp,null);
							onLineUsers.put(username, user);
							listModel.addElement(username);
						}
					} else if (command.equals("DELETE")) {       // refresh the list when someone offline
						String username = stringTokenizer.nextToken();
						User user = (User) onLineUsers.get(username);
						onLineUsers.remove(user);
						listModel.removeElement(username);
					} else if (command.equals("USERLIST")) {     // load the online user list
						int size = Integer
								.parseInt(stringTokenizer.nextToken());
						String username = null;
						String userIp = null;
						for (int i = 0; i < size; i++) {
							username = stringTokenizer.nextToken();
							userIp = stringTokenizer.nextToken();
							User user = new User(username, userIp,null);
							onLineUsers.put(username, user);
							listModel.addElement(username);
						}
					} else if (command.equals("MAX")) {          // server is full
						textArea.append(stringTokenizer.nextToken()
								+ stringTokenizer.nextToken() + "\r\n");
						closeCon();                              // turn off the connect by server
						JOptionPane.showMessageDialog(frame, "Server is full", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;                                  // end the thread
					} else if(command.equals("USED")){
						textArea.append(stringTokenizer.nextToken()
								+ stringTokenizer.nextToken() + "\r\n");
						closeCon();                              // turn off the connect by server
						JOptionPane.showMessageDialog(frame, "This name has been used.", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;                                  // end the thread
					} else if (command.equals("KEY")) {
						String username = stringTokenizer.nextToken();
						String key = stringTokenizer.nextToken();
						onLineUsers.get(username).setKey(key);
						textArea.append("key received! between: " + username + " key: " + key + "\r\n");
//						textArea.append(onLineUsers.get(username).getKey());
					} else if (command.equals("DES")) {
						String sender = stringTokenizer.nextToken();
						String context = stringTokenizer.nextToken();
						context = DES.encryptoperation(context, onLineUsers.get(sender).getKey(), "decrypt");
						textArea.append(sender + " : " + context + "\r\n");
					} else {                                      // normal messages
						textArea.append(message + "\r\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	private String getip() {                                     //use for the client get its own ip address
		String ip = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();
			return ip;
		} catch (Exception e) {
			ip = "127.0.0.1";
			return ip;
		}

}
}