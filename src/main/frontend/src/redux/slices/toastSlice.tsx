import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import Toast from "components/layout/toast";

interface ToastHandlerState {
   stack: JSX.Element[];
}

const initialState: ToastHandlerState = {
   stack: [<Toast />],
};

export const toastSlice = createSlice({
   name: "toast",
   initialState,
   reducers: {
      push: (state: ToastHandlerState, action: PayloadAction<JSX.Element>) => {
         state.stack = [...state.stack, action.payload];
      },
      remove: (state: ToastHandlerState, action: PayloadAction<JSX.Element>) => {
         state.stack = state.stack.filter(
            (element) => !(element.key !== action.payload.key)
         );
      },
   },
});

export const { push, remove } = toastSlice.actions;

export default toastSlice.reducer;
