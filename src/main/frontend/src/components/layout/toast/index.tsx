/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
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
         <div className="close-btn-wrapper" onClick={() => console.log("exit")}>
            <span className="close-btn" />
         </div>
      </div>
   );
};

export default Toast;
