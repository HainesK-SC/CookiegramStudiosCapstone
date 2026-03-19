package com.cookiegramstudios.cookiegram.recipient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecipientServiceTest {

	@Mock
	private RecipientRepository recipientRepository;

	@InjectMocks
	private RecipientService recipientService;

	@Test
	void updateRecipient_nullId_throwsIllegalArgumentException() {
		Recipient input = buildRecipient(null, "Jane Doe");

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> recipientService.updateRecipient(input));

		assertTrue(ex.getMessage().contains("Recipient does not exist with ID: null"));
		verify(recipientRepository, never()).save(any(Recipient.class));
	}

	@Test
	void updateRecipient_nonExistingId_throwsIllegalArgumentException() {
		Recipient input = buildRecipient(999L, "Jane Doe");
		when(recipientRepository.existsById(999L)).thenReturn(false);

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> recipientService.updateRecipient(input));

		assertTrue(ex.getMessage().contains("999"));
		verify(recipientRepository).existsById(999L);
		verify(recipientRepository, never()).save(any(Recipient.class));
	}

	@Test
	void deleteRecipient_existingId_deletes() {
		when(recipientRepository.existsById(12L)).thenReturn(true);

		recipientService.deleteRecipient(12L);

		verify(recipientRepository).existsById(12L);
		verify(recipientRepository).deleteById(12L);
	}

	@Test
	void deleteRecipient_missingId_throwsIllegalArgumentException() {
		when(recipientRepository.existsById(404L)).thenReturn(false);

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> recipientService.deleteRecipient(404L));

		assertTrue(ex.getMessage().contains("404"));
		verify(recipientRepository).existsById(404L);
		verify(recipientRepository, never()).deleteById(anyLong());
	}

	@Test
	void findById_delegatesToRepository() {
		Recipient recipient = buildRecipient(20L, "Lookup Name");
		when(recipientRepository.findById(20L)).thenReturn(Optional.of(recipient));

		Optional<Recipient> result = recipientService.findById(20L);

		assertTrue(result.isPresent());
		assertEquals("Lookup Name", result.get().getName());
		verify(recipientRepository).findById(20L);
	}

	@Test
	void findAllRecipients_delegatesToRepository() {
		when(recipientRepository.findAll()).thenReturn(List.of(buildRecipient(1L, "One"), buildRecipient(2L, "Two")));

		List<Recipient> result = recipientService.findAllRecipients();

		assertEquals(2, result.size());
		verify(recipientRepository).findAll();
	}

	@Test
	void findByCity_delegatesToRepository() {
		when(recipientRepository.findByCity("Toronto")).thenReturn(List.of(buildRecipient(1L, "City Test")));

		List<Recipient> result = recipientService.findByCity("Toronto");

		assertEquals(1, result.size());
		verify(recipientRepository).findByCity("Toronto");
	}

	@Test
	void findByPostalCode_delegatesToRepository() {
		when(recipientRepository.findByPostalCode("M5V 3L9")).thenReturn(List.of(buildRecipient(1L, "Postal Test")));

		List<Recipient> result = recipientService.findByPostalCode("M5V 3L9");

		assertEquals(1, result.size());
		verify(recipientRepository).findByPostalCode("M5V 3L9");
	}

	@Test
	void findByCountry_delegatesToRepository() {
		when(recipientRepository.findByCountry("Canada")).thenReturn(List.of(buildRecipient(1L, "Country Test")));

		List<Recipient> result = recipientService.findByCountry("Canada");

		assertEquals(1, result.size());
		verify(recipientRepository).findByCountry("Canada");
	}

	@Test
	void findRecipientsWithSpecialInstructions_delegatesToRepository() {
		Recipient r = buildRecipient(1L, "Special Person");
		r.setSpecialInstructions("Leave at front door");
		when(recipientRepository.findBySpecialInstructionsIsNotNull()).thenReturn(List.of(r));

		List<Recipient> result = recipientService.findRecipientsWithSpecialInstructions();

		assertEquals(1, result.size());
		assertNotNull(result.get(0).getSpecialInstructions());
		verify(recipientRepository).findBySpecialInstructionsIsNotNull();
	}

	@Test
	void countAllRecipients_delegatesToRepository() {
		when(recipientRepository.count()).thenReturn(42L);

		long result = recipientService.countAllRecipients();

		assertEquals(42L, result);
		verify(recipientRepository).count();
	}

	@Test
	void existsById_delegatesToRepository() {
		when(recipientRepository.existsById(7L)).thenReturn(true);

		boolean result = recipientService.existsById(7L);

		assertTrue(result);
		verify(recipientRepository).existsById(7L);
	}

	@Test
	void getFormattedAddress_existingRecipient_returnsFullAddress() {
		Recipient recipient = buildRecipient(55L, "Address Name");
		when(recipientRepository.findById(55L)).thenReturn(Optional.of(recipient));

		String result = recipientService.getFormattedAddress(55L);

		assertEquals("123 Main St, Toronto, M5V 3L9, Canada", result);
		verify(recipientRepository).findById(55L);
	}

	@Test
	void getFormattedAddress_missingRecipient_throwsIllegalArgumentException() {
		when(recipientRepository.findById(56L)).thenReturn(Optional.empty());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> recipientService.getFormattedAddress(56L));

		assertTrue(ex.getMessage().contains("56"));
		verify(recipientRepository).findById(56L);
	}

	private Recipient buildRecipient(Long id, String name) {
		Recipient recipient = new Recipient();
		recipient.setId(id);
		recipient.setName(name);
		recipient.setStreet("123 Main St");
		recipient.setCity("Toronto");
		recipient.setPostalCode("M5V 3L9");
		recipient.setCountry("Canada");
		return recipient;
	}
}
