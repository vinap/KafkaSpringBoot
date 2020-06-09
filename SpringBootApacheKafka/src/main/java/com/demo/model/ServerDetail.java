package com.demo.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * Model class for the server details.
 * @author vinayak
 *
 */
@Document
public class ServerDetail implements Serializable{
     
	private static final long serialVersionUID = 2869588330936039581L;

	@Id
    private String id;
   
    private String ServerName;
    
    private String ServerStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServerName() {
		return ServerName;
	}

	public void setServerName(String serverName) {
		ServerName = serverName;
	}

	public String getServerStatus() {
		return ServerStatus;
	}

	public void setServerStatus(String serverStatus) {
		ServerStatus = serverStatus;
	}

	@Override
	public String toString() {
		return "ServerDetail [id=" + id + ", ServerName=" + ServerName + ", ServerStatus=" + ServerStatus + "]";
	}
   

    
}
