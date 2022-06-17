import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;


public class SSHConector {
	private Connection sshConnection;
	private Session sshSession;
	private String user;
	private String password;
	private InputStream stdout;
	private InputStream stderr;
	private OutputStream stdin; 
	private int state = 0; //0=stopped, 1=started
	
	public SSHConector(String host, Integer port, String user, String password) {
		sshConnection = new Connection(host, port);
		this.user = user;
		this.password = password;
		this.state = 0;
	}
	
	public void start() {
		try {
			
			
			sshConnection.connect();
			boolean isAuthenticated = sshConnection.authenticateWithPassword(user, password);
			
			
			if (isAuthenticated == false)
				throw new IOException("Authentication failed.");
			
			sshSession = sshConnection.openSession();			
			sshSession.startShell();
			
			stdout = new StreamGobbler(sshSession.getStdout());
			stderr = new StreamGobbler(sshSession.getStderr());
			stdin = sshSession.getStdin();

			read(); //consume login message
			
			this.state = 1;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.state = 0;
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			this.state = 0;
			e.printStackTrace();
		}
	}
	
	public String read() throws IOException, InterruptedException{
		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
		StringBuffer sb = new StringBuffer();
		sshSession.waitForCondition(ChannelCondition.EOF, 5000);
		while (br.ready())
		{
			String line = br.readLine();
			sb.append(line + "<BR>");
		}		
		return sb.toString();
	}
	
	public String readError() throws IOException, InterruptedException{
		BufferedReader br = new BufferedReader(new InputStreamReader(stderr));
		StringBuffer sb = new StringBuffer();
		sshSession.waitForCondition(ChannelCondition.EOF, 5000);
		while (br.ready())
		{
			String line = br.readLine();
			sb.append(line + "<BR>");
		}		
		return sb.toString();
	}
	
	public void write(String message){
		PrintStream ps = new PrintStream(stdin);		
		ps.println(message);
		ps.flush();
	}
	
	public void stop() {
		sshSession.close();
		sshSession.close();
		this.state = 0;
	}
	
	public String execCommand(String command){
		String msg = "NO_ANSWER";
		
		if (state == 0)
			start();	
		
		try {
			write(command);
			msg = read();
			
			if (msg.length() <= 0)
				msg = readError();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return msg;
		
	}
}
