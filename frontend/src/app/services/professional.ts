import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Professional {

  constructor(private http: HttpClient){}

  getProfessionals(){
    return this.http.get<any[]>('professionals.json');
    
  }

}
