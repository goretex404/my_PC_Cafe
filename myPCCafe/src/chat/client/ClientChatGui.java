package chat.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientChatGui extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea jta = new JTextArea(40, 25);
	private JTextField jtf = new JTextField(25);
	private ClientBackground client = new ClientBackground();
	private static String nickName;

	public ClientChatGui() {

		add(jta, BorderLayout.CENTER);
		add(jtf, BorderLayout.SOUTH);
		jtf.addActionListener(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setBounds(200, 100, 400, 600);
		setTitle("채팅창_client");

		client.setGui(this);
		client.setNickname(nickName);
		client.connect();
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("당신의 닉네임을 설정하세요 : "); //자리번호로 바꿔주는게 좋을 듯 함.
		nickName = scanner.nextLine();
		scanner.close();
		
		new ClientChatGui();
	}

	@Override
	// 대화내용을 보내는 부분
	public void actionPerformed(ActionEvent e) {
		String msg = nickName + " : " + jtf.getText() + "\n";
//		jta.append("손님 : " + msg);
//		System.out.println(msg);
		client.sendMessage(msg);

		jtf.setText("");
	}

	public void appendMsg(String msg) {
		jta.append(msg);
	}
}
