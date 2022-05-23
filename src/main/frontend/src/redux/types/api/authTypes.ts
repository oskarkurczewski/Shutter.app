export interface LoginRequest {
   login: string;
   password: string;
   twoFACode: string;
}

export interface LoginResponse {
   token: string;
}
