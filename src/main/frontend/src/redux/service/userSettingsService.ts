import { api } from "./api";
import {
   ChangePasswordRequest as ChangePasswordRequest,
   ChangeOwnUserDataRequest,
   RequestResetPasswordRequest,
   TableAccountChangeLogInfo,
   GetOwnAccountChangeLogRequest,
   ChangeDescriptionRequest,
   GetAccountLocaleResponse,
} from "redux/types/api/accountTypes";
import { EtagData, getListResponse } from "redux/types/api/dataTypes";
import { Language } from "types/Language";

const UserSettingsService = api.injectEndpoints({
   endpoints: (builder) => ({
      changeUserData: builder.mutation<void, EtagData<ChangeOwnUserDataRequest>>({
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

      changePassword: builder.mutation<void, ChangePasswordRequest>({
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

      sendResetPasswordLink: builder.mutation<void, RequestResetPasswordRequest>({
         query: (data) => ({
            url: `/account/${data.login}/request-reset`,
            method: "POST",
            body: { reCaptchaToken: data.captcha },
         }),
      }),

      getOwnAccountChangeLog: builder.mutation<
         getListResponse<TableAccountChangeLogInfo>,
         GetOwnAccountChangeLogRequest
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

      sendChangeDescriptionLink: builder.mutation<void, ChangeDescriptionRequest>({
         query: (data) => ({
            url: `photographer/change-description`,
            method: "PUT",
            body: data,
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
   useSendChangeDescriptionLinkMutation,
   useGetAccountLocaleMutation,
   useChangeAccountLocaleMutation,
} = UserSettingsService;
