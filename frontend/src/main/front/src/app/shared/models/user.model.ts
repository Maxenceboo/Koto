export interface CreateUser {
  email: string;
  username: string;
  password: string;
}

export interface UserState {
  id: string;
  email: string;
  username: string;
}
