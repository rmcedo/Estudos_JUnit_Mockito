package br.com.beatrizcarmo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.beatrizcarmo.dto.LibraryDto;
import br.com.beatrizcarmo.dto.mapper.LibraryMapper;
import br.com.beatrizcarmo.exceptions.NotFoundException;
import br.com.beatrizcarmo.models.Library;
import br.com.beatrizcarmo.repository.LibraryRepository;
import br.com.beatrizcarmo.service.LibraryService;

@Service
public class LibraryServiceImpl implements LibraryService {

	@Autowired
	private LibraryRepository libraryRepository;
	
	@Autowired
	private LibraryMapper libraryMapper;
	
	//Insere uma biblioteca
	public LibraryDto insertLibrary(LibraryDto libraryDto) {
		final Library libraryEntity = libraryMapper.toEntity(libraryDto);
		final Library librarySaved = libraryRepository.save(libraryEntity);
		LibraryDto dto = libraryMapper.toDto(librarySaved);
		
		return dto;
	}
	
	//Retorna todas as bibliotecas cadastradas
	public List<LibraryDto> getLibraries() {
        List<Library> libraries = libraryRepository.findAll();
        return libraryMapper.toDto(libraries);
    }
	
	
	//Pega uma livraria pelo ID
	public LibraryDto getLibraryById(UUID id) {
		Optional<Library> libraryOpt = libraryRepository.findById(id);
		
		if(libraryOpt.isPresent()) {
			LibraryDto dto = libraryMapper.toDto(libraryOpt.get());
			return dto;
		}
		
		throw new NotFoundException();
	}
	
	//Atualiza uma biblioteca
	public LibraryDto updateLibrary(LibraryDto newLibraryDto, UUID id) {
		Optional<Library> libraryOpt = libraryRepository.findById(id);
		Library library = libraryOpt.orElseThrow();
		
		library.setName(newLibraryDto.name);
		library.setAdress(newLibraryDto.adress);
		library.setContact(newLibraryDto.contact);
		
		Library librarySaved = libraryRepository.save(library);
		LibraryDto dto = libraryMapper.toDto(librarySaved);
		
		return dto;
	}
	
	//Exclui uma biblioteca
	public void deletLibrary(UUID id) {
		Optional<Library> libraryOpt = libraryRepository.findById(id);
		Library library = libraryOpt.orElseThrow();
		
		libraryRepository.delete(library);
	}
	
	//Retorna todas as Bibliotecas contendo determinado nome
	public List<Library> getAllLibrariesContainsName(String value) {
		return libraryRepository.findByNameContaining(value);
	}
}