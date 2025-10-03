import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { FormsModule } from '@angular/forms'; 
import { Task } from '../interfaces/task';
import { TaskService } from '../services/task.service';
import { Observable } from 'rxjs'; 

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './task-list.html',
  styleUrls: ['./task-list.scss']
})
export class TaskListComponent implements OnInit {
  
  public tasks: Task[] = []; 
  
  // Omit is used here because we don't send the ID during creation
  public newTask: Omit<Task, 'id'> = { 
    title: '', 
    description: '', 
    status: false 
  };
  
  constructor(private taskService: TaskService) { }

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getAllTasks().subscribe({
      next: (data) => {
        this.tasks = data; 
      },
      error: (err) => {
        console.error('Failed to load tasks from API', err);
      }
    });
  }

  createTask(): void {
    if (!this.newTask.title || !this.newTask.description) {
      console.error('Title and Description are required!'); 
      return;
    }

    this.taskService.createTask(this.newTask).subscribe({
      next: (createdTask) => {
        this.tasks.push(createdTask); 
        this.newTask = { title: '', description: '', status: false }; 
        console.log('Task created successfully:', createdTask);
      },
      error: (err) => {
        console.error('Failed to create task:', err);
      }
    });
  }

  /**
   * --- DELETE LOGIC ---
   * Calls the service to delete the task and updates the local array upon success.
   * @param id The ID of the task to delete.
   */
  deleteTask(id: number): void {
    // IMPORTANT: Using window.confirm() here. For professional apps, use a custom modal UI.
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          // Filter the tasks array to remove the task with the matching ID
          this.tasks = this.tasks.filter(task => task.id !== id);
          console.log(`Task with ID ${id} deleted successfully.`);
        },
        error: (err) => {
          console.error(`Failed to delete task with ID ${id}:`, err);
        }
      });
    }
  }

  /**
   * --- UPDATE LOGIC (Toggle Status) ---
   * Toggles the task's status locally and sends the updated task to the API.
   * @param task The task object to modify.
   */
  toggleStatus(task: Task): void {
    // 1. Locally change the status first (for immediate UI response)
    task.status = !task.status; 
    
    // 2. Send the full updated task to the service
    this.taskService.updateTask(task).subscribe({
      next: (updatedTask) => {
        console.log('Task status updated successfully:', updatedTask);
        // We don't need to update the local array because we already changed it in Step 1.
      },
      error: (err) => {
        console.error('Failed to update task status:', err);
        // Revert the local change if the API update fails
        task.status = !task.status; 
      }
    });
  }
}
