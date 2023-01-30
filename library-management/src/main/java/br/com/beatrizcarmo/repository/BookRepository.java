package br.com.beatrizcarmo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import br.com.beatrizcarmo.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
	
	Optional<Book> findBookById(UUID id);
}
