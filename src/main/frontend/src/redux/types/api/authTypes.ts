import { Language } from "types/Language";

export interface LoginRequest {
   login: string;
   password: string;
   twoFACode?: string;
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
   locale: Language;
}

export interface createAccountRequest {
   login: string;
   password: string;
   email: string;
   name: string;
   surname: string;
   registered: boolean;
   active: boolean;
   locale: string;
}
