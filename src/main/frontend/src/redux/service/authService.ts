import { api } from "./api";
import {
   registerAccountRequest,
   LoginRequest,
   LoginResponse,
   basicUserInfoResponse,
} from "redux/types/api";
import { AccessLevel } from "types/AccessLevel";

const AuthService = api.injectEndpoints({
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

      switchCurrentAccessLevel: builder.mutation<unknown, AccessLevel>({
         query: (group) => ({
            url: `auth/change-group/${group}`,
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
   }),
});

export const {
   useLoginMutation,
   useRegisterMutation,
   useRefreshTokenMutation,
   useSendTwoFACodeMutation,
   useSwitchCurrentAccessLevelMutation,
   useCurrentUserInfoQuery,
} = AuthService;
