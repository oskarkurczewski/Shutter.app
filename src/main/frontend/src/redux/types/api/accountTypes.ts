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

export interface changeOwnUserDataRequest {
   name: string;
   surname: string;
}

export interface changeOwnPasswordRequest {
   password: string;
   oldPassword: string;
}

export interface changeOwnEmailRequest {
   newEmail: string;
   token: string;
}
