
import {UserDTO} from './user.dto';

export interface Nutrition {
  id: number;
  name: string;
  calories: number;
  goal:string;
  description: string;
  user?: {
    id: number;
  };
}