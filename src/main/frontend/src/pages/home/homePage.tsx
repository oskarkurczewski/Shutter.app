import React from "react";
import { Button } from "components/shared";
import { useAppDispatch } from "redux/hooks";
import { useAppSelector } from "redux/hooks";
import { logout } from "redux/slices/authSlice";

export const HomePage = () => {
   const { token } = useAppSelector((state) => state.auth);
   const dispatch = useAppDispatch();

   return (
      <div>
         {token && (
            <Button
               onClick={() => {
                  dispatch(logout());
               }}
            >
               Logout
            </Button>
         )}
      </div>
   );
};
