package com.practice.clientapi.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.clientapi.generated.model.ClientDto;
import com.practice.clientapi.model.Client;
import com.practice.clientapi.respository.IClientRepository;


@Service
public class ClientService implements IClientService {

	@Autowired
	private IClientRepository clientRepository;
	

	 
	@Override
	public List<ClientDto> findAll() {
		List<ClientDto> clients = new ArrayList<ClientDto>();
		
		try {
			clientRepository.findAll().forEach((c) -> {
				//convert  this to auto mapped
				ClientDto cl = new ClientDto()
						.id(c.getId())
						.firstName(c.getFirstName())
						.lastName(c.getLastName())
						.surname(c.getSurname())
						.phone(c.getPhone());
				clients.add(cl);
			});
        
		}catch(Exception ex){
			throw ex;
		}
		
		return clients;
	}



	@Override
	public int save(ClientDto clientdto) {
		int row = 0;
		try {
			
			Client client = new Client();
			
			//convert  this to auto mapped
			
			client.setFirstName(clientdto.getFirstName());
			client.setLastName(clientdto.getLastName());
			client.setSurname(clientdto.getSurname());
			client.setPhone(clientdto.getPhone());
			
			row = clientRepository.save(client);
        
		}catch(Exception ex){
			throw ex;
		}
		
		return row;
	}

	@Override
	public boolean deleteById(int id) {
		return clientRepository.deleteById(id);
	}

}
