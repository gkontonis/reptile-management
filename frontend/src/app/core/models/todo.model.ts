/**
 * Enumeration of possible todo status values.
 */
export type TodoStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';

/**
 * Enumeration of possible todo category values.
 */
export type TodoCategory = 'HOUSEHOLD' | 'MAINTENANCE' | 'GARDEN' | 'CLEANING' | 'OTHER';

/**
 * Represents a todo item in the system.
 */
export interface Todo {
  /** Unique identifier for the todo */
  id?: number;
  /** Title of the todo item */
  title: string;
  /** Detailed description of the todo */
  description: string;
  /** Current status of the todo */
  status: TodoStatus;
  /** Category classification of the todo */
  category: TodoCategory;
  /** ID of the user this todo is assigned to */
  assignedToId: number;
  /** Username of the assigned user (optional, populated by API) */
  assignedToUsername?: string;
  /** Due date in ISO string format, null if no due date */
  dueDate: string | null;
  /** Creation timestamp in ISO string format (optional, set by server) */
  createdAt?: string;
  /** Completion timestamp in ISO string format, null if not completed */
  completedAt?: string | null;
}
