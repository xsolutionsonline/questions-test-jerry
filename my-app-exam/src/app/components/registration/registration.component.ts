import { Component, OnInit } from '@angular/core';

import { ClientService } from '../../services/client.service';
import { Client } from '../../interfaces/client';
import { MatTableDataSource } from '@angular/material/table';

const clientCollection: Client[] = [];

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})

export class RegistrationComponent implements OnInit {

  name: string = "";
  firstName : string = "";
  lastName: string = "";
  phone: string = "";

  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'phone', 'actions'];
  dataSource = new MatTableDataSource(clientCollection);

  constructor(private clientService: ClientService) { }

  ngOnInit(): void {
    this.getClients();
  }

  onSave(){  
    let client: Client;
    client = {
         id:  0,
         firstName:  this.firstName,
         lastName:  this.lastName,
         phone:  this.phone
    } 

    this.clientService.saveClient(client).subscribe((data: any[])=>{
      alert("Cliente guardado correctamente.");
      this.getClients();
    })  
  }

  getClients(){  
    this.clientService.getClients()
    .subscribe(
      (data) => {
        
        this.dataSource.data = [];
        clientCollection.length = 0;

        data.forEach(function (client: Client, index: number) {
          clientCollection.push(
            {
              id: client.id, 
              firstName: client.firstName,
              lastName: client.lastName, 
              phone: client.phone, 
            });
        });

         this.dataSource = new MatTableDataSource(clientCollection);
      }
    );
  }
}
