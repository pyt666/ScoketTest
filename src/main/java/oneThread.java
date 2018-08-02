import javax.websocket.Session;

public class oneThread extends Thread{
	private Session session;
	private String message;
	
	public oneThread() {
		super();
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public oneThread(Session session, String message) {
		super();
		this.session = session;
		this.message = message;
	}

	@Override
	public void run() {
		Messagepojo msg = new Messagepojo();
		msg.setMsgContent(Integer.parseInt(message) + "");
		
			int i =Integer.parseInt(message);			
			while(true) {
				msg.setMsgContent(i + "");
				this.session.getAsyncRemote().sendObject(msg);
				i++;
				System.out.println("session:"+session.getId()+",i:"+i);
				try {
		             //2分钟刷新一次
		             Thread.sleep(2000);
		         } catch (InterruptedException e) {
		             e.printStackTrace();
		         }
				
			}
		
		// TODO Auto-generated method stub
		 
	}

	
	

}
