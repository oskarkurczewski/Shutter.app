import { api } from "./api";
import { resetPasswordRequest } from "redux/types/api/accountTypes";

const TokenBasedService = api.injectEndpoints({
   endpoints: (builder) => ({
      confirmRegistration: builder.mutation<unknown, string>({
         query: (token) => ({
            url: `account/confirm/${token}`,
            method: "POST",
         }),
      }),

      unblockOwnAccount: builder.mutation<unknown, string>({
         query: (token) => ({
            url: `account/unblock-own-account/${token}`,
         }),
      }),

      resetPassword: builder.mutation<unknown, resetPasswordRequest>({
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
   useUnblockOwnAccountMutation,
   useConfirmRegistrationMutation,
} = TokenBasedService;
