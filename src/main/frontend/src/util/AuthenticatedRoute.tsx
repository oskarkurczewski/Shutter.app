import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAppSelector } from "redux/hooks";
import { JsxAttributeLike } from "typescript";

interface AuthenticatedRouteProps {
   children: JSX.Element;
}

const AuthenticatedRoute = ({ children }: AuthenticatedRouteProps) => {
   const username = useAppSelector((state) => state.auth.username);
   const location = useLocation();
   if (username === "") {
      return <Navigate to="/login" state={{ from: location }} replace />;
   }
   return children;
};

export default AuthenticatedRoute;
