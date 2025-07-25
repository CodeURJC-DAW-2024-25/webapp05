import { UserDTO } from './user.dto';
import { Nutrition } from './nutrition.dto';

export interface NutritionCommentDTO {
  id: number;
  name: string;
  description: string;
  isNotified: boolean | false;
  nutrition: Nutrition;
  user?: UserDTO;
}