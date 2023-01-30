package br.com.beatrizcarmo.models;

import java.util.UUID;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users") 
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column
	private String name;
	
	@Column
	private String username;
	 
	@Column
	private String password;
	
	@Column
	private boolean isPunished;
	
	public User() {}
	
	public User(String name, String user, String password, boolean isPunished) {
		this.name = name;
		this.username = user;
		this.password = password;
		this.isPunished = isPunished;
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String user) {
		this.username = user;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean getIsPunished() {
		return isPunished;
	}
	public void setIsPunished(boolean isPunished) {
		this.isPunished = isPunished;
	}
}
