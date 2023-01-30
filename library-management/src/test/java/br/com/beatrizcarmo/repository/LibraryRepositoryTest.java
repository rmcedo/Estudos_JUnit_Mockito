package br.com.beatrizcarmo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.beatrizcarmo.models.Library;

@DataJpaTest
@Sql("/h2/LibraryRepositoryTest.sql")
@RunWith(SpringRunner.class)
public class LibraryRepositoryTest {
	
	@Autowired
	LibraryRepository repository;
	
	//Exemplo
	@Test
	public void save_shouldSaveLibrary() {
		Library library = new Library();
		library.setName("Teste 2");
		
		Long numberOfLibrariesBefore = repository.count();
		
		repository.save(library);
		
		Long numberOfLibrariesAfter = repository.count();
		
		assertThat(numberOfLibrariesAfter).isEqualTo(numberOfLibrariesBefore + 1);
	}
}
