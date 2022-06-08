import { api } from "./api";
import {
   advancedUserInfoResponse,
   basicUserInfoResponse,
   changeAccessLevelRequest,
   getAdvancedUserListRequest,
   getListResponse,
   registerAccountAsAdminRequest,
   editAccountInfoAsAdminRequest,
   AccountListPreferencesResponse,
   tableAccountData,
   tableAccountChangeLogInfo,
   getAccountChangeLogRequest,
   getBasicUserListRequest,
} from "redux/types/api/accountTypes";

const UsersManagementService = api.injectEndpoints({
   endpoints: (builder) => ({
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

      registerAsAdmin: builder.mutation<unknown, registerAccountAsAdminRequest>({
         query: (data) => ({
            url: "account/register",
            method: "POST",
            body: data,
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

      getAdvancedUserList: builder.mutation<
         getListResponse<tableAccountData>,
         getAdvancedUserListRequest
      >({
         query: (data) => ({
            url: "account/list",
            params: data,
         }),
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

      changeAccessLevel: builder.mutation<unknown, changeAccessLevelRequest>({
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
   useUserInfoQuery,
   useGetAdvancedUserListMutation,
   useAdvancedUserInfoQuery,
   useRegisterAsAdminMutation,
   useChangeAccessLevelMutation,
   useGetAccountChangeLogMutation,
   useEditAccountInfoAsAdminMutation,
   useGetAccountListPreferencesMutation,
   useGetBasicUserListMutation,
} = UsersManagementService;
