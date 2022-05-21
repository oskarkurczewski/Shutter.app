import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { AuthState } from "redux/types/stateTypes";
import { AccessLevel } from "types/AccessLevel";

const initialState: AuthState = {
   username: "",
   accessLevel: [AccessLevel.GUEST],
};

export const authSlice = createSlice({
   name: "auth",
   initialState,
   reducers: {
      login: (state: AuthState, action: PayloadAction<AuthState>) => {
         state.username = action.payload.username;
         state.accessLevel = action.payload.accessLevel;

         const selectedAccessLevel = localStorage.getItem("accessLevel");

         if (
            selectedAccessLevel === AccessLevel.GUEST ||
            action.payload.accessLevel.includes(selectedAccessLevel as AccessLevel)
         ) {
            localStorage.setItem("accessLevel", action.payload.accessLevel[0]);
         }
      },
      logout: (state: AuthState) => {
         state.username = initialState.username;
         state.accessLevel = initialState.accessLevel;
         localStorage.setItem("accessLevel", AccessLevel.GUEST);
         localStorage.removeItem("token");
      },
      logout: (state) => {
         return initialState;
      },
   },
});

export const { login, logout } = authSlice.actions;

export default authSlice.reducer;
