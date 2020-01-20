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
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
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

public class Server {
	// the basic layout and structure is same as client
	private JFrame frame;
	private JTextArea contentArea;
	private JTextField txt_message;
	private JTextField txt_max;
	private JTextField txt_port;
	private JButton btn_start;
	private JButton btn_stop;
	private JButton btn_send;
	private JPanel northPanel1;
	private JPanel northPanel2;
	private JPanel northPanelm;
	private JPanel southPanel;
	private JScrollPane rightPanel;
	private JScrollPane leftPanel;
	private JSplitPane centerSplit;
	private JList<String> userList;
	private DefaultListModel<String> listModel;
	private ServerSocket serverSocket;
	private ServerThread serverThread;
	private ArrayList<ClientThread> clients;
	
	private Icon image = null;

	private boolean isStart = false;
	
	String[] commands = {"#BROADCAST+","#PRIVATE-+","#KICK-","#STATIC-","#LIST"};
	
	static String[] alhpalist = {"0","1","2","3","4","5","6",
					   "7","8","9","0","a","b","c",
					   "d","e","f","g","h","i","j",
					   "k","l","m","n","o","p","q",
					   "r","s","t","u","v","w","x","y","z"};

	// main
	public static void main(String[] args) {
		new Server();
	}

	// send method
	@SuppressWarnings("deprecation")
	public void send() {
		if (!isStart) {                                                                  //we check the 
			JOptionPane.showMessageDialog(frame, "Please start server first", "Error",   //connection static
					JOptionPane.ERROR_MESSAGE);                                          //first
			return;                                                                      //then we check 
		}                                                                                //weather there 
		if (clients.size() == 0) {                                                       //is online user
			JOptionPane.showMessageDialog(frame, "no online users", "Error",             //existed
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String message = txt_message.getText().trim();
		if (message == null || message.equals("")) {                                     //check if the message is null
			JOptionPane.showMessageDialog(frame, "Please type something", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (message.contains("#KICK-")) {                                                //the kick command:{KICK-username}
			String username = message.substring(message.indexOf("-")+1);
			for (int k = clients.size()-1; k >= 0; k--){
				if (clients.get(k).user.getName().equals(username)) {
					clients.get(k).getWriter().println("CLOSE");//send fixed command
					for (int i = clients.size() - 1; i >= 0; i--) {
						clients.get(i).getWriter().println(
								"DELETE#" + clients.get(k).user.getName());//send fixed command
						clients.get(i).getWriter().flush();
					}
					listModel.removeElement(clients.get(k).user.getName());              // refresh the userlist
					sendServerMessage("User: " + username + " is kicked out");
					
					// delete this client thread
					for (int i = clients.size() - 1; i >= 0; i--) {
						if (clients.get(i).getUser() == clients.get(k).user) {
							ClientThread temp = clients.get(i);
							clients.remove(i);// delete
							temp.stop();// stop
						}
					}
//					System.out.println("mark");
					break;
				}
				if (k - 1 < 0) {
					contentArea.append("User: " + username + " is not exist" + "\r\n");
					break;
				}
			}
		}
		if (message.contains("#PRIVATE-")) {                                              //the server private{PRIVATE-username+text}
			try {
				@SuppressWarnings("unused")
				String username = message.substring(message.indexOf("-")+1,message.indexOf("+"));//check the command
			} catch (Exception e) {
				// TODO: handle exception
				contentArea.append("Invalid command\n");
				txt_message.setText(null);
				return;
			}
			String username = message.substring(message.indexOf("-")+1,message.indexOf("+"));
			for (int k = clients.size()-1; k >= 0; k--){
				if (clients.get(k).user.getName().equals(username)) {
					String text = message.substring(message.indexOf("+")+1);
					clients.get(k).getWriter().println("Server：" + text.toString() + "(private)");
					clients.get(k).getWriter().flush();
//					System.out.println(text);
					break;
				}
				if (k - 1 < 0) {
					contentArea.append("User: " + username + " is not exist" + "\r\n");//user not find
					break;
				}
			}
		}
		if (message.contains("STATIC-")) {                                             //check someone's command record{STATIC-username}
			contentArea.append(message);
			String username = message.substring(message.indexOf("-")+1);
			for (int k = clients.size()-1; k >= 0; k--){
				if (clients.get(k).user.getName().equals(username)) {//find the user
					contentArea.append("\n" + clients.get(k).user.getHistory());
					System.out.println("mark");
					break;
				}
				if (k - 1 < 0) {
					contentArea.append("User: " + username + " is not exist" + "\r\n");//user not find
					break;
				}
			}
		}
		if(message.contains("#BROADCAST+")){
			try {
//				System.out.println("MARK1");
				@SuppressWarnings("unused")
				String text = message.substring(message.indexOf("+"));//this try catch ensure the command form is correct 
			} catch (Exception e) {                                   //or it will not send to the server
				// TODO: handle exception
				contentArea.append("Invalid command\n");
				txt_message.setText(null);
//				System.out.println("MARK2");
				return;
			}
//			sendServerMessage(frame.getTitle() + "#" + " BROADCAST:" + "#" + message.substring(message.indexOf("+")));
		}
		if (message.equals("#LIST")) {
			for(int i = 0;i<(listModel.size());i++){
				contentArea.append(listModel.get(i) + "\n");
			}
		}
		else{//send to all
			sendServerMessage(message);
			contentArea.append("Server：" + txt_message.getText() + "\r\n");
		}
		txt_message.setText(null);
	}

	// constructer
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Server() {
		frame = new JFrame("Server");
		contentArea = new JTextArea();
		contentArea.setEditable(false);
		contentArea.setForeground(Color.blue);
		txt_message = new JTextField();
		txt_max = new JTextField("30");
		txt_port = new JTextField("0001");
		btn_start = new JButton("Connect");
		btn_stop = new JButton("Disconnect");
		btn_send = new JButton("Send");
		btn_stop.setEnabled(false);
		listModel = new DefaultListModel();
		userList = new JList(listModel);
		
		@SuppressWarnings({ })
		JList command = new JList(commands);
		command.setBorder(new TitledBorder("command"));
		command.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {//setup all the command list and event
				// TODO Auto-generated method stub
				Object index = ((JList)e.getSource()).getSelectedValue();
				if (index == "#KICK-") {
					txt_message.setText("");
					txt_message.setText("#KICK-");
				}
				if (index == "#BROADCAST+") {
					txt_message.setText("#BROADCAST+");
				}
				if (index == "#PRIVATE-+") {
					txt_message.setText("#PRIVATE-+");
				}
				if (index == "#STATIC-") {
					txt_message.setText("#STATIC-");
				}
				if (index == "#LIST") {
					txt_message.setText("#LIST");
				}
			}
		});
		
		southPanel = new JPanel(new BorderLayout());
		southPanel.setBorder(new TitledBorder("input"));
		southPanel.add(txt_message, "Center");
		southPanel.add(btn_send, "East");
		southPanel.add(command,"West");
		leftPanel = new JScrollPane(userList);
		leftPanel.setBorder(new TitledBorder("online user"));
		rightPanel = new JScrollPane(contentArea);
		rightPanel.setBorder(new TitledBorder("text area"));
		centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel,
				rightPanel);
		centerSplit.setDividerLocation(100);
		northPanel1 = new JPanel();
		northPanel1.setLayout(new GridLayout(3, 2));
		northPanel1.add(new JLabel("Max"));
		northPanel1.add(txt_max);
		northPanel1.add(new JLabel("Port"));
		northPanel1.add(txt_port);
		northPanel1.add(btn_start);
		northPanel1.add(btn_stop);
		northPanel1.setBorder(new TitledBorder("server info"));
		
		northPanel2 = new JPanel();
//		northPanel2.setBorder(new TitledBorder("other"));
		try {
//			image = new ImageIcon("timg.jpg");
			JLabel label = new JLabel();
			image = new ImageIcon("server.jpg");
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

		frame.setLayout(new BorderLayout());
		frame.add(northPanelm, "North");
		frame.add(centerSplit, "Center");
		frame.add(southPanel, "South");
		frame.setSize(500, 600);
		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setLocation((screen_width - frame.getWidth()) / 2,
				(screen_height - frame.getHeight()) / 2);
		frame.setVisible(true);

		// event when close window
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (isStart) {
					closeServer();// close server
				}
				System.exit(0);// exit
			}
		});

		// event when press enter
		txt_message.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});

		// event when click send
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send();
			}
		});

		// event when click Connect
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isStart) {
					JOptionPane.showMessageDialog(frame, "Server is already started",
							"ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int max;
				int port;
				try {
					try {
						max = Integer.parseInt(txt_max.getText());
					} catch (Exception e1) {
						throw new Exception("invalid number");
					}
					if (max <= 0) {
						throw new Exception("invalid number");
					}
					try {
						port = Integer.parseInt(txt_port.getText());
					} catch (Exception e1) {
						throw new Exception("invalid port");
					}
					if (port <= 0) {
						throw new Exception("invalid port");
					}
					serverStart(max, port);
					contentArea.append("Server launched. Max people:" + max + ",port:" + port
							+ "\r\n");
					JOptionPane.showMessageDialog(frame, "Launched successfully");
					btn_start.setEnabled(false);
					txt_max.setEnabled(false);
					txt_port.setEnabled(false);
					btn_stop.setEnabled(true);
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(frame, exc.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// event when click Disconnect
		btn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isStart) {
					JOptionPane.showMessageDialog(frame, "Server is down", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					closeServer();
					btn_start.setEnabled(true);
					txt_max.setEnabled(true);
					txt_port.setEnabled(true);
					btn_stop.setEnabled(false);
					contentArea.append("Server is down\r\n");
					JOptionPane.showMessageDialog(frame, "Server is down");
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(frame, "Unknow Error", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	//start server
	public void serverStart(int max, int port) throws java.net.BindException {
		try {
			clients = new ArrayList<ClientThread>();
			serverSocket = new ServerSocket(port);
			serverThread = new ServerThread(serverSocket, max);
			serverThread.start();
			isStart = true;
		} catch (BindException e) {
			isStart = false;
			throw new BindException("port inuse");
		} catch (Exception e1) {
			e1.printStackTrace();
			isStart = false;
			throw new BindException("Unknow Error");
		}
	}

	//close server
	@SuppressWarnings("deprecation")
	public void closeServer() {
		try {
			if (serverThread != null)
				serverThread.stop();// stop thread

			for (int i = clients.size() - 1; i >= 0; i--) {
				// close all client
				clients.get(i).getWriter().println("CLOSE");
				clients.get(i).getWriter().flush();
				// release resource
				clients.get(i).stop();// stop this client's thread
				clients.get(i).reader.close();
				clients.get(i).writer.close();
				clients.get(i).socket.close();
				clients.remove(i);
			}
			if (serverSocket != null) {
				serverSocket.close();// close server
			}
			listModel.removeAllElements();// clear userlist
			isStart = false;
		} catch (IOException e) {
			e.printStackTrace();
			isStart = true;
		}
	}

	// send message to all
	public void sendServerMessage(String message) {
		for (int i = clients.size() - 1; i >= 0; i--) {
			System.out.println("MARK S");
			clients.get(i).getWriter().println("Server:" + message.substring(message.indexOf("+")+1) + "(BROADCAST)");
			clients.get(i).getWriter().flush();
		}
	}

	// server thread
	class ServerThread extends Thread {
		private ServerSocket serverSocket;
		private int max;// max

		// 线程构造器
		public ServerThread(ServerSocket serverSocket, int max) {
			this.serverSocket = serverSocket;
			this.max = max;
		}

		public void run() {
			while (true) {// keep active for client thread
				try {
					Socket socket = serverSocket.accept();//get client socket
					if (clients.size() == max) {// check max
						BufferedReader r = new BufferedReader(//read thread
								new InputStreamReader(socket.getInputStream()));//get user socket->to inputStream->to buffer text
						PrintWriter w = new PrintWriter(socket//write thread
								.getOutputStream());
						// get info of client
						String inf = r.readLine();
						StringTokenizer st = new StringTokenizer(inf, "#");
						User user = new User(st.nextToken(), st.nextToken(),null);
						// result
						w.println("MAX#Server: sorry" + user.getName()
								+ user.getIp() + "Server is full. Please try later");
						w.flush();
						// release resource
						r.close();
						w.close();
						socket.close();
						continue;
					}
					ClientThread client = new ClientThread(socket);//create client thread
					client.start();// fresh user list
					clients.add(client);//add new user
					listModel.addElement(client.getUser().getName());// refresh
					contentArea.append(client.getUser().getName()//send the new user to other
							+ client.getUser().getIp() + "Connected\r\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// constructor of clientthread
	class ClientThread extends Thread {
		private Socket socket;
		private BufferedReader reader;
		private PrintWriter writer;
		private User user;

		public BufferedReader getReader() {
			return reader;
		}

		public PrintWriter getWriter() {
			return writer;
		}

		public User getUser() {
			return user;
		}

		// Client thread constructor
		public ClientThread(Socket socket) {
			try {
				this.socket = socket;
				reader = new BufferedReader(new InputStreamReader(socket
						.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream());
				// get client info
				String inf = reader.readLine();
				StringTokenizer st = new StringTokenizer(inf, "#");
				String name = st.nextToken();
				String ip = st.nextToken();
				for (int i = clients.size() - 1; i >= 0; i--) {//check reused name
					System.out.println(i);
					if(clients.get(i).user.getName().equals(name)){
						System.out.println("mark");
						BufferedReader r = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						PrintWriter w = new PrintWriter(socket
								.getOutputStream());
						String temp = r.readLine();
						StringTokenizer tempst = new StringTokenizer(temp, "#");
						User user = new User(tempst.nextToken(), tempst.nextToken(),null);
						w.println("USED#Server:sorry " + user.getName()
								+ user.getIp() + " this name has already used");
						w.flush();
						r.close();
						w.close();
						socket.close();
						return;
					}
				}
				user = new User(name, ip,null);
				// result success
				writer.println(user.getName() + user.getIp() + "Connected!");
				writer.flush();
				// result user info
				if (clients.size() > 0) {
					String temp = "";
					for (int i = clients.size() - 1; i >= 0; i--) {
						temp += (clients.get(i).getUser().getName() + "/" + clients
								.get(i).getUser().getIp())
								+ "#";
					}
					writer.println("USERLIST#" + clients.size() + "#" + temp);//fixed command
					writer.flush();
				}
				// to all suer
				for (int i = clients.size() - 1; i >= 0; i--) {
					clients.get(i).getWriter().println(
							"ADD#" + user.getName() + user.getIp());//fixed command
					clients.get(i).getWriter().flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("deprecation")
		public void run() {// keep active
			String message = null;
			while (true) {
				try {
					message = reader.readLine();// get message from client
					if (message.contains("DESCONNECT#")){
						String username = message.substring(message.indexOf("#")+1);
						String key = keyGenerator();
						boolean havethisuser = false;
						for (int i = clients.size() - 1;i >= 0; i--){
							if (clients.get(i).user.getName().equals(username)) {
								havethisuser = true;
							}
						}
						if (havethisuser) {
							for (int i = clients.size()-1;i >= 0; i--){
								if (clients.get(i).user.getName().equals(username)) {
									clients.get(i).getWriter().println("KEY#" + this.getUser().getName() + "#" + key);
									clients.get(i).getWriter().flush();
								}
								if (clients.get(i).user.getName().equals(this.getUser().getName())) {
									clients.get(i).getWriter().println("KEY#" + username+ "#" + key);
									clients.get(i).getWriter().flush();
								}
							}
						}
						else{
							for (int i = clients.size()-1;i >= 0; i--){
								if (clients.get(i).user.getName().equals(this.getUser().getName())) {
									clients.get(i).getWriter().println("NO SUCH USER \r\n");
									clients.get(i).getWriter().flush();
								}
							}
						}
						//need more code
						continue;
					}
					if (message.equals("CLOSE"))// fixed command
					{
						contentArea.append(this.getUser().getName()
								+ this.getUser().getIp() + "Disconnected!\r\n");
						dispatcherMessage("Server" + "#" + "ALL" + "#" + this.getUser().getName()
								+ this.getUser().getIp() + "Disconnected!");
						// release resource
						reader.close();
						writer.close();
						socket.close();

						// to all users
						for (int i = clients.size() - 1; i >= 0; i--) {
							clients.get(i).getWriter().println(
									"DELETE#" + user.getName());//Fixed command
							clients.get(i).getWriter().flush();
						}

						listModel.removeElement(user.getName());// refresh user list

						// delete this client's thread
						for (int i = clients.size() - 1; i >= 0; i--) {
							if (clients.get(i).getUser() == user) {
								ClientThread temp = clients.get(i);
								clients.remove(i);// delete this client's thread
								temp.stop();// stop this client's thread
								return;
							}
						}
					}
					if (message.contains("#PRIVATE-")) {// send the private message to the specific client
						String username = message.substring(message.indexOf("-")+1,message.indexOf("+"));
						System.out.println(username);
						for (int k = clients.size()-1; k >= 0; k--){
							if (clients.get(k).user.getName().equals(username)) {//fin correct user
								String text = message.substring(message.indexOf("+")+1);
								clients.get(k).getWriter().println("DES#" + this.getUser().getName() + "#" + text);
								clients.get(k).getWriter().flush();
								this.user.addhistory(message);
//								System.out.println(text);
//								System.out.println(username);
//								System.out.println(text.toString());
//								System.out.println(message);
							}
						}
						continue;
					}
					if (message.contains("#BROADCAST+")) {
						String text = message.substring(message.indexOf("+")+1);
						System.out.println("MARK B");
						for (int k = (clients.size()-1); k >= 0;k--){
								clients.get(k).getWriter().println(this.getUser().getName() + " : " + text.toString() + "(BROADCAST)\n");
								clients.get(k).getWriter().flush();
						}
						continue;
					}
					if (message.equals("#LIST")) {
						System.out.println("MARK B");
						for(int i = 0;i<(listModel.size());i++){
							this.getWriter().println(listModel.get(i) + "\n");
							this.getWriter().flush();
						}
						continue;
					}
					else {
//						dispatcherMessage(message);// to all
						contentArea.append("UNKNOWN ERROR \r\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// to all
		public void dispatcherMessage(String message) {
			StringTokenizer stringTokenizer = new StringTokenizer(message, "#");
			String source = stringTokenizer.nextToken();
			String owner = stringTokenizer.nextToken();
			String content = stringTokenizer.nextToken();
			message = source + " said：" + content;
			contentArea.append(message + "\r\n");
			if (owner.equals("ALL")) {// fixed command
				for (int i = clients.size() - 1; i >= 0; i--) {
					clients.get(i).getWriter().println(message/*.substring(message.indexOf("+")+1)*/ + "(BROADCAST)");
					clients.get(i).getWriter().flush();
				}
			}
		}
	}

	public static String keyGenerator(){
		String key = "";
		Random random = new Random();
		for (int i = 0;i<8;i++){
			key = key + "" + alhpalist[random.nextInt(36)];
		}
		return key;
	}
}