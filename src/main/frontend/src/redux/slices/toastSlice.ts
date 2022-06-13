import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { ToastType } from "components/layout/toast";
import { idText } from "typescript";

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
   stack: [
      {
         type: ToastTypes.INFO,
         text: "Jakaś bardzo poważna informacja biznesowa.",
         id: 1,
      },
      {
         type: ToastTypes.SUCCESS,
         text: "Jakaś bardzo poważna akcja biznesowa zakończyła się pomyślnie.",
         id: 2,
      },
      {
         type: ToastTypes.ERROR,
         text: "Jakiś bardzo poważny błąd biznesowy.",
         id: 3,
      },
      {
         type: ToastTypes.WARNING,
         text: "Jakieś bardzo poważne ostrzeżenie biznesowe.",
         id: 4,
      },
      {
         type: ToastTypes.WARNING,
         text: "Twoja sesja zaraz wygaśnie, kliknij przycisk poniżej aby ją przedłużyć.",
         confirm: { text: "Przedłuż", onClick: () => console.log("przedluzono") },
         id: 5,
      },
      {
         type: ToastTypes.WARNING,
         text: "Czy na pewno chcesz usunąć ten zasób?",
         confirm: { text: "Usuń", onClick: () => console.log("usunieto") },
         cancel: { text: "Anuluj", onClick: () => console.log("anulowano") },
         id: 6,
      },
   ],
   id: 0,
};

export const toastSlice = createSlice({
   name: "toast",
   initialState,
   reducers: {
      push: (state: ToastHandlerState, action: PayloadAction<ToastRenderType>) => {
         const item: ToastRenderType = { ...action.payload, id: state.id };

         state.id++;
         state.stack = [...state.stack, item];
         // if (state.stack.some((element) => element.name === action.payload.name)) return;
         // state.stack = [...state.stack, action.payload];
      },
      remove: (state: ToastHandlerState, action: PayloadAction<number>) => {
         state.stack = state.stack.filter((element) => element.id !== action.payload);
      },
   },
});

export const { push, remove } = toastSlice.actions;

export default toastSlice.reducer;
