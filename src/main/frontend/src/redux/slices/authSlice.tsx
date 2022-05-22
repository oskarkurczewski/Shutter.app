import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { AuthState } from "redux/types/stateTypes";
import { AccessLevel } from "types/AccessLevel";

const initialState: AuthState = {
   username: "",
   roles: [],
   accessLevel: AccessLevel.GUEST,
};

export const authSlice = createSlice({
   name: "auth",
   initialState,
   reducers: {
      login: (state: AuthState, action: PayloadAction<AuthState>) => {
         state.username = action.payload.username;
         state.roles = action.payload.roles;
         state.accessLevel = action.payload.accessLevel;

         const selectedAccessLevel = localStorage.getItem("accessLevel");

         if (
            selectedAccessLevel === AccessLevel.GUEST ||
            state.roles.includes(selectedAccessLevel as AccessLevel)
         ) {
            localStorage.setItem("accessLevel", state.accessLevel);
         }
      },
      logout: (state: AuthState) => {
         state.username = initialState.username;
         state.roles = initialState.roles;
         state.accessLevel = initialState.accessLevel;

         localStorage.setItem("accessLevel", AccessLevel.GUEST);
         localStorage.removeItem("token");
      },
   },
});

export const { login, logout } = authSlice.actions;

export default authSlice.reducer;
