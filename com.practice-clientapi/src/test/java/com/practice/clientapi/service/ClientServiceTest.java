package com.practice.clientapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.practice.clientapi.generated.model.ClientDto;
import com.practice.clientapi.model.Client;
import com.practice.clientapi.respository.IClientRepository;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

	@Mock
	private IClientRepository clientRepository;

	@InjectMocks
	private ClientService clientService;

	private Client aClient(int id, String firstName, String lastName, String surname, String phone) {
		Client client = new Client();
		client.setId(id);
		client.setFirstName(firstName);
		client.setLastName(lastName);
		client.setSurname(surname);
		client.setPhone(phone);
		return client;
	}

	@Test
	void findAll_mapsRepositoryClientsToDtos() {
		Client client = aClient(1, "Juan", "Perez", "Gomez", "3001234567");
		when(clientRepository.findAll()).thenReturn(List.of(client));

		List<ClientDto> result = clientService.findAll();

		assertThat(result).hasSize(1);
		ClientDto dto = result.get(0);
		assertThat(dto.getId()).isEqualTo(1);
		assertThat(dto.getFirstName()).isEqualTo("Juan");
		assertThat(dto.getLastName()).isEqualTo("Perez");
		assertThat(dto.getSurname()).isEqualTo("Gomez");
		assertThat(dto.getPhone()).isEqualTo("3001234567");
	}

	@Test
	void findAll_returnsEmptyListWhenRepositoryHasNoClients() {
		when(clientRepository.findAll()).thenReturn(Collections.emptyList());

		List<ClientDto> result = clientService.findAll();

		assertThat(result).isEmpty();
	}

	@Test
	void save_mapsDtoToClientAndDelegatesToRepository() {
		ClientDto dto = new ClientDto()
				.firstName("Juan")
				.lastName("Perez")
				.surname("Gomez")
				.phone("3001234567");
		when(clientRepository.save(any(Client.class))).thenReturn(1);

		int result = clientService.save(dto);

		assertThat(result).isEqualTo(1);

		ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
		verify(clientRepository).save(captor.capture());
		Client saved = captor.getValue();
		assertThat(saved.getFirstName()).isEqualTo("Juan");
		assertThat(saved.getLastName()).isEqualTo("Perez");
		assertThat(saved.getSurname()).isEqualTo("Gomez");
		assertThat(saved.getPhone()).isEqualTo("3001234567");
	}

	@Test
	void save_returnsZeroWhenRepositoryDoesNotInsertAnyRow() {
		ClientDto dto = new ClientDto().firstName("Juan");
		when(clientRepository.save(any(Client.class))).thenReturn(0);

		int result = clientService.save(dto);

		assertThat(result).isEqualTo(0);
	}

	@Test
	void deleteById_delegatesToRepositoryAndReturnsTrueWhenDeleted() {
		when(clientRepository.deleteById(anyInt())).thenReturn(true);

		boolean result = clientService.deleteById(1);

		assertThat(result).isTrue();
		verify(clientRepository).deleteById(1);
	}

	@Test
	void deleteById_returnsFalseWhenClientDoesNotExist() {
		when(clientRepository.deleteById(anyInt())).thenReturn(false);

		boolean result = clientService.deleteById(99);

		assertThat(result).isFalse();
	}
}
