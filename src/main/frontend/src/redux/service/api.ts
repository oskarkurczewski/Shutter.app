import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { RootState } from "redux/store";

export const api = createApi({
   reducerPath: "tokenBasedService",
   baseQuery: fetchBaseQuery({
      // baseUrl: "https://studapp.it.p.lodz.pl:8002/api/",
      baseUrl: "https://localhost:8402/api/",
      prepareHeaders: (headers, { getState }) => {
         const token = (getState() as RootState).auth.token;
         if (token) {
            headers.set("Authorization", `Bearer ${token}`);
         }
         return headers;
      },
   }),
   tagTypes: ["List", "Preferences"],
   endpoints: () => ({}),
});
