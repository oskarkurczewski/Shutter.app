import { api } from "./api";
import {
   getListResponse,
   changeOwnEmailRequest,
   changeOwnPasswordRequest,
   changeOwnUserDataRequest,
   requestResetPasswordRequest,
   tableAccountChangeLogInfo,
   getOwnAccountChangeLogRequest,
} from "redux/types/api/accountTypes";

const UserSettingsService = api.injectEndpoints({
   endpoints: (builder) => ({
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

      requestResetPassword: builder.mutation<unknown, requestResetPasswordRequest>({
         query: (data) => ({
            url: `/account/${data.login}/request-reset`,
            method: "POST",
            body: { reCaptchaToken: data.captcha },
         }),
      }),

      getOwnAccountChangeLog: builder.mutation<
         getListResponse<tableAccountChangeLogInfo>,
         getOwnAccountChangeLogRequest
      >({
         query: (data) => ({
            url: `account/get-account-change-log`,
            params: data,
         }),
      }),
   }),
});

export const {
   useChangeOwnEmailMutation,
   useChangeOwnUserDataMutation,
   useChangeOwnPasswordMutation,
   useBecomePhotographerMutation,
   useRequestResetPasswordMutation,
   useStopBeingPhotographerMutation,
   useGetOwnAccountChangeLogMutation,
   useSendChangeOwnEmailLinkMutation,
} = UserSettingsService;
