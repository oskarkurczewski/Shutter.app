import { ToastRenderType } from "redux/slices/toastSlice";

export type tableHeader = {
   id: string;
   label: string;
   sort: string | null;
   sortable: boolean;
};

export interface Condition<T> {
   function: (a: T) => boolean;
   message: string;
}

export type Toast = Omit<ToastRenderType, "id">;
