import { api } from "./api";
import {
   changeOwnPasswordRequest,
   changeOwnUserDataRequest,
   requestResetPasswordRequest,
   tableAccountChangeLogInfo,
   getOwnAccountChangeLogRequest,
   changeSomeonesPasswordRequest,
} from "redux/types/api/accountTypes";
import { EtagData, getListResponse } from "redux/types/api/dataTypes";

const UserSettingsService = api.injectEndpoints({
   endpoints: (builder) => ({
      changeUserData: builder.mutation<void, EtagData<changeOwnUserDataRequest>>({
         query: ({ data, etag }) => ({
            url: "account/editOwnAccountInfo",
            method: "PUT",
            body: { ...data, version: etag.version },
            headers: { "If-Match": etag.etag },
         }),
      }),

      sendChangeEmailLink: builder.mutation<void, void>({
         query: () => ({
            url: "account/request-email-update",
            method: "POST",
         }),
      }),

      changeOwnPassword: builder.mutation<void, changeOwnPasswordRequest>({
         query: (data) => ({
            url: "account/change-password",
            method: "PUT",
            body: data,
         }),
      }),
      changeSomeonesPassword: builder.mutation<void, changeSomeonesPasswordRequest>({
         query: (data) => ({
            url: `account/${data.login}/change-password`,
            method: "PUT",
            body: data.data,
         }),
      }),

      becomePhotographer: builder.mutation<void, void>({
         query: () => ({
            url: "/account/become-photographer",
            method: "POST",
         }),
      }),

      stopBeingPhotographer: builder.mutation<void, void>({
         query: () => ({
            url: "/account/stop-being-photographer",
            method: "POST",
         }),
      }),

      sendResetPasswordLink: builder.mutation<void, requestResetPasswordRequest>({
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
   useChangeUserDataMutation,
   useChangeOwnPasswordMutation,
   useChangeSomeonesPasswordMutation,
   useBecomePhotographerMutation,
   useSendResetPasswordLinkMutation,
   useStopBeingPhotographerMutation,
   useGetOwnAccountChangeLogMutation,
   useSendChangeEmailLinkMutation,
} = UserSettingsService;
