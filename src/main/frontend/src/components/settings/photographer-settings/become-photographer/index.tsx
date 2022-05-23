import "./style.scss";
import React, { useEffect } from "react";
import Card from "components/shared/Card";
import Button from "components/shared/Button";
import { useBecomePhotographerMutation, useRefreshMutation } from "redux/service/api";
import { refreshToken } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useAppDispatch } from "redux/hooks";

const BecomePhotographer = () => {
   const [becomePhotographerMutation] = useBecomePhotographerMutation();
   const [refreshMutation, refreshMutationState] = useRefreshMutation();
   const dispatch = useAppDispatch();

   useEffect(() => {
      if (refreshMutationState.isSuccess) {
         const userData = refreshToken(refreshMutationState.data.token);

         dispatch(login(userData));
      }
   }, [refreshMutationState.isSuccess]);

   return (
      <Card id="photographer-settings" className="become-photographer-wrapper">
         <p className="category-title">Ustawienia Fotografa</p>
         <div className="content">
            <p>
               W każdym momencie możesz zostać fotografem! Dzięki temu zyskasz możliwość
               tworzenia profilu, umieszczania w nim zdjęć, a także zarządzania
               rezerwacjami.
            </p>
            <div className="buttons">
               <Button
                  onClick={async () => {
                     await becomePhotographerMutation({});
                     refreshMutation({});
                  }}
               >
                  Zostań fotografem
               </Button>
            </div>
         </div>
      </Card>
   );
};

export default BecomePhotographer;
