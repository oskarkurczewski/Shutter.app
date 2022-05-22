import React, { useEffect } from "react";
import "./style.scss";
import axios from "axios";
import { useParams } from "react-router-dom";
import { apiUrl } from "App";

//TODO dodać przypadek niepowodzenia
const ConfirmRegistrationPage = () => {
   const { registerationToken } = useParams();

   useEffect(() => {
      axios.post(`${apiUrl}/account/confirm/${registerationToken}`);
   }, []);

   return (
      <div className="confirm-registration-wrapper">
         Twoje konto zostało aktywowane pomyślnie. {registerationToken}
      </div>
   );
};

export default ConfirmRegistrationPage;
