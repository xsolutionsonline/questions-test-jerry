package com.chubb.test.service;

import java.util.List;
import com.chubb.test.model.dtos.ClientDto;

public interface IClientService {
	public List<ClientDto> findAll();
	 public int  save(ClientDto client);
}
