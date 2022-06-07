import { api } from "./api";
import {
   advancedUserInfoResponse,
   basicUserInfoResponse,
   changeAccessLevelRequest,
   getAdvancedUserListRequest,
   changeAccountInfoAsAdminRequest,
   createAccountRequest,
   AccountListPreferencesResponse,
   tableAccountData,
   tableAccountChangeLogInfo,
   getAccountChangeLogRequest,
   getBasicUserListRequest,
} from "redux/types/api";
import { EtagData, getListResponse } from "redux/types/api/dataTypes";

const UsersManagementService = api.injectEndpoints({
   endpoints: (builder) => ({
      getUserInfo: builder.query<basicUserInfoResponse, string>({
         query: (login) => ({ url: `account/${login}/info` }),
      }),

      getAdvancedUserInfo: builder.query<EtagData<advancedUserInfoResponse>, string>({
         query: (login) => ({ url: `account/${login}/detailed-info` }),
         transformResponse(data: advancedUserInfoResponse, meta) {
            return {
               data,
               etag: {
                  etag: meta.response.headers.get("etag"),
                  version: data.version,
               },
            };
         },
      }),

      createAccount: builder.mutation<void, createAccountRequest>({
         query: (data) => ({
            url: "account/register",
            method: "POST",
            body: data,
         }),
      }),

      changeAccountInfo: builder.mutation<void, changeAccountInfoAsAdminRequest>({
         query: ({ body, etag }) => ({
            url: `account/${body.login}/editAccountInfo`,
            method: "PUT",
            body: { ...body, version: etag.version },
            headers: { "If-Match": etag.etag },
         }),
         invalidatesTags: ["List"],
      }),

      getBasicUserList: builder.mutation<
         getListResponse<tableAccountData>,
         getBasicUserListRequest
      >({
         query: (data) => ({
            url: "account/list-name",
            params: data,
         }),
      }),

      getAdvancedUserList: builder.mutation<
         getListResponse<tableAccountData>,
         getAdvancedUserListRequest
      >({
         query: (data) => ({
            url: "account/list",
            params: data,
         }),
      }),

      changeAccessLevel: builder.mutation<void, changeAccessLevelRequest>({
         query: ({ params, body }) => ({
            url: `account/${params.login}/accessLevel`,
            method: "POST",
            body: body,
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
            url: `account/${data.pathParam}/get-account-change-log`,
            params: data.params,
         }),
      }),
   }),
});

export const {
   useGetUserInfoQuery,
   useGetAdvancedUserListMutation,
   useGetAdvancedUserInfoQuery,
   useCreateAccountMutation,
   useChangeAccessLevelMutation,
   useGetAccountChangeLogMutation,
   useChangeAccountInfoMutation,
   useGetAccountListPreferencesMutation,
   useGetBasicUserListMutation,
} = UsersManagementService;
