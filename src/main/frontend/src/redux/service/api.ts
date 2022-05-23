import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { RootState } from "../store";

export interface LoginResponse {
   token: string;
}

export interface LoginRequest {
   login: string;
   password: string;
   faCode;
}

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
   }),
});

export const { useLoginMutation } = api;
