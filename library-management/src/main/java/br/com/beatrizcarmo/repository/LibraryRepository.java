package br.com.beatrizcarmo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.beatrizcarmo.models.Library;

@Repository
public interface LibraryRepository extends JpaRepository<Library, UUID> {

	Library findByUsername(String username);
	List<Library> findByNameContaining(String name);
}