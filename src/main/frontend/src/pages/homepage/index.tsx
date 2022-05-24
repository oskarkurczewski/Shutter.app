import Button from "components/shared/Button";
import React from "react";
import { useAppDispatch } from "redux/hooks";
import { useAppSelector } from "redux/hooks";
import { logout } from "redux/slices/authSlice";

const Homepage = () => {
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

export default Homepage;
