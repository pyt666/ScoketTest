
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.xml.registry.infomodel.User;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value = "/websocket",encoders= {ServerEncoder.class})
public class WebSocketTest {
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();

	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	
	oneThread ot;

	
	/**
	 * 连接建立成功调用的方法
	 * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session){
		System.out.println("onOpen");
		/*String ipaddr=(String) session.getUserProperties().get("ClientIP");//将客户端IP地址从WebSocket的session中取出
		System.out.println("Incoming connection from "+ipaddr);*/
		InetSocketAddress remoteAddress = WebSocketUtils.getRemoteAddress(session);
		System.out.println("有新连接加入！" + remoteAddress);
		this.session = session;
		webSocketSet.add(this);     //加入set中
		addOnlineCount();		
		System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
		System.out.println("onClose");
		webSocketSet.remove(this);  //从set中删除
		subOnlineCount();           //在线数减1
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
		if(ot!=null) {
			ot.stop();
		}
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param device_sn 客户端发送过来的消息
	 * @param session 可选的参数
	 */
	@OnMessage
	public void onMessage(String device_sn, Session session) {
		System.out.println("onMessage");
		System.out.println("来自客户端的消息:" + device_sn);
		/*try {
			sendMessage(device_sn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		ot = new oneThread(session, device_sn);		
		ot.start();		
	}

	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("onError");
		System.out.println("发生错误");
		error.printStackTrace();
	}

	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException{
		System.out.println("sendMessage:"+message);
		Messagepojo messagepojo = new Messagepojo();
		messagepojo.setMsgContent(message);

		try {
			this.session.getBasicRemote().sendObject(messagepojo);
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketTest.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketTest.onlineCount--;
	}
}
