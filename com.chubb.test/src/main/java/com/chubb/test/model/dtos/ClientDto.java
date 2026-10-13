package com.chubb.test.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {
	int id;
	String firstName;
	String lastName;
	String surname;
	String phone;
	

	 public ClientDto(int id, String firstName, String lastName, String surname, String phone){
		super();
		this.id = id; 
		this.firstName = firstName; 
		this.lastName = lastName; 
		this.surname = surname; 
		this.phone = phone;
	 }
}
