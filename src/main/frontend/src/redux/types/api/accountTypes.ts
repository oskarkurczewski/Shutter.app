interface traceable {
   modifiedAt: string;
   modifiedBy: string;
   createdAt: string;
   createdBy: string;
}

export interface basicUserInfoResponse {
   version: number;
   email: string;
   name: string;
   surname: string;
}

export interface advancedUserInfoResponse extends basicUserInfoResponse, traceable {
   version: number;
   login: string;
   active: boolean;
   registered: boolean;
   accessLevelList: accessLevel[];
}

interface accessLevel extends traceable {
   name: string;
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
interface tableAccountData {
   accessLevels: string[];
   email: string;
   id: number;
   isActive: boolean;
   isRegistered: boolean;
   login: string;
   name: string;
   surname: string;
}

export interface getListRequest {
   pageNo: number;
   recordsPerPage: number;
   columnName: string;
   order: string;
   login?: string;
   email?: string;
   name?: string;
   surname?: string;
   registered?: boolean;
   active?: boolean;
}
export interface getListResponse {
   allPages: number;
   allRecords: number;
   list: tableAccountData[];
   pageNo: number;
   recordsPerPage: number;
}

export interface resetPasswordRequest {
   token: string;
   newPassword: string;
}

export interface requestResetPasswordRequest {
   login: string;
   captcha: string;
}

export interface changeOwnUserDataRequest {
   body: {
      login: string;
      name: string;
      surname: string;
   };
   etag: etagData;
}

export interface changeOwnPasswordRequest {
   password: string;
   oldPassword: string;
}

export interface changeOwnEmailRequest {
   newEmail: string;
   token: string;
}

export interface changeAccessLevelRequestParams {
   login: string;
}

export interface changeAccessLevelRequestBody {
   accessLevel: string;
   active: boolean;
}

export interface changeAccessLevelRequest {
   params: changeAccessLevelRequestParams;
   body: changeAccessLevelRequestBody;
}

export interface editAccountInfoAsAdminRequest {
   body: {
      email: string;
      name: string;
      surname: string;
      login: string;
   };
   etag: etagData;
}

export interface etagData {
   version: number;
   etag: string;
}

export interface AccountListPreferencesResponse {
   page: number;
   recordsPerPage: number;
   orderBy: string;
   orderAsc: boolean;
}