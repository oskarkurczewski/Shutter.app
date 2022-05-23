import Button from "components/shared/Button";
import React from "react";
import "./style.scss";

const Toast = () => {
   return (
      <div className="toast">
         <p className="label-bold">Powiadomienie</p>
         <p>
            Twoja sesja niedługo wygaśnie, <br />
            kliknij w przycisk poniżej, aby ją przedłużyć!
         </p>
         <Button
            onClick={() => {
               console.log("witam");
            }}
         >
            PRZEDŁUŻ
         </Button>
      </div>
   );
};

export default Toast;
