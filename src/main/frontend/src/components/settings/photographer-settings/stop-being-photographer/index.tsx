import "./style.scss";
import React, { useEffect } from "react";
import Card from "components/shared/Card";
import Button from "components/shared/Button";
import { useStopBeingPhotographerMutation, useRefreshMutation } from "redux/service/api";
import { refreshToken } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useAppDispatch } from "redux/hooks";

const StopBeingPhotographer = () => {
   const [stopBeingPhotographerMutation] = useStopBeingPhotographerMutation();
   const [refreshMutation, refreshMutationState] = useRefreshMutation();
   const dispatch = useAppDispatch();

   useEffect(() => {
      if (refreshMutationState.isSuccess) {
         const userData = refreshToken(refreshMutationState.data.token);

         dispatch(login(userData));
      }
   }, [refreshMutationState.isSuccess]);

   return (
      <Card id="photographer-settings" className="stop-being-photographer-wrapper">
         <p className="category-title">Ustawienia Fotografa</p>
         <div className="content">
            <p>W każdej chwili możesz edytować widoczność swojego profilu fotografa.</p>
            <p>
               Uwaga! Po dokonaniu tej operacji zostaną anulowane wszystkie Twoje przyszłe
               rezerwacje, a klienci dostaną wiadomość o wyłączeniu widoczności profilu
               fotografa.
            </p>
            <div className="buttons">
               <Button
                  className="red-button"
                  onClick={async () => {
                     await stopBeingPhotographerMutation({});
                     refreshMutation({});
                  }}
               >
                  Wyłącz widoczność profilu
               </Button>
            </div>
         </div>
      </Card>
   );
};

export default StopBeingPhotographer;
