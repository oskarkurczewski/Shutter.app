import { AccessLevel } from "types/AccessLevel";

export interface AuthState {
   username: string;
   roles: AccessLevel[];
   accessLevel: AccessLevel;
   token: string;
   exp: number;
}
