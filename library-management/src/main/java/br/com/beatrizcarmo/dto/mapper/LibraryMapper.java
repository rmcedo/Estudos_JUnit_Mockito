package br.com.beatrizcarmo.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.beatrizcarmo.dto.LibraryDto;
import br.com.beatrizcarmo.models.Library;

@Service
public class LibraryMapper {
	
	public LibraryDto toDto(Library library) {
		final LibraryDto dto = new LibraryDto();
		
		dto.id = library.getId().toString();
		dto.name = library.getName();
		dto.adress = library.getAdress();
		dto.contact = library.getContact();
		
		return dto;
	}
	
	public List<LibraryDto> toDto(List<Library> libraries) {
		final ArrayList<LibraryDto> librariesDto = new ArrayList<>();
		for (Library library : libraries) {
			librariesDto.add(toDto(library));
		}
		return librariesDto;
	}
	
	public Library toEntity(LibraryDto dto) {
		final Library library = new Library();
		library.setId(UUID.fromString(dto.id));
		library.setName(dto.name);
		library.setAdress(dto.adress);
		library.setContact(dto.contact);
		
		return library;
	}
}