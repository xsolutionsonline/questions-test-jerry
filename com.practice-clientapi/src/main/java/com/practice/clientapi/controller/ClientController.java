package com.practice.clientapi.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
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


	@Operation(summary = " listar los clientes ", description = "devuevlve todos los clientes registrados")
	@ApiResponse(responseCode = "200", description ="Resultado de la operacion exitosa listando clientes")
	@Override
    public ResponseEntity<List<ClientDto>> getClients() {
    	return ResponseEntity.ok(clientService.findAll());
    }

	@Operation(summary = "crear un cliente", description = "guardan un cliente nuevo")
	@ApiResponse(responseCode = "200", description ="Resultado de la operacion exitosa")
	@Override
    public ResponseEntity<ServiceResponse> saveClient(@RequestBody ClientDto client) {
    	ServiceResponse serviceReponse = new ServiceResponse();
    	int result = clientService.save(client);
    	if (result == 1) {
    		serviceReponse.setSuccess(true);
	    	serviceReponse.setMessage("Client saved with sucesss");
    	}

    	return new ResponseEntity<>(serviceReponse, HttpStatus.OK);

    }

	@Operation(summary = "eliminar un cliente", description = "Elimina el cliente con el id indicado, Devuelve 404 si no existe")
	@ApiResponse(responseCode = "204", description ="cliente eliminado")
	@ApiResponse(responseCode = "404", description ="cliente no encontrado")
	@Override
	public ResponseEntity<Void> deleteClient(Integer id) {
		return clientService.deleteById(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}

}
