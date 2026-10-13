package com.practice.clientapi.respository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.practice.clientapi.model.Client;

@Repository
public class ClientRepository implements IClientRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Client> findAll() {
		return jdbcTemplate.query("select id,first_name,last_name,surname,phone from clients",
				BeanPropertyRowMapper.newInstance(Client.class));
	}

	@Override
	public int save(Client client) {		
		return jdbcTemplate.update("insert into clients (first_name, last_name, surname, phone) values (?, ?, ?, ?)",
				new Object[] {client.getFirstName(), client.getLastName(), client.getSurname(), client.getPhone()});
	}

	@Override
	public boolean deleteById(int id) {
		int rows = jdbcTemplate.update("delete from clients where id = ?", id);
		return rows > 0;
	}

	@Override
	public boolean existById(int id) {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from clients where id = ?", Integer.class,id
		);
		return count != null && count > 0;
	}

}
