import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../interfaces/client';

import {  throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';


const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json; charset=utf-8',
    'Access-Control-Allow-Headers': 'Content-Type',
    'Access-Control-Allow-Methods': 'GET, POST, PUT',
    'Access-Control-Allow-Origin': '*',
    //Authorization: 'my-auth-token'
    'MyClientCert': '',        // This is empty
    'MyToken': ''              // This is empty
  })
};

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  private urlClients = 'http://localhost:8080/api'; 

  constructor(private http: HttpClient) {}

  handleError(error: HttpErrorResponse) {
    let errorMessage = 'Unknown error!';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side errors
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side errors
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    window
    .alert(errorMessage);
    return throwError(errorMessage);
  }

  public saveClient(client: Client): Observable<any> {
      return this.http.post<Client>(this.urlClients + "/client", client, httpOptions).pipe(catchError(this.handleError));
  }

  public getClients(): Observable<any> {
    return this.http.get(this.urlClients + "/clients").pipe(catchError(this.handleError));
  }
}
