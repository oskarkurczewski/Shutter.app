import Toast from "components/layout/toast";
import Button from "components/shared/Button";
import React from "react";
import { useAppDispatch } from "redux/hooks";
import { push } from "redux/slices/toastSlice";
import { useAppSelector } from "redux/hooks";
import { useRegisterMutation, useUserInfoQuery } from "redux/service/api";
import { registerAccountRequest } from "redux/types/api/accountTypes";

const Homepage = () => {
   const login = useAppSelector((state) => state.auth.username);

   const { data, isLoading, isFetching } = useUserInfoQuery(login);

   const [registerAccount] = useRegisterMutation();

   return (
      <div>
         <Button
            onClick={() => {
               console.log(data);
            }}
         >
            TEST
         </Button>
         {isLoading || (isFetching && <p>witam</p>)}
      </div>
   );
};

export default Homepage;
