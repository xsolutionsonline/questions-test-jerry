package com.practice.clientapi.respository;

import java.util.List;

import com.practice.clientapi.model.Client;

public interface IClientRepository {
	
  public List<Client> findAll(); 
  public int  save(Client client);
  public boolean deleteById(int id);
  public boolean existById(int id);

  
}
