export class UserDTO {
  public password?: string;

  constructor(
    public id: number,
    public name: string,
    public roles: string[] = [],
    public email: string,
    password?: string
  ) {
    this.password = undefined;
  }
}
