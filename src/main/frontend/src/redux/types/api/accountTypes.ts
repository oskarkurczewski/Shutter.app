import { Language } from "types/Language";
import { etag } from "./dataTypes";

interface traceable {
   modifiedAt: Date;
   modifiedBy: string;
   createdAt: Date;
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

export interface getAdvancedUserListRequest {
   pageNo: number;
   recordsPerPage: number;
   columnName: string;
   order: string;
   q?: string;
}

export interface getBasicUserListRequest {
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
   login: string;
   name: string;
   surname: string;
}

export interface changePasswordRequest {
   password: string;
   oldPassword?: string;
}

export interface changeSomeonesPasswordRequest {
   login: string;
   data: changePasswordRequest;
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

export interface changeAccountInfoAsAdminRequest {
   body: {
      email: string;
      name: string;
      surname: string;
      login: string;
      active: boolean;
   };
   etag: etag;
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

export interface getOwnAccountChangeLogRequest {
   pageNo: number;
   recordsPerPage: number;
   order: string;
   columnName: string;
}

export interface getAccountChangeLogRequest {
   params: getOwnAccountChangeLogRequest;
   pathParam: string;
}

export interface changeDescriptionRequest {
   content: string;
}

export interface GetAccountLocaleResponse {
   languageTag: Language;
}
