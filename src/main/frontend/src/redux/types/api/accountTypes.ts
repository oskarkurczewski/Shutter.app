interface traceable {
   modifiedAt: Date;
   modifiedBy: string;
   createdAt: Date;
   createdBy: string;
}
export interface getListResponse<T> {
   allPages: number;
   allRecords: number;
   list: T[];
   pageNo: number;
   recordsPerPage: number;
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
   reCaptchaToken: string;
}

export interface registerAccountAsAdminRequest {
   login: string;
   password: string;
   email: string;
   name: string;
   surname: string;
   registered: boolean;
   active: boolean;
}
export interface tableAccountData {
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

export enum ChangeType {
   CREATED = "CREATED",
   MODIFIED = "MODIFIED",
   DELETED = "DELETED",
}

export interface tableAccountChangeLogInfo {
   id: number;
   registered: boolean;
   active: boolean;
   email: string;
   login: string;
   name: string;
   surname: string;
   lastLogIn: Date;
   changedAt: Date;
   changeType: ChangeType;
   changedBy: string;
}

export interface getAccountChangeLogRequest {
   pageNo: number;
   recordsPerPage: number;
   order: string;
   columnName: string;
}

export interface getOwnAccountChangeLogRequest {
   params: getAccountChangeLogRequest;
   pathParam: string;
}
