import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import {
   advancedUserInfoResponse,
   basicUserInfoResponse,
   changeAccessLevelRequest,
   getListRequest,
   getListResponse,
   changeOwnEmailRequest,
   changeOwnPasswordRequest,
   changeOwnUserDataRequest,
   registerAccountAsAdminRequest,
   registerAccountRequest,
   requestResetPasswordRequest,
   resetPasswordRequest,
   editAccountInfoAsAdminRequest,
   AccountListPreferencesResponse,
} from "redux/types/api/accountTypes";
import { LoginRequest, LoginResponse } from "redux/types/api/authTypes";
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

      currentUserInfo: builder.query<
         { data: basicUserInfoResponse; etag: string },
         unknown
      >({
         query: () => ({ url: `account/info` }),
         transformResponse(data: basicUserInfoResponse, meta) {
            return { data, etag: meta.response.headers.get("etag") };
         },
      }),

      userInfo: builder.query<basicUserInfoResponse, string>({
         query: (login) => ({ url: `account/${login}/info` }),
      }),

      advancedUserInfo: builder.query<
         { data: advancedUserInfoResponse; etag: string },
         string
      >({
         query: (login) => ({ url: `account/${login}/detailed-info` }),
         transformResponse(data: advancedUserInfoResponse, meta) {
            return { data, etag: meta.response.headers.get("etag") };
         },
      }),

      changeOwnUserData: builder.mutation<unknown, changeOwnUserDataRequest>({
         query: ({ body, etag }) => ({
            url: "account/editOwnAccountInfo",
            method: "PUT",
            body: { ...body, version: etag.version },
            headers: { "If-Match": etag.etag },
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
         query: ({ body, etag }) => ({
            url: `account/${body.login}/editAccountInfo`,
            method: "PUT",
            body: { ...body, version: etag.version },
            headers: { "If-Match": etag.etag },
         }),
         invalidatesTags: ["List"],
      }),

      getUserList: builder.mutation<getListResponse, getListRequest>({
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
            body: { reCaptchaToken: data.captcha },
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
   }),
});

export const {
   useLoginMutation,
   useChangeOwnPasswordMutation,
   useSendChangeOwnEmailLinkMutation,
   useChangeOwnEmailMutation,
   useChangeOwnUserDataMutation,
   useSendTwoFACodeMutation,
   useRegisterMutation,
   useCurrentUserInfoQuery,
   useUserInfoQuery,
   useAdvancedUserInfoQuery,
   useRegisterAsAdminMutation,
   useUnblockOwnAccountMutation,
   useGetUserListMutation,
   useBecomePhotographerMutation,
   useStopBeingPhotographerMutation,
   useRefreshMutation,
   useRequestResetPasswordMutation,
   useResetPasswordMutation,
   useConfirmRegistrationMutation,
   useRefreshTokenMutation,
   useChangeAccessLevelMutation,
   useSwitchCurrentAccessLevelMutation,
   useEditAccountInfoAsAdminMutation,
   useGetAccountListPreferencesMutation,
} = api;
