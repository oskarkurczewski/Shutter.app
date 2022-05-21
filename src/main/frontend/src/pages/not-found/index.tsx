import React from "react";
import { Link } from "react-router-dom";
import "./style.scss";

const NotFound404 = () => {
   return (
      <div className="content">
         <p>NotFound404</p>
         <Link to="/" replace>
            Wróć na stronę główną
         </Link>
      </div>
   );
};

export default NotFound404;
