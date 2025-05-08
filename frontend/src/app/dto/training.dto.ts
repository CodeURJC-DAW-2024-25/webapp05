
import {UserDTO} from './user.dto';

export interface Training {
  id: number;
  name: string;
  intensity: string;
  duration: number;
  goal:string;
  description: string;
  dishImagePath: string;
  image: boolean;
  userId: number | null;
}
