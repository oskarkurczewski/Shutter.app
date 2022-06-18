import { api } from "./api";
import {
   registerRequest,
   LoginRequest,
   TokenResponse,
   BasicUserInfoResponse,
} from "redux/types/api";
import { AccessLevel } from "types/AccessLevel";
import { EtagData } from "redux/types/api/dataTypes";

const AuthService = api.injectEndpoints({
   endpoints: (builder) => ({
      login: builder.mutation<TokenResponse, LoginRequest>({
         query: (credentials) => ({
            url: "auth/login",
            method: "POST",
            body: credentials,
         }),
      }),

      refreshToken: builder.mutation<TokenResponse, void>({
         query: () => ({
            url: "auth/refresh",
            method: "POST",
         }),
      }),

      sendTwoFACode: builder.mutation<void, string>({
         query: (login) => ({
            url: `account/${login}/request-2fa-code`,
            method: "POST",
         }),
      }),

      register: builder.mutation<void, registerRequest>({
         query: (data) => ({
            url: "account/register",
            method: "POST",
            body: data,
         }),
      }),

      switchCurrentAccessLevel: builder.mutation<void, AccessLevel>({
         query: (group) => ({
            url: `auth/change-group/${group}`,
            method: "POST",
         }),
      }),

      getUserInfo: builder.query<EtagData<BasicUserInfoResponse>, void>({
         query: () => ({ url: `account/info` }),
         transformResponse(data: BasicUserInfoResponse, meta) {
            return {
               data,
               etag: {
                  etag: meta.response.headers.get("etag"),
                  version: data.version,
               },
            };
         },
      }),
   }),
});

export const {
   useLoginMutation,
   useRegisterMutation,
   useRefreshTokenMutation,
   useSendTwoFACodeMutation,
   useSwitchCurrentAccessLevelMutation,
   useGetUserInfoQuery,
} = AuthService;
