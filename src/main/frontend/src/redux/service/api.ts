import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import {
   basicUserInfoResponse,
   registerAccountAsAdminRequest,
   registerAccountRequest,
} from "redux/types/api/accountTypes";
import { LoginRequest, LoginResponse } from "redux/types/api/authTypes";
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
   }),
});

export const {
   useLoginMutation,
   useUserInfoQuery,
   useRegisterMutation,
   useRegisterAsAdminMutation,
} = api;
