import { api } from "./api";
import {
   AdvancedUserInfoResponse,
   BasicUserInfoResponse,
   ChangeAccessLevelRequest,
   GetAdvancedUserListRequest,
   ChangeAccountInfoAsAdminRequest,
   createAccountRequest,
   AccountListPreferencesResponse,
   TableAccountData,
   TableAccountChangeLogInfo,
   GetAccountChangeLogRequest,
   GetBasicUserListRequest,
   ChangeSomeonesPasswordRequest,
   ReservationResponse,
   ReservationRequest,
   accountReport,
} from "redux/types/api";
import {
   EtagData,
   getListResponse,
   getReportListRequest,
} from "redux/types/api/dataTypes";

const UsersManagementService = api.injectEndpoints({
   endpoints: (builder) => ({
      getUserInfo: builder.query<BasicUserInfoResponse, string>({
         query: (login) => ({ url: `account/${login}/info` }),
      }),

      getAdvancedUserInfo: builder.query<EtagData<AdvancedUserInfoResponse>, string>({
         query: (login) => ({ url: `account/${login}/detailed-info` }),
         transformResponse(data: AdvancedUserInfoResponse, meta) {
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

      changeAccountInfo: builder.mutation<void, ChangeAccountInfoAsAdminRequest>({
         query: ({ body, etag }) => ({
            url: `account/${body.login}/editAccountInfo`,
            method: "PUT",
            body: { ...body, version: etag.version },
            headers: { "If-Match": etag.etag },
         }),
         invalidatesTags: ["List"],
      }),

      getBasicUserList: builder.mutation<
         getListResponse<TableAccountData>,
         GetBasicUserListRequest
      >({
         query: (data) => ({
            url: "account/list-name",
            params: data,
         }),
      }),

      getAdvancedUserList: builder.mutation<
         getListResponse<TableAccountData>,
         GetAdvancedUserListRequest
      >({
         query: (data) => ({
            url: "account/list",
            params: data,
         }),
      }),

      cancelReservation: builder.mutation<void, number>({
         query: (id) => ({
            url: `/reservation/${id}/cancel`,
            method: "DELETE",
         }),
      }),

      changeAccessLevel: builder.mutation<void, ChangeAccessLevelRequest>({
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
         getListResponse<TableAccountChangeLogInfo>,
         GetAccountChangeLogRequest
      >({
         query: (data) => ({
            url: `account/${data.pathParam}/get-account-change-log`,
            params: data.params,
         }),
      }),

      changeSomeonesPassword: builder.mutation<void, ChangeSomeonesPasswordRequest>({
         query: (data) => ({
            url: `account/${data.login}/change-password`,
            method: "PUT",
            body: data.data,
         }),
      }),


      resolveAccountReport: builder.mutation<void, number>({
         query: (id) => ({ url: `/report/account/${id}/resolve`, method: "POST" }),
      }),

      getReservationsList: builder.mutation<ReservationResponse[], ReservationRequest>({
         query: (data) => ({
            url: "/reservation/my-reservations",
            params: data,
         }),
      }),

      getAccountReportList: builder.query<
          getListResponse<accountReport>,
          getReportListRequest
          >({
         query: (params) => ({ url: `/report/list/account`, params }),
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
   useGetAccountReportListQuery,
   useChangeSomeonesPasswordMutation,
   useGetReservationsListMutation,
   useResolveAccountReportMutation,
   useCancelReservationMutation,
} = UsersManagementService;
