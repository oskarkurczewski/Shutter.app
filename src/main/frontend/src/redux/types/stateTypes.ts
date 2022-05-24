import { AccessLevel } from "types/AccessLevel";

export interface AuthState {
   username: string;
   name?: string;
   surname?: string;
   email?: string;
   roles: AccessLevel[];
   accessLevel: AccessLevel;
   token: string;
   exp: number;
}

export interface SetAccessLevel {
   accessLevel: AccessLevel;
}

export interface SetUserInfo {
   name: string;
   surname: string;
   email: string;
}
