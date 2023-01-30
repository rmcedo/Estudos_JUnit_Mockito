package br.com.beatrizcarmo.service.impl;

import br.com.beatrizcarmo.dto.BookDto;
import br.com.beatrizcarmo.dto.mapper.BookMapper;
import br.com.beatrizcarmo.exceptions.NotFoundException;
import br.com.beatrizcarmo.exceptions.WrongParametersException;
import br.com.beatrizcarmo.models.Book;
import br.com.beatrizcarmo.models.User;
import br.com.beatrizcarmo.repository.BookRepository;
import br.com.beatrizcarmo.repository.UserRepository;
import br.com.beatrizcarmo.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookMapper bookMapper;

	@Autowired
	private UserRepository userRepository;

	// ======> Exemplo testes

	// Verifica se o usuário pode pegar o livro emprestado
	public boolean checkBookingPossibility(User user, Book book) {

		if (book.getIsBorrowed() == null)
			throw new IllegalArgumentException("O livro não possui o atributo necessário");

		return !user.getIsPunished() && !book.getIsBorrowed();
	}

	// Calcula o desconto baseado em uma porcentagem
	public Double calculateDiscountBasedOnPercentage(Book book, Double percentage) {
		return book.getCost() * percentage / 100;
	}

	// ====================== Exercícios - JUNIT ===========================

	// 1 - Lista os usuários responsáveis pelos empréstimos dos livros - FEITO
	public List<User> getUsersResponsibleForBorrowed(List<Book> books) {
		List<User> users = new ArrayList<>();

		books.forEach(book -> {
			if (book.getIsBorrowed() && book.getUser() != null)
				users.add(book.getUser());
		});

		return users;
	}

	// 2 - Conta o número de livros emprestados - FEITO
	public Long countNumberOfBorrowedBooks(List<Book> books) {
		if (books == null || books.isEmpty())
			throw new IllegalArgumentException("Nenhum livro foi encontrado");

		return books.stream().filter(Book::getIsBorrowed).count();
	}

	// 3 - Calcula o preço total dos livros passados
	public Double calculateTotalCostOfBooks(List<Book> books) {
		if (books == null || books.isEmpty())
			throw new IllegalArgumentException("Nenhum livro foi encontrado");

		Double totalCost = 0.0;

		for (Book book : books) {
			if (book.getCost() == null)
				throw new IllegalArgumentException("Livro cadastrado sem preço");

			totalCost += book.getCost().doubleValue();
		}

		return totalCost;
	}

	// 4 - Pega o valor máximo da lista de livros passada
	public Double getMaxBooksCost(List<Book> books) {
		Double maxCost = 0.0;

		for (Book book : books) {
			if (book.getCost() != null && book.getCost() > maxCost)
				maxCost = book.getCost().doubleValue();
		}

		if (maxCost == 0.0)
			throw new IllegalArgumentException("Nenhum preço cadastrado");

		return maxCost;
	}

	// 5 - Pega a quantidade de anos em que foi lançado
	public Integer getNumberOfYearsReleased(Book book) {

		if (book.getYearEdition() == null)
			throw new IllegalArgumentException("Ano de lançamento não encontrado");

		if (book.getYearEdition().isAfter(LocalDate.now()))
			throw new IllegalArgumentException("Ano de lançamento depois de hoje");

		return LocalDate.now().getYear() - book.getYearEdition().getYear();
	}

	// 6 - Retorna os usuários com livros com a data de devolução atrasada.
	public List<User> getUsersWithBookWithLateDevolutionDate(List<Book> books) {
		List<User> users = new ArrayList<>();
		books.forEach(book -> {
			if (book.getDevolutionDate() != null) {
				if (book.getUser() == null)
					throw new IllegalArgumentException(
							"O livro " + book.getName() + " possui data de devolução mas não tem usuário relacionado.");
				if (book.getDevolutionDate().isBefore(LocalDate.now())) {
					if (!users.contains(book.getUser()))
						users.add(book.getUser());
				}
			}
		});
		return users;
	}

	// 7 - Pega a quantidade de livros alugados pelo usuário
	public Long getNumberOfBooksRentedByUser(List<Book> books, User user) {
		Long numberOfBooks = 0L;

		for (Book book : books) {
			if (book.getUser().equals(user))
				numberOfBooks++;
		}

		return numberOfBooks;
	}

	// 8 - Retorna os livros que possuem o mesmo nome e o mesmo autor que o passado
	public List<BookDto> getBooksSameAuthorAndName(List<Book> books, String name, String author) {
		List<BookDto> booksDto = new ArrayList<>();

		for (Book book : books) {

			if (book.getName().toLowerCase().contains(name.toLowerCase())
					&& book.getAuthor().equalsIgnoreCase(author)) {
				BookDto dto = new BookDto();

				dto.author = book.getAuthor();
				dto.name = book.getName();
				dto.description = book.getDescription();

				booksDto.add(dto);
			}
		}

		return booksDto;
	}

	// 9 - Pega os livros com o mesmo nome que o passado
	public List<BookDto> getBooksSameName(List<Book> books, String name) {
		List<BookDto> booksDto = new ArrayList<>();

		for (Book book : books) {

			if (book.getName().toLowerCase().contains(name.toLowerCase())) {
				BookDto dto = new BookDto();

				dto.author = book.getAuthor();
				dto.name = book.getName();
				dto.description = book.getDescription();

				booksDto.add(dto);
			}
		}

		return booksDto;
	}

	// 10 - Pega os livros com o mesmo autor que o passado
	public List<BookDto> getBooksSameAuthor(List<Book> books, String author) {
		List<BookDto> booksDto = new ArrayList<>();

		for (Book book : books) {

			if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
				BookDto dto = new BookDto();

				dto.author = book.getAuthor();
				dto.name = book.getName();
				dto.description = book.getDescription();

				booksDto.add(dto);
			}
		}
		return booksDto;
	}

	// ======> Exemplo when

	// Verifica se o livro está emprestado
	public boolean verifyIfBookIsBorrowed(UUID bookId) {
		Optional<Book> bookOpt = bookRepository.findById(bookId);

		if (bookOpt.isPresent()) {
			Book book = bookOpt.get();
			return book.getIsBorrowed();

		} else {
			throw new NotFoundException();
		}
	}

	// Verifica se é possível comprar o livro com o valor passado
	public boolean verifyIfIsPossibleToBuyBookWithValue(UUID bookId, Float value) {
		Optional<Book> bookOpt = bookRepository.findBookById(bookId);

		if (bookOpt.isPresent()) {
			Book book = bookOpt.get();
			return value > book.getCost();

		} else {
			throw new NotFoundException();
		}
	}

	// ======> Exemplo verify

	// Deleta um livro
	public void deletBook(UUID id) {
		Optional<Book> bookOpt = bookRepository.findById(id);

		if (bookOpt.isPresent()) {
			bookRepository.delete(bookOpt.get());
		} else {
			throw new NotFoundException();
		}
	}

	// ====================== Exercícios - Mockito ===========================

	// 1 - Emprestar um livro para um usuário
	public void lendBookToUser(UUID userId, UUID bookId) {
		Optional<Book> bookOpt = bookRepository.findById(bookId);
		Book book = bookOpt.orElseThrow();

		Optional<User> userOpt = userRepository.findById(userId);
		User user = userOpt.orElseThrow();

		if (book.getIsBorrowed())
			throw new IllegalArgumentException("Livro já foi emprestado");

		if (user.getIsPunished())
			throw new IllegalArgumentException("O usuário não está autorizado para pegar novos livros");

		book.setUser(user);
		bookRepository.save(book);
	}

	// 2 - Atualizar o custo do livro de acordo com o ano de lançamento
	public void updateBookPriceAccordingYearEdition(UUID bookId) {
		Optional<Book> bookOpt = bookRepository.findById(bookId);
		Book book = bookOpt.orElseThrow();

		if (book.getYearEdition() == null)
			throw new IllegalArgumentException("Ano de lançamento não encontrado");
		Integer numberOfYearsOfReleased = LocalDate.now().getYear() - book.getYearEdition().getYear();

		if (book.getCost() == null)
			throw new IllegalArgumentException("Custo do livro não encontrado");
		Float discount = book.getCost() * numberOfYearsOfReleased / 100;

		book.setCost(book.getCost() - discount);
		bookRepository.save(book);
	}

	// 3 - Inserir um livro no banco
	public BookDto insertBook(BookDto bookDto) {

		if (!bookDto.name.isEmpty() && !bookDto.author.isEmpty()) {

			final Book bookEntity = bookMapper.toEntity(bookDto);
			final Book bookSaved = bookRepository.save(bookEntity);
			BookDto dto = bookMapper.toDto(bookSaved);

			return dto;

		} else {
			throw new NotFoundException();
		}
	}

	// 4 - Listar os livros
	public List<BookDto> getBooks() {
		List<Book> books = bookRepository.findAll();
		return bookMapper.toDto(books);
	}

	// 5 - Pegar um livro pelo ID
	public BookDto getBookById(UUID id) {
		Optional<Book> book = bookRepository.findById(id);

		if (book.isPresent()) {
			BookDto dto = bookMapper.toDto(book.get());
			return dto;
		}

		throw new NotFoundException();
	}

	// 6 - Atualizar um livro
	public BookDto updateBook(BookDto newbookDto, UUID id) {

		if (!newbookDto.name.isEmpty() && !newbookDto.author.isEmpty()) {

			Optional<Book> bookOpt = bookRepository.findById(id);
			Book book = bookOpt.orElseThrow();

			book.setName(newbookDto.name);
			book.setDescription(newbookDto.description);
			book.setAuthor(newbookDto.author);
			book.setCost(newbookDto.cost);

			Book bookSaved = bookRepository.save(book);
			BookDto dto = bookMapper.toDto(bookSaved);

			return dto;
		}

		throw new WrongParametersException();
	}

	// 7 - Tirar os empréstimos do usuário
	public void removeUserLoans(UUID userId) {
		List<Book> books = bookRepository.findAll();

		Optional<User> userOpt = userRepository.findById(userId);
		User user = userOpt.orElseThrow();

		List<Book> userBooks = books.stream().filter(book -> book.getUser() == user).collect(Collectors.toList());
		if (userBooks.isEmpty())
			throw new IllegalArgumentException("Não há nenhum livro emprestado para esse usuário");

		userBooks.forEach(book -> {
			book.setUser(null);
			book.setDevolutionDate(null);

			bookRepository.save(book);
		});
	}

	// 8 - Cobrar uma multa depois de 6 meses
	public Float calculatePenaltyAfterSixMonths(User user) {
		Float penalty = 0.0f;

		if (user.getIsPunished()) {
			List<Book> books = bookRepository.findAll();
			List<Book> userBooks = books.stream().filter(book -> book.getUser() == user).collect(Collectors.toList());

			for (Book book : userBooks) {
				if (book.getIsBorrowed() == null || !book.getIsBorrowed())
					throw new IllegalArgumentException("O livro está associado ao usuário, mas não está emprestado");

				if (book.getDevolutionDate() == null)
					throw new IllegalArgumentException(
							"O livro está associado ao usuário, mas não tem data de devolução");

				if (book.getCost() == null)
					throw new IllegalArgumentException("O livro não possui custo");

				if (book.getDevolutionDate().isAfter(LocalDate.now().plusMonths(6)))
					penalty += book.getCost() * 200 / 100;
			}
		}

		return penalty;
	}

}
