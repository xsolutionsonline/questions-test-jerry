package com.practice.clientapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.practice.clientapi.generated.api.ClientApi;
import com.practice.clientapi.generated.model.ClientDto;
import com.practice.clientapi.generated.model.ServiceResponse;
import com.practice.clientapi.service.IClientService;

@RestController
@CrossOrigin(origins = { "http://localhost:8080", "http://localhost:4200" })
public class ClientController implements ClientApi {
	@Autowired
	private IClientService clientService;

	@Override
    public ResponseEntity<List<ClientDto>> getClients() {
    	return ResponseEntity.ok(clientService.findAll());
    }

	@Override
    public ResponseEntity<ServiceResponse> saveClient(ClientDto client) {
    	ServiceResponse serviceReponse = new ServiceResponse();
    	int result = clientService.save(client);
    	if (result == 1) {
    		serviceReponse.setSuccess(true);
	    	serviceReponse.setMessage("Client saved with sucesss");
    	}

    	return new ResponseEntity<>(serviceReponse, HttpStatus.OK);

    }

	@Override
	public ResponseEntity<Void> deleteClient(Integer id) {
		return clientService.deleteById(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}

}
