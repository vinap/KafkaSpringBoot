package com.demo.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for the server details.
 * 
 * @author vinayak
 *
 */
@Document
public class ServerDetail implements Serializable {

	private static final long serialVersionUID = 2869588330936039581L;

	@Id
	private String id;
	private String name;
	private String status;
	private String hostname;
	private String type;
	private String lastUpdateTimestamp;
	
	
	public ServerDetail() {
		super();
	}
 
	public ServerDetail(String id, String name, String status, String hostname, String type,
			String lastUpdateTimestamp) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.hostname = hostname;
		this.type = type;
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	@Override
	public String toString() {
		return "ServerDetail [id=" + id + ", name=" + name + ", status=" + status + ", hostname=" + hostname + ", type="
				+ type + ", lastUpdateTimestamp=" + lastUpdateTimestamp + "]";
	}
	 
	
	

}
