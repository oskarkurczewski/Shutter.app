import { Language } from "types/Language";
import { etag } from "./dataTypes";

interface Traceable {
   modifiedAt: Date;
   modifiedBy: string;
   createdAt: Date;
   createdBy: string;
}

export interface BasicUserInfoResponse {
   login?: string;
   version: number;
   email: string;
   name: string;
   surname: string;
   lastLogIn: string;
   lastFailedLogInAttempt: string;
   lastFailedLoginIp: string;
}

export interface AdvancedUserInfoResponse extends BasicUserInfoResponse, Traceable {
   version: number;
   login: string;
   active: boolean;
   registered: boolean;
   accessLevelList: AccessLevel[];
}

interface AccessLevel extends Traceable {
   name: string;
}

export interface TableAccountData {
   accessLevels: string[];
   email: string;
   id: number;
   isActive: boolean;
   isRegistered: boolean;
   login: string;
   name: string;
   surname: string;
}

export interface GetAdvancedUserListRequest {
   pageNo: number;
   recordsPerPage: number;
   columnName: string;
   order: string;
   q?: string;
}

export interface GetBasicUserListRequest {
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

export interface ResetPasswordRequest {
   token: string;
   newPassword: string;
}

export interface RequestResetPasswordRequest {
   login: string;
   captcha: string;
}

export interface ChangeOwnUserDataRequest {
   login: string;
   name: string;
   surname: string;
}

export interface ChangePasswordRequest {
   password: string;
   oldPassword?: string;
}

export interface ChangeSomeonesPasswordRequest {
   login: string;
   data: ChangePasswordRequest;
}

export interface ChangeOwnEmailRequest {
   newEmail: string;
   token: string;
}

export interface ChangeAccessLevelRequestParams {
   login: string;
}

export interface ChangeAccessLevelRequestBody {
   accessLevel: string;
   active: boolean;
}

export interface ChangeAccessLevelRequest {
   params: ChangeAccessLevelRequestParams;
   body: ChangeAccessLevelRequestBody;
}

export interface ChangeAccountInfoAsAdminRequest {
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

export interface TableAccountChangeLogInfo {
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

export interface GetOwnAccountChangeLogRequest {
   pageNo: number;
   recordsPerPage: number;
   order: string;
   columnName: string;
}

export interface GetAccountChangeLogRequest {
   params: GetOwnAccountChangeLogRequest;
   pathParam: string;
}

export interface ChangeDescriptionRequest {
   content: string;
}

export interface GetAccountLocaleResponse {
   languageTag: Language;
}

export interface accountReport {
   id: number;
   reportedLogin: string;
   reporteeLogin: string;
   cause: string;
   reviewed: boolean;
   createdAt: Date;
}
