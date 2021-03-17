package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerBackground {

	// step1. 서버 클라이언트 간 메세지 전달 (console)
	// step2. GUI 생성
	// step3. GUI를 통한 서버 클라이언트 메세지 상호 전달 (기능연동)
	// step4. 다중 클라이언트 허용.

	private ServerSocket serverSocket;
	private Socket socket;
	private ServerChatGui gui;
	private String msg;

	/**
	 * client들의 정보를 저장하는 맵.
	 */
	private Map<String, DataOutputStream> clientsMap = new HashMap<String, DataOutputStream>();

	public void setGui(ServerChatGui gui) {
		this.gui = gui;
	}

	public void setting() {
		try {

			Collections.synchronizedMap(clientsMap); // ClientMap을 정리해줌
			serverSocket = new ServerSocket(7777);

			while (true) {
				// 클라이언트를 받아 소켓을 열어주는 부분 while문 처리로 다중 클라이언트를 받음
				System.out.println("서버 대기중...");
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + "에서 접속했습니다.");
				// 새로운 클라이언트 발생 시 쓰레드 클래스로 소켓정보 전달.
				Receiver receiver = new Receiver(socket);
				receiver.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ServerBackground serverBackground = new ServerBackground();
		serverBackground.setting();
	}

	public void addClient(String nick, DataOutputStream out) throws IOException {
		sendMessage(nick + "님이 접속하셨습니다.\n");
		clientsMap.put(nick, out);
	}

	public void removeClient(String nick) {
		sendMessage(nick + "님이 나가셨습니다.");
		clientsMap.remove(nick);
	}

	public void sendMessage(String msg) {
		// iterator를 이용해 clientsMap의 모든 값 가져오기
		Iterator<String> it = clientsMap.keySet().iterator();
		String key = "";

		while (it.hasNext()) {
			key = it.next();
			try {
				clientsMap.get(key).writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 네트워크 소켓을 전달 받아서 대화를 주고받음
	 * 
	 * @author cho
	 *
	 */
	class Receiver extends Thread {
		private DataInputStream in;
		private DataOutputStream out;
		private String nick;

		//
		public Receiver(Socket socket) {
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());

				nick = in.readUTF();
				addClient(nick, out);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				while (in != null) {
					msg = in.readUTF();
					sendMessage(msg);
					gui.appendMsg(msg);
				}
			} catch (IOException e) {
				// 접속 종료 시 예외쪽으로 들어옴
				removeClient(nick);
			}
		}
	}

}
