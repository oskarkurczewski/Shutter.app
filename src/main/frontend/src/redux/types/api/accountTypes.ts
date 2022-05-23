export interface basicUserInfoResponse {
   email: string;
   name: string;
   surname: string;
}

export interface registerAccountRequest {
   login: string;
   password: string;
   email: string;
   name: string;
   surname: string;
}

export interface registerAccountAsAdminRequest extends registerAccountRequest {
   registered: boolean;
   active: boolean;
}

export interface resetPasswordRequest {
   token: string;
   newPassword: string;
}

export interface requestResetPasswordRequest {
   login: string;
}
