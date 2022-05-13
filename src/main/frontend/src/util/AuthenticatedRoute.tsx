import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { JsxAttributeLike } from "typescript";

interface AuthenticatedRouteProps {
   children: JSX.Element;
}

const AuthenticatedRoute = ({ children }: AuthenticatedRouteProps) => {
   const token = localStorage.getItem("token");
   const location = useLocation();
   if (token == "") {
      return <Navigate to="/login" state={{ from: location }} replace />;
   }
   return children;
};

export default AuthenticatedRoute;
