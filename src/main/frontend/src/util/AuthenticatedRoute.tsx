import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { JsxAttributeLike } from "typescript";

interface AuthenticatedRouteProps {
   children: JSX.Element;
   token: string;
}

const AuthenticatedRoute = ({ children, token }: AuthenticatedRouteProps) => {
   const location = useLocation();
   if (token == "") {
      return <Navigate to="/login" state={{ from: location }} replace />;
   }
   return children;
};

export default AuthenticatedRoute;
