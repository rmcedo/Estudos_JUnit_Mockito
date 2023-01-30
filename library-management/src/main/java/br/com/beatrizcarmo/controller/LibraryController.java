package br.com.beatrizcarmo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.beatrizcarmo.dto.LibraryDto;
import br.com.beatrizcarmo.service.LibraryService;


@RestController
@RequestMapping("/libraries")
public class LibraryController {
	
	@Autowired
	private LibraryService libraryService;
	
	
	@PostMapping
	public LibraryDto addLibrary(@RequestBody LibraryDto libraryDto) {
		return libraryService.insertLibrary(libraryDto);
	}
	
	@GetMapping
	public List<LibraryDto> getAllLibraries() {
		return libraryService.getLibraries();
	}
	
	@GetMapping(value = "/{id}")
	public LibraryDto getById(@PathVariable(value = "id") UUID id) {
		return libraryService.getLibraryById(id);
	}
	
	@PutMapping("/{id}")
	public LibraryDto updateLibrary(@RequestBody LibraryDto newLibraryDto, @PathVariable(value = "id") UUID id) {
		return libraryService.updateLibrary(newLibraryDto, id);
	}
	
	@DeleteMapping("/{id}")
	public void deletLibrary(@PathVariable(value = "id") UUID id) {
		libraryService.deletLibrary(id);
	}
}