
import {UserDTO} from './user.dto';

export interface Training {
  id: number;
  name: string;
  intensity: string;
  duration: number;
  goal:string;
  description: string;
  user?: {
    id: number;
  };
}
