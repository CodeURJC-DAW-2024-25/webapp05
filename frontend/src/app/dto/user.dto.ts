export class UserDTO {
    id: number;
    name: string;
    roles?: string[];
    email: string;
    password: string;

    constructor(
      id: number,
      name: string,
      roles: string[],
      email: string,
      password: string
    ) {
      this.id = id;
      this.name = name;
      this.roles = roles;
      this.email = email;
      this.password = password;
    }
}
