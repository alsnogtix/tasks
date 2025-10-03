/**
 * Defines the structure for a Task object, matching the data sent by the Java API.
 * This ensures type safety throughout the Angular application.
 */
export interface Task {
  id: number;
  title: string;
  description: string;
  status: boolean; 
}
