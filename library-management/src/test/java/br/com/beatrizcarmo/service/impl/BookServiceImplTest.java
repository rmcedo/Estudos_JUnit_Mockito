package br.com.beatrizcarmo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.beatrizcarmo.dto.BookDto;
import br.com.beatrizcarmo.dto.mapper.BookMapper;
import br.com.beatrizcarmo.exceptions.NotFoundException;
import br.com.beatrizcarmo.models.Book;
import br.com.beatrizcarmo.models.User;
import br.com.beatrizcarmo.repository.BookRepository;
import br.com.beatrizcarmo.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {

	@InjectMocks
	public BookServiceImpl service;

	@Mock
	public BookRepository bookRepository;
	@Mock
	public BookMapper bookMapper;
	@Mock
	public UserRepository userRepository;

	@Captor
	ArgumentCaptor<Book> bookCaptor;

	public User user;
	public Book book;

	@Before
	public void setUp() {
		this.user = new User();
		this.book = new Book();
	}

	// Exemplo
	@Test
	public void checkBookingPossibility_shouldBePossible() {
		// ARRANGE
		user.setIsPunished(false);
		book.setIsBorrowed(false);

		// ACT
		boolean result = service.checkBookingPossibility(user, book);

		// ASSERT
		assertThat(result).isTrue();
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkBookingPossibility_IsBorrowedIsNull() {
		// ARRANGE
		user.setIsPunished(true);

		// ACT
		service.checkBookingPossibility(user, book);

	}

	@Test
	public void checkBookingPossibility_IsPunishedIsFalse() {
		// ARRANGE
		user.setIsPunished(false);
		book.setIsBorrowed(true);

		// ACT
		boolean result = service.checkBookingPossibility(user, book);

	}

	@Test
	public void checkBookingPossibility_IsPunishedIsFalseAndIsBorrowedIsNull() {
		// ARRANGE
		user.setIsPunished(true);
		book.setIsBorrowed(true);

		// ACT
		boolean result = service.checkBookingPossibility(user, book);

	}

	@Test
	public void calculateDiscountBasedOnPercentage_shouldReturnTheDiscount() {
		book.setCost(10.0f);
		Double result = service.calculateDiscountBasedOnPercentage(book, 10.0);
		assertThat(result).isEqualTo(1);
	}

	// ==================== JUNIT ====================

	// 1
	@Test
	public void getUsersResponsibleForBorrowed_shouldReturnBooksWithUsers() {
		// Arrange
		List<Book> books = new ArrayList<>();
		List<User> list = new ArrayList<>();
		book.setIsBorrowed(true);
		book.setUser(user);
		list.add(book.getUser());
		books.add(book);
		// Act
		List<User> result = service.getUsersResponsibleForBorrowed(books);
		// Assert
		assertThat(result.get(0).getId()).isEqualTo(books.get(0).getUser().getId());

	}

	@Test
	public void getUsersResponsibleForBorrowed_withIsBorrowedFalse() {
		List<Book> books = new ArrayList<>();
		List<User> list = new ArrayList<>();
		book.setIsBorrowed(false);
		book.setUser(user);
		list.add(book.getUser());
		books.add(book);
		// Act
		List<User> result = service.getUsersResponsibleForBorrowed(books);
		// Assert
		assertThat(result).isEmpty();
	}

	@Test
	public void getUsersResponsibleForBorrowed_withUserNull() {
		List<Book> books = new ArrayList<>();
		book.setIsBorrowed(true);
		books.add(book);
		// Act
		List<User> result = service.getUsersResponsibleForBorrowed(books);
		// Assert
		assertThat(result).isEmpty();
	}

	// 2
	@Test
	public void countNumberOfBorrowedBooks_shoudReturnNumbersOfBooks() {
		List<Book> books = new ArrayList<>();
		book.setIsBorrowed(true);
		books.add(book);

		Long result = service.countNumberOfBorrowedBooks(books);

		assertThat(result).isEqualTo(1);

	}

	@Test
	public void countNumberOfBorrowedBooks_shoudReturnExceptionForNullList() {
		List<Book> books = null;

		Throwable exception = catchThrowable(() -> service.countNumberOfBorrowedBooks(books));

		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Nenhum livro foi encontrado");

	}

	@Test
	public void countNumberOfBorrowedBooks_shoudReturnExceptionForEmptyList() {
		List<Book> books = new ArrayList<>();

		Throwable exception = catchThrowable(() -> service.countNumberOfBorrowedBooks(books));

		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Nenhum livro foi encontrado");

	}

	// 3

	@Test
	public void calculateTotalCostOfBooks_shouldReturnTheTotalCost() {

		List<Book> books = new ArrayList<>();
		Book book2 = new Book();
		book.setCost(10.0f);
		book2.setCost(10.5f);
		books.add(book);
		books.add(book2);

		Double result = service.calculateTotalCostOfBooks(books).doubleValue();

		assertThat(result).isEqualTo(20.5);

	}

	@Test
	public void calculateTotalCostOfBooks_shouldReturnExceptionForBookWithouPrice() {

		List<Book> books = new ArrayList<>();
		book.setCost(null);
		books.add(book);

		Throwable exception = catchThrowable(() -> service.calculateTotalCostOfBooks(books).doubleValue());

		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Livro cadastrado sem preço");

	}

	@Test
	public void calculateTotalCostOfBooks_shouldReturnExceptionForEmptyList() {

		List<Book> books = new ArrayList<>();
		book.setCost(null);
		Throwable exception = catchThrowable(() -> service.calculateTotalCostOfBooks(books).doubleValue());

		assertThat(exception).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContainingAll("Nenhum livro foi encontrado");

	}

	@Test
	public void calculateTotalCostOfBooks_shouldReturnExceptionForNullList() {

		List<Book> books = null;
		Throwable exception = catchThrowable(() -> service.calculateTotalCostOfBooks(books).doubleValue());

		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Nenhum livro foi encontrado");

	}

	// 4
	@Test
	public void getMaxBooksCost_shouldReturnBookWithMaxCost() {

		List<Book> books = new ArrayList<>();
		Book book2 = new Book();
		book.setCost(10.0f);
		book2.setCost(11.0f);
		books.add(book);
		books.add(book2);

		Double result = service.getMaxBooksCost(books).doubleValue();

		assertThat(result).isEqualTo(11.0);
	}

	@Test
	public void getMaxBooksCost_shouldReturnExceptionForCostNull() {

		List<Book> books = new ArrayList<>();
		book.setCost(null);
		books.add(book);

		Throwable exception = catchThrowable(() -> service.getMaxBooksCost(books).doubleValue());

		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Nenhum preço cadastrado");
	}

	@Test
	public void getMaxBooksCost_shouldReturnExceptionForMaxCostEqualsZero() {

		List<Book> books = new ArrayList<>();
		book.setCost(0.0f);
		books.add(book);

		Throwable exception = catchThrowable(() -> service.getMaxBooksCost(books).doubleValue());

		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Nenhum preço cadastrado");
	}

	// 5
	@Test
	public void getNumberOfYearsReleased_shouldReturnTheYearsRealeased() {

		book.setYearEdition(LocalDate.of(2000, 1, 1));

		Integer result = service.getNumberOfYearsReleased(book);

		assertThat(result).isEqualTo(23);
	}

	@Test
	public void getNumberOfYearsReleased_shouldReturnExceptionToDateNull() {

		book.setYearEdition(null);

		Throwable exception = catchThrowable(() -> service.getNumberOfYearsReleased(book));

		assertThat(exception).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Ano de lançamento não encontrado");
	}

	@Test
	public void getNumberOfYearsReleased_shouldReturnExceptionForDateAfterToday() {

		book.setYearEdition(LocalDate.of(2024, 1, 1));

		Throwable exception = catchThrowable(() -> service.getNumberOfYearsReleased(book));

		assertThat(exception).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Ano de lançamento depois de hoje");
	}

	// 6

	@Test
	public void getUsersWithBookWithLateDevolutionDate_shouldReturnUsers() {
		List<Book> books = new ArrayList<>();
		List<User> users = new ArrayList<>();
		book.setDevolutionDate(LocalDate.of(2023, 01, 26));
		book.setUser(user);
		user.setUsername("Rafael");
		books.add(book);
		users.add(user);

		List<User> result = service.getUsersWithBookWithLateDevolutionDate(books);

		assertThat(result.get(0).getUsername()).isEqualTo("Rafael");

	}

	@Test
	public void getUsersWithBookWithLateDevolutionDate_shouldReturnExceptionUserIsNull() {
		List<Book> books = new ArrayList<>();
		book.setDevolutionDate(LocalDate.of(2023, 01, 26));
		books.add(book);

		Throwable exception = catchThrowable(() -> service.getUsersWithBookWithLateDevolutionDate(books));

		assertThat(exception).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("O livro " + book.getName() + " possui data de devolução mas não tem usuário relacionado.");
	}

	@Test
	public void getUsersWithBookWithLateDevolutionDate_withDevolutionDateAfterToday() {
		List<Book> books = new ArrayList<>();
		List<User> users = new ArrayList<>();
		book.setDevolutionDate(LocalDate.of(2024, 01, 26));
		book.setUser(user);
		user.setUsername("Rafael");
		books.add(book);
		users.add(user);

		List<User> result = service.getUsersWithBookWithLateDevolutionDate(books);
		assertThat(result).isEmpty();

	}

	@Test
	public void getUsersWithBookWithLateDevolutionDate_withoutDevolutionDate() {
		List<Book> books = new ArrayList<>();
		List<User> users = new ArrayList<>();
		book.setUser(user);
		user.setUsername("Rafael");
		books.add(book);
		users.add(user);

		List<User> result = service.getUsersWithBookWithLateDevolutionDate(books);

	}

	@Test
	public void getUsersWithBookWithLateDevolutionDate_withDevolutionDateBeforeToday() {
		List<Book> books = new ArrayList<>();
		Book book2 = new Book();
		book.setDevolutionDate(LocalDate.of(2023, 01, 26));
		book2.setDevolutionDate(LocalDate.of(2023, 01, 26));
		book.setUser(user);
		book2.setUser(user);
		books.add(book);
		books.add(book2);
		List<User> result = service.getUsersWithBookWithLateDevolutionDate(books);

		assertThat(result).contains(user);
	}

	// 7
	@Test
	public void getNumberOfBooksRentedByUser_ShouldReturnTheNumberOfBooksBorrowedByTheUser() {
		List<Book> books = new ArrayList<>();
		Book book2 = new Book();
		Book book3 = new Book();
		book.setUser(user);
		book2.setUser(user);
		book3.setUser(user);
		books.add(book);
		books.add(book2);
		books.add(book3);

		Long result = service.getNumberOfBooksRentedByUser(books, user);

		assertThat(result).isEqualTo(3);

	}

	@Test(expected = NullPointerException.class)
	public void getNumberOfBooksRentedByUser_ShouldReturnNullPointerBecauseUserDontHaveBooks() {
		List<Book> books = new ArrayList<>();
		books.add(book);

		Long result = service.getNumberOfBooksRentedByUser(books, user);

	}

	@Test
	public void getNumberOfBooksRentedByUser_ShouldReturnTheNumberZero() {
		List<Book> books = new ArrayList<>();
		User user2 = new User();
		book.setUser(user);
		books.add(book);

		Long result = service.getNumberOfBooksRentedByUser(books, user2);

		assertThat(result).isEqualTo(0);

	}

	// 8
	@Test
	public void getBooksSameAuthorAndName_ShouldReturnAListOfBookDto() {

		List<Book> books = new ArrayList<>();
		book.setAuthor("Rafael");
		book.setName("Nome1");
		book.setDescription("Legal1");
		books.add(book);

		List<BookDto> result = service.getBooksSameAuthorAndName(books, "Nome1", "Rafael");

		assertThat(result.get(0).name).isEqualTo(books.get(0).getName());

	}

	@Test
	public void getBooksSameAuthorAndName_ErrorOnAuthor() {

		List<Book> books = new ArrayList<>();
		book.setAuthor("Rafael");
		book.setName("Nome1");
		book.setDescription("Legal1");
		books.add(book);

		List<BookDto> result = service.getBooksSameAuthorAndName(books, "Nome1", "dsadsa");

		assertThat(result).isEmpty();

	}

	@Test
	public void getBooksSameAuthorAndName_ErrorOnName() {

		List<Book> books = new ArrayList<>();
		book.setAuthor("Rafael");
		book.setName("Nome1");
		book.setDescription("Legal1");
		books.add(book);

		List<BookDto> result = service.getBooksSameAuthorAndName(books, "suiahuishaui", "Rafael");

		assertThat(result).isEmpty();

	}

	// 9

	@Test
	public void getBooksSameName_shouldReturnTheAListOfBookDto() {

		List<Book> books = new ArrayList<>();
		book.setName("Nome1");
		books.add(book);

		List<BookDto> result = service.getBooksSameName(books, "Nome1");
		assertThat(result.get(0).name).isEqualTo(book.getName());
	}

	@Test
	public void getBooksSameName_WithErrorOnName() {

		List<Book> books = new ArrayList<>();
		book.setName("Nome1");
		books.add(book);

		List<BookDto> result = service.getBooksSameName(books, "dsadsadsa");
		assertThat(result).isEmpty();
	}

	// 10
	@Test
	public void getBooksSameAuthor_shouldReturnTheAListOfBookDto() {

		List<Book> books = new ArrayList<>();
		book.setAuthor("Rafael");
		books.add(book);

		List<BookDto> result = service.getBooksSameAuthor(books, "Rafael");

		assertThat(result.get(0).author).isEqualTo(books.get(0).getAuthor());

	}

	@Test
	public void getBooksSameAuthor_WithErrorOnAuthor() {

		List<Book> books = new ArrayList<>();
		book.setAuthor("Rafael");
		books.add(book);

		List<BookDto> result = service.getBooksSameAuthor(books, "hsuiahsiua");

		assertThat(result).isEmpty();

	}

	// ==================== MOCKITO ====================
	@Test
	public void verifyIfBookIsBorrowed() {

		// Arrange
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setIsBorrowed(true);
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

		// Act
		boolean result = service.verifyIfBookIsBorrowed(bookId);

		// Assert
		assertThat(result).isTrue();

	}

	@Test
	public void verifyIfBookIsBorrowed_shouldNotFound() {

		// Arrange
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
		// Act
		Throwable exception = catchThrowable(() -> service.verifyIfBookIsBorrowed(bookId));
		// Assert
		assertThat(exception).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");

	}
	@Test
	public void verifyIfIsPossibleToBuyBookWithValue_shoulReturnTrueToBuyTheBook() {
		
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		book.setCost(9f);
		when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(book));
		boolean result = service.verifyIfIsPossibleToBuyBookWithValue(bookId, 10f);
		assertThat(result).isTrue();
		
	}
	@Test
	public void verifyIfIsPossibleToBuyBookWithValue_shoulReturnNotFoundException() {
		
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		when(bookRepository.findBookById(bookId)).thenReturn(Optional.empty());
		Throwable exception = catchThrowable(() -> service.verifyIfIsPossibleToBuyBookWithValue(bookId, 10f)) ;
		assertThat(exception).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");
		
		
	}
	@Test
	public void verifyIfIsPossibleToBuyBookWithValue_shoulReturnFalseToBuyTheBook() {
		
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		book.setCost(11f);
		when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(book));
		boolean result = service.verifyIfIsPossibleToBuyBookWithValue(bookId, 10f);
		assertThat(result).isFalse();
		
	}

	@Test
	public void deletBook_shouldDelete() {
		// Arrange
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

		// Act
		service.deletBook(bookId);

		// Assert
		verify(bookRepository).delete(book);
		verify(bookRepository).findById(bookId);

	}

	@Test
	public void deletBook_shouldThrowANotFoundException() {
		// Arrange
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");

		// Act
		Throwable exception = catchThrowable(() -> service.deletBook(bookId));

		// Assert
		assertThat(exception).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");

	}

	// 1
	@Test
	public void lendBookToUser_shouldBorrowABookToAUser() {
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		UUID userId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee63");
		book.setIsBorrowed(false);
		book.setId(bookId);
		user.setIsPunished(false);
		user.setId(userId);

		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		service.lendBookToUser(userId, bookId);

		verify(bookRepository).save(book);

	}

	@Test
	public void lendBookToUser_withABookAlreadyBorrowed() {
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		UUID userId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee63");
		book.setIsBorrowed(true);
		book.setId(bookId);
		user.setIsPunished(false);
		user.setId(userId);

		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		Throwable exception = catchThrowable(() -> service.lendBookToUser(userId, bookId));

		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Livro já foi emprestado");

	}

	@Test
	public void lendBookToUser_withAPunishedUser() {
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		UUID userId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee63");
		book.setIsBorrowed(false);
		book.setId(bookId);
		user.setIsPunished(true);
		user.setId(userId);

		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		Throwable exception = catchThrowable(() -> service.lendBookToUser(userId, bookId));

		assertThat(exception).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("O usuário não está autorizado para pegar novos livros");

	}

	// 2

	@Test
	public void updateBookPriceAccordingYearEdition_shouldUpdateThePriceForTheBook() {

		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		book.setYearEdition(LocalDate.of(2020, 01, 30));
		book.setCost(10f);

		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		service.updateBookPriceAccordingYearEdition(bookId);
		assertThat(book.getCost()).isEqualTo(9.7f);

	}

	@Test
	public void updateBookPriceAccordingYearEdition_shouldReturnAExceptionToYearEditionNull() {

		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		book.setCost(10f);

		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		Throwable exception = catchThrowable(() -> service.updateBookPriceAccordingYearEdition(bookId));

		assertThat(exception).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Ano de lançamento não encontrado");

	}

	@Test
	public void updateBookPriceAccordingYearEdition_shouldReturnAExceptionToBookCostNull() {

		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		book.setYearEdition(LocalDate.of(2020, 01, 30));

		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		Throwable exception = catchThrowable(() -> service.updateBookPriceAccordingYearEdition(bookId));

		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Custo do livro não encontrado");

	}

	//3
	@Test
	public void insertBook_shouldSaveTheBook() {
		ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
		BookDto bookDto = new BookDto();
		bookDto.author = "Rafael";
		bookDto.name = "Livro";

		service.insertBook(bookDto);

		verify(bookMapper).toEntity(bookDto);
		verify(bookRepository).save(bookCaptor.capture());
		verify(bookMapper).toDto(bookCaptor.capture());

	}

	@Test
	public void insertBook_shouldReturnNotFoundExceptionBookNameAndAuthorIsEmpty() {
		BookDto bookDto = new BookDto();
		bookDto.author = "";
		bookDto.name = "";

		Throwable exception = catchThrowable(() -> service.insertBook(bookDto));

		assertThat(exception).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");

	}

	@Test
	public void insertBook_shouldReturnNotFoundExceptionBookNameIsEmpty() {
		BookDto bookDto = new BookDto();
		bookDto.author = "Rafael";
		bookDto.name = "";

		Throwable exception = catchThrowable(() -> service.insertBook(bookDto));

		assertThat(exception).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");

	}

	@Test
	public void insertBook_shouldReturnNotFoundExceptionBookAuthorIsEmpty() {
		BookDto bookDto = new BookDto();
		bookDto.author = "";
		bookDto.name = "Livro";

		Throwable exception = catchThrowable(() -> service.insertBook(bookDto));

		assertThat(exception).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");

	}

//4
	
	@Test
	public void getBooks_shouldReturnAListOfBooks() {
		List<Book> books = new ArrayList<>();
		books.add(book);
		when(bookRepository.findAll()).thenReturn(books);
		service.getBooks();
		verify(bookMapper).toDto(books);
		
	}

	
//5
	@Test
	public void getBookById_shouldReturnAListOfBooks() {
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		
		BookDto result = service.getBookById(bookId);
		
		verify(bookMapper).toDto(book);
		assertThat(result).isNull();
		
	}
	@Test
	public void getBookById_shouldReturnAExceptionForEmptyOptional() {
		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
		
		
		Throwable exception = catchThrowable(() -> service.getBookById(bookId));

		assertThat(exception).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");
		
		
	}
	
	//6
	
	@Test
	public void updateBook_shouldUpdateTheBook() {

		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		BookDto newBookDto = new BookDto();
		newBookDto.author = "Rafael";
		newBookDto.name = "Livro";
		
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		
		service.updateBook(newBookDto, bookId);
		
		verify(bookRepository).save(book);
		assertThat(book.getAuthor()).isEqualTo(newBookDto.author);
		
		
	}
	@Test
	public void updateBook_shouldReturnAExceptionOfWrongParameters() {

		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		BookDto newBookDto = new BookDto();
		newBookDto.author = "";
		newBookDto.name = "";
		
		Throwable exception = catchThrowable(() -> service.updateBook(newBookDto, bookId));

		assertThat(exception).isInstanceOf(RuntimeException.class).hasMessage("The parameters are wrong");
		
		
	}
	@Test
	public void updateBook_shouldReturnAExceptionOfWrongParametersBecauseNameIsEmpty() {

		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		BookDto newBookDto = new BookDto();
		newBookDto.author = "Rafael";
		newBookDto.name = "";
		
		Throwable exception = catchThrowable(() -> service.updateBook(newBookDto, bookId));

		assertThat(exception).isInstanceOf(RuntimeException.class).hasMessage("The parameters are wrong");
		
		
	}
	@Test
	public void updateBook_shouldReturnAExceptionOfWrongParametersBecauseAuthorIsEmpty() {

		UUID bookId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee62");
		book.setId(bookId);
		BookDto newBookDto = new BookDto();
		newBookDto.author = "";
		newBookDto.name = "Livro";
		
		Throwable exception = catchThrowable(() -> service.updateBook(newBookDto, bookId));

		assertThat(exception).isInstanceOf(RuntimeException.class).hasMessage("The parameters are wrong");
		
		
	}
		

	//7
	
	@Test
	public void removeUserLoans_shouldRemoveBookFromTheUser() {
		
		List<Book> books = new ArrayList<>();
		UUID userId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee64");
		book.setUser(user);
		books.add(book);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(bookRepository.findAll()).thenReturn(books);
		
		service.removeUserLoans(userId);
		
		verify(bookRepository).save(book);
		assertThat(book.getUser()).isNull();
		assertThat(book.getDevolutionDate()).isNull();
		
		
	}

	@Test
	public void removeUserLoans_shouldReturnExceptionBecauseThereIs2UsersAndBooks() {
		
		List<Book> books = new ArrayList<>();
		UUID userId = UUID.fromString("b5cf7620-d659-4b66-b7c7-25d45021ee64");
		book.setUser(null);
		books.add(book);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(bookRepository.findAll()).thenReturn(books);
		
		Throwable exception = catchThrowable(()-> service.removeUserLoans(userId));
		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Não há nenhum livro emprestado para esse usuário");
		
		
	}
	
	//8
	
	@Test
	public void calculatePenaltyAfterSixMonths_shouldReturnExceptionForDevolutionDateNull() {
		List<Book> books = new ArrayList<>();
		user.setIsPunished(true);
		book.setUser(user);
		book.setIsBorrowed(true);
		books.add(book);
		when(bookRepository.findAll()).thenReturn(books);
		Throwable exception = catchThrowable(()->service.calculatePenaltyAfterSixMonths(user));
		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("O livro está associado ao usuário, mas não tem data de devolução");
		
		
		
	}
	@Test
	public void calculatePenaltyAfterSixMonths_shouldReturnExceptionForIsBorrowedNull() {
		List<Book> books = new ArrayList<>();
		user.setIsPunished(true);
		book.setUser(user);
		book.setIsBorrowed(null);
		books.add(book);
		when(bookRepository.findAll()).thenReturn(books);
		Throwable exception = catchThrowable(()->service.calculatePenaltyAfterSixMonths(user));
		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("O livro está associado ao usuário, mas não está emprestado");
		
	}
	
	@Test
	public void calculatePenaltyAfterSixMonths_shouldReturnExceptionForIsBorrowedFalseWithTwoUsers() {
		List<Book> books = new ArrayList<>();
		Book book2 = new Book();
		User user2 = new User();
		user.setIsPunished(true);
		book.setUser(user);
		book.setIsBorrowed(false);
		book2.setUser(user2);
		books.add(book);
		books.add(book2);
		when(bookRepository.findAll()).thenReturn(books);
		
		Throwable exception = catchThrowable(()->service.calculatePenaltyAfterSixMonths(user));
		
		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("O livro está associado ao usuário, mas não está emprestado");
		
	}
	@Test
	public void calculatePenaltyAfterSixMonths_shouldReturnExceptionForCostNull() {
		List<Book> books = new ArrayList<>();
		user.setIsPunished(true);
		book.setUser(user);
		book.setIsBorrowed(true);
		book.setDevolutionDate(LocalDate.of(2022, 1, 1));
		book.setCost(null);
		books.add(book);
		when(bookRepository.findAll()).thenReturn(books);
		
		Throwable exception = catchThrowable(()->service.calculatePenaltyAfterSixMonths(user));
		
		assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("O livro não possui custo");
		
	}
	@Test
	public void calculatePenaltyAfterSixMonths_shouldReturnExceptionForDateBefore() {
		List<Book> books = new ArrayList<>();
		user.setIsPunished(true);
		book.setUser(user);
		book.setIsBorrowed(true);
		book.setDevolutionDate(LocalDate.of(2020, 1, 1));
		book.setCost(10f);
		books.add(book);
		when(bookRepository.findAll()).thenReturn(books);
		service.calculatePenaltyAfterSixMonths(user);
		
	}
	@Test
	public void calculatePenaltyAfterSixMonths_shouldReturnPenaltyOf20() {
		List<Book> books = new ArrayList<>();
		user.setIsPunished(true);
		book.setUser(user);
		book.setIsBorrowed(true);
		book.setDevolutionDate(LocalDate.of(2025, 1, 1));
		book.setCost(10f);
		books.add(book);
		when(bookRepository.findAll()).thenReturn(books);
		float result = service.calculatePenaltyAfterSixMonths(user);
		assertThat(result).isEqualTo(20f);
		
	}
	@Test
	public void calculatePenaltyAfterSixMonths_shouldReturnExceptionForUserNotPunished() {
		List<Book> books = new ArrayList<>();
		user.setIsPunished(false);
		book.setUser(user);
		book.setIsBorrowed(true);
		book.setDevolutionDate(LocalDate.of(2025, 1, 1));
		book.setCost(10f);
		books.add(book);
		Float result = service.calculatePenaltyAfterSixMonths(user);
		assertThat(result).isZero();
		
	}
	
	}

