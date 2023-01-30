package br.com.beatrizcarmo.models;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="book") 
public class Book {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String name;
    
    @Column
    private String description;

    @Column()
    private String author;

    @Column()
    private Float cost;

    @Column()
    private LocalDate yearEdition;

    @Column
    private String publisher;
    
    @Column
    private Boolean isBorrowed;
    
    @Column
    private LocalDate devolutionDate;
    
    @ManyToOne
    private Library library;
    
    @ManyToOne
    private User user;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public LocalDate getYearEdition() {
        return yearEdition;
    }

    public void setYearEdition(LocalDate yearEdition) {
        this.yearEdition = yearEdition;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Boolean getIsBorrowed() {
		return isBorrowed;
	}
    
    public void setIsBorrowed(Boolean isBorrowed) {
		this.isBorrowed = isBorrowed;
	}
    
    public LocalDate getDevolutionDate() {
    	return devolutionDate;
    }
    
    public void setDevolutionDate(LocalDate devolutionDate) {
    	this.devolutionDate = devolutionDate;
    }

    public Library getLibrary() {
		return library;
	}
    public void setLibrary(Library library) {
		this.library = library;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	} 
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}