import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types/AccessLevel";

interface Props {
   roles: AccessLevel[];
   children: React.ReactNode;
}

const ProtectedRoute = ({ roles, children }: Props) => {
   const navigate = useNavigate();
   const level = useAppSelector((state) => state.auth.accessLevel);

   useEffect(() => {
      if (!roles.includes(level)) {
         navigate(-1);
      }
   }, []);

   return <>{children}</>;
};

export default ProtectedRoute;
