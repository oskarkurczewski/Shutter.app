import { configureStore } from "@reduxjs/toolkit";
import authSlice from "redux/slices/authSlice";
import toastSlice from "./slices/toastSlice";

export const store = configureStore({
   reducer: { auth: authSlice, toast: toastSlice },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
