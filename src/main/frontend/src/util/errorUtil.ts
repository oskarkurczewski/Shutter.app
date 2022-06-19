import { ErrorResponse } from "types";

export const parseError = (err: ErrorResponse, prefix?: string) => {
   if (!err.data) {
      return "exception.server_down";
   }

   const msg = err.data.message.split(":")[0];
   return prefix ? `${prefix}.${msg}` : msg;
};
