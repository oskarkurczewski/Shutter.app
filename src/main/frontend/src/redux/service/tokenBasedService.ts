import { api } from "./api";
import {
   ResetPasswordRequest,
   ChangeOwnEmailRequest,
} from "redux/types/api/accountTypes";

const TokenBasedService = api.injectEndpoints({
   endpoints: (builder) => ({
      confirmRegistration: builder.mutation<void, string>({
         query: (token) => ({
            url: `account/confirm/${token}`,
            method: "POST",
         }),
      }),

      unblockAccount: builder.mutation<void, string>({
         query: (token) => ({
            url: `account/unblock-own-account/${token}`,
         }),
      }),

      changeEmail: builder.mutation<void, ChangeOwnEmailRequest>({
         query: (data) => ({
            url: "account/verify-email-update",
            method: "POST",
            body: data,
         }),
      }),

      resetPassword: builder.mutation<void, ResetPasswordRequest>({
         query: (data) => ({
            url: "account/password-reset",
            method: "POST",
            body: data,
         }),
      }),
   }),
});

export const {
   useResetPasswordMutation,
   useUnblockAccountMutation,
   useChangeEmailMutation,
   useConfirmRegistrationMutation,
} = TokenBasedService;
