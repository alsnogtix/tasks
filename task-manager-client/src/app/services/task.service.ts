import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Task } from '../interfaces/task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl = 'http://localhost:8080/api/v1/tasks'; 

  constructor(private http: HttpClient) { } 

  // --- READ OPERATION ---
  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.apiUrl);
  }

  // --- CREATE OPERATION ---
  /**
   * Sends a POST request to the API to create a new task.
   * @param task The Task object (without the ID) to be created.
   * @returns An Observable of the created Task (which now includes the generated ID).
   */
  createTask(task: Omit<Task, 'id'>): Observable<Task> {
    // We send the task data to the same base URL (POST /api/v1/tasks)
    return this.http.post<Task>(this.apiUrl, task);
  }

  // --- DELETE OPERATION ---
  /**
   * Sends a DELETE request to the API to remove a task by its ID.
   * @param id The ID of the task to delete.
   * @returns An Observable that completes when the deletion is successful (204 No Content).
   */
  deleteTask(id: number): Observable<void> {
    // The URL for delete includes the ID as a path variable: /api/v1/tasks/{id}
    const deleteUrl = `${this.apiUrl}/${id}`;
    // The <void> generic parameter indicates we expect no content back (204)
    return this.http.delete<void>(deleteUrl);
  }

  // --- UPDATE OPERATION (New Method) ---
  /**
   * Sends a PUT request to the API to update an existing task.
   * @param task The Task object, which MUST include the ID for identification.
   * @returns An Observable of the updated Task.
   */
  updateTask(task: Task): Observable<Task> {
    // The URL for update is /api/v1/tasks/{id}
    const updateUrl = `${this.apiUrl}/${task.id}`;
    // We use HttpClient.put, sending the full task object in the body
    return this.http.put<Task>(updateUrl, task);
  }
}
