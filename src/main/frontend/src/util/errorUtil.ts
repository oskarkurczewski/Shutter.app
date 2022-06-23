import { ErrorResponse } from "types";

export const parseError = (err: ErrorResponse, prefix?: string): string[] => {
   const defaultError = "exception.default";

   if (!err || !err.data || !err.data.message) {
      return ["exception.server_down"];
   }

   const msg = err.data.message.split(":")[0];
   return prefix ? [`${prefix}.${msg}`, defaultError] : [msg, defaultError];
};
