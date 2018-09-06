package com.watermelon.manager.common.manager.common.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class RemoteShellTool {
	private Connection conn;
	private String ipAddr;
	private int port;
	private String charset = Charset.defaultCharset().toString();
	private String userName;
	private String password;

	public RemoteShellTool(String ipAddr, int port, String userName, String password, String charset) {
		this.ipAddr = ipAddr;
		this.port = port;
		this.userName = userName;
		this.password = password;
		if (charset != null) {
			this.charset = charset;
		}

	}

	public boolean login() throws IOException {
		this.conn = new Connection(this.ipAddr, this.port);
		this.conn.connect();
		return this.conn.authenticateWithPassword(this.userName, this.password);
	}

	public String exec(String cmds) {
		InputStream in = null;
		String result = "";

		try {
			if (this.login()) {
				Session e1 = this.conn.openSession();
				e1.execCommand(cmds);
				in = e1.getStdout();
				result = this.processStdout(in, this.charset);
				this.conn.close();
			}
		} catch (IOException arg4) {
			arg4.printStackTrace();
		}

		return result;
	}

	public String processStdout(InputStream in, String charset) {
		StreamGobbler stdout = new StreamGobbler(in);
		BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
		StringBuffer sb = new StringBuffer();

		try {
			while (true) {
				String e = stdoutReader.readLine();
				if (e == null) {
					break;
				}

				sb.append(e);
				sb.append("\n");
			}
		} catch (IOException arg6) {
			arg6.printStackTrace();
		}

		return sb.toString();
	}
}