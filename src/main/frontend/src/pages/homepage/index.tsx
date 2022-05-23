import Toast from "components/layout/toast";
import Button from "components/shared/Button";
import React from "react";
import { useAppDispatch } from "redux/hooks";
import { push } from "redux/slices/toastSlice";

const Homepage = () => {
   const dispatch = useAppDispatch();

   const data = {
      name: "refreshToken",
      label: "Powiadomienie",
      text: "Twoja sesja niedługo wygaśnie, kliknij w przycisk poniżej, aby ją przedłużyć!",
      content: (
         <Button
            onClick={() => {
               console.log("witam");
            }}
         >
            Przedłuż
         </Button>
      ),
   };

   return (
      <div>
         <button
            onClick={() => {
               dispatch(push(data));
            }}
         >
            TEST
         </button>
      </div>
   );
};

export default Homepage;
