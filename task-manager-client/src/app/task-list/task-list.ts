import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { FormsModule } from '@angular/forms'; 
import { Task } from '../interfaces/task';
import { TaskService } from '../services/task.service';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './task-list.html',
  styleUrls: ['./task-list.scss']
})
export class TaskListComponent implements OnInit {
  
  public tasks: Task[] = []; 
  
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

  deleteTask(id: number): void {
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          this.tasks = this.tasks.filter(task => task.id !== id);
          console.log(`Task with ID ${id} deleted successfully.`);
        },
        error: (err) => {
          console.error(`Failed to delete task with ID ${id}:`, err);
        }
      });
    }
  }

  toggleStatus(task: Task): void {
    task.status = !task.status; 
    
    this.taskService.updateTask(task).subscribe({
      next: (updatedTask) => {
        console.log('Task status updated successfully:', updatedTask);
      },
      error: (err) => {
        console.error('Failed to update task status:', err);
        task.status = !task.status; 
      }
    });
  }
}
