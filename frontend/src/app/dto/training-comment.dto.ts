import { UserDTO } from './user.dto';
import { Training } from './training.dto';

export interface TrainingCommentDTO {
  id: number;
  name: string;
  description: string;
  isNotified: boolean | false;
  training: Training;
  user?: UserDTO;
}
