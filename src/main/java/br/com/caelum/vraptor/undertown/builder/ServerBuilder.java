package br.com.caelum.vraptor.undertown.builder;

import java.util.HashMap;
import java.util.Map;

public class ServerBuilder {

	public class WebAppContext {

		private String context;

		public WebAppContext(String context) {
			this.context = context;
		}

		public WebAppFolder webAppFolder(String folderPath) {
			return new WebAppFolder(folderPath, context);
		}

	}

	public class ServerAddress {

		private String warName;
		private String folderPath;
		private String context;
		private int port;
		private String address;
		private Map<String, String> initParameters = new HashMap<String, String>();

		public ServerAddress(String warName, String folderPath, 
				String context, int port, String address) {
			this.warName = warName;
			this.folderPath = folderPath;
			this.context = context;
			this.port = port;
			this.address = address;
		}
		
		public ServerAddress addInitParameter(String key, String value) {
			initParameters.put(key, value);
			return this;
		}

		public void start() {
			VRaptorServer.start(context, folderPath, warName, 
					port, address, initParameters);
		}

	}

	public class ServerPort {

		private String warName;
		private String folderPath;
		private String context;
		private int port;

		public ServerPort(String warName, String folderPath, String context, int port) {
			this.warName = warName;
			this.folderPath = folderPath;
			this.context = context;
			this.port = port;
		}

		public ServerAddress address(String address) {
			return new ServerAddress(warName, folderPath, context, port, address);
		}

	}

	public class WarName {

		private String warName;
		private String folderPath;
		private String context;

		public WarName(String warName, String folderPath, String context) {
			this.warName = warName;
			this.folderPath = folderPath;
			this.context = context;
		}

		public ServerPort port(int port) {
			return new ServerPort(warName, folderPath, context, port);
		}

	}

	public class WebAppFolder {

		private String folderPath;
		private String context;

		public WebAppFolder(String folderPath, String context) {
			this.folderPath = folderPath;
			this.context = context;
		}

		public WarName warName(String warName) {
			return new WarName(warName, folderPath, context);
		}

	}

	private ServerBuilder() {

	}

	public static WebAppContext context(String context) {
		if(context == null){
			throw new IllegalArgumentException("You must pass a not null context");
		}
		return new ServerBuilder().new WebAppContext(context);
	}
	
	public static WebAppContext root() {
		return context("");
	}

}
