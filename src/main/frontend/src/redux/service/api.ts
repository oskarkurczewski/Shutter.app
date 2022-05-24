import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import {
   basicUserInfoResponse,
   getListRequest,
   getListResponse,
   changeOwnEmailRequest,
   changeOwnPasswordRequest,
   changeOwnUserDataRequest,
   registerAccountAsAdminRequest,
   registerAccountRequest,
   requestResetPasswordRequest,
   resetPasswordRequest,
} from "redux/types/api/accountTypes";
import { LoginRequest, LoginResponse } from "redux/types/api/authTypes";
import { AuthState } from "redux/types/stateTypes";
import { RootState } from "../store";

export const api = createApi({
   reducerPath: "api",
   baseQuery: fetchBaseQuery({
      baseUrl: "https://localhost:8002/api/",
      prepareHeaders: (headers, { getState }) => {
         const token = (getState() as RootState).auth.token;
         if (token) {
            headers.set("Authorization", `Bearer ${token}`);
         }
         return headers;
      },
   }),
   endpoints: (builder) => ({
      login: builder.mutation<LoginResponse, LoginRequest>({
         query: (credentials) => ({
            url: "auth/login",
            method: "POST",
            body: credentials,
         }),
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

      userInfo: builder.query<basicUserInfoResponse, string>({
         query: (login) => ({ url: `account/${login}/info` }),
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

      getUserList: builder.query<getListResponse, getListRequest>({
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
            url: "account/request-reset",
            method: "POST",
            body: data,
         }),
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
   useUserInfoQuery,
   useRegisterAsAdminMutation,
   useUnblockOwnAccountMutation,
   useGetUserListQuery,
   useBecomePhotographerMutation,
   useStopBeingPhotographerMutation,
   useRefreshMutation,
   useRequestResetPasswordMutation,
   useResetPasswordMutation,
   useConfirmRegistrationMutation,
} = api;
