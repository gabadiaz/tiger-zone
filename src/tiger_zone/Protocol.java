package tiger_zone;

enum ProtocolState
{
	TOURNAMENT,
	AUTHENTICATION,
	CHALLENGE,
	ROUND,
	MATCH,
	MOVE,
	UNDEFINED
}

public class Protocol extends Client
{
	private String tPassword, pid, gid, cid, rounds, userName, password;
	private ProtocolState pState; 
	
	public Protocol(String serverName, int portNumber, String tPassword, String userName, String password)
	{
		super(serverName, portNumber);
		setPid("-1");
		setProtocolState(ProtocolState.UNDEFINED);
		setTournamentPassword(tPassword);
		setUserName(userName);
		setPassword(password);
		
	}
	
	
	public void tournamentProtocol()
	{
		String fromServer = this.receiveFromServer();
		System.out.println("Server: " + fromServer);
		if(fromServer.equals("THIS IS SPARTA!"))
		{
			authenticationProtocol(fromServer);
		}
	}
	
	public void authenticationProtocol(String fromServer)
	{
		if(!fromServer.equals("THIS IS SPARTA!"))
			return;
		String toServer = "Join " + this.getTournamentPassword();
		sendToServer(toServer);
		System.out.println("Client: " + toServer);
		
		fromServer = receiveFromServer();
		System.out.println("Server: "  + fromServer);
		//THIS ALWAYS GOES TO FALSE FOR SOMEREASON
		//boolean serverTest = "Hello!".equals(fromServer.trim());
		//if(!serverTest)
		//	return;
		toServer = "I AM " + getUserName() + " " + getPassword();
		sendToServer(toServer);
		System.out.println("Client: " + toServer);
		
		fromServer = receiveFromServer();
		System.out.println("Server: "  + fromServer);
		
		for(int i = 0; i < fromServer.length(); i++)
		{
			char j = fromServer.charAt(i);
			
			if(j == ' ')
			{
				pid = "";
				for(int k = i + 1; k < fromServer.length(); k++)
				{
					if(fromServer.charAt(k) == ' ')
						break;
					pid = pid + fromServer.charAt(k);
				}
				break;
			}
		}
		challengeProtocol(receiveFromServer());
	}
	
	public void challengeProtocol(String fromServer)
	{
		System.out.println("Server: " + fromServer);
		int spaceCount = 0;
		for(int i = 0; i < fromServer.length(); i++)
		{
			if(fromServer.charAt(i) == ' ')
				spaceCount++;
			switch(spaceCount)
			{
			case 2 : cid = "";
				for(int k = i+1; k < fromServer.length(); k++)
				{
					if(fromServer.charAt(k) == ' ')
					{
						spaceCount++;
						i = k;
						break;
					}
					cid = cid + fromServer.charAt(k);
				}
				break;
			case 6 : rounds = "";
				for(int k = i+1; k < fromServer.length(); k++)
				{
					if(fromServer.charAt(k) == ' ')
					{
						spaceCount++;
						i = k;
						break;
					}
					rounds = rounds + fromServer.charAt(k);
				}
				break;
			default:
				break;
			}

		}
		
		
		for(int i = 0; i < Integer.parseInt(rounds); i++)
		{
			roundProtocol(receiveFromServer());
		}
		
		fromServer = receiveFromServer();
		//END OF CHALLENGES or PLEASE WAIT FOR NEXT CHALLEGE
	}
	
	public void roundProtocol(String fromServer)
	{
		//fromServer should start with BEGIN ROUND <rid> OF <rounds>
		
	}
	
	public void matchProtocol(String fromServer)
	{
		
	}
	
	public void moveProtocol(String clientMove){
		
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTournamentPassword() {
		return tPassword;
	}

	public void setTournamentPassword(String tPassword) {
		this.tPassword = tPassword;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public ProtocolState getProtocolState() {
		return pState;
	}

	public void setProtocolState(ProtocolState pState) {
		this.pState = pState;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getRounds() {
		return rounds;
	}


	public void setRounds(String rounds) {
		this.rounds = rounds;
	}
	
	
}