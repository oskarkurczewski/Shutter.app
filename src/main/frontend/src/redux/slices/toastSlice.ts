import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { ToastType } from "components/layout/toast";

interface ToastHandlerState {
   stack: ToastType[];
}

const initialState: ToastHandlerState = {
   stack: [],
};

export const toastSlice = createSlice({
   name: "toast",
   initialState,
   reducers: {
      push: (state: ToastHandlerState, action: PayloadAction<ToastType>) => {
         if (state.stack.some((element) => element.name === action.payload.name)) return;
         state.stack = [...state.stack, action.payload];
      },
      remove: (state: ToastHandlerState, action: PayloadAction<string>) => {
         state.stack = state.stack.filter((element) => element.name !== action.payload);
      },
   },
});

export const { push, remove } = toastSlice.actions;

export default toastSlice.reducer;
