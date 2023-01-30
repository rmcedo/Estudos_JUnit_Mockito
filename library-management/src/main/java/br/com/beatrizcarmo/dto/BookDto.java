package br.com.beatrizcarmo.dto;

import java.time.LocalDate;

public class BookDto {

    public String id;
    public String name;
    public String description;
    public String author;
    public Float cost;
    public LocalDate yearEdition;
    public String publisher;
    public Boolean isBorrowed;
    public String idLibrary;
    public String idUser;
}