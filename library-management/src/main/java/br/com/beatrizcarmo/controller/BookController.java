package br.com.beatrizcarmo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.beatrizcarmo.dto.BookDto;
import br.com.beatrizcarmo.service.BookService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping
    public BookDto addBook(@RequestBody BookDto bookDto) {
        return bookService.insertBook(bookDto);
    }
    
    @GetMapping
    public List<BookDto> all() {
    	return bookService.getBooks();
    }

    @GetMapping(value = "/{id}")
    public BookDto getById(@PathVariable(value = "id") UUID id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable(value = "id") UUID id, @RequestBody BookDto newbookDto){
        return bookService.updateBook(newbookDto, id);
    }

    @DeleteMapping("/{id}")
    public void deletBook(@PathVariable(value = "id") UUID id) {
        bookService.deletBook(id);
    }
}
