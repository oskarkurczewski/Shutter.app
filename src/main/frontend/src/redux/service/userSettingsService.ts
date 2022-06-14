import { api } from "./api";
import {
   changeOwnPasswordRequest,
   changeOwnUserDataRequest,
   requestResetPasswordRequest,
   tableAccountChangeLogInfo,
   getOwnAccountChangeLogRequest,
   GetAccountLocaleResponse,
} from "redux/types/api/accountTypes";
import { EtagData, getListResponse } from "redux/types/api/dataTypes";
import { Language } from "types/Language";

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

      changePassword: builder.mutation<void, changeOwnPasswordRequest>({
         query: (data) => ({
            url: "account/change-password",
            method: "PUT",
            body: data,
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

      getAccountLocale: builder.mutation<GetAccountLocaleResponse, void>({
         query: () => ({
            url: "account/locale",
         }),
      }),

      changeAccountLocale: builder.mutation<void, Language>({
         query: (lang) => ({
            url: `account/locale/${lang}`,
            method: "POST",
         }),
      }),
   }),
});

export const {
   useChangeUserDataMutation,
   useChangePasswordMutation,
   useBecomePhotographerMutation,
   useSendResetPasswordLinkMutation,
   useStopBeingPhotographerMutation,
   useGetOwnAccountChangeLogMutation,
   useSendChangeEmailLinkMutation,
   useGetAccountLocaleMutation,
   useChangeAccountLocaleMutation,
} = UserSettingsService;
