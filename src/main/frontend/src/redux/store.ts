import { configureStore } from "@reduxjs/toolkit";
import authSlice from "redux/slices/authSlice";
import { api } from "./service/api";

export const store = configureStore({
   reducer: { [api.reducerPath]: api.reducer, auth: authSlice },
   middleware: (getDefaultMiddleWare) => getDefaultMiddleWare().concat(api.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
