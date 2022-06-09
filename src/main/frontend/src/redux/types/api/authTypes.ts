export interface LoginRequest {
   login: string;
   password: string;
   twoFACode: string;
}

export interface TokenResponse {
   token: string;
}

export interface registerRequest {
   login: string;
   password: string;
   email: string;
   name: string;
   surname: string;
   reCaptchaToken: string;
}

export interface createAccountRequest {
   login: string;
   password: string;
   email: string;
   name: string;
   surname: string;
   registered: boolean;
   active: boolean;
}
