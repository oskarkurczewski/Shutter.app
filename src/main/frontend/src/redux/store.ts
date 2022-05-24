import { configureStore } from "@reduxjs/toolkit";
import authSlice from "redux/slices/authSlice";
import { api } from "./service/api";
import toastSlice from "./slices/toastSlice";

export const store = configureStore({
   reducer: { [api.reducerPath]: api.reducer, auth: authSlice, toast: toastSlice },
   middleware: (getDefaultMiddleWare) =>
      getDefaultMiddleWare({
         serializableCheck: {
            ignoredActions: ["toast/push"],
         },
      }).concat(api.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
