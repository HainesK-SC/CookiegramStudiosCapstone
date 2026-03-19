package com.cookiegramstudios.cookiegram.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.cookiegramstudios.cookiegram.common.exceptions.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@Test
	void findByEmail_returnsUser() {
		User user = buildUser(1L, "test@example.com");
		when(userRepository.findByEmail("test@example.com")).thenReturn(user);

		User result = userService.findByEmail("test@example.com");

		assertNotNull(result);
		assertEquals("test@example.com", result.getEmail());
	}

	@Test
	void getUserByEmail_whenFound_returnsUser() {
		User user = buildUser(1L, "found@example.com");
		when(userRepository.findByEmail("found@example.com")).thenReturn(user);

		User result = userService.getUserByEmail("found@example.com");

		assertNotNull(result);
		assertEquals("found@example.com", result.getEmail());
	}

	@Test
	void getUserByEmail_whenMissing_throwsUserNotFoundException() {
		when(userRepository.findByEmail("missing@example.com")).thenReturn(null);

		UserNotFoundException ex = assertThrows(UserNotFoundException.class,
				() -> userService.getUserByEmail("missing@example.com"));

		assertTrue(ex.getMessage().contains("missing@example.com"));
	}

	@Test
	void getUserById_whenFound_returnsUser() {
		User user = buildUser(7L, "id@example.com");
		when(userRepository.findById(7L)).thenReturn(Optional.of(user));

		User result = userService.getUserById(7L);

		assertNotNull(result);
		assertEquals(7L, result.getId());
	}

	@Test
	void getUserById_whenMissing_throwsUserNotFoundException() {
		when(userRepository.findById(99L)).thenReturn(Optional.empty());

		UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L));

		assertTrue(ex.getMessage().contains("99"));
	}

	@Test
	void getAllUsers_returnsList() {
		when(userRepository.findAll())
				.thenReturn(List.of(buildUser(1L, "a@example.com"), buildUser(2L, "b@example.com")));

		List<User> result = userService.getAllUsers();

		assertEquals(2, result.size());
	}

	@Test
	void getUsersByRole_returnsList() {
		when(userRepository.findAllByRole(UserRole.EMPLOYEE))
				.thenReturn(List.of(buildUser(10L, "emp1@example.com"), buildUser(11L, "emp2@example.com")));

		List<User> result = userService.getUsersByRole(UserRole.EMPLOYEE);

		assertEquals(2, result.size());
		assertEquals("emp1@example.com", result.get(0).getEmail());
	}

	@Test
	void updateUser_whenMissingUser_throwsUserNotFoundException() {
		when(userRepository.findById(404L)).thenReturn(Optional.empty());

		User update = new User();
		update.setEmail("x@example.com");
		update.setFirstName("X");
		update.setLastName("Y");

		UserNotFoundException ex = assertThrows(UserNotFoundException.class,
				() -> userService.updateUser(404L, update));

		assertTrue(ex.getMessage().contains("404"));
	}

	@Test
	void deleteUser_whenFound_deletesById() {
		User existing = buildUser(12L, "delete@example.com");
		when(userRepository.findById(12L)).thenReturn(Optional.of(existing));

		userService.deleteUser(12L);

		verify(userRepository).deleteById(12L);
	}

	@Test
	void deleteUser_whenMissing_throwsUserNotFoundException() {
		when(userRepository.findById(12L)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.deleteUser(12L));
		verify(userRepository, never()).deleteById(anyLong());
	}

	private User buildUser(Long id, String email) {
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setPassword("password123");
		user.setRole(UserRole.EMPLOYEE);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setCreatedAt(LocalDateTime.now());
		return user;
	}

}
