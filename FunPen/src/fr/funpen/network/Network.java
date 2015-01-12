package fr.funpen.network;


public class Network {
	private static Network 			instance = null;
	
	private Server					server;
	
	private Network() {
		server = Server.getInstance();
	}
	
	public static Network getInstance() {
		if (instance == null)
			instance = new Network();
		return instance;
	}
	
	public void loadServerFromSettings() {
		String ip = ""; //TODO load from phone
		int port = -1; //TODO load from phone
		String servicePage = ""; //TODO load from phone
		
		server.setIp(ip);
		server.setPort(port);
		server.setServicePage(servicePage);
	}
	
	public void sendRequest(RequestCallback callback) {
		//TODO send request to server
		String response = "";
		callback.onResponse(response);
	}
	
	/*********** Inner Callback **************/
	public interface RequestCallback {
		public void onResponse(String response);
	}
}
