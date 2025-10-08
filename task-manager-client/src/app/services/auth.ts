import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from '../interfaces/user';
import { LoginRequest } from '../interfaces/login-request';
import { LoginResponse } from '../interfaces/login-response';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  private apiUrl = 'http://localhost:8080/auth';
  private _isLoggedIn = new BehaviorSubject<boolean>(this.hasToken());
  public isLoggedIn = this._isLoggedIn.asObservable();

  constructor(private http: HttpClient) { }

  private hasToken(): boolean {
    return !!localStorage.getItem('auth_token');
  }

  register(user:Omit<User, 'id' | 'role'>): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, user);
  }

  login(credentials: LoginRequest): Observable<LoginResponse>{
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem('authToken', response.token);
        this._isLoggedIn.next(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('authToken');
    this._isLoggedIn.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }
  
}
