import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { ToastType } from "components/layout/toast";
import { Toast } from "types";

export interface ToastRenderType extends ToastType {
   type: ToastTypes;
   confirm?: ButtonObject;
   cancel?: ButtonObject;
}

interface ButtonObject {
   onClick: () => void;
   text: string;
}

export enum ToastTypes {
   INFO = "info",
   SUCCESS = "success",
   WARNING = "warning",
   ERROR = "error",
}

interface ToastHandlerState {
   stack: ToastRenderType[];
   id: number;
}

const initialState: ToastHandlerState = {
   stack: [],
   id: 0,
};

export const toastSlice = createSlice({
   name: "toast",
   initialState,
   reducers: {
      push: (state: ToastHandlerState, action: PayloadAction<Toast>) => {
         const item: ToastRenderType = { ...action.payload, id: state.id };

         if (
            action.payload.name &&
            state.stack.some((t) => t.name === action.payload.name)
         ) {
            return;
         }

         state.id++;
         state.stack = [...state.stack, item];
      },
      remove: (state: ToastHandlerState, action: PayloadAction<number>) => {
         state.stack = state.stack.filter((element) => element.id !== action.payload);
      },
   },
});

export const { push, remove } = toastSlice.actions;

export default toastSlice.reducer;
