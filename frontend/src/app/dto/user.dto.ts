
export interface UserDTO {
  id: number;
  name: string;
  email: string;
  password?: string;
  roles?: string[];
  image?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}
