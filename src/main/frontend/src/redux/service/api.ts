import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import {
   advancedUserInfoResponse,
   basicUserInfoResponse,
   changeAccessLevelRequest,
   editAccountInfoAsAdminRequest,
   getListRequest,
   getListResponse,
   changeOwnEmailRequest,
   changeOwnPasswordRequest,
   changeOwnUserDataRequest,
   registerAccountAsAdminRequest,
   registerAccountRequest,
   requestResetPasswordRequest,
   resetPasswordRequest,
   AccountListPreferencesResponse,
   tableAccountData,
   tableAccountChangeLogInfo,
   getAccountChangeLogRequest,
   getOwnAccountChangeLogRequest,
} from "redux/types/api/accountTypes";
import { LoginRequest, LoginResponse } from "redux/types/api/authTypes";
import { AuthState } from "redux/types/stateTypes";
import { AccessLevel } from "types/AccessLevel";
import { RootState } from "../store";

export const api = createApi({
   reducerPath: "api",
   baseQuery: fetchBaseQuery({
      baseUrl: "https://studapp.it.p.lodz.pl:8002/api/",
      prepareHeaders: (headers, { getState }) => {
         const token = (getState() as RootState).auth.token;
         if (token) {
            headers.set("Authorization", `Bearer ${token}`);
         }
         return headers;
      },
   }),
   tagTypes: ["List", "Preferences"],
   endpoints: (builder) => ({
      login: builder.mutation<LoginResponse, LoginRequest>({
         query: (credentials) => ({
            url: "auth/login",
            method: "POST",
            body: credentials,
         }),
      }),

      refreshToken: builder.mutation<LoginResponse, object>({
         query: () => ({
            url: "auth/refresh",
            method: "POST",
         }),
      }),

      userInfo: builder.query<basicUserInfoResponse, string>({
         query: (login) => ({ url: `account/${login}/info` }),
      }),

      advancedUserInfo: builder.query<advancedUserInfoResponse, string>({
         query: (login) => ({ url: `account/${login}/detailed-info` }),
      }),

      changeOwnUserData: builder.mutation<unknown, changeOwnUserDataRequest>({
         query: (data) => ({
            url: "account/editOwnAccountInfo",
            method: "PUT",
            body: data,
         }),
      }),

      sendChangeOwnEmailLink: builder.mutation<unknown, unknown>({
         query: () => ({
            url: "account/request-email-update",
            method: "POST",
         }),
      }),

      changeOwnEmail: builder.mutation<unknown, changeOwnEmailRequest>({
         query: (data) => ({
            url: "account/verify-email-update",
            method: "POST",
            body: data,
         }),
      }),

      changeOwnPassword: builder.mutation<unknown, changeOwnPasswordRequest>({
         query: (data) => ({
            url: "account/change-password",
            method: "PUT",
            body: data,
         }),
      }),

      sendTwoFACode: builder.mutation<unknown, string>({
         query: (login) => ({
            url: `account/${login}/request-2fa-code`,
            method: "POST",
         }),
      }),

      register: builder.mutation<unknown, registerAccountRequest>({
         query: (data) => ({
            url: "account/register",
            method: "POST",
            body: data,
         }),
      }),

      registerAsAdmin: builder.mutation<unknown, registerAccountAsAdminRequest>({
         query: (data) => ({
            url: "account/register",
            method: "POST",
            body: data,
         }),
      }),

      confirmRegistration: builder.mutation<unknown, string>({
         query: (token) => ({
            url: `account/confirm/${token}`,
            method: "POST",
         }),
      }),

      unblockOwnAccount: builder.mutation<unknown, string>({
         query: (token) => ({
            url: `account/unblock-own-account/${token}`,
         }),
      }),

      editAccountInfoAsAdmin: builder.mutation<unknown, editAccountInfoAsAdminRequest>({
         query: ({ params, body }) => ({
            url: `account/${params.login}/editAccountInfo`,
            method: "PUT",
            body: body,
         }),
         invalidatesTags: ["List"],
      }),

      getUserList: builder.mutation<getListResponse<tableAccountData>, getListRequest>({
         query: (data) => ({
            url: "account/list",
            params: data,
         }),
      }),

      becomePhotographer: builder.mutation<unknown, unknown>({
         query: (data) => ({
            url: "/account/become-photographer",
            method: "POST",
         }),
      }),

      stopBeingPhotographer: builder.mutation<unknown, unknown>({
         query: (data) => ({
            url: "/account/stop-being-photographer",
            method: "POST",
         }),
      }),

      refresh: builder.mutation<LoginResponse, unknown>({
         query: (data) => ({
            url: "/auth/refresh",
            method: "POST",
         }),
      }),

      resetPassword: builder.mutation<unknown, resetPasswordRequest>({
         query: (data) => ({
            url: "account/password-reset",
            method: "POST",
            body: data,
         }),
      }),

      requestResetPassword: builder.mutation<unknown, requestResetPasswordRequest>({
         query: (data) => ({
            url: `/account/${data.login}/request-reset`,
            method: "POST",
         }),
      }),

      changeAccessLevel: builder.mutation<unknown, changeAccessLevelRequest>({
         query: ({ params, body }) => ({
            url: `account/${params.login}/accessLevel`,
            method: "POST",
            body: body,
         }),
      }),

      switchCurrentAccessLevel: builder.mutation<unknown, AccessLevel>({
         query: (group) => ({
            url: `auth/change-group/${group}`,
            method: "POST",
         }),
      }),

      getAccountListPreferences: builder.mutation<AccountListPreferencesResponse, void>({
         query: () => ({
            url: "account/list/preferences",
            method: "GET",
         }),
         invalidatesTags: ["Preferences"],
      }),

      getAccountChangeLog: builder.mutation<
         getListResponse<tableAccountChangeLogInfo>,
         getAccountChangeLogRequest
      >({
         query: (data) => ({
            url: "account/get-account-change-log",
            params: data,
         }),
      }),

      getOwnAccountChangeLog: builder.mutation<
         getListResponse<tableAccountChangeLogInfo>,
         getOwnAccountChangeLogRequest
      >({
         query: (data) => ({
            url: `account/${data.pathParam}/get-account-change-log`,
            params: data.params,
         }),
      }),
   }),
});

export const {
   useLoginMutation,
   useUserInfoQuery,
   useRefreshMutation,
   useRegisterMutation,
   useGetUserListMutation,
   useRefreshTokenMutation,
   useAdvancedUserInfoQuery,
   useSendTwoFACodeMutation,
   useResetPasswordMutation,
   useChangeOwnEmailMutation,
   useRegisterAsAdminMutation,
   useChangeOwnUserDataMutation,
   useChangeAccessLevelMutation,
   useUnblockOwnAccountMutation,
   useChangeOwnPasswordMutation,
   useBecomePhotographerMutation,
   useGetAccountChangeLogMutation,
   useConfirmRegistrationMutation,
   useRequestResetPasswordMutation,
   useStopBeingPhotographerMutation,
   useGetOwnAccountChangeLogMutation,
   useEditAccountInfoAsAdminMutation,
   useSendChangeOwnEmailLinkMutation,
   useSwitchCurrentAccessLevelMutation,
   useGetAccountListPreferencesMutation,
} = api;
