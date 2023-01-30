package br.com.beatrizcarmo.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.beatrizcarmo.dto.BookDto;
import br.com.beatrizcarmo.models.Book;
import br.com.beatrizcarmo.models.Library;
import br.com.beatrizcarmo.models.User;
import br.com.beatrizcarmo.repository.LibraryRepository;
import br.com.beatrizcarmo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookMapper {

	@Autowired
	private LibraryRepository libraryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
    public BookDto toDto (Book book) {
        final BookDto dto = new BookDto();
        
        dto.id = book.getId().toString();
        dto.name = book.getName();
        dto.description = book.getDescription();
        dto.author = book.getAuthor();
        dto.cost = book.getCost();
        dto.yearEdition = book.getYearEdition();
        dto.publisher = book.getPublisher();
        dto.isBorrowed = book.getIsBorrowed();
        dto.idLibrary = book.getLibrary().getId().toString();
        dto.idUser = book.getUser().getId().toString();
        
        return dto;
    }

    public List<BookDto> toDto(List<Book> books) {
        final ArrayList<BookDto> booksDto = new ArrayList<>();
        
        for(Book book : books) {
            booksDto.add(toDto(book));
        }
        
        return booksDto;
    }

    public Book toEntity(BookDto dto) {
        final Book book = new Book();
        
        book.setId(UUID.fromString(dto.id));
        book.setName(dto.name);
        book.setDescription(dto.description);
        book.setAuthor(dto.author);
        book.setCost(dto.cost);
        book.setYearEdition(dto.yearEdition);
        book.setPublisher(dto.publisher);
        book.setIsBorrowed(dto.isBorrowed);
        
        Optional<Library> libraryOpt = libraryRepository.findById(UUID.fromString(dto.idLibrary));
        if(libraryOpt.isPresent()) book.setLibrary(libraryOpt.get());
        
        Optional<User> userOpt = userRepository.findById(UUID.fromString(dto.idUser));
        if(userOpt.isPresent()) book.setUser(userOpt.get());
        
        return book;
    }
}
