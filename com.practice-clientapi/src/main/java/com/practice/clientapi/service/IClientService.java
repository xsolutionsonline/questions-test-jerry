package com.practice.clientapi.service;

import java.util.List;
import com.practice.clientapi.generated.model.ClientDto;

public interface IClientService {
	public List<ClientDto> findAll();
	 public int  save(ClientDto client);
	 public boolean deleteById(int id);
}
