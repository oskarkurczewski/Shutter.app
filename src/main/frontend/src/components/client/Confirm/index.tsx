import axios from "axios";
import React, { useEffect } from "react";
import "./style.scss";
import { useParams } from "react-router-dom";
import { apiUrl } from "App";

//TODO dodać przypadek niepowodzenia
const Confirm = () => {
   const { registerationToken } = useParams();

   useEffect(() => {
      axios.post(`${apiUrl}/account/confirm/${registerationToken}`);
   }, []);

   return (
      <div className="confirm-container">
         Twoje konto zostało aktywowane pomyślnie. {registerationToken}
      </div>
   );
};

export default Confirm;
