import { AccessLevel } from "types/AccessLevel";

export interface AuthState {
   username: string;
   accessLevel: AccessLevel[];
}
