package br.com.beatrizcarmo.models;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="library") 
public class Library {

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
    private String adress;
    
    @Column
    private Integer contact;
        
    @JsonIgnore
	@ManyToMany()
	private List<User> users = new ArrayList<>();

    public Library() {}
    
	public Library(UUID id, String name, String username, String password, String adress, int contact) {
		this.setId(id);
		this.setName(name);
		this.setUsername(username);
		this.setPassword(password);
		this.setAdress(adress);
		this.setContact(contact);
	}
	
	public Library(String name) {
		this.setName(name);
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
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public int getContact() {
		return contact;
	}

	public void setContact(int contact) {
		this.contact = contact;
	}
}