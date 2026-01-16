import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Todo } from '../models/todo.model';

/**
 * Service for managing todo operations.
 * Provides CRUD operations for todos and maintains reactive state using signals.
 */
@Injectable({
  providedIn: 'root'
})
export class TodoService {
  private readonly API_URL = '/api/todos';

  // Signal for reactive todo list
  todos = signal<Todo[]>([]);

  constructor(private http: HttpClient) {}

  /**
   * Retrieves all todos from the server (admin only).
   * Updates the reactive todos signal with the response.
   * @returns Observable of Todo array
   */
  getAllTodos(): Observable<Todo[]> {
    return this.http.get<Todo[]>(this.API_URL)
      .pipe(tap(todos => this.todos.set(todos)));
  }

  /**
   * Retrieves todos assigned to the current user.
   * Updates the reactive todos signal with the response.
   * @returns Observable of Todo array
   */
  getMyTodos(): Observable<Todo[]> {
    return this.http.get<Todo[]>(`${this.API_URL}/my`)
      .pipe(tap(todos => this.todos.set(todos)));
  }

  /**
   * Creates a new todo item.
   * Refreshes the todo list after successful creation.
   * @param todo - The todo data to create
   * @returns Observable of the created Todo
   */
  createTodo(todo: Todo): Observable<Todo> {
    return this.http.post<Todo>(this.API_URL, todo)
      .pipe(tap(() => this.getMyTodos().subscribe()));
  }

  /**
   * Updates an existing todo item.
   * Refreshes the todo list after successful update.
   * @param id - The ID of the todo to update
   * @param todo - The updated todo data
   * @returns Observable of the updated Todo
   */
  updateTodo(id: number, todo: Todo): Observable<Todo> {
    return this.http.put<Todo>(`${this.API_URL}/${id}`, todo)
      .pipe(tap(() => this.getMyTodos().subscribe()));
  }

  /**
   * Deletes a todo item by ID.
   * Refreshes the todo list after successful deletion.
   * @param id - The ID of the todo to delete
   * @returns Observable of void
   */
  deleteTodo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`)
      .pipe(tap(() => this.getMyTodos().subscribe()));
  }
}
