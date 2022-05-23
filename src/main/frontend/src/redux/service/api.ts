import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import {
   basicUserInfoResponse,
   registerAccountAsAdminRequest,
   registerAccountRequest,
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

      userInfo: builder.query<basicUserInfoResponse, string>({
         query: (login) => ({ url: `account/${login}/info` }),
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
   }),
});

export const {
   useLoginMutation,
   useUserInfoQuery,
   useRegisterMutation,
   useRegisterAsAdminMutation,
   useBecomePhotographerMutation,
   useStopBeingPhotographerMutation,
   useRefreshMutation,
} = api;
